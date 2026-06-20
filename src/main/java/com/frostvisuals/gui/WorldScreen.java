package com.frostvisuals.gui;

import com.frostvisuals.config.FrostConfig;
import com.frostvisuals.gui.widgets.ContextMenu;
import com.frostvisuals.gui.widgets.FrostButton;
import com.frostvisuals.utils.ColorUtils;
import com.frostvisuals.utils.RenderUtils;
import com.mojang.blaze3d.matrix.MatrixStack;

public class WorldScreen extends BaseTabScreen {

    public WorldScreen(FrostMainScreen parent) { super(parent, "WORLD"); }

    @Override
    protected void initTab() {
        int x = contentX(), y = contentY(), w = contentW() - 8;

        // ── Master WORLD group button ──────────────────────────
        FrostButton worldBtn = new FrostButton(x, y, w, 18, "World Effects  \u25BA", b -> {})
            .withRightClick(() -> showWorldContext(x, y + 20));
        buttons.add(worldBtn);
        y += 22;

        // ── Snowflakes group ────────────────────────────────────
        FrostButton snowBtn = new FrostButton(x, y, w, 18, "Falling Snowflakes  \u25BA", b -> {})
            .withRightClick(() -> showSnowflakesContext(x, y + 20));
        buttons.add(snowBtn);
        y += 22;

        // ── Sky group ──────────────────────────────────────────
        FrostButton skyBtn = new FrostButton(x, y, w, 18, "Sky & Atmosphere  \u25BA", b -> {})
            .withRightClick(() -> showSkyContext(x, y + 20));
        buttons.add(skyBtn);
        y += 22;

        // ── Block visuals group ────────────────────────────────
        FrostButton blockBtn = new FrostButton(x, y, w, 18, "Block & Color Visuals  \u25BA", b -> {})
            .withRightClick(() -> showBlockContext(x, y + 20));
        buttons.add(blockBtn);
        y += 22;

        // ── Time control ───────────────────────────────────────
        int col = w / 2 - 4;
        addToggleButton("Freeze Time", FrostConfig.timeFreeze, x, y, col, () -> {
            FrostConfig.timeFreeze = !FrostConfig.timeFreeze; FrostConfig.save();
        });
        addToggleButton("Dynamic Lighting", FrostConfig.dynamicLighting, x + col + 8, y, col, () -> {
            FrostConfig.dynamicLighting = !FrostConfig.dynamicLighting; FrostConfig.save();
        });
        y += 20;

        addToggleButton("Weather Control", FrostConfig.weatherControl, x, y, col, () -> {
            FrostConfig.weatherControl = !FrostConfig.weatherControl; FrostConfig.save();
        });
        addToggleButton("Falling Leaves", FrostConfig.fallingLeaves, x + col + 8, y, col, () -> {
            FrostConfig.fallingLeaves = !FrostConfig.fallingLeaves; FrostConfig.save();
        });
        y += 20;

        addToggleButton("Entity Glow (World)", FrostConfig.entityGlow, x, y, col, () -> {
            FrostConfig.entityGlow = !FrostConfig.entityGlow; FrostConfig.save();
        });
        addToggleButton("Hide Void Fog", !FrostConfig.voidFog, x + col + 8, y, col, () -> {
            FrostConfig.voidFog = !FrostConfig.voidFog; FrostConfig.save();
        });
        y += 24;

        // Sliders
        addSlider("Time of Day", 0, 24000, FrostConfig.frozenTime, x, y, w, v -> {
            FrostConfig.frozenTime = (int)v; FrostConfig.save();
        });
        y += 28;
        addSlider("Sun Size", 0.1f, 5f, FrostConfig.sunSize, x, y, w, v -> {
            FrostConfig.sunSize = v; FrostConfig.save();
        });
        y += 28;
        addSlider("Moon Size", 0.1f, 5f, FrostConfig.moonSize, x, y, w, v -> {
            FrostConfig.moonSize = v; FrostConfig.save();
        });
        y += 28;
        addSlider("Stars Visibility", 0f, 2f, FrostConfig.starsVisibility, x, y, w, v -> {
            FrostConfig.starsVisibility = v; FrostConfig.save();
        });
        y += 28;
        addSlider("Fog Density", 0f, 3f, FrostConfig.fogDensity, x, y, w, v -> {
            FrostConfig.fogDensity = v; FrostConfig.save();
        });
        y += 28;
        addSlider("Lava Transparency", 0f, 1f, FrostConfig.lavaTransparency, x, y, w, v -> {
            FrostConfig.lavaTransparency = v; FrostConfig.save();
        });
        y += 28;
        addSlider("Particle Density", 0f, 3f, FrostConfig.particleDensity, x, y, w, v -> {
            FrostConfig.particleDensity = v; FrostConfig.save();
        });
        y += 28;

        maxScroll = Math.max(0, y - parent.getContentY() - (parent.getPanelH() - 60));
    }

    private void showWorldContext(int cx, int cy) {
        ContextMenu cm = parent.getContextMenu();
        cm.clear();
        cm.addItem((FrostConfig.customFog ? "\u2714 " : "  ") + "Custom Fog Color", () -> { FrostConfig.customFog = !FrostConfig.customFog; FrostConfig.save(); });
        cm.addItem((FrostConfig.weatherControl ? "\u2714 " : "  ") + "Rain On/Off", () -> { FrostConfig.rainEnabled = !FrostConfig.rainEnabled; FrostConfig.save(); });
        cm.addItem((FrostConfig.netherFog ? "  " : "\u2714 ") + "Hide Nether Fog", () -> { FrostConfig.netherFog = !FrostConfig.netherFog; FrostConfig.save(); });
        cm.addItem((FrostConfig.voidFog ? "  " : "\u2714 ") + "Hide Void Fog", () -> { FrostConfig.voidFog = !FrostConfig.voidFog; FrostConfig.save(); });
        cm.show(cx, cy);
    }

    private void showSnowflakesContext(int cx, int cy) {
        ContextMenu cm = parent.getContextMenu();
        cm.clear();
        cm.addItem((FrostConfig.fallingSnowflakes ? "\u2714 " : "  ") + "Enable Snowflakes", () -> { FrostConfig.fallingSnowflakes = !FrostConfig.fallingSnowflakes; FrostConfig.save(); });
        cm.addItem("Count: " + FrostConfig.snowflakeCount, () -> { FrostConfig.snowflakeCount = (FrostConfig.snowflakeCount % 200) + 25; FrostConfig.save(); });
        cm.addItem("Speed: " + String.format("%.1f", FrostConfig.snowflakeSpeed), () -> { FrostConfig.snowflakeSpeed = FrostConfig.snowflakeSpeed >= 3f ? 0.5f : FrostConfig.snowflakeSpeed + 0.5f; FrostConfig.save(); });
        cm.addItem("Size: " + String.format("%.1f", FrostConfig.snowflakeSize), () -> { FrostConfig.snowflakeSize = FrostConfig.snowflakeSize >= 3f ? 0.5f : FrostConfig.snowflakeSize + 0.5f; FrostConfig.save(); });
        cm.show(cx, cy);
    }

    private void showSkyContext(int cx, int cy) {
        ContextMenu cm = parent.getContextMenu();
        cm.clear();
        cm.addItem((FrostConfig.customSkyColor ? "\u2714 " : "  ") + "Custom Sky Color", () -> { FrostConfig.customSkyColor = !FrostConfig.customSkyColor; FrostConfig.save(); });
        cm.addItem("Sky Day: #" + ColorUtils.toHex(FrostConfig.skyColorDay), null);
        cm.addItem("Sky Night: #" + ColorUtils.toHex(FrostConfig.skyColorNight), null);
        cm.show(cx, cy);
    }

    private void showBlockContext(int cx, int cy) {
        ContextMenu cm = parent.getContextMenu();
        cm.clear();
        cm.addItem((FrostConfig.customGrassColor ? "\u2714 " : "  ") + "Custom Grass Color", () -> { FrostConfig.customGrassColor = !FrostConfig.customGrassColor; FrostConfig.save(); });
        cm.addItem((FrostConfig.customWaterColor ? "\u2714 " : "  ") + "Custom Water Color", () -> { FrostConfig.customWaterColor = !FrostConfig.customWaterColor; FrostConfig.save(); });
        cm.addItem("Outline: #" + ColorUtils.toHex(FrostConfig.blockOutlineColor), null);
        cm.show(cx, cy);
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partial) {
        drawSectionHeader(ms, "World Visuals", contentX(), parent.getContentY() - 12);
        super.render(ms, mouseX, mouseY, partial);
    }
}
