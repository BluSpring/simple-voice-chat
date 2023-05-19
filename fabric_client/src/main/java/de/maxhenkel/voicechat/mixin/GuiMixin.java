package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.extensions.GuiExtension;
import net.minecraft.src.Gui;
import net.minecraft.src.Tessellator;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Gui.class)
public class GuiMixin implements GuiExtension {
    @Override
    public void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
        GuiExtension.staticDrawModalRectWithCustomSizedTexture(x, y, u, v, width, height, textureWidth, textureHeight);
    }

    @Override
    public void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        GuiExtension.staticDrawScaledCustomSizeModalRect(x, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
    }
}
