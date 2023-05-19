package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.extensions.NetHandlerExtension;
import de.maxhenkel.voicechat.net.FabricNetworkEvents;
import de.maxhenkel.voicechat.net.Packet135ClientCustomPayload;
import net.minecraft.src.NetHandler;
import net.minecraft.src.NetServerHandler;
import net.minecraft.src.Packet;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(NetHandler.class)
public class NetHandlerMixin implements NetHandlerExtension {
    @Override
    public void processCustomPayload(Packet packet) {
        if (packet instanceof Packet135ClientCustomPayload) {
            FabricNetworkEvents.onCustomPayloadServer((Packet135ClientCustomPayload) packet, Voicechat.serverInstance.getPlayerByName(((NetServerHandler) (Object) this).getUsername()));
        }
    }
}
