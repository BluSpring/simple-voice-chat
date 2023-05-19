package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.extensions.FontRendererExtension;
import net.minecraft.src.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FontRenderer.class)
public class FontRendererMixin implements FontRendererExtension {
}
