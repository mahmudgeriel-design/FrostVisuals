package com.frostvisuals;

import com.frostvisuals.config.FrostConfig;
import com.frostvisuals.events.ClientEventHandler;
import com.frostvisuals.keys.KeyBindings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(FrostVisuals.MOD_ID)
public class FrostVisuals {

    public static final String MOD_ID = "frostvisuals";
    public static final String MOD_NAME = "FrostVisuals";
    public static final String VERSION = "1.0.0";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public FrostVisuals() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::clientSetup);
        FrostConfig.init();
        LOGGER.info("❄ FrostVisuals {} loading...", VERSION);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        KeyBindings.register();
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        LOGGER.info("❄ FrostVisuals {} loaded successfully!", VERSION);
    }
}
