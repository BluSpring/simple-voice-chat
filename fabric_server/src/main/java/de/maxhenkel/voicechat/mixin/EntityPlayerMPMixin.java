package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.intercompatibility.FabricCommonCompatibilityManager;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerMP.class)
public class EntityPlayerMPMixin {
    @Inject(method = "onDeath", at = @At("HEAD"))
    public void onPlayerDeath(DamageSource par1, CallbackInfo ci) {
        //FabricCommonCompatibilityManager.instance.onPlayerLogOut((EntityPlayerMP) (Object) this);
    }
}
