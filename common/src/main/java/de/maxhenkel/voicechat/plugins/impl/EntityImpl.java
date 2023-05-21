package de.maxhenkel.voicechat.plugins.impl;

import de.maxhenkel.voicechat.api.Entity;
import de.maxhenkel.voicechat.api.Position;
import net.minecraft.src.Vec3D;

import java.util.UUID;

public class EntityImpl implements Entity {
    private final UUID uuid = UUID.randomUUID();


    protected net.minecraft.src.Entity entity;

    public EntityImpl(net.minecraft.src.Entity entity) {
        this.entity = entity;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public Object getEntity() {
        return entity;
    }

    @Override
    public Position getPosition() {
        return new PositionImpl(Vec3D.createVector(entity.posX, entity.posY, entity.posZ));
    }

    public net.minecraft.src.Entity getRealEntity() {
        return entity;
    }

}
