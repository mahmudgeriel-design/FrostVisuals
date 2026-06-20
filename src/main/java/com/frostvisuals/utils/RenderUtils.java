package com.frostvisuals.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.math.vector.Matrix4f;

public class RenderUtils {

    /** Filled rectangle */
    public static void fillRect(MatrixStack ms, float x, float y, float w, float h, int color) {
        float a = ((color >> 24) & 0xFF) / 255f;
        if (a == 0) a = 1f;
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8)  & 0xFF) / 255f;
        float b = (color         & 0xFF) / 255f;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableTexture();
        RenderSystem.color4f(r, g, b, a);

        net.minecraft.client.renderer.Tessellator tess = net.minecraft.client.renderer.Tessellator.getInstance();
        net.minecraft.client.renderer.BufferBuilder buf = tess.getBuilder();
        Matrix4f mat = ms.last().pose();
        buf.begin(7, net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION);
        buf.vertex(mat, x,     y + h, 0).endVertex();
        buf.vertex(mat, x + w, y + h, 0).endVertex();
        buf.vertex(mat, x + w, y,     0).endVertex();
        buf.vertex(mat, x,     y,     0).endVertex();
        tess.end();

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    /** Outlined rectangle with given line width */
    public static void drawOutline(MatrixStack ms, float x, float y, float w, float h, int color, float lineW) {
        fillRect(ms, x,             y,             w,     lineW, color); // top
        fillRect(ms, x,             y + h - lineW, w,     lineW, color); // bottom
        fillRect(ms, x,             y,             lineW, h,     color); // left
        fillRect(ms, x + w - lineW, y,             lineW, h,     color); // right
    }

    /** Rounded rect outline (approximated with 4 lines + corner dots) */
    public static void drawRoundedOutline(MatrixStack ms, float x, float y, float w, float h, int color) {
        float r = 2f;
        float lw = 1.5f;
        // sides
        fillRect(ms, x + r, y,         w - 2*r, lw,  color);
        fillRect(ms, x + r, y + h - lw, w - 2*r, lw, color);
        fillRect(ms, x,         y + r, lw,  h - 2*r, color);
        fillRect(ms, x + w - lw, y + r, lw,  h - 2*r, color);
        // corners (1x1 pixels)
        fillRect(ms, x + 1,         y + 1,         1, 1, color);
        fillRect(ms, x + w - 2,     y + 1,         1, 1, color);
        fillRect(ms, x + 1,         y + h - 2,     1, 1, color);
        fillRect(ms, x + w - 2,     y + h - 2,     1, 1, color);
    }

    /** Draw a circular cooldown arc (0..1 progress) */
    public static void drawCooldownArc(MatrixStack ms, float cx, float cy, float radius, float progress, int color) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableTexture();
        float a = ((color >> 24) & 0xFF) / 255f;
        if (a == 0) a = 1f;
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        RenderSystem.color4f(r, g, b, a);
        net.minecraft.client.renderer.Tessellator tess = net.minecraft.client.renderer.Tessellator.getInstance();
        net.minecraft.client.renderer.BufferBuilder buf = tess.getBuilder();
        Matrix4f mat = ms.last().pose();
        int segments = 32;
        buf.begin(3, net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION);
        for (int i = 0; i <= (int)(segments * progress); i++) {
            float angle = (float)(-Math.PI / 2 + 2 * Math.PI * i / segments);
            buf.vertex(mat, cx + (float)Math.cos(angle) * radius, cy + (float)Math.sin(angle) * radius, 0).endVertex();
        }
        tess.end();
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
}
