package de.maxhenkel.voicechat.voice.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ConnectionCheckAckPacket implements Packet<ConnectionCheckAckPacket> {

    public ConnectionCheckAckPacket() {

    }

    @Override
    public ConnectionCheckAckPacket fromBytes(DataInputStream buf) {
        return new ConnectionCheckAckPacket();
    }

    @Override
    public void toBytes(DataOutputStream buf) {

    }
}
