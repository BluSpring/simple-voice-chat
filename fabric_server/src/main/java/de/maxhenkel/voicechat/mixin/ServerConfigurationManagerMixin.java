package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.intercompatibility.FabricCommonCompatibilityManager;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.NetLoginHandler;
import net.minecraft.src.ServerConfigurationManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerConfigurationManager.class)
public class ServerConfigurationManagerMixin {
    @Inject(method = "playerLoggedIn", at = @At("HEAD"))
    public void playerLogIn(EntityPlayerMP par1, CallbackInfo ci) {
        FabricCommonCompatibilityManager.instance.onPlayerLogIn(par1);
    }

    @Inject(method = "playerLoggedOut", at = @At("HEAD"))
    public void playerLogOut(EntityPlayerMP par1, CallbackInfo ci) {
        FabricCommonCompatibilityManager.instance.onPlayerLogOut(par1);
    }
}
