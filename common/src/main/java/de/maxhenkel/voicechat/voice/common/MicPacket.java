package de.maxhenkel.voicechat.voice.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MicPacket implements Packet<MicPacket> {

    private byte[] data;
    private boolean whispering;
    private long sequenceNumber;

    public MicPacket(byte[] data, boolean whispering, long sequenceNumber) {
        this.data = data;
        this.whispering = whispering;
        this.sequenceNumber = sequenceNumber;
    }

    public MicPacket() {

    }

    @Override
    public long getTTL() {
        return 500L;
    }

    public byte[] getData() {
        return data;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public boolean isWhispering() {
        return whispering;
    }

    @Override
    public MicPacket fromBytes(DataInputStream buf) throws IOException {
        MicPacket soundPacket = new MicPacket();
        int length = buf.readInt();
        soundPacket.data = new byte[length];

        for (int i = 0; i < length; i++) {
            soundPacket.data[i] = buf.readByte();
        }

        soundPacket.sequenceNumber = buf.readLong();
        soundPacket.whispering = buf.readBoolean();
        return soundPacket;
    }

    @Override
    public void toBytes(DataOutputStream buf) throws IOException {
        buf.writeInt(data.length);
        buf.write(data);
        buf.writeLong(sequenceNumber);
        buf.writeBoolean(whispering);
    }
}
