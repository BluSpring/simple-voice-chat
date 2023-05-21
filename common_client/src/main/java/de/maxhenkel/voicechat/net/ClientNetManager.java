package de.maxhenkel.voicechat.net;

import de.maxhenkel.voicechat.MinecraftAccessor;
import de.maxhenkel.voicechat.Voicechat;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.NetHandler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public abstract class ClientNetManager extends NetManager {
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

    public interface ClientReceiver<T extends Packet<T>> {
        void onPacket(Minecraft client, NetHandler handler, T packet);
    }
}
