package de.maxhenkel.voicechat.net;

import de.maxhenkel.voicechat.util.ConnectionUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class LeaveGroupPacket implements Packet<LeaveGroupPacket> {

    public static final String LEAVE_GROUP = ConnectionUtil.format(NetManager.CHANNEL, "leave_group");

    public LeaveGroupPacket() {

    }

    @Override
    public String getIdentifier() {
        return LEAVE_GROUP;
    }

    @Override
    public LeaveGroupPacket fromBytes(DataInputStream buf) {
        return this;
    }

    @Override
    public void toBytes(DataOutputStream buf) {

    }

}
