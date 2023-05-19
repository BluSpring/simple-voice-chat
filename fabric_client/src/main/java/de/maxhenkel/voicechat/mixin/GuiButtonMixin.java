package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.extensions.GuiButtonExtension;
import net.minecraft.src.GuiButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiButton.class)
public class GuiButtonMixin implements GuiButtonExtension {
    @Shadow protected int width;

    @Shadow protected int height;

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }
}
