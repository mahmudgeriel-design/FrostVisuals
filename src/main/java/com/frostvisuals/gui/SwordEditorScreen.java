package com.frostvisuals.gui;

import com.frostvisuals.config.FrostConfig;
import com.frostvisuals.utils.ColorUtils;
import com.frostvisuals.utils.RenderUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

/**
 * Blockbench-style sword position editor.
 * World is "frozen" (tick rate manipulation via clientTickCount is not
 * directly possible, but we pause by not calling the game loop via the
 * screen pause mechanism — isPauseScreen() returns true here for singleplayer).
 *
 * Arrow keys → move X/Y
 * Shift+Arrows → rotate
 * Z/X → scale
 * Enter → save, Esc → cancel
 */
public class SwordEditorScreen extends Screen {

    private float sx, sy, sz, srx, sry, srz, sscale;
    private static final float MOVE_STEP  = 0.01f;
    private static final float ROT_STEP   = 1.0f;
    private static final float SCALE_STEP = 0.02f;

    private String mode = "MOVE"; // MOVE / ROTATE / SCALE

    public SwordEditorScreen() {
        super(new StringTextComponent("FrostVisuals Sword Editor"));
        // Cache current values so we can cancel
        sx     = FrostConfig.swordX;     sy     = FrostConfig.swordY;     sz     = FrostConfig.swordZ;
        srx    = FrostConfig.swordRotX;  sry    = FrostConfig.swordRotY;  srz    = FrostConfig.swordRotZ;
        sscale = FrostConfig.swordScale;
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partial) {
        // Dim the world
        RenderUtils.fillRect(ms, 0, 0, width, height, ColorUtils.withAlpha(0x000000, 120));

        // Top overlay bar
        RenderUtils.fillRect(ms, 0, 0, width, 24, ColorUtils.withAlpha(0x0A0E1A, 230));
        long time = System.currentTimeMillis();
        int frostColor = ColorUtils.frostGradient(time / 1000f);
        RenderUtils.fillRect(ms, 0, 23, width, 1, ColorUtils.withAlpha(frostColor, 180));
        font.draw(ms, "\u2744 FrostVisuals  \u2502  SWORD EDITOR  \u2502  Mode: " + mode, 8, 8, frostColor);

        // Values panel bottom-left
        int px = 8, py = height - 90;
        RenderUtils.fillRect(ms, px - 4, py - 4, 200, 84, ColorUtils.withAlpha(0x0A0E1A, 210));
        RenderUtils.drawRoundedOutline(ms, px - 4, py - 4, 200, 84, ColorUtils.withAlpha(frostColor, 180));

        font.draw(ms, String.format("X: %.3f   Y: %.3f   Z: %.3f",     FrostConfig.swordX, FrostConfig.swordY, FrostConfig.swordZ), px, py,      0xAABBCC); py += 11;
        font.draw(ms, String.format("RX: %.1f  RY: %.1f  RZ: %.1f",    FrostConfig.swordRotX, FrostConfig.swordRotY, FrostConfig.swordRotZ), px, py, 0xAABBCC); py += 11;
        font.draw(ms, String.format("Scale: %.2f",                       FrostConfig.swordScale), px, py, 0xAABBCC); py += 14;
        font.draw(ms, "[Arrows] Move  [Shift+Arrows] Rotate",            px, py, 0x666688); py += 11;
        font.draw(ms, "[Z/X] Scale  [Enter] Save  [Esc] Cancel",         px, py, 0x666688);

        // Mode buttons top-right
        String[] modes = {"MOVE", "ROTATE", "SCALE"};
        int bx = width - 190;
        for (String m : modes) {
            boolean sel = mode.equals(m);
            int bg = sel ? ColorUtils.withAlpha(0x6A35FF, 180) : ColorUtils.withAlpha(0x0A0E1A, 200);
            RenderUtils.fillRect(ms, bx, 4, 56, 16, bg);
            RenderUtils.drawRoundedOutline(ms, bx, 4, 56, 16, ColorUtils.withAlpha(frostColor, sel ? 255 : 140));
            font.draw(ms, m, bx + 4, 8, sel ? 0xFFFFFF : 0x8888AA);
            bx += 62;
        }

        super.render(ms, mouseX, mouseY, partial);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean shift = (modifiers & 1) != 0;

        // GLFW key codes
        final int KEY_UP = 265, KEY_DOWN = 264, KEY_LEFT = 263, KEY_RIGHT = 262;
        final int KEY_Z = 90, KEY_X = 88, KEY_ENTER = 257, KEY_NUMPAD_ENTER = 335;

        if (keyCode == KEY_ENTER || keyCode == KEY_NUMPAD_ENTER) {
            FrostConfig.save();
            onClose();
            return true;
        }

        if (mode.equals("MOVE") || (!shift && mode.equals("MOVE"))) {
            if (!shift) {
                // Arrow = move XY
                if (keyCode == KEY_UP)    FrostConfig.swordY -= MOVE_STEP;
                if (keyCode == KEY_DOWN)  FrostConfig.swordY += MOVE_STEP;
                if (keyCode == KEY_LEFT)  FrostConfig.swordX -= MOVE_STEP;
                if (keyCode == KEY_RIGHT) FrostConfig.swordX += MOVE_STEP;
            } else {
                // Shift+Arrow = move Z + rotate
                if (keyCode == KEY_UP)    FrostConfig.swordZ -= MOVE_STEP;
                if (keyCode == KEY_DOWN)  FrostConfig.swordZ += MOVE_STEP;
                if (keyCode == KEY_LEFT)  FrostConfig.swordRotY -= ROT_STEP;
                if (keyCode == KEY_RIGHT) FrostConfig.swordRotY += ROT_STEP;
            }
        }

        if (mode.equals("ROTATE")) {
            if (!shift) {
                if (keyCode == KEY_UP)    FrostConfig.swordRotX -= ROT_STEP;
                if (keyCode == KEY_DOWN)  FrostConfig.swordRotX += ROT_STEP;
                if (keyCode == KEY_LEFT)  FrostConfig.swordRotZ -= ROT_STEP;
                if (keyCode == KEY_RIGHT) FrostConfig.swordRotZ += ROT_STEP;
            } else {
                if (keyCode == KEY_LEFT)  FrostConfig.swordRotY -= ROT_STEP;
                if (keyCode == KEY_RIGHT) FrostConfig.swordRotY += ROT_STEP;
            }
        }

        if (mode.equals("SCALE")) {
            if (keyCode == KEY_UP || keyCode == KEY_RIGHT) FrostConfig.swordScale += SCALE_STEP;
            if (keyCode == KEY_DOWN || keyCode == KEY_LEFT) FrostConfig.swordScale = Math.max(0.1f, FrostConfig.swordScale - SCALE_STEP);
        }

        // Z/X cycle scale regardless of mode
        if (keyCode == KEY_Z) { FrostConfig.swordScale += SCALE_STEP; return true; }
        if (keyCode == KEY_X) { FrostConfig.swordScale = Math.max(0.1f, FrostConfig.swordScale - SCALE_STEP); return true; }

        // Tab cycle modes
        if (keyCode == 258 /* TAB */) {
            if (mode.equals("MOVE"))   mode = "ROTATE";
            else if (mode.equals("ROTATE")) mode = "SCALE";
            else mode = "MOVE";
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        // Mode buttons
        String[] modes = {"MOVE", "ROTATE", "SCALE"};
        int bx = width - 190;
        for (String m : modes) {
            if (mx >= bx && mx <= bx + 56 && my >= 4 && my <= 20) { mode = m; return true; }
            bx += 62;
        }
        return super.mouseClicked(mx, my, button);
    }

    @Override
    public void onClose() {
        if (minecraft != null) minecraft.setScreen(null);
    }

    @Override
    public boolean isPauseScreen() { return true; }
}
