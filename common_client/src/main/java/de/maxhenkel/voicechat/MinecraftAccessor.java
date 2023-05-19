package de.maxhenkel.voicechat;

import net.minecraft.client.Minecraft;

import java.util.concurrent.atomic.AtomicReference;

public interface MinecraftAccessor {
    AtomicReference<Minecraft> instance = new AtomicReference<>();

    static void setInstance(Minecraft minecraft) {
        instance.set(minecraft);
    }

    static Minecraft getMinecraft() {
        return instance.get();
    }
}
