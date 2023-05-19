package de.maxhenkel.voicechat.gui.group;

import de.maxhenkel.voicechat.MinecraftAccessor;
import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.extensions.GuiButtonExtension;
import de.maxhenkel.voicechat.extensions.GuiExtension;
import de.maxhenkel.voicechat.gui.GameProfileUtils;
import de.maxhenkel.voicechat.gui.VoiceChatScreenBase;
import de.maxhenkel.voicechat.gui.volume.AdjustVolumeSlider;
import de.maxhenkel.voicechat.gui.volume.PlayerVolumeEntry;
import de.maxhenkel.voicechat.gui.widgets.ListScreenBase;
import de.maxhenkel.voicechat.gui.widgets.ListScreenEntryBase;
import de.maxhenkel.voicechat.util.TextureHelper;
import de.maxhenkel.voicechat.voice.client.ClientManager;
import de.maxhenkel.voicechat.voice.client.ClientVoicechat;
import de.maxhenkel.voicechat.voice.common.PlayerState;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GroupEntry extends ListScreenEntryBase {

    protected static final String TALK_OUTLINE = TextureHelper.format(Voicechat.MODID, "textures/icons/talk_outline.png");
    protected static final String SPEAKER_OFF = TextureHelper.format(Voicechat.MODID, "textures/icons/speaker_small_off.png");

    protected static final int PADDING = 4;
    protected static final int BG_FILL = VoiceChatScreenBase.color(255, 74, 74, 74);
    protected static final int PLAYER_NAME_COLOR = VoiceChatScreenBase.color(255, 255, 255, 255);

    protected final ListScreenBase parent;
    protected final Minecraft minecraft;
    protected PlayerState state;
    protected final AdjustVolumeSlider volumeSlider;

    public GroupEntry(ListScreenBase parent, PlayerState state) {
        this.parent = parent;
        this.minecraft = MinecraftAccessor.getMinecraft();
        this.state = state;
        this.volumeSlider = new AdjustVolumeSlider(0, 0, 0, 100, 20, new PlayerVolumeEntry.PlayerVolumeConfigEntry(state.getUuid()));
        this.children.add(volumeSlider);
    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
        super.drawEntry(slotIndex, x, y, listWidth, slotHeight, mouseX, mouseY, isSelected, partialTicks);
        parent.drawRect(x, y, x + listWidth, y + slotHeight, BG_FILL);

        GL11.glPushMatrix();
        int outlineSize = slotHeight - PADDING * 2;

        GL11.glTranslated(x + PADDING, y + PADDING, 0D);
        float scale = outlineSize / 10F;
        GL11.glScalef(scale, scale, scale);

        GL11.glColor4f(1F, 1F, 1F, 1F);

        if (!state.isDisabled()) {
            ClientVoicechat client = ClientManager.getClient();
            if (client != null && client.getTalkCache().isTalking(state.getUuid())) {
                TextureHelper.bindTexture(TALK_OUTLINE);
                ((GuiExtension) parent).drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 10, 10, 16, 16);
            }
        }

        //TextureHelper.bindTexture(GameProfileUtils.getSkin(state.getUuid()));
        GameProfileUtils.bindSkinTexture(state.getName());

        ((GuiExtension) parent).drawScaledCustomSizeModalRect(1, 1, 8F, 8F, 8, 8, 8, 8, 64F, 64F);
        GL11.glEnable(GL11.GL_BLEND);
        ((GuiExtension) parent).drawScaledCustomSizeModalRect(1, 1, 40F, 8F, 8, 8, 8, 8, 64F, 64F);
        GL11.glDisable(GL11.GL_BLEND);

        if (state.isDisabled()) {
            GL11.glPushMatrix();
            GL11.glTranslated(1D, 1D, 0D);
            GL11.glScalef(0.5F, 0.5F, 1F);
            TextureHelper.bindTexture(SPEAKER_OFF);
            ((GuiExtension) parent).drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 16, 16, 16, 16);
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();

        minecraft.fontRenderer.drawString(state.getName(), x + PADDING + outlineSize + PADDING, (int) (y + slotHeight / 2 - TextureHelper.FONT_HEIGHT / 2), PLAYER_NAME_COLOR);

        if (isSelected && !ClientManager.getPlayerStateManager().getOwnID().equals(state.getUuid())) {
            ((GuiButtonExtension) volumeSlider).setWidth(Math.min(listWidth - (PADDING + outlineSize + PADDING + minecraft.fontRenderer.getStringWidth(state.getName()) + PADDING + PADDING), 100));
            volumeSlider.xPosition = x + (listWidth - ((GuiButtonExtension) volumeSlider).getWidth() - PADDING);
            volumeSlider.yPosition = y + (slotHeight - ((GuiButtonExtension) volumeSlider).getHeight()) / 2;
            volumeSlider.drawButton(minecraft, mouseX, mouseY);
        }
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }
}
