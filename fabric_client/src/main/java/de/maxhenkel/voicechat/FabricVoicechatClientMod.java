package de.maxhenkel.voicechat;

import net.fabricmc.api.ClientModInitializer;

public class FabricVoicechatClientMod extends VoicechatClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        this.initializeClient();
    }
}
