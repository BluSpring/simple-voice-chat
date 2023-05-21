package de.maxhenkel.voicechat.net;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetHandler;

import javax.annotation.Nullable;

public class Channel<T extends Packet<T>> {
    @Nullable
    private NetManager.ServerReceiver<T> serverListener;

    public Channel() {

    }

    public void setServerListener(NetManager.ServerReceiver<T> packetReceiver) {
        serverListener = packetReceiver;
    }

    public void onServerPacket(EntityPlayer player, NetHandler handler, T packet) {
        if (serverListener != null) {
            serverListener.onPacket(player, handler, packet);
        }
    }
}
