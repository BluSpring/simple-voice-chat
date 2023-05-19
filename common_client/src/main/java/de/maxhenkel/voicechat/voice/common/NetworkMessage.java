package de.maxhenkel.voicechat.voice.common;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.api.RawUdpPacket;
import de.maxhenkel.voicechat.voice.client.ClientVoicechatConnection;
import de.maxhenkel.voicechat.voice.server.ClientConnection;
import de.maxhenkel.voicechat.voice.server.Server;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.SocketAddress;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NetworkMessage {

    public static final byte MAGIC_BYTE = (byte) 0b11111111;

    private final long timestamp;
    private Packet<? extends Packet> packet;
    private SocketAddress address;

    public NetworkMessage(long timestamp, Packet<?> packet) {
        this(timestamp);
        this.packet = packet;
    }

    public NetworkMessage(Packet<?> packet) {
        this(System.currentTimeMillis());
        this.packet = packet;
    }

    private NetworkMessage(long timestamp) {
        this.timestamp = timestamp;
    }

    @Nonnull
    public Packet<? extends Packet> getPacket() {
        return packet;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getTTL() {
        return packet.getTTL();
    }

    public SocketAddress getAddress() {
        return address;
    }

    private static final Map<Byte, Class<? extends Packet>> packetRegistry;

    static {
        packetRegistry = new HashMap<>();
        packetRegistry.put((byte) 0x1, MicPacket.class);
        packetRegistry.put((byte) 0x2, PlayerSoundPacket.class);
        packetRegistry.put((byte) 0x3, GroupSoundPacket.class);
        packetRegistry.put((byte) 0x4, LocationSoundPacket.class);
        packetRegistry.put((byte) 0x5, AuthenticatePacket.class);
        packetRegistry.put((byte) 0x6, AuthenticateAckPacket.class);
        packetRegistry.put((byte) 0x7, PingPacket.class);
        packetRegistry.put((byte) 0x8, KeepAlivePacket.class);
        packetRegistry.put((byte) 0x9, ConnectionCheckPacket.class);
        packetRegistry.put((byte) 0xA, ConnectionCheckAckPacket.class);
    }

    @Nullable
    public static NetworkMessage readPacketClient(RawUdpPacket packet, ClientVoicechatConnection client) throws IllegalAccessException, InstantiationException, IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvocationTargetException, NoSuchMethodException {
        byte[] data = packet.getData();
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        DataInputStream dataStream = new DataInputStream(stream);
        if (dataStream.readByte() != MAGIC_BYTE) {
            Voicechat.logDebug("Received invalid packet from {}", client.getAddress());
            return null;
        }

        int length = dataStream.readInt();
        byte[] bytes = new byte[length];

        for (int i = 0; i < length; i++) {
            bytes[i] = dataStream.readByte();
        }

        return readFromBytes(packet.getSocketAddress(), client.getData().getSecret(), bytes, System.currentTimeMillis());
    }

    @Nullable
    public static NetworkMessage readPacketServer(RawUdpPacket packet, Server server) throws IllegalAccessException, InstantiationException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvocationTargetException, NoSuchMethodException, IOException {
        byte[] data = packet.getData();
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        DataInputStream b = new DataInputStream(stream);
        if (b.readByte() != MAGIC_BYTE) {
            Voicechat.logDebug("Received invalid packet from {}", packet.getSocketAddress());
            return null;
        }
        UUID playerID = UUID.fromString(b.readUTF());
        if (!server.hasSecret(playerID)) {
            // Ignore packets if they are not from a player that has a secret
            Voicechat.logDebug("Player " + playerID + " does not have a secret");
            return null;
        }

        int length = b.readInt();
        byte[] bytes = new byte[length];

        for (int i = 0; i < length; i++) {
            bytes[i] = b.readByte();
        }

        return readFromBytes(packet.getSocketAddress(), server.getSecret(playerID), bytes, packet.getTimestamp());
    }

    @Nullable
    private static NetworkMessage readFromBytes(SocketAddress socketAddress, UUID secret, byte[] encryptedPayload, long timestamp) throws InstantiationException, IllegalAccessException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, NoSuchMethodException, InvocationTargetException, IOException {
        byte[] decrypt;
        try {
            decrypt = AES.decrypt(secret, encryptedPayload);
        } catch (Exception e) {
            // Return null if the encryption fails due to a wrong secret
            Voicechat.logDebug("Failed to decrypt packet from {}", socketAddress);
            return null;
        }
        ByteArrayInputStream stream = new ByteArrayInputStream(decrypt);
        DataInputStream buffer = new DataInputStream(stream);
        byte packetType = buffer.readByte();
        Class<? extends Packet> packetClass = packetRegistry.get(packetType);
        if (packetClass == null) {
            Voicechat.logDebug("Got invalid packet ID {}", packetType);
            return null;
        }
        Packet<? extends Packet<?>> p = packetClass.getDeclaredConstructor().newInstance();

        NetworkMessage message = new NetworkMessage(timestamp);
        message.address = socketAddress;
        message.packet = p.fromBytes(buffer);

        return message;
    }

    private static byte getPacketType(Packet<? extends Packet> packet) {
        for (Map.Entry<Byte, Class<? extends Packet>> entry : packetRegistry.entrySet()) {
            if (packet.getClass().equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public byte[] writeClient(ClientVoicechatConnection client) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IOException {
        byte[] payload = write(client.getData().getSecret());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1 + 16 + payload.length);
        DataOutputStream buffer = new DataOutputStream(outputStream);

        buffer.writeByte(MAGIC_BYTE);
        buffer.writeUTF(client.getData().getPlayerUUID().toString());
        buffer.writeInt(payload.length);
        buffer.write(payload);

        return outputStream.toByteArray();
    }

    public byte[] writeServer(Server server, ClientConnection connection) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IOException {
        byte[] payload = write(server.getSecret(connection.getPlayerUUID()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1 + payload.length);
        DataOutputStream buffer = new DataOutputStream(outputStream);

        buffer.writeByte(MAGIC_BYTE);
        buffer.writeInt(payload.length);
        buffer.write(payload);

        return outputStream.toByteArray();
    }

    public byte[] write(UUID secret) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream buffer = new DataOutputStream(outputStream);

        byte type = getPacketType(packet);
        if (type < 0) {
            throw new IllegalArgumentException("Packet type not found");
        }

        buffer.writeByte(type);
        packet.toBytes(buffer);

        return AES.encrypt(secret, outputStream.toByteArray());
    }

}
