package de.maxhenkel.voicechat.voice.client;

import de.maxhenkel.voicechat.MinecraftAccessor;
import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.api.VolumeCategory;
import de.maxhenkel.voicechat.gui.volume.AdjustVolumeList;
import de.maxhenkel.voicechat.intercompatibility.ClientCompatibilityManager;
import de.maxhenkel.voicechat.intercompatibility.CommonCompatibilityManager;
import de.maxhenkel.voicechat.net.AddCategoryPacket;
import de.maxhenkel.voicechat.net.ClientChannel;
import de.maxhenkel.voicechat.net.RemoveCategoryPacket;
import de.maxhenkel.voicechat.plugins.CategoryManager;
import de.maxhenkel.voicechat.plugins.impl.VolumeCategoryImpl;
import de.maxhenkel.voicechat.util.TextureHelper;
import net.minecraft.client.Minecraft;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientCategoryManager extends CategoryManager implements ClientCategoryManagerApi {

    protected final Map<String, CustomTextureObject> images;

    public ClientCategoryManager() {
        images = new ConcurrentHashMap<>();
        ((ClientChannel<AddCategoryPacket>) CommonCompatibilityManager.INSTANCE.getNetManager().addCategoryChannel).setClientListener((client, handler, packet) -> {
            addCategory(packet.getCategory());
            Voicechat.logDebug("Added category {}", packet.getCategory().getId());
        });
        ((ClientChannel<RemoveCategoryPacket>) CommonCompatibilityManager.INSTANCE.getNetManager().removeCategoryChannel).setClientListener((client, handler, packet) -> {
            removeCategory(packet.getCategoryId());
            Voicechat.logDebug("Removed category {}", packet.getCategoryId());
        });
        ClientCompatibilityManager.INSTANCE.onDisconnect(this::clear);
    }

    @Override
    public void addCategory(VolumeCategory category) {
        super.addCategory(category);

        if (category.getIcon() != null) {
            registerImage(category.getId(), fromIntArray(category.getIcon()));
        }
        AdjustVolumeList.update();
    }

    @Override
    @Nullable
    public VolumeCategory removeCategory(String categoryId) {
        VolumeCategory volumeCategory = super.removeCategory(categoryId);
        unRegisterImage(categoryId);
        AdjustVolumeList.update();
        return volumeCategory;
    }

    public void clear() {
        categories.keySet().forEach(this::unRegisterImage);
        categories.clear();
    }

    private void registerImage(String id, BufferedImage image) {
        String resourceLocation = TextureHelper.format(Voicechat.MODID, "category_" + id);//Minecraft.getMinecraft().getEntityRenderDispatcher().textureManager.register(id, new CustomTextureObject(image));
        images.put(id, new CustomTextureObject(resourceLocation, image));
    }

    private void unRegisterImage(String id) {
        CustomTextureObject customTextureObject = images.get(id);
        if (customTextureObject != null) {
            customTextureObject.deleteGlTexture();
            images.remove(id);
        }
    }

    private BufferedImage fromIntArray(int[][] icon) {
        if (icon.length != 16) {
            throw new IllegalStateException("Icon is not 16x16");
        }
        BufferedImage nativeImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < icon.length; x++) {
            if (icon[x].length != 16) {
                throw new IllegalStateException("Icon is not 16x16");
            }
            for (int y = 0; y < icon.length; y++) {
                nativeImage.setRGB(x, y, icon[x][y]);
            }
        }
        return nativeImage;
    }

    public String getTexture(String id, String defaultImage) {
        CustomTextureObject customTextureObject = images.get(id);
        if (customTextureObject == null) {
            return defaultImage;
        }
        return customTextureObject.getResourceLocation();
    }

    private static class CustomTextureObject {
        private static final Minecraft mc = MinecraftAccessor.getMinecraft();

        private final BufferedImage image;
        private final String resourceLocation;
        private int id = -1;

        public CustomTextureObject(String textureResourceLocation, BufferedImage image) {
            this.resourceLocation = textureResourceLocation;
            this.image = image;
        }

        public void uploadGlTexture() {
            if (id != -1)
                deleteGlTexture();
            id = mc.renderEngine.allocateAndSetupTexture(this.image);
        }

        public void bindTexture() {
            this.uploadGlTexture();
            mc.renderEngine.bindTexture(id);
        }

        public void deleteGlTexture() {
            if (id != -1)
                mc.renderEngine.deleteTexture(id);
            else {
                int index = mc.renderEngine.getTexture(resourceLocation);
                mc.renderEngine.deleteTexture(index);
            }
        }

        public String getResourceLocation() {
            return resourceLocation;
        }
    }

}
