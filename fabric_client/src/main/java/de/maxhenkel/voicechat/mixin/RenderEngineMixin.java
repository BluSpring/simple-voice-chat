package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.gui.GameProfileUtils;
import de.maxhenkel.voicechat.util.TextureHelper;
import net.minecraft.src.RenderEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderEngine.class)
public class RenderEngineMixin {
    @Inject(method = "refreshTextures", at = @At("HEAD"))
    public void resetTextureHelper(CallbackInfo ci) {
        TextureHelper.refreshTextures();
        GameProfileUtils.refreshTextures();
    }
}
