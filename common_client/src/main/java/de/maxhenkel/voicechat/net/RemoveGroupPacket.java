package de.maxhenkel.voicechat.net;

import de.maxhenkel.voicechat.util.ConnectionUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class RemoveGroupPacket implements Packet<RemoveGroupPacket> {

    public static final String REMOVE_GROUP = ConnectionUtil.format(NetManager.CHANNEL, "remove_group");

    private UUID groupId;

    public RemoveGroupPacket() {

    }

    public RemoveGroupPacket(UUID groupId) {
        this.groupId = groupId;
    }

    public UUID getGroupId() {
        return groupId;
    }

    @Override
    public String getIdentifier() {
        return REMOVE_GROUP;
    }

    @Override
    public RemoveGroupPacket fromBytes(DataInputStream buf) throws IOException {
        groupId = UUID.fromString(buf.readUTF());
        return this;
    }

    @Override
    public void toBytes(DataOutputStream buf) throws IOException {
        buf.writeUTF(groupId.toString());
    }

}
