package de.maxhenkel.voicechat.gui.widgets;

import net.minecraft.src.GuiButton;
import net.minecraft.util.text.ITextComponent;

public abstract class ButtonBase extends GuiButton {

    public ButtonBase(int id, int x, int y, int width, int height, ITextComponent text) {
        super(id, x, y, width, height, text.getUnformattedComponentText());
    }

    public ButtonBase(int id, int x, int y, int width, int height, String text) {
        super(id, x, y, width, height, text);
    }

    public abstract void onPress();

    public void renderTooltips(int mouseX, int mouseY, float delta) {

    }

}
