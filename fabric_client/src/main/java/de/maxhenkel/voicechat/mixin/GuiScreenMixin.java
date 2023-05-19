package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.intercompatibility.FabricClientCompatibilityManager;
import de.maxhenkel.voicechat.intercompatibility.FabricCommonCompatibilityManager;
import net.minecraft.src.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public class GuiScreenMixin {
    @Inject(method = "handleInput", at = @At("HEAD"))
    public void mouseTick(CallbackInfo ci) {
        FabricClientCompatibilityManager.getInstance().onTickMouse();
    }

    @Inject(method = "handleInput", at = @At("TAIL"))
    public void keyboardTick(CallbackInfo ci) {
        FabricClientCompatibilityManager.getInstance().onTickKey();
    }
}
