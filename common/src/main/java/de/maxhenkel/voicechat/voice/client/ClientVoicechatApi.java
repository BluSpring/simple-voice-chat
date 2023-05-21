package de.maxhenkel.voicechat.voice.client;

import de.maxhenkel.voicechat.voice.common.SoundPacket;

public interface ClientVoicechatApi {
    void processSoundPacket(SoundPacket packet);
    ClientVoicechatConnectionApi getConnection();
}
