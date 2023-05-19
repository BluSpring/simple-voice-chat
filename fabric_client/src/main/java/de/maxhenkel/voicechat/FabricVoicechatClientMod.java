package de.maxhenkel.voicechat;

import net.fabricmc.api.ClientModInitializer;

public class FabricVoicechatClientMod extends VoicechatClient implements ClientModInitializer {
    public static FabricVoicechatClientMod instance;

    @Override
    public void onInitializeClient() {
        instance = this;
    }
}
