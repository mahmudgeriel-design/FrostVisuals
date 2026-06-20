package com.frostvisuals.gui;

import com.frostvisuals.config.FrostConfig;
import com.frostvisuals.gui.widgets.FrostButton;
import com.frostvisuals.gui.widgets.ContextMenu;
import com.mojang.blaze3d.matrix.MatrixStack;

public class SoundsScreen extends BaseTabScreen {

    public SoundsScreen(FrostMainScreen parent) { super(parent, "SOUNDS"); }

    @Override
    protected void initTab() {
        int x = contentX(), y = contentY(), w = contentW() - 8;
        int col = w / 2 - 4;

        // Hit Sound group
        FrostButton hitBtn = new FrostButton(x, y, w, 18, "Hit Sound  \u25BA", b -> {})
            .withRightClick(() -> showHitSoundContext(x, y + 20));
        buttons.add(hitBtn);
        y += 22;

        addToggleButton("Bow Hit Sound", FrostConfig.bowHitSound, x, y, col, () -> { FrostConfig.bowHitSound = !FrostConfig.bowHitSound; FrostConfig.save(); });
        addToggleButton("Kill Sound", FrostConfig.killSound, x + col + 8, y, col, () -> { FrostConfig.killSound = !FrostConfig.killSound; FrostConfig.save(); });
        y += 20;

        addToggleButton("Crit Sound", FrostConfig.critSound, x, y, col, () -> { FrostConfig.critSound = !FrostConfig.critSound; FrostConfig.save(); });
        addToggleButton("Headshot Sound", FrostConfig.headShotSound, x + col + 8, y, col, () -> { FrostConfig.headShotSound = !FrostConfig.headShotSound; FrostConfig.save(); });
        y += 20;

        addToggleButton("Combo Sound", FrostConfig.comboSound, x, y, col, () -> { FrostConfig.comboSound = !FrostConfig.comboSound; FrostConfig.save(); });
        addToggleButton("Totem Sound", FrostConfig.totemSound, x + col + 8, y, col, () -> { FrostConfig.totemSound = !FrostConfig.totemSound; FrostConfig.save(); });
        y += 20;

        addToggleButton("Buff Sound", FrostConfig.buffSound, x, y, col, () -> { FrostConfig.buffSound = !FrostConfig.buffSound; FrostConfig.save(); });
        addToggleButton("Cooldown Ready", FrostConfig.cooldownReadySound, x + col + 8, y, col, () -> { FrostConfig.cooldownReadySound = !FrostConfig.cooldownReadySound; FrostConfig.save(); });
        y += 20;

        addToggleButton("Low Health Sound", FrostConfig.lowHealthSound, x, y, col, () -> { FrostConfig.lowHealthSound = !FrostConfig.lowHealthSound; FrostConfig.save(); });
        addToggleButton("Item Swap Sound", FrostConfig.itemSwapSound, x + col + 8, y, col, () -> { FrostConfig.itemSwapSound = !FrostConfig.itemSwapSound; FrostConfig.save(); });
        y += 24;

        addSlider("Hit Sound Pitch", 0.1f, 3.0f, FrostConfig.hitSoundPitch, x, y, w, v -> { FrostConfig.hitSoundPitch = v; FrostConfig.save(); });
        y += 28;
        addSlider("Hit Sound Volume", 0f, 2.0f, FrostConfig.hitSoundVolume, x, y, w, v -> { FrostConfig.hitSoundVolume = v; FrostConfig.save(); });
        y += 28;
        addSlider("Low HP Threshold", 1f, 10f, FrostConfig.lowHealthThreshold, x, y, w, v -> { FrostConfig.lowHealthThreshold = v; FrostConfig.save(); });
        y += 28;

        maxScroll = Math.max(0, y - parent.getContentY() - (parent.getPanelH() - 60));
    }

    private void showHitSoundContext(int cx, int cy) {
        ContextMenu cm = parent.getContextMenu();
        cm.clear();
        cm.addItem((FrostConfig.hitSound ? "\u2714 " : "  ") + "Enable Hit Sound", () -> { FrostConfig.hitSound = !FrostConfig.hitSound; FrostConfig.save(); });
        cm.addSeparator();
        String[] presets = {"CLASSIC", "SOFT", "THUD", "PING", "ORB"};
        for (String p : presets) {
            cm.addItem((FrostConfig.hitSoundPreset.equals(p) ? "\u2714 " : "  ") + p, () -> { FrostConfig.hitSoundPreset = p; FrostConfig.save(); });
        }
        cm.show(cx, cy);
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partial) {
        drawSectionHeader(ms, "Custom Sounds", contentX(), parent.getContentY() - 12);
        super.render(ms, mouseX, mouseY, partial);
    }
}
