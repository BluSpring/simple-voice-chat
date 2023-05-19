package de.maxhenkel.voicechat.net;

import de.maxhenkel.voicechat.MinecraftAccessor;
import de.maxhenkel.voicechat.Voicechat;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.NetHandler;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public abstract class NetManager {

    public static final String CHANNEL = "vc";

    public Channel<UpdateStatePacket> updateStateChannel;
    public Channel<PlayerStatePacket> playerStateChannel;
    public Channel<PlayerStatesPacket> playerStatesChannel;
    public Channel<SecretPacket> secretChannel;
    public Channel<RequestSecretPacket> requestSecretChannel;
    public Channel<AddGroupPacket> addGroupChannel;
    public Channel<RemoveGroupPacket> removeGroupChannel;
    public Channel<JoinGroupPacket> joinGroupChannel;
    public Channel<CreateGroupPacket> createGroupChannel;
    public Channel<LeaveGroupPacket> leaveGroupChannel;
    public Channel<JoinedGroupPacket> joinedGroupChannel;
    public Channel<AddCategoryPacket> addCategoryChannel;
    public Channel<RemoveCategoryPacket> removeCategoryChannel;

    public void init() {
        updateStateChannel = registerReceiver(UpdateStatePacket.class, false, true);
        playerStateChannel = registerReceiver(PlayerStatePacket.class, true, false);
        playerStatesChannel = registerReceiver(PlayerStatesPacket.class, true, false);
        secretChannel = registerReceiver(SecretPacket.class, true, false);
        requestSecretChannel = registerReceiver(RequestSecretPacket.class, false, true);
        addGroupChannel = registerReceiver(AddGroupPacket.class, true, false);
        removeGroupChannel = registerReceiver(RemoveGroupPacket.class, true, false);
        joinGroupChannel = registerReceiver(JoinGroupPacket.class, false, true);
        createGroupChannel = registerReceiver(CreateGroupPacket.class, false, true);
        leaveGroupChannel = registerReceiver(LeaveGroupPacket.class, false, true);
        joinedGroupChannel = registerReceiver(JoinedGroupPacket.class, true, false);
        addCategoryChannel = registerReceiver(AddCategoryPacket.class, true, false);
        removeCategoryChannel = registerReceiver(RemoveCategoryPacket.class, true, false);
    }

    public abstract <T extends Packet<T>> Channel<T> registerReceiver(Class<T> packetType, boolean toClient, boolean toServer);

    public static void sendToServer(Packet<?> packet) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream buffer = new DataOutputStream(outputStream);
        try {
            packet.toBytes(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetClientHandler connection = MinecraftAccessor.getMinecraft().func_20001_q();
        if (connection != null) {
            connection.addToSendQueue(new Packet135ClientCustomPayload(packet.getIdentifier().toString(), outputStream.toByteArray()));
        }
    }

    public static void sendToClient(EntityPlayer player, Packet<?> packet) {
        if (!Voicechat.SERVER.isCompatible(player)) {
            return;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream buffer = new DataOutputStream(outputStream);
        try {
            packet.toBytes(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Voicechat.serverInstance.sendToPlayer(player, new Packet136ServerCustomPayload(packet.getIdentifier().toString(), outputStream.toByteArray()));
    }

    public interface ServerReceiver<T extends Packet<T>> {
        void onPacket(EntityPlayer player, NetHandler handler, T packet);
    }

    public interface ClientReceiver<T extends Packet<T>> {
        void onPacket(Minecraft client, NetHandler handler, T packet);
    }

}
