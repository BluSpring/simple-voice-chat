package de.maxhenkel.voicechat.plugins.impl;

import de.maxhenkel.voicechat.api.ServerLevel;
import de.maxhenkel.voicechat.api.ServerPlayer;
import net.minecraft.src.EntityPlayer;

public class ServerPlayerImpl extends PlayerImpl implements ServerPlayer {

    public ServerPlayerImpl(EntityPlayer entity) {
        super(entity);
    }

    public EntityPlayer getRealServerPlayer() {
        return (EntityPlayer) entity;
    }

    @Override
    public ServerLevel getServerLevel() {
        return new ServerLevelImpl(entity.worldObj);
    }
}
