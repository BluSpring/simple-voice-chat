package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.net.Packet135ClientCustomPayload;
import de.maxhenkel.voicechat.net.Packet136ServerCustomPayload;
import net.minecraft.src.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Packet.class)
public abstract class PacketMixin {
    @Shadow
    static void addIdClassMapping(int i, Class class_) {}

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void registerCustomPayloadPackets(CallbackInfo ci) {
        addIdClassMapping(135, Packet135ClientCustomPayload.class);
        addIdClassMapping(136, Packet136ServerCustomPayload.class);
    }
}
