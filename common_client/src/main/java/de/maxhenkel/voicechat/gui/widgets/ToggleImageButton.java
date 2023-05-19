package de.maxhenkel.voicechat.gui.widgets;

import de.maxhenkel.voicechat.extensions.GuiExtension;
import de.maxhenkel.voicechat.util.TextureHelper;

import java.util.function.Supplier;

public class ToggleImageButton extends ImageButton {

    protected Supplier<Boolean> stateSupplier;

    public ToggleImageButton(int id, int x, int y, String texture, Supplier<Boolean> stateSupplier, PressAction onPress, TooltipSupplier tooltipSupplier) {
        super(id, x, y, texture, onPress, tooltipSupplier);
        this.stateSupplier = stateSupplier;
    }

    @Override
    protected void renderImage(int mouseX, int mouseY) {
        TextureHelper.bindTexture(texture);

        int x = xPosition;
        int y = yPosition;

        if (stateSupplier.get()) {
            ((GuiExtension) this).drawModalRectWithCustomSizedTexture(x + 2, y + 2, 16, 0, 16, 16, 32, 32);
        } else {
            ((GuiExtension) this).drawModalRectWithCustomSizedTexture(x + 2, y + 2, 0, 0, 16, 16, 32, 32);
        }
    }

}
