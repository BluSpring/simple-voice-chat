package de.maxhenkel.voicechat.extensions;

import net.minecraft.src.Tessellator;

public interface GuiExtension {
    void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight);
    void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight);

    static void staticDrawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
        float textureU = 1F / textureWidth;
        float textureV = 1F / textureHeight;

        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawing(7);
        tessellator.addVertexWithUV(x, y + height, 0f, u * textureU, (v + height) * textureV);
        tessellator.addVertexWithUV(x + width, y + height, 0f, (u + width) * textureU, (v + height) * textureV);
        tessellator.addVertexWithUV(x + width, y, 0f, (u + width) * textureU, v * textureV);
        tessellator.addVertexWithUV(x, y, 0f, u * textureU, v * textureV);
        tessellator.draw();
    }

    static void staticDrawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float tileU = 1F / tileWidth;
        float tileV = 1F / tileHeight;

        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawing(7);

        tessellator.addVertexWithUV(x, y + height, 0f, u * tileU, (v + vHeight) * tileV);
        tessellator.addVertexWithUV(x + width, y + height, 0f, (u + uWidth) * tileU, (v + vHeight) * tileV);
        tessellator.addVertexWithUV(x + width, y, 0f, (u + uWidth) * tileU, v * tileV);
        tessellator.addVertexWithUV(x, y, 0f, u * tileU, v * tileV);

        tessellator.draw();
    }
}
