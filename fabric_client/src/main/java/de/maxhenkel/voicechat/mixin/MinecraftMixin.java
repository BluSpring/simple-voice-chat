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

    @Inject(method = "checkGLError", at = @At(value = "INVOKE", target = "Ljava/io/PrintStream;println(Ljava/lang/String;)V", ordinal = 2, shift = At.Shift.AFTER))
    public void addStackTrace(String par1, CallbackInfo ci) {
        StackTraceElement[] es2 = Thread.currentThread().getStackTrace();
        for (StackTraceElement e2 : es2) {
            System.out.println(" in class:" + e2.getClassName() + " in source file:" + e2.getFileName() + " in method:" + e2.getMethodName() + " at line:" + e2.getLineNumber() + " " + (e2.isNativeMethod() ? "native" : ""));
        }
    }
}
