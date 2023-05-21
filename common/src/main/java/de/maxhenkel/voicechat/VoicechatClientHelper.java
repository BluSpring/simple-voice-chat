package de.maxhenkel.voicechat;

import de.maxhenkel.voicechat.voice.client.ClientCategoryManagerApi;
import de.maxhenkel.voicechat.voice.client.ClientPlayerStateManagerApi;
import de.maxhenkel.voicechat.voice.client.ClientVoicechatApi;

public interface VoicechatClientHelper {
    boolean hasConfig();
    float getDefaultDistance();
    boolean isSingleplayer();
    ClientVoicechatApi getClient();
    boolean shouldUseNatives();

    ClientPlayerStateManagerApi getPlayerStateManager();
    ClientCategoryManagerApi getCategoryManager();
}
