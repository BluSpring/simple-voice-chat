package de.maxhenkel.voicechat.voice.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Packet<T extends Packet> {

    T fromBytes(DataInputStream buf) throws IOException;

    void toBytes(DataOutputStream buf) throws IOException;

    default long getTTL() {
        return 10_000L;
    }

}
