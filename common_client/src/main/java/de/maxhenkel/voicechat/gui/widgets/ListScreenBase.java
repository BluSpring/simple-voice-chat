package de.maxhenkel.voicechat.gui.widgets;

import de.maxhenkel.voicechat.gui.VoiceChatScreenBase;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;
import net.minecraft.util.text.ITextComponent;

import java.io.IOException;

public abstract class ListScreenBase extends VoiceChatScreenBase {

    private Runnable postRender;
    private ListScreenListBase<?> list;

    public ListScreenBase(ITextComponent title, int xSize, int ySize) {
        super(title, xSize, ySize);
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        if (list != null) {
            list.handleMouseInput();
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (list != null) {
            list.actionPerformed(button);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float delta) {
        super.drawScreen(mouseX, mouseY, delta);
        if (list != null) {
            list.drawScreen(mouseX, mouseY, delta);
        }
        if (postRender != null) {
            postRender.run();
            postRender = null;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (list != null) {
            list.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void mouseMovedOrUp(int mouseX, int mouseY, int mouseButton) {
        super.mouseMovedOrUp(mouseX, mouseY, mouseButton);
        if (list != null) {
            list.mouseMovedOrUp(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void setWorldAndResolution(Minecraft mcIn, int w, int h) {
        super.setWorldAndResolution(mcIn, w, h);
    }

    public void setList(ListScreenListBase<?> list) {
        this.list = list;
    }

    public void removeList() {
        this.list = null;
    }

    public void postRender(Runnable postRender) {
        this.postRender = postRender;
    }

}
