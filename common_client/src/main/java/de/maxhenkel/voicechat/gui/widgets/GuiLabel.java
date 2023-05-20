package de.maxhenkel.voicechat.gui.widgets;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Gui;
import net.minecraft.src.StringTranslate;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.util.List;

public class GuiLabel extends Gui {
    protected int width = 200;
    protected int height = 20;
    public int x;
    public int y;
    private final List<String> labels;
    public int id;
    private boolean centered;
    public boolean visible = true;
    private boolean labelBgEnabled;
    private final int textColor;
    private int backColor;
    private int ulColor;
    private int brColor;
    private final FontRenderer fontRenderer;
    private int border;

    public GuiLabel(FontRenderer fontRendererObj, int p_i45540_2_, int p_i45540_3_, int p_i45540_4_, int p_i45540_5_, int p_i45540_6_, int p_i45540_7_)
    {
        this.fontRenderer = fontRendererObj;
        this.id = p_i45540_2_;
        this.x = p_i45540_3_;
        this.y = p_i45540_4_;
        this.width = p_i45540_5_;
        this.height = p_i45540_6_;
        this.labels = Lists.<String>newArrayList();
        this.centered = false;
        this.labelBgEnabled = false;
        this.textColor = p_i45540_7_;
        this.backColor = -1;
        this.ulColor = -1;
        this.brColor = -1;
        this.border = 0;
    }

    public void addLine(String p_175202_1_)
    {
        this.labels.add(StringTranslate.getInstance().translateKey(p_175202_1_));
    }

    public GuiLabel setCentered()
    {
        this.centered = true;
        return this;
    }

    public void drawLabel(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            GL11.glEnable(GL11.GL_BLEND);
            GL14.glBlendFuncSeparate(GL14.GL_BLEND_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
            this.drawLabelBackground(mc, mouseX, mouseY);
            int i = this.y + this.height / 2 + this.border / 2;
            int j = i - this.labels.size() * 10 / 2;

            for (int k = 0; k < this.labels.size(); ++k)
            {
                if (this.centered)
                {
                    this.drawCenteredString(this.fontRenderer, this.labels.get(k), this.x + this.width / 2, j + k * 10, this.textColor);
                }
                else
                {
                    this.drawString(this.fontRenderer, this.labels.get(k), this.x, j + k * 10, this.textColor);
                }
            }
        }
    }

    protected void drawLabelBackground(Minecraft mcIn, int p_146160_2_, int p_146160_3_)
    {
        if (this.labelBgEnabled)
        {
            int i = this.width + this.border * 2;
            int j = this.height + this.border * 2;
            int k = this.x - this.border;
            int l = this.y - this.border;
            drawRect(k, l, k + i, l + j, this.backColor);
            this.drawHorizontalLine(k, k + i, l, this.ulColor);
            this.drawHorizontalLine(k, k + i, l + j, this.brColor);
            this.drawVerticalLine(k, l, l + j, this.ulColor);
            this.drawVerticalLine(k + i, l, l + j, this.brColor);
        }
    }

    protected void drawHorizontalLine(int startX, int endX, int y, int color)
    {
        if (endX < startX)
        {
            int i = startX;
            startX = endX;
            endX = i;
        }

        drawRect(startX, y, endX + 1, y + 1, color);
    }

    protected void drawVerticalLine(int x, int startY, int endY, int color)
    {
        if (endY < startY)
        {
            int i = startY;
            startY = endY;
            endY = i;
        }

        drawRect(x, startY + 1, x + 1, endY, color);
    }
}
