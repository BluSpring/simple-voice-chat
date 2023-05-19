package de.maxhenkel.voicechat.gui.widgets;

import de.maxhenkel.voicechat.MinecraftAccessor;
import de.maxhenkel.voicechat.extensions.GuiExtension;
import de.maxhenkel.voicechat.util.TextureHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;

public class ImageButton extends ButtonBase {

    protected Minecraft mc;
    protected String texture;
    protected PressAction onPress;
    protected TooltipSupplier tooltipSupplier;

    public ImageButton(int id, int x, int y, String texture, PressAction onPress, TooltipSupplier tooltipSupplier) {
        super(id, x, y, 20, 20, new TextComponentString(""));
        mc = MinecraftAccessor.getMinecraft();
        this.texture = texture;
        this.onPress = onPress;
        this.tooltipSupplier = tooltipSupplier;
    }

    @Override
    public void onPress() {
        this.onPress.onPress(this);
    }

    protected void renderImage(int x, int y) {
        TextureHelper.bindTexture(texture);
        ((GuiExtension) this).drawModalRectWithCustomSizedTexture(x + 2, y + 2, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
        super.drawButton(minecraft, mouseX, mouseY);
        renderImage(mouseX, mouseY);
    }

    @Override
    public void renderTooltips(int mouseX, int mouseY, float delta) {
        if (false) {
            tooltipSupplier.onTooltip(this, mouseX, mouseY);
        }
    }

    public interface TooltipSupplier {
        void onTooltip(ImageButton button, int mouseX, int mouseY);
    }

    public interface PressAction {
        void onPress(ImageButton button);
    }

}
