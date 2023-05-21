package de.maxhenkel.voicechat.voice.common;

import javax.annotation.Nullable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class PlayerSoundPacket extends SoundPacket<PlayerSoundPacket> {

    protected boolean whispering;
    protected float distance;

    public PlayerSoundPacket(UUID sender, byte[] data, long sequenceNumber, boolean whispering, float distance, @Nullable String category) {
        super(sender, data, sequenceNumber, category);
        this.whispering = whispering;
        this.distance = distance;
    }

    public PlayerSoundPacket(UUID sender, short[] data, boolean whispering, float distance, @Nullable String category) {
        super(sender, data, category);
        this.whispering = whispering;
        this.distance = distance;
    }

    public PlayerSoundPacket() {

    }

    public UUID getSender() {
        return sender;
    }

    public boolean isWhispering() {
        return whispering;
    }

    public float getDistance() {
        return distance;
    }

    @Override
    public PlayerSoundPacket fromBytes(DataInputStream buf) throws IOException {
        PlayerSoundPacket soundPacket = new PlayerSoundPacket();
        soundPacket.sender = UUID.fromString(buf.readUTF());

        int length = buf.readInt();
        soundPacket.data = new byte[length];

        for (int i = 0; i < length; i++) {
            soundPacket.data[i] = buf.readByte();
        }

        soundPacket.sequenceNumber = buf.readLong();
        soundPacket.distance = buf.readFloat();

        byte data = buf.readByte();
        soundPacket.whispering = hasFlag(data, WHISPER_MASK);
        if (hasFlag(data, HAS_CATEGORY_MASK)) {
            soundPacket.category = buf.readUTF();
        }
        return soundPacket;
    }

    @Override
    public void toBytes(DataOutputStream buf) throws IOException {
        buf.writeUTF(sender.toString());
        buf.writeInt(data.length);
        buf.write(data);
        buf.writeLong(sequenceNumber);
        buf.writeFloat(distance);

        byte data = 0b0;
        if (whispering) {
            data = setFlag(data, WHISPER_MASK);
        }
        if (category != null) {
            data = setFlag(data, HAS_CATEGORY_MASK);
        }
        buf.writeByte(data);
        if (category != null) {
            buf.writeUTF(category);
        }
    }

}
