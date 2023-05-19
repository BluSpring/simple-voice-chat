package de.maxhenkel.voicechat.net;

import net.minecraft.src.EntityPlayer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FabricNetworkEvents {
    private static final Map<String, ClientCustomPayloadEvent> clientPackets = new ConcurrentHashMap<>();

    public static void registerClientPacket(String channel, ClientCustomPayloadEvent event) {
        clientPackets.put(channel, event);
    }

    public static boolean onCustomPayloadClient(Packet136ServerCustomPayload packet) {
        ClientCustomPayloadEvent event = clientPackets.get(packet.getChannel());
        if (event != null) {
            event.onCustomPayload(packet);
            return true;
        }
        return false;
    }

    public interface ClientCustomPayloadEvent {
        void onCustomPayload(Packet136ServerCustomPayload packet);
    }
}
