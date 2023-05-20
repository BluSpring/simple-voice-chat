package de.maxhenkel.voicechat;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.util.concurrent.atomic.AtomicReference;

public interface MinecraftAccessor {
    AtomicReference<Minecraft> instance = new AtomicReference<>();

    static void setInstance(Minecraft minecraft) {
        instance.set(minecraft);
    }

    static Minecraft getMinecraft() {
        return instance.get();
    }

    static void checkGLError(String label) {
        int var2 = GL11.glGetError();
        if (var2 != 0) {
            String var3 = GLU.gluErrorString(var2);
            System.out.println("########## GL ERROR ##########");
            System.out.println("@ " + label);
            System.out.println(var2 + ": " + var3);
            System.exit(0);
        }
    }
}
