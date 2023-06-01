package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.FabricVoicechatClientMod;
import de.maxhenkel.voicechat.MinecraftAccessor;
import de.maxhenkel.voicechat.intercompatibility.FabricClientCompatibilityManager;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "startGame", at = @At("TAIL"))
    public void assignMinecraft(CallbackInfo ci) {
        MinecraftAccessor.setInstance((Minecraft) (Object) this);
        FabricVoicechatClientMod.instance.initializeClient();
    }

    @Inject(method = "runTick", at = @At("TAIL"))
    public void postTick(CallbackInfo ci) {
        FabricClientCompatibilityManager.getInstance().onInput();
    }

    @Inject(method = "changeWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityPlayerSP;preparePlayerToSpawn()V", ordinal = 0))
    public void joinWorld(World string, String entityPlayer, EntityPlayer par3, CallbackInfo ci) {
        FabricClientCompatibilityManager.getInstance().onJoinServer();
    }

    @Inject(method = "changeWorld", at = @At("HEAD"))
    public void disconnectEvent(World world, String entityPlayer, EntityPlayer par3, CallbackInfo ci) {
        if (world == null) {
            FabricClientCompatibilityManager.getInstance().onDisconnect();
        }
    }
}
