package de.maxhenkel.voicechat.gui.volume;

import de.maxhenkel.voicechat.MinecraftAccessor;
import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.extensions.GuiButtonExtension;
import de.maxhenkel.voicechat.gui.VoiceChatScreenBase;
import de.maxhenkel.voicechat.gui.widgets.ListScreenEntryBase;
import de.maxhenkel.voicechat.util.TextureHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.StringTranslate;

public abstract class VolumeEntry extends ListScreenEntryBase {

    protected static final String OTHER_VOLUME = StringTranslate.getInstance().translateKey("message.voicechat.other_volume");
    protected static final String OTHER_VOLUME_DESCRIPTION = StringTranslate.getInstance().translateKey("message.voicechat.other_volume.description");
    protected static final String OTHER_VOLUME_ICON = TextureHelper.format(Voicechat.MODID, "textures/icons/other_volume.png");

    protected static final int SKIN_SIZE = 24;
    protected static final int PADDING = 4;
    protected static final int BG_FILL = VoiceChatScreenBase.color(255, 74, 74, 74);
    protected static final int PLAYER_NAME_COLOR = VoiceChatScreenBase.color(255, 255, 255, 255);

    protected final Minecraft minecraft;
    protected final AdjustVolumesScreen screen;
    protected final AdjustVolumeSlider volumeSlider;

    public VolumeEntry(AdjustVolumesScreen screen, AdjustVolumeSlider.VolumeConfigEntry entry) {
        this.minecraft = MinecraftAccessor.getMinecraft();
        this.screen = screen;
        this.volumeSlider = new AdjustVolumeSlider(0, 0, 0, 100, 20, entry);
        this.children.add(volumeSlider);
    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
        super.drawEntry(slotIndex, x, y, listWidth, slotHeight, mouseX, mouseY, isSelected, partialTicks);
        int skinX = x + PADDING;
        int skinY = y + (slotHeight - SKIN_SIZE) / 2;
        int textX = skinX + SKIN_SIZE + PADDING;
        int textY = y + (int) (slotHeight - TextureHelper.FONT_HEIGHT) / 2;

        screen.drawRect(x, y, x + listWidth, y + slotHeight, BG_FILL);

        renderElement(slotIndex, x, y, listWidth, slotHeight, mouseX, mouseY, isSelected, partialTicks, skinX, skinY, textX, textY);

        volumeSlider.xPosition = x + (listWidth - ((GuiButtonExtension) volumeSlider).getWidth() - PADDING);
        volumeSlider.yPosition = y + (slotHeight - ((GuiButtonExtension) volumeSlider).getHeight()) / 2;
        volumeSlider.drawButton(minecraft, mouseX, mouseY);
    }

    public abstract void renderElement(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks, int skinX, int skinY, int textX, int textY);

}
