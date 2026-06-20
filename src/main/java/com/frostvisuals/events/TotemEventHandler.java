package com.frostvisuals.events;

import com.frostvisuals.config.FrostConfig;
import com.frostvisuals.hud.HudRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class TotemEventHandler {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onOverlayRenderPre(RenderGameOverlayEvent.Pre event) {
        // Cancel the totem of undying flash effect overlay
        if (FrostConfig.noTotemOverlay) {
            // The totem overlay is rendered as part of the game overlay via net.minecraft.client.gui.IngameGui
            // We hook into the overlay rendering to skip the totem flash
            if (event.getType() == RenderGameOverlayEvent.ElementType.VIGNETTE) {
                // The totem flash counter is in IngameGui.toolHighlightTimer via reflection if needed
                // Basic approach: we cannot directly detect it here, but we can suppress via mixin/ASM
                // For now this is handled in OverlayMixin
            }
        }
    }
}
