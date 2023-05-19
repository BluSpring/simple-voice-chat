package de.maxhenkel.voicechat.net;

import de.maxhenkel.voicechat.util.ConnectionUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UpdateStatePacket implements Packet<UpdateStatePacket> {

    public static final String PLAYER_STATE = ConnectionUtil.format(NetManager.CHANNEL, "update_state");

    private boolean disabled;

    public UpdateStatePacket() {

    }

    public UpdateStatePacket(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public String getIdentifier() {
        return PLAYER_STATE;
    }

    @Override
    public UpdateStatePacket fromBytes(DataInputStream buf) throws IOException {
        disabled = buf.readBoolean();
        return this;
    }

    @Override
    public void toBytes(DataOutputStream buf) throws IOException {
        buf.writeBoolean(disabled);
    }

}
