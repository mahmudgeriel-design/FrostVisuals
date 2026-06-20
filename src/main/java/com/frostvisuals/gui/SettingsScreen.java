package com.frostvisuals.gui;

import com.frostvisuals.config.FrostConfig;
import com.frostvisuals.gui.widgets.FrostButton;
import com.frostvisuals.utils.ColorUtils;
import com.mojang.blaze3d.matrix.MatrixStack;

public class SettingsScreen extends BaseTabScreen {

    public SettingsScreen(FrostMainScreen parent) { super(parent, "SETTINGS"); }

    @Override
    protected void initTab() {
        int x = contentX(), y = contentY(), w = contentW() - 8;

        addToggleButton("Discord Presence", FrostConfig.discordPresence, x, y, w, () -> {
            FrostConfig.discordPresence = !FrostConfig.discordPresence; FrostConfig.save();
        });
        y += 22;

        buttons.add(new FrostButton(x, y, w, 18, "Reset All Settings", b -> {
            FrostConfig.init();
            FrostConfig.save();
            initTab();
        }));
        y += 22;

        buttons.add(new FrostButton(x, y, w, 18, "Save Config", b -> {
            FrostConfig.save();
        }));
        y += 22;

        maxScroll = 0;
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partial) {
        drawSectionHeader(ms, "Settings", contentX(), parent.getContentY() - 12);
        // Version info
        font.draw(ms, "\u2744 FrostVisuals v1.0.0  |  MC 1.16.5",
                contentX(), parent.getPanelY() + parent.getPanelH() - 20, ColorUtils.withAlpha(0x6A35FF, 200));
        super.render(ms, mouseX, mouseY, partial);
    }
}
