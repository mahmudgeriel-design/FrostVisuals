package com.frostvisuals.hud;

import com.frostvisuals.config.FrostConfig;
import com.frostvisuals.utils.ColorUtils;
import com.frostvisuals.utils.RenderUtils;
import com.mojang.blaze3d.matrix.MatrixStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnowflakeRenderer {

    private static final List<float[]> flakes = new ArrayList<>();
    private static long lastUpdate = 0;
    private static final Random RNG = new Random();

    private static void ensureFlakes(int count) {
        while (flakes.size() < count) {
            flakes.add(new float[]{
                RNG.nextFloat() * 1000,
                RNG.nextFloat() * 600,
                RNG.nextFloat() * 1.5f + 0.5f,
                RNG.nextFloat() * 0.5f + 0.5f,
                RNG.nextFloat() * 2f * (float)Math.PI
            });
        }
        while (flakes.size() > count) flakes.remove(flakes.size() - 1);
    }

    public static void render(MatrixStack ms, int sw, int sh) {
        if (!FrostConfig.fallingSnowflakes) return;

        int count = FrostConfig.snowflakeCount;
        float speed = FrostConfig.snowflakeSpeed;
        float size = FrostConfig.snowflakeSize;

        ensureFlakes(count);

        long now = System.currentTimeMillis();
        float dt = Math.min((now - lastUpdate) / 1000f, 0.05f);
        lastUpdate = now;

        for (float[] sf : flakes) {
            // Update position
            sf[1] += speed * sf[2] * dt * 30f;
            sf[4] += dt * 0.3f;
            sf[0] += (float)Math.sin(sf[4]) * dt * 15f * speed;

            // Wrap
            if (sf[1] > sh + 5) { sf[1] = -5; sf[0] = RNG.nextFloat() * sw; }
            if (sf[0] < 0) sf[0] = sw;
            if (sf[0] > sw) sf[0] = 0;

            float s = sf[2] * size;
            int alpha = (int)(120 * sf[3]);
            int col = ColorUtils.withAlpha(0xCCDDFF, alpha);
            RenderUtils.fillRect(ms, sf[0], sf[1], s, s, col);
        }
    }
}
