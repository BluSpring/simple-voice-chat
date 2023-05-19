package de.maxhenkel.voicechat.util;

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class ActiveRenderInfo {
    private static final IntBuffer VIEWPORT = GLAllocation.createDirectIntBuffer(16);
    private static final FloatBuffer MODELVIEW = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer PROJECTION = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer OBJECTCOORDS = GLAllocation.createDirectFloatBuffer(3);
    private static Vec3D position = Vec3D.createVector(0.0D, 0.0D, 0.0D);
    private static float rotationX;
    private static float rotationXZ;
    private static float rotationZ;
    private static float rotationYZ;
    private static float rotationXY;

    public static void updateRenderInfo(EntityPlayer entityplayerIn, boolean p_74583_1_)
    {
        updateRenderInfo((Entity) entityplayerIn, p_74583_1_);
    }

    public static void updateRenderInfo(Entity entityplayerIn, boolean p_74583_1_)
    {
        GL11.glGetFloat(GL11.GL_MODELVIEW, MODELVIEW);
        GL11.glGetFloat(GL11.GL_PROJECTION, PROJECTION);
        GL11.glGetInteger(GL11.GL_VIEWPORT, VIEWPORT);
        float f = (float)((VIEWPORT.get(0) + VIEWPORT.get(2)) / 2);
        float f1 = (float)((VIEWPORT.get(1) + VIEWPORT.get(3)) / 2);
        GLU.gluUnProject(f, f1, 0.0F, MODELVIEW, PROJECTION, VIEWPORT, OBJECTCOORDS);
        position = Vec3D.createVector((double)OBJECTCOORDS.get(0), (double)OBJECTCOORDS.get(1), (double)OBJECTCOORDS.get(2));
        int i = p_74583_1_ ? 1 : 0;
        float f2 = entityplayerIn.rotationPitch;
        float f3 = entityplayerIn.rotationYaw;
        rotationX = MathHelper.cos(f3 * 0.017453292F) * (float)(1 - i * 2);
        rotationZ = MathHelper.sin(f3 * 0.017453292F) * (float)(1 - i * 2);
        rotationYZ = -rotationZ * MathHelper.sin(f2 * 0.017453292F) * (float)(1 - i * 2);
        rotationXY = rotationX * MathHelper.sin(f2 * 0.017453292F) * (float)(1 - i * 2);
        rotationXZ = MathHelper.cos(f2 * 0.017453292F);
    }

    public static Vec3D projectViewFromEntity(Entity entityIn, double p_178806_1_)
    {
        double d0 = entityIn.prevPosX + (entityIn.posX - entityIn.prevPosX) * p_178806_1_;
        double d1 = entityIn.prevPosY + (entityIn.posY - entityIn.prevPosY) * p_178806_1_;
        double d2 = entityIn.prevPosZ + (entityIn.posZ - entityIn.prevPosZ) * p_178806_1_;
        double d3 = d0 + position.xCoord;
        double d4 = d1 + position.yCoord;
        double d5 = d2 + position.zCoord;
        return Vec3D.createVector(d3, d4, d5);
    }

    public static float getRotationX()
    {
        return rotationX;
    }

    public static float getRotationXZ()
    {
        return rotationXZ;
    }

    public static float getRotationZ()
    {
        return rotationZ;
    }

    public static float getRotationYZ()
    {
        return rotationYZ;
    }

    public static float getRotationXY()
    {
        return rotationXY;
    }

    /* ======================================== FORGE START =====================================*/

    /**
     * Vector from render view entity position (corrected for partialTickTime) to the middle of screen
     */
    public static Vec3D getCameraPosition()
    {
        return position;
    }
}
