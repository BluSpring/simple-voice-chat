package de.maxhenkel.voicechat;

import net.fabricmc.api.ModInitializer;

public class FabricVoicechatMod extends Voicechat implements ModInitializer {
    @Override
    public void onInitialize() {
        this.initialize();
    }
}
