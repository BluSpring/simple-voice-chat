package de.maxhenkel.voicechat.voice.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ConnectionCheckPacket implements Packet<ConnectionCheckPacket> {

    public ConnectionCheckPacket() {

    }

    @Override
    public ConnectionCheckPacket fromBytes(DataInputStream buf) {
        return new ConnectionCheckPacket();
    }

    @Override
    public void toBytes(DataOutputStream buf) {

    }
}
