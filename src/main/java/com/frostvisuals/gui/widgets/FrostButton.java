package com.frostvisuals.gui.widgets;

import com.frostvisuals.utils.ColorUtils;
import com.frostvisuals.utils.RenderUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class FrostButton extends Button {

    private boolean expanded = false;
    private boolean hovered = false;
    private float animProgress = 0f;
    private long lastRender = 0;

    // Context-menu callback (right-click)
    private Runnable onRightClick = null;
    // Toggle state
    private boolean toggled = false;
    private boolean isToggle = false;
    private Runnable onToggle = null;

    public FrostButton(int x, int y, int width, int height, String label, IPressable onPress) {
        super(x, y, width, height, new StringTextComponent(label), onPress);
    }

    public FrostButton withRightClick(Runnable r) { this.onRightClick = r; return this; }
    public FrostButton asToggle(boolean initial, Runnable r) { this.isToggle = true; this.toggled = initial; this.onToggle = r; return this; }

    public boolean isToggled() { return toggled; }
    public void setToggled(boolean t) { this.toggled = t; }

    @Override
    public void renderButton(MatrixStack ms, int mouseX, int mouseY, float partialTick) {
        long now = System.currentTimeMillis();
        float dt = (now - lastRender) / 1000f;
        lastRender = now;

        boolean isHov = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        this.hovered = isHov;
        animProgress = Math.min(1f, Math.max(0f, animProgress + (isHov ? 1f : -1f) * dt * 6f));

        // Background
        int bg = isToggle && toggled ? ColorUtils.withAlpha(0x1A0A3A, 230) : ColorUtils.withAlpha(0x0D1020, 210);
        int hoverBg = ColorUtils.withAlpha(0x1A1040, 230);
        int finalBg = isHov ? hoverBg : bg;
        RenderUtils.fillRect(ms, x, y, width, height, finalBg);

        // Gradient outline (frost purple→blue)
        float time = now / 1000f;
        int outlineColor = ColorUtils.withAlpha(ColorUtils.frostGradient(time), 200 + (int)(55 * animProgress));
        RenderUtils.drawRoundedOutline(ms, x, y, width, height, outlineColor);

        // Toggle indicator on right side
        if (isToggle) {
            int indicatorColor = toggled ? ColorUtils.withAlpha(0x6A35FF, 255) : ColorUtils.withAlpha(0x333355, 200);
            RenderUtils.fillRect(ms, x + width - 6, y + height / 2f - 4, 4, 8, indicatorColor);
        }

        // Label
        int textColor = isToggle && toggled ? 0xCCBBFF : (isHov ? 0xFFFFFF : 0xCCCCDD);
        Minecraft.getInstance().font.draw(ms, getMessage(),
                x + 6, y + (height - 8) / 2f, textColor);
    }

    /** Returns true if right-clicked */
    public boolean mouseRightClicked(double mx, double my) {
        if (mx >= x && mx <= x + width && my >= y && my <= y + height) {
            if (onRightClick != null) onRightClick.run();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(double mx, double my) {
        if (isToggle) {
            toggled = !toggled;
            if (onToggle != null) onToggle.run();
        } else {
            super.onClick(mx, my);
        }
    }
}
