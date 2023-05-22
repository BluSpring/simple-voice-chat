package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.VoicechatServerImpl;
import de.maxhenkel.voicechat.intercompatibility.FabricCommonCompatibilityManager;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "func_6008_d", at = @At("TAIL"))
    public void serverStart(CallbackInfoReturnable<Boolean> cir) {
        FabricCommonCompatibilityManager.instance.onServerStart();
    }

    @Inject(method = "func_6013_g", at = @At("HEAD"))
    public void serverStop(CallbackInfo ci) {
        FabricCommonCompatibilityManager.instance.onServerStop();
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void assignMinecraftServerProxy(CallbackInfo ci) {
        Voicechat.serverInstance = new VoicechatServerImpl((MinecraftServer) (Object) this);
    }
}
