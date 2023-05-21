package de.maxhenkel.voicechat.voice.common;

import net.minecraft.src.Vec3D;

import javax.annotation.Nullable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class LocationSoundPacket extends SoundPacket<LocationSoundPacket> {

    protected Vec3D location;
    protected float distance;

    public LocationSoundPacket(UUID sender, Vec3D location, byte[] data, long sequenceNumber, float distance, @Nullable String category) {
        super(sender, data, sequenceNumber, category);
        this.location = location;
        this.distance = distance;
    }

    public LocationSoundPacket(UUID sender, short[] data, Vec3D location, float distance, @Nullable String category) {
        super(sender, data, category);
        this.location = location;
        this.distance = distance;
    }

    public LocationSoundPacket() {

    }

    public Vec3D getLocation() {
        return location;
    }

    public float getDistance() {
        return distance;
    }

    @Override
    public LocationSoundPacket fromBytes(DataInputStream buf) throws IOException {
        LocationSoundPacket soundPacket = new LocationSoundPacket();
        soundPacket.sender = UUID.fromString(buf.readUTF());
        soundPacket.location = Vec3D.createVector(buf.readDouble(), buf.readDouble(), buf.readDouble());
        int bytesToRead = buf.readInt();
        soundPacket.data = new byte[bytesToRead];
        for (int i = 0; i < bytesToRead; i++) {
            soundPacket.data[i] = buf.readByte();
        }
        soundPacket.sequenceNumber = buf.readLong();
        soundPacket.distance = buf.readFloat();

        byte data = buf.readByte();
        if (hasFlag(data, HAS_CATEGORY_MASK)) {
            soundPacket.category = buf.readUTF();
        }

        return soundPacket;
    }

    @Override
    public void toBytes(DataOutputStream buf) throws IOException {
        buf.writeUTF(sender.toString());
        buf.writeDouble(location.xCoord);
        buf.writeDouble(location.yCoord);
        buf.writeDouble(location.zCoord);
        buf.writeInt(data.length);
        buf.write(data);
        buf.writeLong(sequenceNumber);
        buf.writeFloat(distance);

        byte data = 0b0;
        if (category != null) {
            data = setFlag(data, HAS_CATEGORY_MASK);
        }
        buf.writeByte(data);
        if (category != null) {
            buf.writeUTF(category);
        }
    }
}
