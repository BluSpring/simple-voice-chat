package de.maxhenkel.voicechat.voice.common;

import javax.annotation.Nullable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class GroupSoundPacket extends SoundPacket<GroupSoundPacket> {

    public GroupSoundPacket(UUID sender, byte[] data, long sequenceNumber, @Nullable String category) {
        super(sender, data, sequenceNumber, category);
    }

    public GroupSoundPacket(UUID sender, short[] data, @Nullable String category) {
        super(sender, data, category);
    }

    public GroupSoundPacket() {

    }

    @Override
    public GroupSoundPacket fromBytes(DataInputStream buf) throws IOException {
        GroupSoundPacket soundPacket = new GroupSoundPacket();
        soundPacket.sender = UUID.fromString(buf.readUTF());
        int length = buf.readInt();
        soundPacket.data = new byte[length];

        for (int i = 0; i < length; i++) {
            soundPacket.data[i] = buf.readByte();
        }

        soundPacket.sequenceNumber = buf.readLong();

        byte data = buf.readByte();
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
