package de.maxhenkel.voicechat.plugins.impl;

import de.maxhenkel.voicechat.api.ServerLevel;
import net.minecraft.src.World;

public class ServerLevelImpl implements ServerLevel {

    private final World serverLevel;

    public ServerLevelImpl(World serverLevel) {
        this.serverLevel = serverLevel;
    }

    @Override
    public Object getServerLevel() {
        return serverLevel;
    }
}
