package de.maxhenkel.voicechat.net;

import net.minecraft.src.EntityPlayer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FabricNetworkEvents {
    private static final Map<String, ServerCustomPayloadEvent> serverPackets = new ConcurrentHashMap<>();

    public static void registerServerPacket(String channel, ServerCustomPayloadEvent event) {
        serverPackets.put(channel, event);
    }

    public static boolean onCustomPayloadServer(Packet135ClientCustomPayload packet, EntityPlayer player) {
        ServerCustomPayloadEvent event = serverPackets.get(packet.getChannel());
        if (event != null) {
            event.onCustomPayload(packet, player);
            return true;
        }
        return false;
    }

    public interface ServerCustomPayloadEvent {
        void onCustomPayload(Packet135ClientCustomPayload packet, EntityPlayer player);
    }
}
