package com.frostvisuals.gui;

import com.frostvisuals.config.FrostConfig;
import com.frostvisuals.gui.widgets.FrostButton;
import com.mojang.blaze3d.matrix.MatrixStack;

public class HudScreen extends BaseTabScreen {

    public HudScreen(FrostMainScreen parent) { super(parent, "HUD"); }

    @Override
    protected void initTab() {
        int x = contentX(), y = contentY(), w = contentW() - 8;
        int col = w / 2 - 4;

        addToggleButton("FrostVisuals Branding Bar", FrostConfig.hudBrandingBar, x, y, w, () -> { FrostConfig.hudBrandingBar = !FrostConfig.hudBrandingBar; FrostConfig.save(); });
        y += 20;
        addToggleButton("FPS Counter", FrostConfig.fpsCounter, x, y, col, () -> { FrostConfig.fpsCounter = !FrostConfig.fpsCounter; FrostConfig.save(); });
        addToggleButton("CPS Counter", FrostConfig.cpsCounter, x + col + 8, y, col, () -> { FrostConfig.cpsCounter = !FrostConfig.cpsCounter; FrostConfig.save(); });
        y += 20;
        addToggleButton("Ping Display", FrostConfig.pingDisplay, x, y, col, () -> { FrostConfig.pingDisplay = !FrostConfig.pingDisplay; FrostConfig.save(); });
        addToggleButton("Coordinates", FrostConfig.coordsDisplay, x + col + 8, y, col, () -> { FrostConfig.coordsDisplay = !FrostConfig.coordsDisplay; FrostConfig.save(); });
        y += 20;
        addToggleButton("Direction", FrostConfig.directionDisplay, x, y, col, () -> { FrostConfig.directionDisplay = !FrostConfig.directionDisplay; FrostConfig.save(); });
        addToggleButton("Biome", FrostConfig.biomeDisplay, x + col + 8, y, col, () -> { FrostConfig.biomeDisplay = !FrostConfig.biomeDisplay; FrostConfig.save(); });
        y += 20;
        addToggleButton("Speed Display", FrostConfig.speedDisplay, x, y, col, () -> { FrostConfig.speedDisplay = !FrostConfig.speedDisplay; FrostConfig.save(); });
        addToggleButton("Armor Display", FrostConfig.armorDisplay, x + col + 8, y, col, () -> { FrostConfig.armorDisplay = !FrostConfig.armorDisplay; FrostConfig.save(); });
        y += 20;
        addToggleButton("Armor Column", FrostConfig.armorColumn, x, y, col, () -> { FrostConfig.armorColumn = !FrostConfig.armorColumn; FrostConfig.save(); });
        addToggleButton("Potion Effects", FrostConfig.potionEffects, x + col + 8, y, col, () -> { FrostConfig.potionEffects = !FrostConfig.potionEffects; FrostConfig.save(); });
        y += 20;
        addToggleButton("Buffs & Debuffs", FrostConfig.buffsDisplay, x, y, col, () -> { FrostConfig.buffsDisplay = !FrostConfig.buffsDisplay; FrostConfig.save(); });
        addToggleButton("Item Cooldowns", FrostConfig.itemCooldowns, x + col + 8, y, col, () -> { FrostConfig.itemCooldowns = !FrostConfig.itemCooldowns; FrostConfig.save(); });
        y += 20;
        addToggleButton("Totem Counter", FrostConfig.totemCounter, x, y, col, () -> { FrostConfig.totemCounter = !FrostConfig.totemCounter; FrostConfig.save(); });
        addToggleButton("Target HUD", FrostConfig.targetHud, x + col + 8, y, col, () -> { FrostConfig.targetHud = !FrostConfig.targetHud; FrostConfig.save(); });
        y += 20;
        addToggleButton("Transparent Inventory", FrostConfig.transparentInventory, x, y, col, () -> { FrostConfig.transparentInventory = !FrostConfig.transparentInventory; FrostConfig.save(); });
        addToggleButton("Combo Counter", FrostConfig.comboCounter, x + col + 8, y, col, () -> { FrostConfig.comboCounter = !FrostConfig.comboCounter; FrostConfig.save(); });
        y += 20;
        addToggleButton("Kill/Death Counter", FrostConfig.killDeathCounter, x, y, col, () -> { FrostConfig.killDeathCounter = !FrostConfig.killDeathCounter; FrostConfig.save(); });
        addToggleButton("Keystrokes", FrostConfig.keystrokesDisplay, x + col + 8, y, col, () -> { FrostConfig.keystrokesDisplay = !FrostConfig.keystrokesDisplay; FrostConfig.save(); });
        y += 20;
        addToggleButton("Clock", FrostConfig.clockDisplay, x, y, col, () -> { FrostConfig.clockDisplay = !FrostConfig.clockDisplay; FrostConfig.save(); });
        addToggleButton("Entity Counter", FrostConfig.entityCounter, x + col + 8, y, col, () -> { FrostConfig.entityCounter = !FrostConfig.entityCounter; FrostConfig.save(); });
        y += 20;
        addToggleButton("Chunk Display", FrostConfig.chunkDisplay, x, y, col, () -> { FrostConfig.chunkDisplay = !FrostConfig.chunkDisplay; FrostConfig.save(); });
        addToggleButton("Custom Scoreboard", FrostConfig.scoreboardCustom, x + col + 8, y, col, () -> { FrostConfig.scoreboardCustom = !FrostConfig.scoreboardCustom; FrostConfig.save(); });
        y += 28;
        addSlider("Inventory Opacity", 0f, 1f, FrostConfig.inventoryOpacity, x, y, w, v -> { FrostConfig.inventoryOpacity = v; FrostConfig.save(); });
        y += 28;
        maxScroll = Math.max(0, y - parent.getContentY() - (parent.getPanelH() - 60));
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partial) {
        drawSectionHeader(ms, "HUD Elements", contentX(), parent.getContentY() - 12);
        super.render(ms, mouseX, mouseY, partial);
    }
}
