package de.maxhenkel.voicechat.mixin;

import net.minecraft.src.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.net.Socket;

@Mixin(NetworkManager.class)
public interface NetworkManagerAccessor {
    @Accessor
    Socket getNetworkSocket();
}
