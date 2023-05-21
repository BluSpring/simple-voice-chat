package de.maxhenkel.voicechat.net;

import net.minecraft.client.Minecraft;
import net.minecraft.src.NetHandler;

import javax.annotation.Nullable;

public class ClientChannel<T extends Packet<T>> extends Channel<T> {
    @Nullable
    private ClientNetManager.ClientReceiver<T> clientListener;

    public void setClientListener(ClientNetManager.ClientReceiver<T> packetReceiver) {
        clientListener = packetReceiver;
    }

    public void onClientPacket(Minecraft client, NetHandler handler, T packet) {
        if (clientListener != null) {
            clientListener.onPacket(client, handler, packet);
        }
    }
}
