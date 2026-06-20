package com.frostvisuals.events;

import com.frostvisuals.FrostVisuals;
import com.frostvisuals.config.FrostConfig;
import com.frostvisuals.gui.FrostMainScreen;
import com.frostvisuals.hud.HudRenderer;
import com.frostvisuals.keys.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEventHandler {

    private static final Minecraft MC = Minecraft.getInstance();

    // Toggle state
    private static boolean toggleSneaking = false;
    private static boolean toggleSprinting = false;
    private static int lastHotbarSlot = -1;
    private static boolean zoomActive = false;
    private static float prevFov = 70f;

    // ── Key handling (client tick) ────────────────────────
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (MC.player == null || MC.level == null) return;

        ClientPlayerEntity player = MC.player;

        // Open menu
        if (KeyBindings.openMenu.consumeClick()) {
            MC.setScreen(new FrostMainScreen());
        }

        // Toggle Sneak
        if (FrostConfig.toggleSneak && KeyBindings.toggleSneakBind.consumeClick()) {
            toggleSneaking = !toggleSneaking;
        }
        if (FrostConfig.toggleSneak && toggleSneaking) {
            MC.options.keyShift.setDown(true);
        }

        // Toggle Sprint
        if (FrostConfig.toggleSprint && KeyBindings.toggleSprintBind.consumeClick()) {
            toggleSprinting = !toggleSprinting;
        }
        if (FrostConfig.toggleSprint && toggleSprinting) {
            MC.options.keySprint.setDown(true);
        }

        // Anti-Blind
        if (FrostConfig.antiBlind && player.hasEffect(Effects.BLINDNESS)) {
            player.removeEffect(Effects.BLINDNESS);
        }

        // ── Item Binds (hotbar only) ──────────────────────────
        if (FrostConfig.pearlBind && KeyBindings.pearlBind.consumeClick()) {
            activateHotbarItem(Items.ENDER_PEARL, true);
        }
        if (FrostConfig.chorusBind && KeyBindings.chorusBind.consumeClick()) {
            activateHotbarItem(Items.CHORUS_FRUIT, true);
            HudRenderer.startChorusCooldown();
        }
        if (FrostConfig.rodBind && KeyBindings.rodBind.consumeClick()) {
            activateHotbarItem(Items.FISHING_ROD, true);
        }
        if (FrostConfig.gapBind && KeyBindings.gapBind.consumeClick()) {
            activateHotbarItem(Items.GOLDEN_APPLE, true);
        }
        if (FrostConfig.swordSwapBind && KeyBindings.swordSwapBind.consumeClick()) {
            activateSwordInHotbar();
        }
        if (FrostConfig.shieldBind && KeyBindings.shieldBind.consumeClick()) {
            // Shield is typically in offhand — simulate use key
            if (player.getOffhandItem().getItem() == Items.SHIELD) {
                MC.options.keyUse.setDown(true);
            }
        }
    }

    /**
     * Selects the slot containing the given item in the hotbar and uses it.
     * Does NOTHING if item is not in hotbar (slots 0-8).
     */
    private void activateHotbarItem(Item target, boolean useItem) {
        ClientPlayerEntity player = MC.player;
        if (player == null) return;
        for (int i = 0; i < 9; i++) {
            if (player.inventory.getItem(i).getItem() == target) {
                lastHotbarSlot = player.inventory.selected;
                player.inventory.selected = i;
                if (useItem) {
                    MC.options.keyUse.setDown(true);
                }
                return;
            }
        }
        // Item not in hotbar — do nothing
    }

    private void activateSwordInHotbar() {
        ClientPlayerEntity player = MC.player;
        if (player == null) return;
        for (int i = 0; i < 9; i++) {
            if (player.inventory.getItem(i).getItem() instanceof SwordItem) {
                player.inventory.selected = i;
                return;
            }
        }
    }

    // ── HUD Rendering ─────────────────────────────────────
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        HudRenderer.render(event);
    }

    // ── Overlay Cancelling ────────────────────────────────
    @SubscribeEvent
    public void onRenderOverlayPre(RenderGameOverlayEvent.Pre event) {
        RenderGameOverlayEvent.ElementType type = event.getType();

        if (FrostConfig.noFireOverlay && type == RenderGameOverlayEvent.ElementType.HELMET) {
            // Check if it's fire specifically — cancel fire overlay
            if (MC.player != null && MC.player.isOnFire()) event.setCanceled(true);
        }
        if (FrostConfig.noPumpkinOverlay && type == RenderGameOverlayEvent.ElementType.HELMET) {
            if (MC.player != null && MC.player.getItemBySlot(EquipmentSlotType.HEAD).getItem() == Items.CARVED_PUMPKIN)
                event.setCanceled(true);
        }
    }

    // ── FOV for Zoom & Scope ───────────────────────────────
    @SubscribeEvent
    public void onFov(EntityViewRenderEvent.FOVModifier event) {
        if (MC.player == null) return;

        // Zoom
        if (FrostConfig.freelook || KeyBindings.zoomBind.isDown()) {
            event.setFOV(event.getFOV() / 4.0);
            zoomActive = true;
        } else {
            zoomActive = false;
        }

        // Scope effect when holding bow and drawing
        if (FrostConfig.scopeEffect) {
            ItemStack held = MC.player.getMainHandItem();
            if (held.getItem() instanceof BowItem && MC.player.getUseItemRemainingTicks() > 0) {
                int drawTicks = 72000 - MC.player.getUseItemRemainingTicks();
                if (drawTicks > 10) {
                    double scopeProgress = Math.min(1.0, (drawTicks - 10) / 20.0);
                    event.setFOV(event.getFOV() * (1.0 - 0.6 * scopeProgress));
                }
            }
        }
    }

    // ── Attack events (CPS, Combo) ────────────────────────
    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (event.getPlayer() == MC.player) {
            HudRenderer.registerLmbClick();
        }
    }

    // ── Living Hurt (hit sound, hit color) ───────────────
    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        if (MC.player == null) return;
        if (event.getSource().getEntity() == MC.player) {
            // Hit sound
            if (FrostConfig.hitSound) {
                playHitSound();
            }
        }
    }

    // ── Living Death (kill sound, counter) ───────────────
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (MC.player == null) return;
        if (event.getSource().getEntity() == MC.player) {
            if (FrostConfig.killSound) {
                MC.player.level.playLocalSound(
                    MC.player.getX(), MC.player.getY(), MC.player.getZ(),
                    net.minecraft.util.SoundEvents.EXPERIENCE_ORB_PICKUP, net.minecraft.util.SoundCategory.PLAYERS,
                    FrostConfig.hitSoundVolume, FrostConfig.hitSoundPitch + 0.5f, false
                );
            }
            HudRenderer.registerKill();
        }
        if (event.getEntityLiving() == MC.player) {
            HudRenderer.registerDeath();
        }
    }

    // ── Fullbright ─────────────────────────────────────────
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (FrostConfig.fullbright && MC.options.gamma != 100.0) {
            MC.options.gamma = 100.0;
        }
    }

    private void playHitSound() {
        if (MC.player == null) return;
        net.minecraft.util.SoundEvent sound;
        switch (FrostConfig.hitSoundPreset) {
            case "SOFT":    sound = net.minecraft.util.SoundEvents.ARROW_HIT_PLAYER; break;
            case "THUD":    sound = net.minecraft.util.SoundEvents.PLAYER_ATTACK_SWEEP; break;
            case "PING":    sound = net.minecraft.util.SoundEvents.NOTE_BLOCK_BELL; break;
            case "ORB":     sound = net.minecraft.util.SoundEvents.EXPERIENCE_ORB_PICKUP; break;
            default:        sound = net.minecraft.util.SoundEvents.PLAYER_ATTACK_STRONG; break;
        }
        MC.player.level.playLocalSound(
            MC.player.getX(), MC.player.getY(), MC.player.getZ(),
            sound, net.minecraft.util.SoundCategory.PLAYERS,
            FrostConfig.hitSoundVolume, FrostConfig.hitSoundPitch, false
        );
    }
}
