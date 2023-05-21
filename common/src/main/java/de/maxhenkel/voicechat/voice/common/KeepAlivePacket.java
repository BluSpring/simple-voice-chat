package de.maxhenkel.voicechat.voice.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class KeepAlivePacket implements Packet<KeepAlivePacket> {

    public KeepAlivePacket() {

    }

    @Override
    public KeepAlivePacket fromBytes(DataInputStream buf) {
        return new KeepAlivePacket();
    }

    @Override
    public void toBytes(DataOutputStream buf) {

    }
}
