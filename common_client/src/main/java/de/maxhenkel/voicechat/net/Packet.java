package de.maxhenkel.voicechat.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Packet<T extends Packet<T>> {

    String getIdentifier();

    T fromBytes(DataInputStream buf) throws IOException;

    void toBytes(DataOutputStream buf) throws IOException;

}
