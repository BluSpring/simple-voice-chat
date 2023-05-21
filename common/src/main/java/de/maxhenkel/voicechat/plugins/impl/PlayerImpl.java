package de.maxhenkel.voicechat.plugins.impl;

import de.maxhenkel.voicechat.api.Player;
import net.minecraft.src.EntityPlayer;

public class PlayerImpl extends EntityImpl implements Player {

    public PlayerImpl(EntityPlayer entity) {
        super(entity);
    }

    @Override
    public Object getPlayer() {
        return entity;
    }

    public EntityPlayer getRealPlayer() {
        return (EntityPlayer) entity;
    }

}
