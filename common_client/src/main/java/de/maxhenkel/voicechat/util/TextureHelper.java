package de.maxhenkel.voicechat.util;

import de.maxhenkel.voicechat.MinecraftAccessor;
import de.maxhenkel.voicechat.Voicechat;
import net.minecraft.client.Minecraft;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TextureHelper {
    private static final Minecraft mc = MinecraftAccessor.getMinecraft();
    public static final float FONT_HEIGHT = 7.99F;

    private static final Map<String, BufferedImage> imageMap = new ConcurrentHashMap<>();
    private static final Map<String, Integer> imageToIdMap = new ConcurrentHashMap<>();

    public static String format(String namespace, String path) {
        return String.format("/assets/%s/%s", namespace, path);
    }

    public static void refreshTextures() {
        imageMap.clear();
        imageToIdMap.clear();
    }

    public static void bindTexture(String path) {
        if (!imageToIdMap.containsKey(path)) {
            BufferedImage image;

            if (!imageMap.containsKey(path)) {
                try {
                    InputStream stream = Voicechat.class.getResourceAsStream(path);
                    image = ImageIO.read(stream);
                    stream.close();
                    image.flush();

                    imageMap.put(path, image);
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            } else {
                image = imageMap.get(path);
            }

            int id = mc.renderEngine.allocateAndSetupTexture(image);

            imageToIdMap.put(path, id);
            mc.renderEngine.bindTexture(id);
        } else {
            int id = imageToIdMap.get(path);
            mc.renderEngine.bindTexture(id);
        }
    }
}
