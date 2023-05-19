package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.extensions.KeyBindingExtension;
import net.minecraft.src.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyBinding.class)
public class KeyBindingMixin implements KeyBindingExtension {
    @Shadow public int keyCode;

    @Override
    public boolean isPressed() {
        return Keyboard.isKeyDown(this.keyCode);
    }
}
