package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.gui.GuiEnhancedControls;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GameSettings;
import net.minecraft.src.GuiOptions;
import net.minecraft.src.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiOptions.class)
public abstract class GuiOptionsMixin {
    @Accessor
    public abstract GameSettings getOptions();

    @Redirect(method = "actionPerformed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/src/GuiScreen;)V", ordinal = 0))
    public void useEnhancedControls(Minecraft instance, GuiScreen guiScreen) {
        instance.displayGuiScreen(new GuiEnhancedControls((GuiOptions) (Object) this, this.getOptions()));
    }
}
