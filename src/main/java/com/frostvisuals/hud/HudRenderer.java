package com.frostvisuals.hud;

import com.frostvisuals.config.FrostConfig;
import com.frostvisuals.utils.ColorUtils;
import com.frostvisuals.utils.RenderUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.text.SimpleDateFormat;
import java.util.*;

public class HudRenderer {

    private static final Minecraft MC = Minecraft.getInstance();
    private static int lmbCps = 0, rmbCps = 0;
    private static final List<Long> lmbClicks = new ArrayList<>(), rmbClicks = new ArrayList<>();
    private static int kills = 0, deaths = 0, combo = 0;
    private static long lastHitTime = 0;
    private static int totemActivations = 0;

    // Cooldown tracking
    private static long pearlCooldownStart = 0;
    private static final long PEARL_CD_MS = 1000;
    private static long chorusCooldownStart = 0;
    private static final long CHORUS_CD_MS = 5000;

    // Drag state
    private static boolean dragging = false;
    private static String draggingElement = "";
    private static int dragOffX, dragOffY;

    public static void registerLmbClick() {
        long now = System.currentTimeMillis();
        lmbClicks.add(now);
        lmbClicks.removeIf(t -> now - t > 1000);
        lmbCps = lmbClicks.size();
        combo++;
        lastHitTime = now;
    }

    public static void registerRmbClick() {
        long now = System.currentTimeMillis();
        rmbClicks.add(now);
        rmbClicks.removeIf(t -> now - t > 1000);
        rmbCps = rmbClicks.size();
    }

    public static void registerKill() { kills++; combo = 0; }
    public static void registerDeath() { deaths++; combo = 0; }
    public static void registerTotem() { totemActivations++; }
    public static void startPearlCooldown() { pearlCooldownStart = System.currentTimeMillis(); }
    public static void startChorusCooldown() { chorusCooldownStart = System.currentTimeMillis(); }

    public static void render(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        if (MC.player == null) return;

        MatrixStack ms = event.getMatrixStack();
        int sw = MC.getWindow().getGuiScaledWidth();
        int sh = MC.getWindow().getGuiScaledHeight();
        long now = System.currentTimeMillis();

        // Reset CPS every second
        lmbClicks.removeIf(t -> now - t > 1000);
        rmbClicks.removeIf(t -> now - t > 1000);
        lmbCps = lmbClicks.size();
        rmbCps = rmbClicks.size();

        // Reset combo if no hit in 3s
        if (combo > 0 && now - lastHitTime > 3000) combo = 0;

        int frostColor = ColorUtils.frostGradient(now / 1000f);

        // ── Branding Bar ──────────────────────────────────────
        if (FrostConfig.hudBrandingBar) {
            drawBrandingBar(ms, sw, frostColor);
        }

        // ── FPS ───────────────────────────────────────────────
        if (FrostConfig.fpsCounter) {
            drawHudElement(ms, "FPS: " + MC.getFps(), FrostConfig.fpsX, FrostConfig.fpsY, frostColor);
        }

        // ── CPS ───────────────────────────────────────────────
        if (FrostConfig.cpsCounter) {
            drawHudElement(ms, "LMB: " + lmbCps + " | RMB: " + rmbCps, FrostConfig.cpsX, FrostConfig.cpsY, frostColor);
        }

        // ── Ping ──────────────────────────────────────────────
        if (FrostConfig.pingDisplay && MC.getConnection() != null) {
            int ping = MC.getConnection().getPlayerInfo(MC.player.getUUID()) != null
                    ? MC.getConnection().getPlayerInfo(MC.player.getUUID()).getLatency() : 0;
            drawHudElement(ms, "Ping: " + ping + "ms", FrostConfig.fpsX + 80, FrostConfig.fpsY, pingColor(ping));
        }

        // ── Coordinates ───────────────────────────────────────
        if (FrostConfig.coordsDisplay) {
            Vector3d pos = MC.player.position();
            drawHudElement(ms, String.format("XYZ: %.0f / %.0f / %.0f", pos.x, pos.y, pos.z),
                    FrostConfig.coordsX, FrostConfig.coordsY, 0xAABBCC);
        }

        // ── Direction ─────────────────────────────────────────
        if (FrostConfig.directionDisplay) {
            float yaw = MC.player.getViewYRot(1f) % 360;
            if (yaw < 0) yaw += 360;
            String dir = yaw < 22.5 || yaw > 337.5 ? "S" : yaw < 67.5 ? "SW" : yaw < 112.5 ? "W" :
                         yaw < 157.5 ? "NW" : yaw < 202.5 ? "N" : yaw < 247.5 ? "NE" : yaw < 292.5 ? "E" : "SE";
            drawHudElement(ms, "Dir: " + dir, FrostConfig.coordsX + 160, FrostConfig.coordsY, 0x88AABB);
        }

        // ── Speed ─────────────────────────────────────────────
        if (FrostConfig.speedDisplay) {
            Vector3d vel = MC.player.getDeltaMovement();
            double speed = Math.sqrt(vel.x * vel.x + vel.z * vel.z) * 20;
            drawHudElement(ms, String.format("Speed: %.2f", speed), FrostConfig.coordsX, FrostConfig.coordsY + 10, 0x88BBAA);
        }

        // ── Biome ─────────────────────────────────────────────
        if (FrostConfig.biomeDisplay && MC.level != null) {
            String biome = MC.level.getBiome(MC.player.blockPosition()).getRegistryName() != null
                    ? MC.level.getBiome(MC.player.blockPosition()).getRegistryName().getPath() : "?";
            drawHudElement(ms, "Biome: " + biome, FrostConfig.coordsX, FrostConfig.coordsY + 20, 0x88CC88);
        }

        // ── Combo ─────────────────────────────────────────────
        if (FrostConfig.comboCounter && combo > 1) {
            drawHudElement(ms, combo + " \u2605 Combo", FrostConfig.coordsX, FrostConfig.coordsY + 30, 0xFFAA00);
        }

        // ── Kill/Death ────────────────────────────────────────
        if (FrostConfig.killDeathCounter) {
            drawHudElement(ms, "K: " + kills + " | D: " + deaths, 2, sh - 30, 0xCCCCDD);
        }

        // ── Totem Counter ─────────────────────────────────────
        if (FrostConfig.totemCounter && totemActivations > 0) {
            drawHudElement(ms, "\uD83D\uDEE1 Totems: " + totemActivations, sw - 80, 30, 0xFFDD44);
        }

        // ── Armor Display ─────────────────────────────────────
        if (FrostConfig.armorDisplay) {
            drawArmorDisplay(ms, FrostConfig.armorX, FrostConfig.armorY);
        }

        // ── Potion Effects ────────────────────────────────────
        if (FrostConfig.potionEffects) {
            drawPotionEffects(ms, FrostConfig.potionX, FrostConfig.potionY, frostColor);
        }

        // ── Item Cooldowns ────────────────────────────────────
        if (FrostConfig.itemCooldowns) {
            drawCooldowns(ms, FrostConfig.itemCooldownX, FrostConfig.itemCooldownY, now);
        }

        // ── Clock ─────────────────────────────────────────────
        if (FrostConfig.clockDisplay) {
            String clock = new SimpleDateFormat("HH:mm:ss").format(new Date());
            drawHudElement(ms, clock, sw - 50, 2, 0xCCDDEE);
        }

        // ── Entity Counter ────────────────────────────────────
        if (FrostConfig.entityCounter && MC.level != null) {
            int count = MC.level.getEntities(MC.player, MC.player.getBoundingBox().inflate(50)).size();
            drawHudElement(ms, "Nearby: " + count, 2, sh - 40, 0xBBAA88);
        }

        // ── Chunk Display ─────────────────────────────────────
        if (FrostConfig.chunkDisplay) {
            int cx = (int)MC.player.getX() >> 4;
            int cz = (int)MC.player.getZ() >> 4;
            drawHudElement(ms, "Chunk: " + cx + " / " + cz, 2, sh - 50, 0xAABBAA);
        }

        // ── Keystrokes ────────────────────────────────────────
        if (FrostConfig.keystrokesDisplay) {
            drawKeystrokes(ms, sw - 58, sh - 70, frostColor);
        }

        // ── Target HUD ────────────────────────────────────────
        if (FrostConfig.targetHud) {
            drawTargetHud(ms, FrostConfig.targetHudX, FrostConfig.targetHudY, frostColor);
        }

        // ── Snowflakes (World overlay) ─────────────────────────
        if (FrostConfig.fallingSnowflakes) {
            SnowflakeRenderer.render(ms, sw, sh);
        }
    }

    private static void drawBrandingBar(MatrixStack ms, int sw, int frostColor) {
        FontRenderer fr = MC.font;
        String brand = "\u2744 FrostVisuals";
        int bw = fr.width(brand) + 6;
        int fps = MC.getFps();
        String stats = " | FPS: " + fps + " | CPS: " + lmbCps + " | Ping: " + getPing() + "ms";
        int totalW = bw + fr.width(stats) + 10;

        RenderUtils.fillRect(ms, 0, 0, totalW, 12, ColorUtils.withAlpha(0x0A0E1A, 200));
        RenderUtils.fillRect(ms, 0, 11, totalW, 1, ColorUtils.withAlpha(frostColor, 160));

        fr.draw(ms, brand, 3, 2, frostColor);
        fr.draw(ms, stats, bw + 3, 2, 0xAABBCC);
    }

    private static void drawHudElement(MatrixStack ms, String text, int x, int y, int color) {
        FontRenderer fr = MC.font;
        int tw = fr.width(text);
        RenderUtils.fillRect(ms, x - 1, y - 1, tw + 2, 10, ColorUtils.withAlpha(0x000000, 100));
        fr.draw(ms, text, x, y, color);
    }

    private static void drawArmorDisplay(MatrixStack ms, int x, int y) {
        PlayerEntity p = MC.player;
        if (p == null) return;
        ItemStack[] armor = {
            p.getItemBySlot(net.minecraft.inventory.EquipmentSlotType.HEAD),
            p.getItemBySlot(net.minecraft.inventory.EquipmentSlotType.CHEST),
            p.getItemBySlot(net.minecraft.inventory.EquipmentSlotType.LEGS),
            p.getItemBySlot(net.minecraft.inventory.EquipmentSlotType.FEET)
        };
        int iy = y;
        for (ItemStack stack : armor) {
            if (!stack.isEmpty()) {
                int dur = stack.getMaxDamage() - stack.getDamageValue();
                float pct = (float)dur / stack.getMaxDamage();
                int col = pct > 0.5f ? 0x44FF44 : pct > 0.25f ? 0xFFAA00 : 0xFF3333;
                MC.font.draw(ms, stack.getHoverName().getString().substring(0, Math.min(3, stack.getHoverName().getString().length())) + " " + dur, x, iy, col);
                iy += 10;
            }
        }
    }

    private static void drawPotionEffects(MatrixStack ms, int x, int y, int frostColor) {
        PlayerEntity p = MC.player;
        if (p == null) return;
        int iy = y;
        for (EffectInstance effect : p.getActiveEffects()) {
            int dur = effect.getDuration();
            String name = effect.getEffect().getDisplayName().getString();
            String text = name.substring(0, Math.min(6, name.length())) + " " + formatDuration(dur);
            int col = effect.getEffect().isBeneficial() ? 0x44AAFF : 0xFF6644;
            drawHudElement(ms, text, x, iy, col);
            iy += 10;
        }
    }

    private static void drawCooldowns(MatrixStack ms, int x, int y, long now) {
        if (pearlCooldownStart > 0) {
            long elapsed = now - pearlCooldownStart;
            float progress = Math.min(1f, (float)elapsed / PEARL_CD_MS);
            if (progress < 1f) {
                RenderUtils.drawCooldownArc(ms, x + 8, y + 8, 8, progress, ColorUtils.withAlpha(0x6A35FF, 200));
                MC.font.draw(ms, "EP", x + 18, y + 3, 0xAABBCC);
                y += 20;
            } else {
                pearlCooldownStart = 0;
            }
        }
        if (chorusCooldownStart > 0) {
            long elapsed = now - chorusCooldownStart;
            float progress = Math.min(1f, (float)elapsed / CHORUS_CD_MS);
            if (progress < 1f) {
                RenderUtils.drawCooldownArc(ms, x + 8, y + 8, 8, progress, ColorUtils.withAlpha(0xAA35FF, 200));
                MC.font.draw(ms, "CH", x + 18, y + 3, 0xAABBCC);
            } else {
                chorusCooldownStart = 0;
            }
        }
    }

    private static void drawKeystrokes(MatrixStack ms, int x, int y, int frostColor) {
        net.minecraft.client.settings.GameSettings gs = MC.options;
        int[][] layout = {{1, 0}, {0, 1}, {1, 1}, {2, 1}};
        String[] keys = {
            gs.keyUp.getKey().getDisplayName().getString(),
            gs.keyLeft.getKey().getDisplayName().getString(),
            gs.keyDown.getKey().getDisplayName().getString(),
            gs.keyRight.getKey().getDisplayName().getString()
        };
        boolean[] active = {
            gs.keyUp.isDown(),
            gs.keyLeft.isDown(),
            gs.keyDown.isDown(),
            gs.keyRight.isDown()
        };

        int ks = 14;
        for (int i = 0; i < 4; i++) {
            int kx = x + layout[i][0] * (ks + 2);
            int ky = y + layout[i][1] * (ks + 2);
            int bg = active[i] ? ColorUtils.withAlpha(0x6A35FF, 200) : ColorUtils.withAlpha(0x0A0E1A, 160);
            RenderUtils.fillRect(ms, kx, ky, ks, ks, bg);
            RenderUtils.drawRoundedOutline(ms, kx, ky, ks, ks, ColorUtils.withAlpha(frostColor, active[i] ? 255 : 150));
            String label = keys[i].length() > 1 ? keys[i].substring(0, 1) : keys[i];
            MC.font.draw(ms, label, kx + ks / 2f - MC.font.width(label) / 2f, ky + 3, active[i] ? 0xFFFFFF : 0x888899);
        }

        // LMB / RMB
        boolean lmb = net.minecraft.client.Minecraft.getInstance().options.keyAttack.isDown();
        boolean rmb = net.minecraft.client.Minecraft.getInstance().options.keyUse.isDown();
        int lx = x, rx = x + ks + 2;
        int bottomY = y + 2 * (ks + 2) + 2;
        for (boolean[] kb : new boolean[][]{{lmb, true}, {rmb, false}}) {
            int kx = kb[1] ? lx : rx;
            int kbg = kb[0] ? ColorUtils.withAlpha(0x3A8FFF, 200) : ColorUtils.withAlpha(0x0A0E1A, 160);
            RenderUtils.fillRect(ms, kx, bottomY, ks, ks, kbg);
            RenderUtils.drawRoundedOutline(ms, kx, bottomY, ks, ks, ColorUtils.withAlpha(frostColor, kb[0] ? 255 : 150));
            String label = kb[1] ? "L" : "R";
            MC.font.draw(ms, label, kx + ks / 2f - MC.font.width(label) / 2f, bottomY + 3, kb[0] ? 0xFFFFFF : 0x888899);
        }
    }

    private static void drawTargetHud(MatrixStack ms, int x, int y, int frostColor) {
        if (MC.crosshairPickEntity instanceof LivingEntity) {
            LivingEntity target = (LivingEntity) MC.crosshairPickEntity;
            String name = target.getDisplayName().getString();
            float hp = target.getHealth();
            float maxHp = target.getMaxHealth();
            float hpPct = hp / maxHp;

            int pw = 140, ph = 36;
            RenderUtils.fillRect(ms, x, y, pw, ph, ColorUtils.withAlpha(0x0A0E1A, 220));
            RenderUtils.drawRoundedOutline(ms, x, y, pw, ph, ColorUtils.withAlpha(frostColor, 200));

            // Name
            MC.font.draw(ms, name, x + 6, y + 4, 0xFFFFFF);

            // HP bar
            int barY = y + 16;
            RenderUtils.fillRect(ms, x + 6, barY, pw - 12, 6, ColorUtils.withAlpha(0x333333, 200));
            int hpColor = hpPct > 0.6f ? 0x44FF44 : hpPct > 0.3f ? 0xFFAA00 : 0xFF3333;
            RenderUtils.fillRect(ms, x + 6, barY, (int)((pw - 12) * hpPct), 6, ColorUtils.withAlpha(hpColor, 230));
            RenderUtils.drawRoundedOutline(ms, x + 6, barY, pw - 12, 6, ColorUtils.withAlpha(frostColor, 150));

            // HP text
            MC.font.draw(ms, String.format("HP: %.1f / %.1f", hp, maxHp), x + 6, barY + 9, 0xAABBCC);
        }
    }

    private static String formatDuration(int ticks) {
        int secs = ticks / 20;
        return (secs / 60) + ":" + String.format("%02d", secs % 60);
    }

    private static int pingColor(int ping) {
        if (ping < 80) return 0x44FF44;
        if (ping < 150) return 0xFFAA00;
        return 0xFF3333;
    }

    private static int getPing() {
        if (MC.getConnection() == null || MC.player == null) return 0;
        var info = MC.getConnection().getPlayerInfo(MC.player.getUUID());
        return info != null ? info.getLatency() : 0;
    }
}
