package com.frostvisuals.gui;

import com.frostvisuals.config.FrostConfig;
import com.frostvisuals.gui.widgets.FrostButton;
import com.frostvisuals.gui.widgets.FrostSlider;
import com.frostvisuals.gui.widgets.ContextMenu;
import com.frostvisuals.utils.ColorUtils;
import com.frostvisuals.utils.RenderUtils;
import com.mojang.blaze3d.matrix.MatrixStack;

public class VisualsScreen extends BaseTabScreen {

    public VisualsScreen(FrostMainScreen parent) { super(parent, "VISUALS"); }

    @Override
    protected void initTab() {
        int x = contentX(), y = contentY(), w = contentW() - 8;
        int col = w / 2 - 4;

        // ── Overlays group button ─────────────────────────────
        FrostButton overlaysBtn = new FrostButton(x, y, w, 18, "Overlays  \u25BA", b -> {})
            .withRightClick(() -> showOverlaysContext(x + w, y));
        buttons.add(overlaysBtn);
        y += 22;

        // ── Sword Position ─────────────────────────────────────
        FrostButton swordBtn = new FrostButton(x, y, w, 18, "Sword Position Editor  \u25BA", b -> {})
            .withRightClick(() -> showSwordContext(x + w, y));
        buttons.add(swordBtn);
        y += 22;

        // ── Toggles row 1 ─────────────────────────────────────
        addToggleButton("Old Animations", FrostConfig.oldAnimations, x, y, col, () -> {
            FrostConfig.oldAnimations = !FrostConfig.oldAnimations; FrostConfig.save();
        });
        addToggleButton("Fullbright", FrostConfig.fullbright, x + col + 8, y, col, () -> {
            FrostConfig.fullbright = !FrostConfig.fullbright; FrostConfig.save();
        });
        y += 20;

        addToggleButton("Item Physics", FrostConfig.itemPhysics, x, y, col, () -> {
            FrostConfig.itemPhysics = !FrostConfig.itemPhysics; FrostConfig.save();
        });
        addToggleButton("Freelook", FrostConfig.freelook, x + col + 8, y, col, () -> {
            FrostConfig.freelook = !FrostConfig.freelook; FrostConfig.save();
        });
        y += 20;

        addToggleButton("No Hurt Camera", FrostConfig.noHurtCamera, x, y, col, () -> {
            FrostConfig.noHurtCamera = !FrostConfig.noHurtCamera; FrostConfig.save();
        });
        addToggleButton("Damage Tilt", FrostConfig.damageTilt, x + col + 8, y, col, () -> {
            FrostConfig.damageTilt = !FrostConfig.damageTilt; FrostConfig.save();
        });
        y += 20;

        addToggleButton("Entity Glow", FrostConfig.entityGlow, x, y, col, () -> {
            FrostConfig.entityGlow = !FrostConfig.entityGlow; FrostConfig.save();
        });
        addToggleButton("Scope Effect", FrostConfig.scopeEffect, x + col + 8, y, col, () -> {
            FrostConfig.scopeEffect = !FrostConfig.scopeEffect; FrostConfig.save();
        });
        y += 24;

        // ── Sliders ───────────────────────────────────────────
        addSlider("Swing Speed", 0.1f, 3.0f, FrostConfig.swingSpeed, x, y, w, v -> {
            FrostConfig.swingSpeed = v; FrostConfig.save();
        });
        y += 28;

        addSlider("Enchant Glint Speed", 0.1f, 5.0f, FrostConfig.enchantGlintSpeed, x, y, w, v -> {
            FrostConfig.enchantGlintSpeed = v; FrostConfig.save();
        });
        y += 28;

        addSlider("Block Outline Width", 0.5f, 4.0f, FrostConfig.blockOutlineWidth, x, y, w, v -> {
            FrostConfig.blockOutlineWidth = v; FrostConfig.save();
        });
        y += 28;

        addSlider("Arm Sway", 0f, 2.0f, FrostConfig.armSwayAmount, x, y, w, v -> {
            FrostConfig.armSwayAmount = v; FrostConfig.save();
        });
        y += 28;

        // ── Tool Swap Animation ───────────────────────────────
        String[] anims = {"SLIDE", "ROTATE", "BOUNCE", "NONE"};
        for (String anim : anims) {
            boolean sel = FrostConfig.toolSwapAnimation.equals(anim);
            FrostButton ab = new FrostButton(x, y, w / 4 - 2, 16, anim, b -> {
                FrostConfig.toolSwapAnimation = anim; FrostConfig.save(); initTab();
            }).asToggle(sel, () -> {});
            ab.setToggled(sel);
            buttons.add(ab);
            x += w / 4;
        }
        x = contentX(); y += 22;

        maxScroll = Math.max(0, y - parent.getContentY() - (parent.getPanelH() - 60));
    }

    private void showOverlaysContext(int cx, int cy) {
        ContextMenu cm = parent.getContextMenu();
        cm.clear();
        cm.addItem((FrostConfig.noTotemOverlay ? "\u2714 " : "  ") + "No Totem Overlay", () -> { FrostConfig.noTotemOverlay = !FrostConfig.noTotemOverlay; FrostConfig.save(); });
        cm.addItem((FrostConfig.noLavaOverlay ? "\u2714 " : "  ") + "No Lava Overlay", () -> { FrostConfig.noLavaOverlay = !FrostConfig.noLavaOverlay; FrostConfig.save(); });
        cm.addItem((FrostConfig.noFireOverlay ? "\u2714 " : "  ") + "No Fire Overlay", () -> { FrostConfig.noFireOverlay = !FrostConfig.noFireOverlay; FrostConfig.save(); });
        cm.addItem((FrostConfig.noWaterOverlay ? "\u2714 " : "  ") + "No Water Overlay", () -> { FrostConfig.noWaterOverlay = !FrostConfig.noWaterOverlay; FrostConfig.save(); });
        cm.addItem((FrostConfig.noPumpkinOverlay ? "\u2714 " : "  ") + "No Pumpkin Overlay", () -> { FrostConfig.noPumpkinOverlay = !FrostConfig.noPumpkinOverlay; FrostConfig.save(); });
        cm.addItem((FrostConfig.noSnowOverlay ? "\u2714 " : "  ") + "No Snow Overlay", () -> { FrostConfig.noSnowOverlay = !FrostConfig.noSnowOverlay; FrostConfig.save(); });
        cm.addItem((FrostConfig.noDarknessEffect ? "\u2714 " : "  ") + "No Darkness Effect", () -> { FrostConfig.noDarknessEffect = !FrostConfig.noDarknessEffect; FrostConfig.save(); });
        cm.addItem((FrostConfig.noNauseaEffect ? "\u2714 " : "  ") + "No Nausea Effect", () -> { FrostConfig.noNauseaEffect = !FrostConfig.noNauseaEffect; FrostConfig.save(); });
        cm.show(cx - 130, cy);
    }

    private void showSwordContext(int cx, int cy) {
        ContextMenu cm = parent.getContextMenu();
        cm.clear();
        cm.addItem("EDIT (Blockbench Mode)", () -> {
            if (minecraft != null && minecraft.screen != null) {
                minecraft.setScreen(new SwordEditorScreen());
            }
        });
        cm.addItem("Reset to Default", () -> {
            FrostConfig.swordX = 0; FrostConfig.swordY = 0; FrostConfig.swordZ = 0;
            FrostConfig.swordRotX = 0; FrostConfig.swordRotY = 0; FrostConfig.swordRotZ = 0;
            FrostConfig.swordScale = 1.0f; FrostConfig.save();
        });
        cm.show(cx - 130, cy);
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partial) {
        drawSectionHeader(ms, "Visual Enhancements", contentX(), parent.getContentY() - 12);
        super.render(ms, mouseX, mouseY, partial);
    }
}
