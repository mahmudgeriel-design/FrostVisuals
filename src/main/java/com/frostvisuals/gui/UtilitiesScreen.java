package com.frostvisuals.gui;

import com.frostvisuals.config.FrostConfig;
import com.mojang.blaze3d.matrix.MatrixStack;

public class UtilitiesScreen extends BaseTabScreen {

    public UtilitiesScreen(FrostMainScreen parent) { super(parent, "UTILITIES"); }

    @Override
    protected void initTab() {
        int x = contentX(), y = contentY(), w = contentW() - 8;
        int col = w / 2 - 4;

        addToggleButton("Toggle Sneak", FrostConfig.toggleSneak, x, y, col, () -> { FrostConfig.toggleSneak = !FrostConfig.toggleSneak; FrostConfig.save(); });
        addToggleButton("Toggle Sprint", FrostConfig.toggleSprint, x + col + 8, y, col, () -> { FrostConfig.toggleSprint = !FrostConfig.toggleSprint; FrostConfig.save(); });
        y += 20;
        addToggleButton("Anti-Blind", FrostConfig.antiBlind, x, y, col, () -> { FrostConfig.antiBlind = !FrostConfig.antiBlind; FrostConfig.save(); });
        addToggleButton("No Invisible Mobs", FrostConfig.noInvisible, x + col + 8, y, col, () -> { FrostConfig.noInvisible = !FrostConfig.noInvisible; FrostConfig.save(); });
        y += 20;
        addToggleButton("Chunk Borders", FrostConfig.chunkBorders, x, y, col, () -> { FrostConfig.chunkBorders = !FrostConfig.chunkBorders; FrostConfig.save(); });
        addToggleButton("Show Hitboxes", FrostConfig.hitboxes, x + col + 8, y, col, () -> { FrostConfig.hitboxes = !FrostConfig.hitboxes; FrostConfig.save(); });
        y += 20;
        maxScroll = 0;
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partial) {
        drawSectionHeader(ms, "Utilities", contentX(), parent.getContentY() - 12);
        super.render(ms, mouseX, mouseY, partial);
    }
}
