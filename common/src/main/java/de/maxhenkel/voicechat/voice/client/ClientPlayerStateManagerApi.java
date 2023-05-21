package de.maxhenkel.voicechat.voice.client;

import de.maxhenkel.voicechat.voice.common.ClientGroup;

import java.util.UUID;

public interface ClientPlayerStateManagerApi {
    boolean isMuted();
    boolean isDisabled();
    boolean isDisconnected();
    UUID getGroupID();
    ClientGroup getGroup();
}
