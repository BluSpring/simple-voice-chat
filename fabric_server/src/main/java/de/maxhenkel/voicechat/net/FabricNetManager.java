package de.maxhenkel.voicechat.net;

import de.maxhenkel.voicechat.Voicechat;
import net.minecraft.src.EntityPlayerMP;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class FabricNetManager extends NetManager {
    @Override
    public <T extends Packet<T>> Channel<T> registerReceiver(Class<T> packetType, boolean toClient, boolean toServer) {
        Channel<T> c = new Channel<>();

        try {
            T dummy = packetType.getDeclaredConstructor().newInstance();

            if (toServer) {
                FabricNetworkEvents.registerServerPacket(dummy.getIdentifier(), (packet, player) -> {
                    try {
                        if (!Voicechat.SERVER.isCompatible(player) && !packetType.equals(RequestSecretPacket.class))
                            return;

                        T vcPacket = packetType.getDeclaredConstructor().newInstance();

                        ByteArrayInputStream inputStream = new ByteArrayInputStream(packet.getBuf());
                        DataInputStream stream = new DataInputStream(inputStream);
                        vcPacket.fromBytes(stream);

                        c.onServerPacket(player, ((EntityPlayerMP) player).field_20908_a, vcPacket);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        return c;
    }
}
