package de.maxhenkel.voicechat.net;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetHandler;

import java.util.ArrayList;
import java.util.List;

public class ServerChannel<T extends Packet<T>> {

    private final List<NetManager.ServerReceiver<T>> listeners;

    public ServerChannel() {
        listeners = new ArrayList<>();
    }

    public void registerServerListener(NetManager.ServerReceiver<T> packetReceiver) {
        listeners.add(packetReceiver);
    }

    public void onPacket(EntityPlayer player, NetHandler handler, T packet) {
        listeners.forEach(receiver -> receiver.onPacket(player, handler, packet));
    }

    public List<NetManager.ServerReceiver<T>> getListeners() {
        return listeners;
    }
}
