package de.maxhenkel.voicechat.net;

import de.maxhenkel.voicechat.util.ConnectionUtil;
import de.maxhenkel.voicechat.voice.common.PlayerState;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PlayerStatePacket implements Packet<PlayerStatePacket> {

    public static final String PLAYER_STATE = ConnectionUtil.format(NetManager.CHANNEL, "player_state");

    private PlayerState playerState;

    public PlayerStatePacket() {

    }

    public PlayerStatePacket(PlayerState playerState) {
        this.playerState = playerState;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    @Override
    public String getIdentifier() {
        return PLAYER_STATE;
    }

    @Override
    public PlayerStatePacket fromBytes(DataInputStream buf) throws IOException {
        playerState = PlayerState.fromBytes(buf);
        return this;
    }

    @Override
    public void toBytes(DataOutputStream buf) throws IOException {
        playerState.toBytes(buf);
    }

}
