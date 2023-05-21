package de.maxhenkel.voicechat.plugins.impl;

import de.maxhenkel.voicechat.api.Position;
import net.minecraft.src.Vec3D;

public class PositionImpl implements Position {

    private final Vec3D position;

    public PositionImpl(Vec3D position) {
        this.position = position;
    }

    public PositionImpl(double x, double y, double z) {
        this.position = Vec3D.createVector(x, y, z);
    }

    @Override
    public double getX() {
        return position.xCoord;
    }

    @Override
    public double getY() {
        return position.yCoord;
    }

    @Override
    public double getZ() {
        return position.zCoord;
    }

    public Vec3D getPosition() {
        return position;
    }
}
