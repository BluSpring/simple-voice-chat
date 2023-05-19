package de.maxhenkel.voicechat.voice.common;

import net.minecraft.network.PacketBuffer;

import javax.annotation.Nullable;
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
    public PlayerSoundPacket fromBytes(PacketBuffer buf) {
        PlayerSoundPacket soundPacket = new PlayerSoundPacket();
        soundPacket.sender = buf.readUniqueId();
        soundPacket.data = buf.readByteArray();
        soundPacket.sequenceNumber = buf.readLong();
        soundPacket.distance = buf.readFloat();

        byte data = buf.readByte();
        soundPacket.whispering = hasFlag(data, WHISPER_MASK);
        if (hasFlag(data, HAS_CATEGORY_MASK)) {
            soundPacket.category = buf.readString(16);
        }
        return soundPacket;
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeUniqueId(sender);
        buf.writeByteArray(data);
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
            buf.writeString(category);
        }
    }

}
