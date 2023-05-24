package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.net.Packet135ClientCustomPayload;
import de.maxhenkel.voicechat.net.Packet136ServerCustomPayload;
import net.minecraft.src.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Packet.class)
public abstract class PacketMixin {
    @Invoker
    private static void invokeAddIdClassMapping(int i, Class class_) {}

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void registerCustomPayloadPackets(CallbackInfo ci) {
        invokeAddIdClassMapping(135, Packet135ClientCustomPayload.class);
        invokeAddIdClassMapping(136, Packet136ServerCustomPayload.class);
    }
}
