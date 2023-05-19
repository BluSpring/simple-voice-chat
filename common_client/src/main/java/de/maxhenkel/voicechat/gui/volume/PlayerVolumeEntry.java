package de.maxhenkel.voicechat.gui.volume;

import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.extensions.GuiExtension;
import de.maxhenkel.voicechat.gui.GameProfileUtils;
import de.maxhenkel.voicechat.util.TextureHelper;
import de.maxhenkel.voicechat.voice.common.PlayerState;
import net.minecraft.src.Gui;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.UUID;

public class PlayerVolumeEntry extends VolumeEntry {

    @Nullable
    protected final PlayerState state;

    public PlayerVolumeEntry(@Nullable PlayerState state, AdjustVolumesScreen screen) {
        super(screen, new PlayerVolumeConfigEntry(state != null ? state.getUuid() : new UUID(0L, 0L)));
        this.state = state;
    }

    @Nullable
    public PlayerState getState() {
        return state;
    }

    @Override
    public void renderElement(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks, int skinX, int skinY, int textX, int textY) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        if (state != null) {
            GameProfileUtils.bindSkinTexture(state.getName());
            ((GuiExtension) screen).drawScaledCustomSizeModalRect(skinX, skinY, 8F, 8F, 8, 8, SKIN_SIZE, SKIN_SIZE, 64F, 64F);
            GL11.glEnable(GL11.GL_BLEND);
            ((GuiExtension) screen).drawScaledCustomSizeModalRect(skinX, skinY, 40F, 8F, 8, 8, SKIN_SIZE, SKIN_SIZE, 64F, 64F);
            GL11.glDisable(GL11.GL_BLEND);
            minecraft.fontRenderer.drawString(state.getName(), textX, textY, PLAYER_NAME_COLOR);
        } else {
            TextureHelper.bindTexture(OTHER_VOLUME_ICON);
            ((GuiExtension) screen).drawScaledCustomSizeModalRect(skinX, skinY, 16, 16, 16, 16, SKIN_SIZE, SKIN_SIZE, 16, 16);
            minecraft.fontRenderer.drawString(OTHER_VOLUME, textX, textY, PLAYER_NAME_COLOR);
            if (isSelected) {
                screen.postRender(() -> {
                    screen.drawHoveringText(OTHER_VOLUME_DESCRIPTION, mouseX, mouseY);
                });
            }
        }
    }

    public static class PlayerVolumeConfigEntry implements AdjustVolumeSlider.VolumeConfigEntry {

        private final UUID playerUUID;

        public PlayerVolumeConfigEntry(UUID playerUUID) {
            this.playerUUID = playerUUID;
        }

        @Override
        public void save(double value) {
            VoicechatClient.VOLUME_CONFIG.setPlayerVolume(playerUUID, value);
            VoicechatClient.VOLUME_CONFIG.save();
        }

        @Override
        public double get() {
            return VoicechatClient.VOLUME_CONFIG.getPlayerVolume(playerUUID);
        }
    }

}
