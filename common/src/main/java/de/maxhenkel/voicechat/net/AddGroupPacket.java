package de.maxhenkel.voicechat.net;

import de.maxhenkel.voicechat.util.ConnectionUtil;
import de.maxhenkel.voicechat.voice.common.ClientGroup;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class AddGroupPacket implements Packet<AddGroupPacket> {

    public static final String ADD_ADD_GROUP = ConnectionUtil.format(NetManager.CHANNEL, "add_group");

    private ClientGroup group;

    public AddGroupPacket() {

    }

    public AddGroupPacket(ClientGroup group) {
        this.group = group;
    }

    public ClientGroup getGroup() {
        return group;
    }

    @Override
    public String getIdentifier() {
        return ADD_ADD_GROUP;
    }

    @Override
    public AddGroupPacket fromBytes(DataInputStream buf) throws IOException {
        group = ClientGroup.fromBytes(buf);
        return this;
    }

    @Override
    public void toBytes(DataOutputStream buf) throws IOException {
        group.toBytes(buf);
    }

}
