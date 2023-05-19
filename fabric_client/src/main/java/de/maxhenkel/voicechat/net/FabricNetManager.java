package de.maxhenkel.voicechat.net;

import de.maxhenkel.voicechat.MinecraftAccessor;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class FabricNetManager extends NetManager {
    @Override
    public <T extends Packet<T>> Channel<T> registerReceiver(Class<T> packetType, boolean toClient, boolean toServer) {
        Channel<T> c = new Channel<>();

        try {
            T dummy = packetType.getDeclaredConstructor().newInstance();

            if (toClient) {
                FabricNetworkEvents.registerClientPacket(dummy.getIdentifier(), payload -> {
                    try {
                        T packet = packetType.getDeclaredConstructor().newInstance();

                        ByteArrayInputStream inputStream = new ByteArrayInputStream(payload.getBuf());
                        DataInputStream stream = new DataInputStream(inputStream);

                        packet.fromBytes(stream);

                        onClientPacket(c, packet);
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

    private <T extends Packet<T>> void onClientPacket(Channel<T> channel, T packet) {
        channel.onClientPacket(MinecraftAccessor.getMinecraft(), MinecraftAccessor.getMinecraft().func_20001_q(), packet);
    }
}
