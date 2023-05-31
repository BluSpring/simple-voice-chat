package de.maxhenkel.voicechat.gui;

import net.minecraft.src.*;
import org.lwjgl.input.Keyboard;

import java.util.LinkedList;
import java.util.List;

public class GuiEnhancedControls extends GuiScreen {
    private GuiScreen parent;
    private GameSettings options;
    private int buttonId = -1;

    public GuiEnhancedControls(GuiScreen parent, GameSettings options) {
        this.parent = parent;
        this.options = options;
    }

    private final List<GuiButton> scrollableList = new LinkedList<>();
    private int currentPage = 0;
    private int maxPage = 0;

    private int splitEvery = 10;

    @Override
    public void initGui() {
        maxPage = 0;
        currentPage = 0;

        StringTranslate translate = StringTranslate.getInstance();
        int buttonWidth = this.width / 2 - 155;

        int baseY = this.height / 6;
        int j = 0;
        for (int i = 0; i < this.options.keyBindings.length; ++i) {
            int y = baseY + 24 * (j >> 1);

            if (splitEvery == j) {
                j = 0;
                y = baseY;
                maxPage++;
            }

            this.scrollableList.add(new GuiSmallButton(i, buttonWidth + j % 2 * 160, y, 70, 20, this.options.getOptionDisplayString(i)));
            j++;
        }

        this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, translate.translateKey("gui.done")));

        this.controlList.add(new GuiSmallButton(201, this.width / 2 - 80, this.height / 6 + 138, 20, 20, "<"));
        this.controlList.add(new GuiSmallButton(203, this.width / 2 + 60, this.height / 6 + 138, 20, 20, ">"));
    }

    protected void actionPerformed(GuiButton guiButton) {
        for(int var2 = 0; var2 < this.options.keyBindings.length; ++var2) {
            (this.scrollableList.get(var2)).displayString = this.options.getOptionDisplayString(var2);
        }

        if (guiButton.id == 200) { // Ok
            this.mc.displayGuiScreen(this.parent);
        } else if (guiButton.id == 201) { // Back
            if (currentPage > 0) {
                currentPage--;
            }
        } else if (guiButton.id == 203) { // Front
            if (currentPage < maxPage) {
                currentPage++;
            }
        } else {
            this.buttonId = guiButton.id;
            guiButton.displayString = "> " + this.options.getOptionDisplayString(guiButton.id) + " <";
        }

    }

    protected void keyTyped(char c, int i) {
        if (this.buttonId >= 0) {
            if (i == Keyboard.KEY_ESCAPE) // add the ability to not assign keys
                i = Keyboard.KEY_NONE;

            this.options.setKeyBinding(this.buttonId, i);
            (this.scrollableList.get(this.buttonId)).displayString = this.options.getOptionDisplayString(this.buttonId);
            this.buttonId = -1;
        } else {
            super.keyTyped(c, i);
        }
    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        if (k == 0) {
            for(int var4 = 0; var4 < this.scrollableList.size(); ++var4) {
                GuiButton var5 = this.scrollableList.get(var4);
                if (this.isItemVisible(var4) && var5.mousePressed(this.mc, i, j)) {
                    this.mc.sndManager.func_337_a("random.click", 1.0F, 1.0F);
                    this.actionPerformed(var5);
                }
            }
        }

        super.mouseClicked(i, j, k);
    }

    public boolean isItemVisible(int i) {
        return i <= ((currentPage + 1) * splitEvery) - 1 && i >= (currentPage * splitEvery);
    }

    public void drawScreen(int i, int j, float f) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "Controls", this.width / 2, 20, 16777215);
        int buttonWidth = this.width / 2 - 155;

        for(int var5 = 0; var5 < this.options.keyBindings.length; ++var5) {
            if (!isItemVisible(var5))
                continue;

            GuiButton button = this.scrollableList.get(var5);
            button.drawButton(mc, i, j);
            this.drawString(this.fontRenderer, this.options.getKeyBindingDescription(var5), buttonWidth + var5 % 2 * 160 + 70, button.yPosition + 7, -1);
        }

        this.drawCenteredString(this.fontRenderer, String.format("%d / %d", currentPage + 1, maxPage + 1), this.width / 2, this.height / 6 + 148 - 4, 16777215);

        super.drawScreen(i, j, f);
    }
}
