package de.maxhenkel.voicechat.util;

import de.maxhenkel.voicechat.MinecraftAccessor;
import net.minecraft.client.Minecraft;

public class TextureHelper {
    private static final Minecraft mc = MinecraftAccessor.getMinecraft();
    public static final float FONT_HEIGHT = 7.99F;

    public static String format(String namespace, String path) {
        return String.format("/%s/%s", namespace, path);
    }

    public static void bindTexture(String path) {
        int id = mc.renderEngine.getTexture(path);
        mc.renderEngine.bindTexture(id);
    }
}
