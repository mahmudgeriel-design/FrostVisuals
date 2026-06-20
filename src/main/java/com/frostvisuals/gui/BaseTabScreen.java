package com.frostvisuals.gui;

import com.frostvisuals.config.FrostConfig;
import com.frostvisuals.gui.widgets.ContextMenu;
import com.frostvisuals.gui.widgets.FrostButton;
import com.frostvisuals.gui.widgets.FrostSlider;
import com.frostvisuals.utils.ColorUtils;
import com.frostvisuals.utils.RenderUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTabScreen extends Screen {

    protected final FrostMainScreen parent;
    protected int scrollOffset = 0;
    protected int maxScroll = 0;

    // Current right-click context button for this tab
    protected ContextMenu contextMenu = new ContextMenu();

    public BaseTabScreen(FrostMainScreen parent, String title) {
        super(new StringTextComponent(title));
        this.parent = parent;
    }

    @Override
    public void init(Minecraft mc, int w, int h) {
        this.minecraft = mc;
        this.font = mc.font;
        this.width = w;
        this.height = h;
        this.buttons.clear();
        initTab();
    }

    protected abstract void initTab();

    protected int contentX() { return parent.getPanelX() + 8; }
    protected int contentY() { return parent.getContentY() - scrollOffset; }
    protected int contentW() { return parent.getPanelW() - 16; }

    protected void drawSectionHeader(MatrixStack ms, String text, int x, int y) {
        long time = System.currentTimeMillis();
        int color = ColorUtils.frostGradient(time / 1000f);
        RenderUtils.fillRect(ms, x, y + 8, contentW(), 1, ColorUtils.withAlpha(color, 100));
        font.draw(ms, text, x, y, color);
    }

    protected FrostButton addToggleButton(String label, boolean current, int x, int y, int w, Runnable onToggle) {
        FrostButton btn = new FrostButton(x, y, w, 16, label, b -> {}).asToggle(current, onToggle);
        buttons.add(btn);
        return btn;
    }

    protected FrostSlider addSlider(String label, float min, float max, float current, int x, int y, int w, java.util.function.Consumer<Float> onChange) {
        FrostSlider slider = new FrostSlider(x, y + 10, w, 14, label, min, max, current, onChange);
        buttons.add(slider);
        return slider;
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double delta) {
        scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset - (int)(delta * 12)));
        initTab();
        return true;
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (button == 1) {
            for (net.minecraft.client.gui.widget.Widget w : buttons) {
                if (w instanceof FrostButton) {
                    if (((FrostButton) w).mouseRightClicked(mx, my)) return true;
                }
            }
        }
        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}
