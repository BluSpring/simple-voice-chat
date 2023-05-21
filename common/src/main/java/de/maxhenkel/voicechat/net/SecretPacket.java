package de.maxhenkel.voicechat.net;

import de.maxhenkel.voicechat.config.ServerConfig;
import de.maxhenkel.voicechat.plugins.PluginManager;
import de.maxhenkel.voicechat.util.ConnectionUtil;
import net.minecraft.src.EntityPlayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class SecretPacket implements Packet<SecretPacket> {

    public static final String SECRET = ConnectionUtil.format(NetManager.CHANNEL, "secret");

    private UUID secret;
    private int serverPort;
    private UUID playerUUID;
    private ServerConfig.Codec codec;
    private int mtuSize;
    private double voiceChatDistance;
    private int keepAlive;
    private boolean groupsEnabled;
    private String voiceHost;
    private boolean allowRecording;

    public SecretPacket() {

    }

    public SecretPacket(EntityPlayer player, UUID secret, int port, ServerConfig serverConfig) {
        this.secret = secret;
        this.serverPort = port;
        this.playerUUID = UUID.nameUUIDFromBytes(player.username.getBytes(StandardCharsets.UTF_8));
        this.codec = serverConfig.voiceChatCodec.get();
        this.mtuSize = serverConfig.voiceChatMtuSize.get();
        this.voiceChatDistance = serverConfig.voiceChatDistance.get();
        this.keepAlive = serverConfig.keepAlive.get();
        this.groupsEnabled = serverConfig.groupsEnabled.get();
        this.voiceHost = PluginManager.instance().getVoiceHost(serverConfig.voiceHost.get());
        this.allowRecording = serverConfig.allowRecording.get();
    }

    public UUID getSecret() {
        return secret;
    }

    public int getServerPort() {
        return serverPort;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public ServerConfig.Codec getCodec() {
        return codec;
    }

    public int getMtuSize() {
        return mtuSize;
    }

    public double getVoiceChatDistance() {
        return voiceChatDistance;
    }

    public int getKeepAlive() {
        return keepAlive;
    }

    public boolean groupsEnabled() {
        return groupsEnabled;
    }

    public String getVoiceHost() {
        return voiceHost;
    }

    @Override
    public String getIdentifier() {
        return SECRET;
    }

    public boolean allowRecording() {
        return allowRecording;
    }

    @Override
    public SecretPacket fromBytes(DataInputStream buf) throws IOException {
        secret = UUID.fromString(buf.readUTF());
        serverPort = buf.readInt();
        playerUUID = UUID.fromString(buf.readUTF());
        codec = ServerConfig.Codec.values()[buf.readByte()];
        mtuSize = buf.readInt();
        voiceChatDistance = buf.readDouble();
        keepAlive = buf.readInt();
        groupsEnabled = buf.readBoolean();
        voiceHost = buf.readUTF();
        allowRecording = buf.readBoolean();
        return this;
    }

    @Override
    public void toBytes(DataOutputStream buf) throws IOException {
        buf.writeUTF(secret.toString());
        buf.writeInt(serverPort);
        buf.writeUTF(playerUUID.toString());
        buf.writeByte(codec.ordinal());
        buf.writeInt(mtuSize);
        buf.writeDouble(voiceChatDistance);
        buf.writeInt(keepAlive);
        buf.writeBoolean(groupsEnabled);
        buf.writeUTF(voiceHost.toString());
        buf.writeBoolean(allowRecording);
    }

}
