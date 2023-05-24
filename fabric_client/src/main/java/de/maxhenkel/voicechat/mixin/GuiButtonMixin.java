package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.extensions.GuiButtonExtension;
import net.minecraft.src.GuiButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiButton.class)
public abstract class GuiButtonMixin implements GuiButtonExtension {
    @Accessor
    public abstract void setWidth(int width);

    @Accessor
    public abstract void setHeight(int height);

    @Accessor
    public abstract int getWidth();

    @Accessor
    public abstract int getHeight();
}
