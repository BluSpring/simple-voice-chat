package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.extensions.NetClientHandlerExtension;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NetClientHandler.class)
public abstract class NetClientHandlerMixin implements NetClientHandlerExtension {
    @Accessor
    public abstract NetworkManager getNetManager();

    @Override
    public NetworkManager getNetworkManager() {
        return getNetManager();
    }
}
