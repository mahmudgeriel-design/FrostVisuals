package com.frostvisuals.gui.widgets;

import com.frostvisuals.utils.ColorUtils;
import com.frostvisuals.utils.RenderUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class ContextMenu {

    public static class ContextItem {
        public String label;
        public Runnable action;
        public boolean separator;
        public ContextItem(String label, Runnable action) { this.label = label; this.action = action; }
        public static ContextItem separator() { ContextItem i = new ContextItem("", null); i.separator = true; return i; }
    }

    private int x, y;
    private List<ContextItem> items = new ArrayList<>();
    private boolean visible = false;
    private int hoveredIndex = -1;
    private static final int ITEM_H = 14;
    private static final int WIDTH = 120;

    public void show(int x, int y) {
        this.x = x; this.y = y; this.visible = true;
    }
    public void hide() { visible = false; }
    public boolean isVisible() { return visible; }
    public void addItem(String label, Runnable action) { items.add(new ContextItem(label, action)); }
    public void addSeparator() { items.add(ContextItem.separator()); }
    public void clear() { items.clear(); }

    public void render(MatrixStack ms, int mouseX, int mouseY) {
        if (!visible) return;
        int totalH = 0;
        for (ContextItem item : items) totalH += item.separator ? 4 : ITEM_H;

        // Shadow
        RenderUtils.fillRect(ms, x + 2, y + 2, WIDTH, totalH + 6, ColorUtils.withAlpha(0x000000, 100));
        // Background
        RenderUtils.fillRect(ms, x, y, WIDTH, totalH + 6, ColorUtils.withAlpha(0x0A0E1A, 240));
        // Outline
        long time = System.currentTimeMillis();
        RenderUtils.drawRoundedOutline(ms, x, y, WIDTH, totalH + 6, ColorUtils.withAlpha(ColorUtils.frostGradient(time / 1000f), 220));

        int iy = y + 3;
        int idx = 0;
        hoveredIndex = -1;
        for (ContextItem item : items) {
            if (item.separator) {
                RenderUtils.fillRect(ms, x + 4, iy + 1, WIDTH - 8, 1, ColorUtils.withAlpha(0x6A35FF, 100));
                iy += 4;
            } else {
                boolean hov = mouseX >= x && mouseX <= x + WIDTH && mouseY >= iy && mouseY <= iy + ITEM_H;
                if (hov) {
                    hoveredIndex = idx;
                    RenderUtils.fillRect(ms, x + 1, iy, WIDTH - 2, ITEM_H, ColorUtils.withAlpha(0x1A0A3A, 200));
                }
                int textCol = hov ? 0xCCBBFF : 0xAABBCC;
                Minecraft.getInstance().font.draw(ms, item.label, x + 6, iy + 3, textCol);
                iy += ITEM_H;
                idx++;
            }
        }
    }

    public boolean mouseClicked(double mx, double my, int button) {
        if (!visible) return false;
        if (button == 0) {
            int iy = y + 3;
            int idx = 0;
            for (ContextItem item : items) {
                if (item.separator) { iy += 4; continue; }
                if (mx >= x && mx <= x + WIDTH && my >= iy && my <= iy + ITEM_H) {
                    if (item.action != null) item.action.run();
                    hide();
                    return true;
                }
                iy += ITEM_H;
                idx++;
            }
            // Click outside = close
            int totalH = 0;
            for (ContextItem it : items) totalH += it.separator ? 4 : ITEM_H;
            if (mx < x || mx > x + WIDTH || my < y || my > y + totalH + 6) { hide(); return false; }
        }
        return false;
    }
}
