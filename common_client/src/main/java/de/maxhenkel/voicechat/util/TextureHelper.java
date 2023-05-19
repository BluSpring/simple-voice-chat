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

    public static String format(String namespace, String path) {
        return String.format("/assets/%s/%s", namespace, path);
    }

    public static void bindTexture(String path) {
        BufferedImage image;

        if (!imageMap.containsKey(path)) {
            try {
                InputStream stream = Voicechat.class.getResourceAsStream(path);
                image = ImageIO.read(stream);
                stream.close();

                imageMap.put(path, image);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        } else {
            image = imageMap.get(path);
        }

        int id = mc.renderEngine.allocateAndSetupTexture(image);
        mc.renderEngine.bindTexture(id);
    }
}
