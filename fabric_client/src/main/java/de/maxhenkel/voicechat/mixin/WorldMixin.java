package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.intercompatibility.FabricClientCompatibilityManager;
import net.minecraft.src.Entity;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class WorldMixin {
    @Inject(method = "entityJoinedWorld", at = @At("HEAD"))
    public void entityJoinWorld(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        FabricClientCompatibilityManager.getInstance().onJoinWorld(entity);
    }
}
