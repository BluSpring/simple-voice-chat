package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.extensions.NetHandlerExtension;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(NetHandler.class)
public class NetHandlerMixin implements NetHandlerExtension {

    @Override
    public void processCustomPayload(Packet packet) {

    }
}
