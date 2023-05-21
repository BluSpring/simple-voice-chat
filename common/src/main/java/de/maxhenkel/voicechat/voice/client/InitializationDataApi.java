package de.maxhenkel.voicechat.voice.client;

import java.util.UUID;

public interface InitializationDataApi {
    UUID getSecret();
    UUID getPlayerUUID();
    int getMtuSize();
}
