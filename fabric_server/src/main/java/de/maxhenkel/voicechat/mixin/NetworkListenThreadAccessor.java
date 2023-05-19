package de.maxhenkel.voicechat.mixin;

import net.minecraft.src.NetworkListenThread;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.net.ServerSocket;

@Mixin(NetworkListenThread.class)
public interface NetworkListenThreadAccessor {
    @Accessor
    ServerSocket getServerSocket();
}
