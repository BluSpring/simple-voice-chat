package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.extensions.NetClientHandlerExtension;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NetClientHandler.class)
public class NetClientHandlerMixin implements NetClientHandlerExtension {
    @Shadow private NetworkManager netManager;

    @Override
    public NetworkManager getNetworkManager() {
        return netManager;
    }
}
