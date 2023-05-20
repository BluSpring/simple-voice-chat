package de.maxhenkel.voicechat.gui.widgets;

import de.maxhenkel.voicechat.MinecraftAccessor;
import de.maxhenkel.voicechat.util.MathHelper2;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Gui;
import net.minecraft.src.GuiButton;
import net.minecraft.src.Tessellator;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

public abstract class GuiSlot {
    protected final Minecraft mc;
    public int width;
    public int height;
    public int top;
    public int bottom;
    public int right;
    public int left;
    public final int slotHeight;
    private int scrollUpButtonID;
    private int scrollDownButtonID;
    protected int mouseX;
    protected int mouseY;
    protected boolean centerListVertically = true;
    protected int initialClickY = -2;
    protected float scrollMultiplier;
    protected float amountScrolled;
    protected int selectedElement = -1;
    protected long lastClicked;
    protected boolean visible = true;
    protected boolean showSelectionBox = true;
    protected boolean hasListHeader;
    public int headerPadding;
    private boolean enabled = true;

    public GuiSlot(Minecraft mcIn, int width, int height, int topIn, int bottomIn, int slotHeightIn)
    {
        this.mc = mcIn;
        this.width = width;
        this.height = height;
        this.top = topIn;
        this.bottom = bottomIn;
        this.slotHeight = slotHeightIn;
        this.left = 0;
        this.right = width;
    }

    public void setDimensions(int widthIn, int heightIn, int topIn, int bottomIn)
    {
        this.width = widthIn;
        this.height = heightIn;
        this.top = topIn;
        this.bottom = bottomIn;
        this.left = 0;
        this.right = widthIn;
    }

    public void setShowSelectionBox(boolean showSelectionBoxIn)
    {
        this.showSelectionBox = showSelectionBoxIn;
    }

    protected void setHasListHeader(boolean hasListHeaderIn, int headerPaddingIn)
    {
        this.hasListHeader = hasListHeaderIn;
        this.headerPadding = headerPaddingIn;

        if (!hasListHeaderIn)
        {
            this.headerPadding = 0;
        }
    }

    protected abstract int getSize();

    protected abstract void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY);

    protected abstract boolean isSelected(int slotIndex);

    protected int getContentHeight()
    {
        return this.getSize() * this.slotHeight + this.headerPadding;
    }

    protected abstract void drawBackground();

    protected void updateItemPos(int entryID, int insideLeft, int yPos, float partialTicks)
    {
    }

    protected abstract void drawSlot(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn, float partialTicks);

    protected void drawListHeader(int insideLeft, int insideTop, Tessellator tessellatorIn)
    {
    }

    protected void clickedHeader(int p_148132_1_, int p_148132_2_)
    {
    }

    protected void renderDecorations(int mouseXIn, int mouseYIn)
    {
    }

    public int getSlotIndexFromScreenCoords(int posX, int posY)
    {
        int i = this.left + this.width / 2 - this.getListWidth() / 2;
        int j = this.left + this.width / 2 + this.getListWidth() / 2;
        int k = posY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
        int l = k / this.slotHeight;
        return posX < this.getScrollBarX() && posX >= i && posX <= j && l >= 0 && k >= 0 && l < this.getSize() ? l : -1;
    }

    public void registerScrollButtons(int scrollUpButtonIDIn, int scrollDownButtonIDIn)
    {
        this.scrollUpButtonID = scrollUpButtonIDIn;
        this.scrollDownButtonID = scrollDownButtonIDIn;
    }

    protected void bindAmountScrolled()
    {
        this.amountScrolled = MathHelper2.clamp(this.amountScrolled, 0.0F, (float)this.getMaxScroll());
    }

    public int getMaxScroll()
    {
        return Math.max(0, this.getContentHeight() - (this.bottom - this.top - 4));
    }

    public int getAmountScrolled()
    {
        return (int)this.amountScrolled;
    }

    public boolean isMouseYWithinSlotBounds(int p_148141_1_)
    {
        return p_148141_1_ >= this.top && p_148141_1_ <= this.bottom && this.mouseX >= this.left && this.mouseX <= this.right;
    }

    public void scrollBy(int amount)
    {
        this.amountScrolled += (float)amount;
        this.bindAmountScrolled();
        this.initialClickY = -2;
    }

    public void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            if (button.id == this.scrollUpButtonID)
            {
                this.amountScrolled -= (float)(this.slotHeight * 2 / 3);
                this.initialClickY = -2;
                this.bindAmountScrolled();
            }
            else if (button.id == this.scrollDownButtonID)
            {
                this.amountScrolled += (float)(this.slotHeight * 2 / 3);
                this.initialClickY = -2;
                this.bindAmountScrolled();
            }
        }
    }

    public void drawScreen(int mouseXIn, int mouseYIn, float partialTicks)
    {
        if (this.visible)
        {
            this.mouseX = mouseXIn;
            this.mouseY = mouseYIn;
            this.drawBackground();
            int i = this.getScrollBarX();
            int j = i + 6;
            this.bindAmountScrolled();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_FOG);
            Tessellator tessellator = Tessellator.instance;
            // Forge: background rendering moved into separate method.
            this.drawContainerBackground(tessellator);
            int k = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            int l = this.top + 4 - (int)this.amountScrolled;

            if (this.hasListHeader)
            {
                this.drawListHeader(k, l, tessellator);
            }

            this.drawSelectionBox(k, l, mouseXIn, mouseYIn, partialTicks);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            this.overlayBackground(0, this.top, 255, 255);
            this.overlayBackground(this.bottom, this.height, 255, 255);
            GL11.glEnable(GL11.GL_BLEND);
            GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glShadeModel(7425);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            int i1 = 4;
            tessellator.startDrawing(7);

            tessellator.setColorRGBA(0, 0, 0, 0);
            tessellator.addVertexWithUV((double)this.left, (double)(this.top + 4), 0.0D, 0.0D, 1.0D);

            tessellator.setColorRGBA(0, 0, 0, 0);
            tessellator.addVertexWithUV((double)this.right, (double)(this.top + 4), 0.0D, 1.0D, 1.0D);

            tessellator.setColorRGBA(0, 0, 0, modifiedAlpha());
            tessellator.addVertexWithUV((double)this.right, (double)this.top, 0.0D, 1.0D, 0.0D);

            tessellator.setColorRGBA(0, 0, 0, modifiedAlpha());
            tessellator.addVertexWithUV((double)this.left, (double)this.top, 0.0D, 0.0D, 0.0D);

            tessellator.draw();

            tessellator.startDrawing(7);

            tessellator.setColorRGBA(0, 0, 0, modifiedAlpha());
            tessellator.addVertexWithUV((double)this.left, (double)this.bottom, 0.0D, 0.0D, 1.0D);

            tessellator.setColorRGBA(0, 0, 0, modifiedAlpha());
            tessellator.addVertexWithUV((double)this.right, (double)this.bottom, 0.0D, 1.0D, 1.0D);

            tessellator.setColorRGBA(0, 0, 0, 0);
            tessellator.addVertexWithUV((double)this.right, (double)(this.bottom - 4), 0.0D, 1.0D, 0.0D);

            tessellator.setColorRGBA(0, 0, 0, 0);
            tessellator.addVertexWithUV((double)this.left, (double)(this.bottom - 4), 0.0D, 0.0D, 0.0D);

            tessellator.draw();

            int j1 = this.getMaxScroll();

            if (j1 > 0)
            {
                int k1 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
                k1 = MathHelper2.clamp(k1, 32, this.bottom - this.top - 8);
                int l1 = (int)this.amountScrolled * (this.bottom - this.top - k1) / j1 + this.top;

                if (l1 < this.top)
                {
                    l1 = this.top;
                }

                tessellator.startDrawing(7);

                tessellator.setColorRGBA(0, 0, 0, 255);
                tessellator.addVertexWithUV((double)i, (double)this.bottom, 0.0D, 0.0D, 1.0D);

                tessellator.setColorRGBA(0, 0, 0, 255);
                tessellator.addVertexWithUV((double)j, (double)this.bottom, 0.0D, 1.0D, 1.0D);

                tessellator.setColorRGBA(0, 0, 0, 255);
                tessellator.addVertexWithUV((double)j, (double)this.top, 0.0D, 1.0D, 0.0D);

                tessellator.setColorRGBA(0, 0, 0, 255);
                tessellator.addVertexWithUV((double)i, (double)this.top, 0.0D, 0.0D, 0.0D);

                tessellator.draw();


                tessellator.startDrawing(7);
                tessellator.setColorRGBA(128, 128, 128, 255);
                tessellator.addVertexWithUV((double)i, (double)(l1 + k1), 0.0D, 0.0D, 1.0D);
                tessellator.setColorRGBA(128, 128, 128, 255);
                tessellator.addVertexWithUV((double)j, (double)(l1 + k1), 0.0D, 1.0D, 1.0D);
                tessellator.setColorRGBA(128, 128, 128, 255);
                tessellator.addVertexWithUV((double)j, (double)l1, 0.0D, 1.0D, 0.0D);
                tessellator.setColorRGBA(128, 128, 128, 255);
                tessellator.addVertexWithUV((double)i, (double)l1, 0.0D, 0.0D, 0.0D);
                tessellator.draw();

                tessellator.startDrawing(7);
                tessellator.setColorRGBA(192, 192, 192, 255);
                tessellator.addVertexWithUV((double)i, (double)(l1 + k1 - 1), 0.0D, 0.0D, 1.0D);
                tessellator.setColorRGBA(192, 192, 192, 255);
                tessellator.addVertexWithUV((double)(j - 1), (double)(l1 + k1 - 1), 0.0D, 1.0D, 1.0D);
                tessellator.setColorRGBA(192, 192, 192, 255);
                tessellator.addVertexWithUV((double)(j - 1), (double)l1, 0.0D, 1.0D, 0.0D);
                tessellator.setColorRGBA(192, 192, 192, 255);
                tessellator.addVertexWithUV((double)i, (double)l1, 0.0D, 0.0D, 0.0D);
                tessellator.draw();
            }

            this.renderDecorations(mouseXIn, mouseYIn);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glShadeModel(7424);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
    }

    public void handleMouseInput()
    {
        if (this.isMouseYWithinSlotBounds(this.mouseY))
        {
            if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && this.mouseY >= this.top && this.mouseY <= this.bottom)
            {
                int i = (this.width - this.getListWidth()) / 2;
                int j = (this.width + this.getListWidth()) / 2;
                int k = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
                int l = k / this.slotHeight;

                if (l < this.getSize() && this.mouseX >= i && this.mouseX <= j && l >= 0 && k >= 0)
                {
                    this.elementClicked(l, false, this.mouseX, this.mouseY);
                    this.selectedElement = l;
                }
                else if (this.mouseX >= i && this.mouseX <= j && k < 0)
                {
                    this.clickedHeader(this.mouseX - i, this.mouseY - this.top + (int)this.amountScrolled - 4);
                }
            }

            if (Mouse.isButtonDown(0) && this.getEnabled())
            {
                if (this.initialClickY == -1)
                {
                    boolean flag1 = true;

                    if (this.mouseY >= this.top && this.mouseY <= this.bottom)
                    {
                        int j2 = (this.width - this.getListWidth()) / 2;
                        int k2 = (this.width + this.getListWidth()) / 2;
                        int l2 = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
                        int i1 = l2 / this.slotHeight;

                        if (i1 < this.getSize() && this.mouseX >= j2 && this.mouseX <= k2 && i1 >= 0 && l2 >= 0)
                        {
                            boolean flag = i1 == this.selectedElement && getSystemTime() - this.lastClicked < 250L;
                            this.elementClicked(i1, flag, this.mouseX, this.mouseY);
                            this.selectedElement = i1;
                            this.lastClicked = getSystemTime();
                        }
                        else if (this.mouseX >= j2 && this.mouseX <= k2 && l2 < 0)
                        {
                            this.clickedHeader(this.mouseX - j2, this.mouseY - this.top + (int)this.amountScrolled - 4);
                            flag1 = false;
                        }

                        int i3 = this.getScrollBarX();
                        int j1 = i3 + 6;

                        if (this.mouseX >= i3 && this.mouseX <= j1)
                        {
                            this.scrollMultiplier = -1.0F;
                            int k1 = this.getMaxScroll();

                            if (k1 < 1)
                            {
                                k1 = 1;
                            }

                            int l1 = (int)((float)((this.bottom - this.top) * (this.bottom - this.top)) / (float)this.getContentHeight());
                            l1 = MathHelper2.clamp(l1, 32, this.bottom - this.top - 8);
                            this.scrollMultiplier /= (float)(this.bottom - this.top - l1) / (float)k1;
                        }
                        else
                        {
                            this.scrollMultiplier = 1.0F;
                        }

                        if (flag1)
                        {
                            this.initialClickY = this.mouseY;
                        }
                        else
                        {
                            this.initialClickY = -2;
                        }
                    }
                    else
                    {
                        this.initialClickY = -2;
                    }
                }
                else if (this.initialClickY >= 0)
                {
                    this.amountScrolled -= (float)(this.mouseY - this.initialClickY) * this.scrollMultiplier;
                    this.initialClickY = this.mouseY;
                }
            }
            else
            {
                this.initialClickY = -1;
            }

            int i2 = Mouse.getEventDWheel();

            if (i2 != 0)
            {
                if (i2 > 0)
                {
                    i2 = -1;
                }
                else if (i2 < 0)
                {
                    i2 = 1;
                }

                this.amountScrolled += (float)(i2 * this.slotHeight / 2);
            }
        }
    }

    public void setEnabled(boolean enabledIn)
    {
        this.enabled = enabledIn;
    }

    public boolean getEnabled()
    {
        return this.enabled;
    }

    public int getListWidth()
    {
        return 220;
    }

    protected void drawSelectionBox(int insideLeft, int insideTop, int mouseXIn, int mouseYIn, float partialTicks)
    {
        int i = this.getSize();
        Tessellator tessellator = Tessellator.instance;

        for (int j = 0; j < i; ++j)
        {
            int k = insideTop + j * this.slotHeight + this.headerPadding;
            int l = this.slotHeight - 4;

            if (k > this.bottom || k + l < this.top)
            {
                this.updateItemPos(j, insideLeft, k, partialTicks);
            }

            if (this.showSelectionBox && this.isSelected(j))
            {
                int i1 = this.left + (this.width / 2 - this.getListWidth() / 2);
                int j1 = this.left + this.width / 2 + this.getListWidth() / 2;
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                MinecraftAccessor.checkGLError("disable tex2d dsb");
                tessellator.startDrawing(7);
                tessellator.setColorRGBA(128, 128, 128, 255);
                tessellator.addVertexWithUV((double)i1, (double)(k + l + 2), 0.0D, 0.0D, 1.0D);
                tessellator.setColorRGBA(128, 128, 128, 255);
                tessellator.addVertexWithUV((double)j1, (double)(k + l + 2), 0.0D, 1.0D, 1.0D);
                tessellator.setColorRGBA(128, 128, 128, 255);
                tessellator.addVertexWithUV((double)j1, (double)(k - 2), 0.0D, 1.0D, 0.0D);
                tessellator.setColorRGBA(128, 128, 128, 255);
                tessellator.addVertexWithUV((double)i1, (double)(k - 2), 0.0D, 0.0D, 0.0D);

                tessellator.setColorRGBA(0, 0, 0, 255);
                tessellator.addVertexWithUV((double)(i1 + 1), (double)(k + l + 1), 0.0D, 0.0D, 1.0D);
                tessellator.setColorRGBA(0, 0, 0, 255);
                tessellator.addVertexWithUV((double)(j1 - 1), (double)(k + l + 1), 0.0D, 1.0D, 1.0D);
                tessellator.setColorRGBA(0, 0, 0, 255);
                tessellator.addVertexWithUV((double)(j1 - 1), (double)(k - 1), 0.0D, 1.0D, 0.0D);
                tessellator.setColorRGBA(0, 0, 0, 255);
                tessellator.addVertexWithUV((double)(i1 + 1), (double)(k - 1), 0.0D, 0.0D, 0.0D);
                tessellator.draw();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }

            this.drawSlot(j, insideLeft, k, l, mouseXIn, mouseYIn, partialTicks);
        }
    }

    protected int getScrollBarX()
    {
        return this.width / 2 + 124;
    }

    protected void overlayBackground(int startY, int endY, int startAlpha, int endAlpha)
    {
        Tessellator tessellator = Tessellator.instance;
        this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("/gui/background.png"));
        GL11.glColor4f(1F, 1F, 1F, 1F);
        float f = 32.0F;
        tessellator.startDrawing(7);
        
        tessellator.setColorRGBA(64, 64, 64, endAlpha);
        tessellator.addVertexWithUV((double)this.left, (double)endY, 0.0D, 0.0D, (double)((float)endY / 32.0F));
        tessellator.setColorRGBA(64, 64, 64, endAlpha);
        tessellator.addVertexWithUV((double)(this.left + this.width), (double)endY, 0.0D, (double)((float)this.width / 32.0F), (double)((float)endY / 32.0F));
        tessellator.setColorRGBA(64, 64, 64, startAlpha);
        tessellator.addVertexWithUV((double)(this.left + this.width), (double)startY, 0.0D, (double)((float)this.width / 32.0F), (double)((float)startY / 32.0F));
        tessellator.setColorRGBA(64, 64, 64, startAlpha);
        tessellator.addVertexWithUV((double)this.left, (double)startY, 0.0D, 0.0D, (double)((float)startY / 32.0F));
        
        tessellator.draw();
    }

    public void setSlotXBoundsFromLeft(int leftIn)
    {
        this.left = leftIn;
        this.right = leftIn + this.width;
    }

    public int getSlotHeight()
    {
        return this.slotHeight;
    }

    protected void drawContainerBackground(Tessellator tessellator)
    {
        this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("/gui/background.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        tessellator.startDrawing(7);
        tessellator.setColorRGBA(32, 32, 32, 255);
        tessellator.addVertexWithUV((double)this.left,  (double)this.bottom, 0.0D, (double)((float)this.left  / f), (double)((float)(this.bottom + (int)this.amountScrolled) / f));
        tessellator.setColorRGBA(32, 32, 32, 255);
        tessellator.addVertexWithUV((double)this.right, (double)this.bottom, 0.0D, (double)((float)this.right / f), (double)((float)(this.bottom + (int)this.amountScrolled) / f));
        tessellator.setColorRGBA(32, 32, 32, 255);
        tessellator.addVertexWithUV((double)this.right, (double)this.top,    0.0D, (double)((float)this.right / f), (double)((float)(this.top    + (int)this.amountScrolled) / f));
        tessellator.setColorRGBA(32, 32, 32, 255);
        tessellator.addVertexWithUV((double)this.left,  (double)this.top,    0.0D, (double)((float)this.left  / f), (double)((float)(this.top    + (int)this.amountScrolled) / f));
        tessellator.draw();
    }

    public static long getSystemTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }

    private boolean isVoicechatList() {
        return this instanceof ListScreenListBase;
    }

    private int modifiedAlpha() {
        return isVoicechatList() ? 0 : 255;
    }
}
