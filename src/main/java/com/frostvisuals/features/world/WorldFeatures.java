package com.frostvisuals.features.world;

import com.frostvisuals.config.FrostConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class WorldFeatures {

    private static final Minecraft MC = Minecraft.getInstance();

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onFogDensity(EntityViewRenderEvent.FogDensity event) {
        if (FrostConfig.customFog) {
            event.setDensity(event.getDensity() * FrostConfig.fogDensity);
            event.setCanceled(true);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onFogColors(EntityViewRenderEvent.FogColors event) {
        if (FrostConfig.customFog) {
            int fog = FrostConfig.fogColor;
            event.setRed(  ((fog >> 16) & 0xFF) / 255f);
            event.setGreen(((fog >>  8) & 0xFF) / 255f);
            event.setBlue( ( fog        & 0xFF) / 255f);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (MC.level == null) return;

        // Time freeze
        if (FrostConfig.timeFreeze) {
            MC.level.setDayTime(FrostConfig.frozenTime);
        }
    }
}
