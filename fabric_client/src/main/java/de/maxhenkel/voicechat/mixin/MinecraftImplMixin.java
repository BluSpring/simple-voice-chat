package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.MinecraftAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftApplet;
import net.minecraft.src.MinecraftImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(MinecraftImpl.class)
public class MinecraftImplMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    public void assignMinecraftInstance(Component canvas, Canvas minecraftApplet, MinecraftApplet i, int j, int bl, boolean frame, Frame par7, CallbackInfo ci) {
        MinecraftAccessor.setInstance((Minecraft) (Object) this);
    }
}
