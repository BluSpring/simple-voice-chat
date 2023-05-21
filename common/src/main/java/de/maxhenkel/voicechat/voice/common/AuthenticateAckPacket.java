package de.maxhenkel.voicechat.voice.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class AuthenticateAckPacket implements Packet<AuthenticateAckPacket> {

    public AuthenticateAckPacket() {

    }

    @Override
    public AuthenticateAckPacket fromBytes(DataInputStream buf) {
        return new AuthenticateAckPacket();
    }

    @Override
    public void toBytes(DataOutputStream buf) {

    }
}
