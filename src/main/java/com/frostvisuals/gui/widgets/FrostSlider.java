package com.frostvisuals.gui.widgets;

import com.frostvisuals.utils.ColorUtils;
import com.frostvisuals.utils.RenderUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.AbstractSlider;
import net.minecraft.util.text.StringTextComponent;

import java.util.function.Consumer;

public class FrostSlider extends AbstractSlider {

    private final String label;
    private final float min, max;
    private final Consumer<Float> onChange;

    public FrostSlider(int x, int y, int w, int h, String label, float min, float max, float current, Consumer<Float> onChange) {
        super(x, y, w, h, new StringTextComponent(""), (current - min) / (max - min));
        this.label = label;
        this.min = min;
        this.max = max;
        this.onChange = onChange;
        updateMessage();
    }

    public float getValue() {
        return min + (float) value * (max - min);
    }

    @Override
    protected void updateMessage() {
        float v = min + (float) value * (max - min);
        setMessage(new StringTextComponent(String.format("%.2f", v)));
    }

    @Override
    protected void applyValue() {
        if (onChange != null) onChange.accept(getValue());
    }

    @Override
    public void renderButton(MatrixStack ms, int mouseX, int mouseY, float partial) {
        long time = System.currentTimeMillis();
        int outlineColor = ColorUtils.withAlpha(ColorUtils.frostGradient(time / 1000f), 200);

        // Track
        RenderUtils.fillRect(ms, x, y + height / 2f - 2, width, 4, ColorUtils.withAlpha(0x0D1020, 220));
        RenderUtils.drawRoundedOutline(ms, x, y + height / 2f - 2, width, 4, outlineColor);

        // Fill
        float fillW = (float)(value * (width - 10));
        RenderUtils.fillRect(ms, x + 1, y + height / 2f - 1, fillW, 2, ColorUtils.withAlpha(0x6A35FF, 200));

        // Thumb
        float thumbX = x + (float)(value * (width - 10));
        RenderUtils.fillRect(ms, thumbX, y + 2, 10, height - 4, ColorUtils.withAlpha(0x3A8FFF, 230));
        RenderUtils.drawRoundedOutline(ms, thumbX, y + 2, 10, height - 4, outlineColor);

        // Label + value
        Minecraft mc = Minecraft.getInstance();
        mc.font.draw(ms, label + ": " + getMessage().getString(), x, y - 10, 0xAABBCC);
    }
}
