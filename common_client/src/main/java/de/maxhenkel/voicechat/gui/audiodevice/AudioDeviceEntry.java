package de.maxhenkel.voicechat.gui.audiodevice;

import de.maxhenkel.voicechat.MinecraftAccessor;
import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.extensions.GuiExtension;
import de.maxhenkel.voicechat.gui.VoiceChatScreenBase;
import de.maxhenkel.voicechat.gui.widgets.ListScreenEntryBase;
import de.maxhenkel.voicechat.util.TextureHelper;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class AudioDeviceEntry extends ListScreenEntryBase {

    protected static final String SELECTED = TextureHelper.format(Voicechat.MODID, "textures/icons/device_selected.png");

    protected static final int PADDING = 4;
    protected static final int BG_FILL = VoiceChatScreenBase.color(255, 74, 74, 74);
    protected static final int BG_FILL_HOVERED = VoiceChatScreenBase.color(255, 90, 90, 90);
    protected static final int BG_FILL_SELECTED = VoiceChatScreenBase.color(255, 40, 40, 40);
    protected static final int DEVICE_NAME_COLOR = VoiceChatScreenBase.color(255, 255, 255, 255);

    protected final Minecraft minecraft;
    protected final String device;
    protected final String visibleDeviceName;
    protected final SelectDeviceScreen parent;

    public AudioDeviceEntry(SelectDeviceScreen parent, String device) {
        this.parent = parent;
        this.device = device;
        this.visibleDeviceName = parent.getVisibleName(device);
        this.minecraft = MinecraftAccessor.getMinecraft();
    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
        super.drawEntry(slotIndex, x, y, listWidth, slotHeight, mouseX, mouseY, isSelected, partialTicks);
        boolean selected = parent.getSelectedDevice().equals(device);
        if (selected) {
            parent.drawRect(x, y, x + listWidth, y + slotHeight, BG_FILL_SELECTED);
        } else if (isSelected) {
            parent.drawRect(x, y, x + listWidth, y + slotHeight, BG_FILL_HOVERED);
        } else {
            parent.drawRect(x, y, x + listWidth, y + slotHeight, BG_FILL);
        }
        GL11.glColor4f(1F, 1F, 1F, 1F);

        TextureHelper.bindTexture(parent.getIcon(device));
        ((GuiExtension) parent).drawModalRectWithCustomSizedTexture(x + PADDING, y + slotHeight / 2 - 8, 16, 16, 16, 16, 16, 16);
        if (selected) {
            TextureHelper.bindTexture(SELECTED);
            ((GuiExtension) parent).drawModalRectWithCustomSizedTexture(x + PADDING, y + slotHeight / 2 - 8, 16, 16, 16, 16, 16, 16);
        }

        float deviceWidth = minecraft.fontRenderer.getStringWidth(visibleDeviceName);
        float space = listWidth - PADDING - 16 - PADDING - PADDING;
        float scale = Math.min(space / deviceWidth, 1F);

        GL11.glPushMatrix();
        GL11.glTranslated(x + PADDING + 16 + PADDING, y + slotHeight / 2 - (TextureHelper.FONT_HEIGHT * scale) / 2, 0D);
        GL11.glScalef(scale, scale, 1F);

        minecraft.fontRenderer.drawString(visibleDeviceName, 0, 0, DEVICE_NAME_COLOR);
        GL11.glPopMatrix();
    }

    public String getDevice() {
        return device;
    }
}
