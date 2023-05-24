package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.extensions.KeyBindingExtension;
import net.minecraft.src.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin implements KeyBindingExtension {
    @Accessor
    public abstract int getKeyCode();

    @Override
    public boolean isPressed() {
        return Keyboard.isKeyDown(this.getKeyCode());
    }
}
