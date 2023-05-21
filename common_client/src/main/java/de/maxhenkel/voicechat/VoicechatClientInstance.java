package de.maxhenkel.voicechat;

import de.maxhenkel.voicechat.voice.client.*;

public class VoicechatClientInstance implements VoicechatClientHelper {

    @Override
    public boolean hasConfig() {
        return VoicechatClient.CLIENT_CONFIG != null;
    }

    @Override
    public float getDefaultDistance() {
        ClientVoicechat client = ClientManager.getClient();
        if (client == null) {
            return 48F;
        }
        ClientVoicechatConnection connection = client.getConnection();
        if (connection == null) {
            return 48F;
        }
        return (float) connection.getData().getVoiceChatDistance();
    }

    @Override
    public boolean isSingleplayer() {
        return !MinecraftAccessor.getMinecraft().isMultiplayerWorld();
    }

    @Override
    public ClientVoicechatApi getClient() {
        return ClientManager.getClient();
    }

    @Override
    public boolean shouldUseNatives() {
        return VoicechatClient.CLIENT_CONFIG.useNatives.get();
    }

    @Override
    public ClientPlayerStateManagerApi getPlayerStateManager() {
        return ClientManager.getPlayerStateManager();
    }

    @Override
    public ClientCategoryManagerApi getCategoryManager() {
        return ClientManager.getCategoryManager();
    }
}
