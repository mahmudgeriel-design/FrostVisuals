package com.frostvisuals.gui;

import com.frostvisuals.config.FrostConfig;
import com.frostvisuals.gui.widgets.FrostButton;
import com.frostvisuals.utils.ColorUtils;
import com.frostvisuals.utils.RenderUtils;
import com.mojang.blaze3d.matrix.MatrixStack;

public class BindsScreen extends BaseTabScreen {

    public BindsScreen(FrostMainScreen parent) { super(parent, "BINDS"); }

    @Override
    protected void initTab() {
        int x = contentX(), y = contentY(), w = contentW() - 8;
        int col = w / 2 - 4;

        addToggleButton("Pearl Bind [Hotbar only]", FrostConfig.pearlBind, x, y, w, () -> {
            FrostConfig.pearlBind = !FrostConfig.pearlBind; FrostConfig.save();
        });
        y += 20;

        addToggleButton("Chorus Bind [Hotbar only]", FrostConfig.chorusBind, x, y, w, () -> {
            FrostConfig.chorusBind = !FrostConfig.chorusBind; FrostConfig.save();
        });
        y += 20;

        addToggleButton("Fishing Rod Bind [Hotbar only]", FrostConfig.rodBind, x, y, w, () -> {
            FrostConfig.rodBind = !FrostConfig.rodBind; FrostConfig.save();
        });
        y += 20;

        addToggleButton("Shield Bind [Offhand]", FrostConfig.shieldBind, x, y, w, () -> {
            FrostConfig.shieldBind = !FrostConfig.shieldBind; FrostConfig.save();
        });
        y += 20;

        addToggleButton("Golden Apple Bind [Hotbar only]", FrostConfig.gapBind, x, y, w, () -> {
            FrostConfig.gapBind = !FrostConfig.gapBind; FrostConfig.save();
        });
        y += 20;

        addToggleButton("Sword Swap [Hotbar only]", FrostConfig.swordSwapBind, x, y, w, () -> {
            FrostConfig.swordSwapBind = !FrostConfig.swordSwapBind; FrostConfig.save();
        });
        y += 28;

        maxScroll = Math.max(0, y - parent.getContentY() - (parent.getPanelH() - 60));
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partial) {
        drawSectionHeader(ms, "Item Binds", contentX(), parent.getContentY() - 12);
        // Info text
        int ix = contentX();
        int iy = parent.getContentY() - 2;
        font.draw(ms, "Binds only activate if item is in hotbar", ix, iy, 0x6A6A88);
        super.render(ms, mouseX, mouseY, partial);
    }
}
