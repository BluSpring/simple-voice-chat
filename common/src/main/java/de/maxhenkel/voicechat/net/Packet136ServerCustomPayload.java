package de.maxhenkel.voicechat.net;

import de.maxhenkel.voicechat.extensions.NetHandlerExtension;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet136ServerCustomPayload extends Packet {
    private String channel;
    private byte[] buf;

    public Packet136ServerCustomPayload() {}

    public Packet136ServerCustomPayload(String channel, byte[] buf) {
        this.channel = channel;
        this.buf = buf;

        if (buf.length > 1048576) {
            throw new IllegalArgumentException("Payload may not be larger than 1048576 bytes");
        }
    }

    @Override
    public void readPacketData(DataInputStream dataInputStream) {
        try {
            this.channel = dataInputStream.readUTF();
            int i = dataInputStream.readInt();

            if (i >= 0 && i <= 1048576) {
                this.buf = new byte[i];
                for (int j = 0; j < i; j++) {
                    this.buf[j] = dataInputStream.readByte();
                }
            } else {
                throw new IOException("Payload may not be larger than 1048576 bytes");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writePacketData(DataOutputStream dataOutputStream) {
        try {
            dataOutputStream.writeUTF(this.channel);
            dataOutputStream.writeInt(this.buf.length);
            dataOutputStream.write(this.buf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        ((NetHandlerExtension) netHandler).processCustomPayload(this);

        if (this.buf != null) {
            this.buf = null;
        }
    }

    int utfLength = -1;

    @Override
    public int getPacketSize() {
        if (utfLength == -1) {
            int strLength = this.channel.length();
            utfLength = strLength;

            for (int i = 0; i < strLength; i++) {
                int c = this.channel.charAt(i);
                if (c >= 0x80 || c == 0)
                    utfLength += (c >= 0x800) ? 2 : 1;
            }
        }

        return utfLength + 4 + this.buf.length;
    }

    public byte[] getBuf() {
        return buf;
    }

    public String getChannel() {
        return channel;
    }
}
