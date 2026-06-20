package com.frostvisuals.config;

import com.frostvisuals.FrostVisuals;
import com.google.gson.*;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.nio.file.*;

public class FrostConfig {

    private static JsonObject config = new JsonObject();
    private static Path configPath;

    // ── VISUALS ──────────────────────────────────────────
    public static boolean noTotemOverlay = true;
    public static boolean noLavaOverlay = false;
    public static boolean noFireOverlay = false;
    public static boolean noWaterOverlay = false;
    public static boolean noPumpkinOverlay = true;
    public static boolean noSnowOverlay = false;
    public static boolean noDarknessEffect = false;
    public static boolean noNauseaEffect = false;
    public static boolean noHurtCamera = false;
    public static boolean damageTilt = true;
    public static boolean fullbright = false;
    public static boolean oldAnimations = false;
    public static float swingSpeed = 1.0f;
    public static int hitColor = 0xFF0000;
    public static boolean itemPhysics = true;
    public static boolean freelook = false;
    public static int enchantGlintColor = 0x6400FF;
    public static float enchantGlintSpeed = 1.0f;
    public static int blockOutlineColor = 0x6A35FF;
    public static float blockOutlineWidth = 1.5f;
    public static boolean entityGlow = false;
    public static int entityGlowColorHostile = 0xFF0000;
    public static int entityGlowColorPassive = 0x00FF44;
    public static boolean scopeEffect = true;
    public static float armSwayAmount = 1.0f;
    public static String toolSwapAnimation = "SLIDE";

    // ── HUD ──────────────────────────────────────────────
    public static boolean hudBrandingBar = true;
    public static boolean fpsCounter = true;
    public static boolean cpsCounter = true;
    public static boolean pingDisplay = true;
    public static boolean coordsDisplay = true;
    public static boolean directionDisplay = true;
    public static boolean biomeDisplay = false;
    public static boolean speedDisplay = false;
    public static boolean armorDisplay = true;
    public static boolean armorColumn = false;
    public static boolean potionEffects = true;
    public static boolean buffsDisplay = true;
    public static boolean itemCooldowns = true;
    public static boolean totemCounter = false;
    public static boolean transparentInventory = false;
    public static float inventoryOpacity = 0.6f;
    public static boolean targetHud = true;
    public static boolean comboCounter = false;
    public static boolean killDeathCounter = false;
    public static boolean keystrokesDisplay = true;
    public static boolean clockDisplay = false;
    public static boolean entityCounter = false;
    public static boolean chunkDisplay = false;
    public static boolean scoreboardCustom = false;

    // HUD positions (x, y)
    public static int hudBarX = 2, hudBarY = 2;
    public static int fpsX = 2, fpsY = 14;
    public static int cpsX = 2, cpsY = 24;
    public static int coordsX = 2, coordsY = 34;
    public static int armorX = 2, armorY = 44;
    public static int potionX = 2, potionY = 60;
    public static int keystrokesX = 4, keystrokesY = 4;
    public static int targetHudX = 4, targetHudY = 80;
    public static int itemCooldownX = 2, itemCooldownY = 120;

    // ── WORLD ─────────────────────────────────────────────
    public static boolean fallingSnowflakes = false;
    public static int snowflakeCount = 50;
    public static float snowflakeSpeed = 1.0f;
    public static float snowflakeSize = 1.0f;
    public static boolean fallingLeaves = false;
    public static boolean customSkyColor = false;
    public static int skyColorDay = 0x87CEEB;
    public static int skyColorNight = 0x0A0E1A;
    public static boolean customFog = false;
    public static int fogColor = 0xC0D8FF;
    public static float fogDensity = 1.0f;
    public static boolean timeFreeze = false;
    public static int frozenTime = 6000;
    public static float sunSize = 1.0f;
    public static float moonSize = 1.0f;
    public static float starsVisibility = 1.0f;
    public static boolean voidFog = true;
    public static boolean netherFog = true;
    public static boolean dynamicLighting = false;
    public static float lavaTransparency = 1.0f;
    public static boolean weatherControl = false;
    public static boolean rainEnabled = true;
    public static float particleDensity = 1.0f;
    public static boolean customGrassColor = false;
    public static int grassColor = 0x79C05A;
    public static boolean customWaterColor = false;
    public static int waterColor = 0x3F76E4;
    public static boolean entityGlowWorld = false;

    // ── SOUNDS ───────────────────────────────────────────
    public static boolean hitSound = false;
    public static float hitSoundPitch = 1.0f;
    public static float hitSoundVolume = 1.0f;
    public static String hitSoundPreset = "CLASSIC";
    public static boolean bowHitSound = false;
    public static boolean killSound = false;
    public static boolean critSound = false;
    public static boolean headShotSound = false;
    public static boolean comboSound = false;
    public static boolean totemSound = true;
    public static boolean buffSound = false;
    public static boolean cooldownReadySound = true;
    public static boolean lowHealthSound = false;
    public static float lowHealthThreshold = 6.0f;
    public static boolean itemSwapSound = false;

    // ── BINDS ────────────────────────────────────────────
    public static boolean pearlBind = false;
    public static boolean chorusBind = false;
    public static boolean rodBind = false;
    public static boolean shieldBind = false;
    public static boolean gapBind = false;
    public static boolean swordSwapBind = false;

    // ── UTILITIES ─────────────────────────────────────────
    public static boolean toggleSneak = false;
    public static boolean toggleSprint = false;
    public static boolean antiBlind = false;
    public static boolean noInvisible = false;
    public static boolean chunkBorders = false;
    public static boolean hitboxes = false;

    // ── SETTINGS ──────────────────────────────────────────
    public static String language = "en";
    public static boolean discordPresence = true;

    // Sword editor values
    public static float swordX = 0f, swordY = 0f, swordZ = 0f;
    public static float swordRotX = 0f, swordRotY = 0f, swordRotZ = 0f;
    public static float swordScale = 1.0f;

    public static void init() {
        configPath = Paths.get("config", "frostvisuals.json");
        load();
    }

    public static void load() {
        try {
            if (Files.exists(configPath)) {
                String json = new String(Files.readAllBytes(configPath));
                config = new JsonParser().parse(json).getAsJsonObject();
                readValues();
            } else {
                save();
            }
        } catch (Exception e) {
            FrostVisuals.LOGGER.error("Failed to load FrostVisuals config: {}", e.getMessage());
        }
    }

    private static void readValues() {
        noTotemOverlay = getBool("noTotemOverlay", noTotemOverlay);
        noLavaOverlay = getBool("noLavaOverlay", noLavaOverlay);
        noFireOverlay = getBool("noFireOverlay", noFireOverlay);
        noWaterOverlay = getBool("noWaterOverlay", noWaterOverlay);
        noPumpkinOverlay = getBool("noPumpkinOverlay", noPumpkinOverlay);
        noSnowOverlay = getBool("noSnowOverlay", noSnowOverlay);
        noDarknessEffect = getBool("noDarknessEffect", noDarknessEffect);
        noNauseaEffect = getBool("noNauseaEffect", noNauseaEffect);
        noHurtCamera = getBool("noHurtCamera", noHurtCamera);
        damageTilt = getBool("damageTilt", damageTilt);
        fullbright = getBool("fullbright", fullbright);
        oldAnimations = getBool("oldAnimations", oldAnimations);
        swingSpeed = getFloat("swingSpeed", swingSpeed);
        hitColor = getInt("hitColor", hitColor);
        itemPhysics = getBool("itemPhysics", itemPhysics);
        freelook = getBool("freelook", freelook);
        enchantGlintColor = getInt("enchantGlintColor", enchantGlintColor);
        enchantGlintSpeed = getFloat("enchantGlintSpeed", enchantGlintSpeed);
        blockOutlineColor = getInt("blockOutlineColor", blockOutlineColor);
        blockOutlineWidth = getFloat("blockOutlineWidth", blockOutlineWidth);
        entityGlow = getBool("entityGlow", entityGlow);
        entityGlowColorHostile = getInt("entityGlowColorHostile", entityGlowColorHostile);
        entityGlowColorPassive = getInt("entityGlowColorPassive", entityGlowColorPassive);
        scopeEffect = getBool("scopeEffect", scopeEffect);
        armSwayAmount = getFloat("armSwayAmount", armSwayAmount);
        toolSwapAnimation = getString("toolSwapAnimation", toolSwapAnimation);

        hudBrandingBar = getBool("hudBrandingBar", hudBrandingBar);
        fpsCounter = getBool("fpsCounter", fpsCounter);
        cpsCounter = getBool("cpsCounter", cpsCounter);
        pingDisplay = getBool("pingDisplay", pingDisplay);
        coordsDisplay = getBool("coordsDisplay", coordsDisplay);
        directionDisplay = getBool("directionDisplay", directionDisplay);
        biomeDisplay = getBool("biomeDisplay", biomeDisplay);
        speedDisplay = getBool("speedDisplay", speedDisplay);
        armorDisplay = getBool("armorDisplay", armorDisplay);
        armorColumn = getBool("armorColumn", armorColumn);
        potionEffects = getBool("potionEffects", potionEffects);
        buffsDisplay = getBool("buffsDisplay", buffsDisplay);
        itemCooldowns = getBool("itemCooldowns", itemCooldowns);
        totemCounter = getBool("totemCounter", totemCounter);
        transparentInventory = getBool("transparentInventory", transparentInventory);
        inventoryOpacity = getFloat("inventoryOpacity", inventoryOpacity);
        targetHud = getBool("targetHud", targetHud);
        comboCounter = getBool("comboCounter", comboCounter);
        killDeathCounter = getBool("killDeathCounter", killDeathCounter);
        keystrokesDisplay = getBool("keystrokesDisplay", keystrokesDisplay);
        clockDisplay = getBool("clockDisplay", clockDisplay);
        entityCounter = getBool("entityCounter", entityCounter);
        chunkDisplay = getBool("chunkDisplay", chunkDisplay);
        scoreboardCustom = getBool("scoreboardCustom", scoreboardCustom);

        hudBarX = getInt("hudBarX", hudBarX); hudBarY = getInt("hudBarY", hudBarY);
        fpsX = getInt("fpsX", fpsX); fpsY = getInt("fpsY", fpsY);
        cpsX = getInt("cpsX", cpsX); cpsY = getInt("cpsY", cpsY);
        coordsX = getInt("coordsX", coordsX); coordsY = getInt("coordsY", coordsY);
        armorX = getInt("armorX", armorX); armorY = getInt("armorY", armorY);
        potionX = getInt("potionX", potionX); potionY = getInt("potionY", potionY);
        keystrokesX = getInt("keystrokesX", keystrokesX); keystrokesY = getInt("keystrokesY", keystrokesY);
        targetHudX = getInt("targetHudX", targetHudX); targetHudY = getInt("targetHudY", targetHudY);
        itemCooldownX = getInt("itemCooldownX", itemCooldownX); itemCooldownY = getInt("itemCooldownY", itemCooldownY);

        fallingSnowflakes = getBool("fallingSnowflakes", fallingSnowflakes);
        snowflakeCount = getInt("snowflakeCount", snowflakeCount);
        snowflakeSpeed = getFloat("snowflakeSpeed", snowflakeSpeed);
        snowflakeSize = getFloat("snowflakeSize", snowflakeSize);
        fallingLeaves = getBool("fallingLeaves", fallingLeaves);
        customSkyColor = getBool("customSkyColor", customSkyColor);
        skyColorDay = getInt("skyColorDay", skyColorDay);
        skyColorNight = getInt("skyColorNight", skyColorNight);
        customFog = getBool("customFog", customFog);
        fogColor = getInt("fogColor", fogColor);
        fogDensity = getFloat("fogDensity", fogDensity);
        timeFreeze = getBool("timeFreeze", timeFreeze);
        frozenTime = getInt("frozenTime", frozenTime);
        sunSize = getFloat("sunSize", sunSize);
        moonSize = getFloat("moonSize", moonSize);
        starsVisibility = getFloat("starsVisibility", starsVisibility);
        voidFog = getBool("voidFog", voidFog);
        netherFog = getBool("netherFog", netherFog);
        dynamicLighting = getBool("dynamicLighting", dynamicLighting);
        lavaTransparency = getFloat("lavaTransparency", lavaTransparency);
        weatherControl = getBool("weatherControl", weatherControl);
        rainEnabled = getBool("rainEnabled", rainEnabled);
        particleDensity = getFloat("particleDensity", particleDensity);
        customGrassColor = getBool("customGrassColor", customGrassColor);
        grassColor = getInt("grassColor", grassColor);
        customWaterColor = getBool("customWaterColor", customWaterColor);
        waterColor = getInt("waterColor", waterColor);

        hitSound = getBool("hitSound", hitSound);
        hitSoundPitch = getFloat("hitSoundPitch", hitSoundPitch);
        hitSoundVolume = getFloat("hitSoundVolume", hitSoundVolume);
        hitSoundPreset = getString("hitSoundPreset", hitSoundPreset);
        bowHitSound = getBool("bowHitSound", bowHitSound);
        killSound = getBool("killSound", killSound);
        critSound = getBool("critSound", critSound);
        headShotSound = getBool("headShotSound", headShotSound);
        comboSound = getBool("comboSound", comboSound);
        totemSound = getBool("totemSound", totemSound);
        buffSound = getBool("buffSound", buffSound);
        cooldownReadySound = getBool("cooldownReadySound", cooldownReadySound);
        lowHealthSound = getBool("lowHealthSound", lowHealthSound);
        lowHealthThreshold = getFloat("lowHealthThreshold", lowHealthThreshold);
        itemSwapSound = getBool("itemSwapSound", itemSwapSound);

        pearlBind = getBool("pearlBind", pearlBind);
        chorusBind = getBool("chorusBind", chorusBind);
        rodBind = getBool("rodBind", rodBind);
        shieldBind = getBool("shieldBind", shieldBind);
        gapBind = getBool("gapBind", gapBind);
        swordSwapBind = getBool("swordSwapBind", swordSwapBind);

        toggleSneak = getBool("toggleSneak", toggleSneak);
        toggleSprint = getBool("toggleSprint", toggleSprint);
        antiBlind = getBool("antiBlind", antiBlind);
        noInvisible = getBool("noInvisible", noInvisible);
        chunkBorders = getBool("chunkBorders", chunkBorders);
        hitboxes = getBool("hitboxes", hitboxes);

        language = getString("language", language);
        discordPresence = getBool("discordPresence", discordPresence);

        swordX = getFloat("swordX", swordX); swordY = getFloat("swordY", swordY); swordZ = getFloat("swordZ", swordZ);
        swordRotX = getFloat("swordRotX", swordRotX); swordRotY = getFloat("swordRotY", swordRotY); swordRotZ = getFloat("swordRotZ", swordRotZ);
        swordScale = getFloat("swordScale", swordScale);
    }

    public static void save() {
        try {
            Files.createDirectories(configPath.getParent());
            JsonObject obj = new JsonObject();

            obj.addProperty("noTotemOverlay", noTotemOverlay);
            obj.addProperty("noLavaOverlay", noLavaOverlay);
            obj.addProperty("noFireOverlay", noFireOverlay);
            obj.addProperty("noWaterOverlay", noWaterOverlay);
            obj.addProperty("noPumpkinOverlay", noPumpkinOverlay);
            obj.addProperty("noSnowOverlay", noSnowOverlay);
            obj.addProperty("noDarknessEffect", noDarknessEffect);
            obj.addProperty("noNauseaEffect", noNauseaEffect);
            obj.addProperty("noHurtCamera", noHurtCamera);
            obj.addProperty("damageTilt", damageTilt);
            obj.addProperty("fullbright", fullbright);
            obj.addProperty("oldAnimations", oldAnimations);
            obj.addProperty("swingSpeed", swingSpeed);
            obj.addProperty("hitColor", hitColor);
            obj.addProperty("itemPhysics", itemPhysics);
            obj.addProperty("freelook", freelook);
            obj.addProperty("enchantGlintColor", enchantGlintColor);
            obj.addProperty("enchantGlintSpeed", enchantGlintSpeed);
            obj.addProperty("blockOutlineColor", blockOutlineColor);
            obj.addProperty("blockOutlineWidth", blockOutlineWidth);
            obj.addProperty("entityGlow", entityGlow);
            obj.addProperty("entityGlowColorHostile", entityGlowColorHostile);
            obj.addProperty("entityGlowColorPassive", entityGlowColorPassive);
            obj.addProperty("scopeEffect", scopeEffect);
            obj.addProperty("armSwayAmount", armSwayAmount);
            obj.addProperty("toolSwapAnimation", toolSwapAnimation);

            obj.addProperty("hudBrandingBar", hudBrandingBar);
            obj.addProperty("fpsCounter", fpsCounter);
            obj.addProperty("cpsCounter", cpsCounter);
            obj.addProperty("pingDisplay", pingDisplay);
            obj.addProperty("coordsDisplay", coordsDisplay);
            obj.addProperty("directionDisplay", directionDisplay);
            obj.addProperty("biomeDisplay", biomeDisplay);
            obj.addProperty("speedDisplay", speedDisplay);
            obj.addProperty("armorDisplay", armorDisplay);
            obj.addProperty("armorColumn", armorColumn);
            obj.addProperty("potionEffects", potionEffects);
            obj.addProperty("buffsDisplay", buffsDisplay);
            obj.addProperty("itemCooldowns", itemCooldowns);
            obj.addProperty("totemCounter", totemCounter);
            obj.addProperty("transparentInventory", transparentInventory);
            obj.addProperty("inventoryOpacity", inventoryOpacity);
            obj.addProperty("targetHud", targetHud);
            obj.addProperty("comboCounter", comboCounter);
            obj.addProperty("killDeathCounter", killDeathCounter);
            obj.addProperty("keystrokesDisplay", keystrokesDisplay);
            obj.addProperty("clockDisplay", clockDisplay);
            obj.addProperty("entityCounter", entityCounter);
            obj.addProperty("chunkDisplay", chunkDisplay);
            obj.addProperty("scoreboardCustom", scoreboardCustom);

            obj.addProperty("hudBarX", hudBarX); obj.addProperty("hudBarY", hudBarY);
            obj.addProperty("fpsX", fpsX); obj.addProperty("fpsY", fpsY);
            obj.addProperty("cpsX", cpsX); obj.addProperty("cpsY", cpsY);
            obj.addProperty("coordsX", coordsX); obj.addProperty("coordsY", coordsY);
            obj.addProperty("armorX", armorX); obj.addProperty("armorY", armorY);
            obj.addProperty("potionX", potionX); obj.addProperty("potionY", potionY);
            obj.addProperty("keystrokesX", keystrokesX); obj.addProperty("keystrokesY", keystrokesY);
            obj.addProperty("targetHudX", targetHudX); obj.addProperty("targetHudY", targetHudY);
            obj.addProperty("itemCooldownX", itemCooldownX); obj.addProperty("itemCooldownY", itemCooldownY);

            obj.addProperty("fallingSnowflakes", fallingSnowflakes);
            obj.addProperty("snowflakeCount", snowflakeCount);
            obj.addProperty("snowflakeSpeed", snowflakeSpeed);
            obj.addProperty("snowflakeSize", snowflakeSize);
            obj.addProperty("fallingLeaves", fallingLeaves);
            obj.addProperty("customSkyColor", customSkyColor);
            obj.addProperty("skyColorDay", skyColorDay);
            obj.addProperty("skyColorNight", skyColorNight);
            obj.addProperty("customFog", customFog);
            obj.addProperty("fogColor", fogColor);
            obj.addProperty("fogDensity", fogDensity);
            obj.addProperty("timeFreeze", timeFreeze);
            obj.addProperty("frozenTime", frozenTime);
            obj.addProperty("sunSize", sunSize);
            obj.addProperty("moonSize", moonSize);
            obj.addProperty("starsVisibility", starsVisibility);
            obj.addProperty("voidFog", voidFog);
            obj.addProperty("netherFog", netherFog);
            obj.addProperty("dynamicLighting", dynamicLighting);
            obj.addProperty("lavaTransparency", lavaTransparency);
            obj.addProperty("weatherControl", weatherControl);
            obj.addProperty("rainEnabled", rainEnabled);
            obj.addProperty("particleDensity", particleDensity);
            obj.addProperty("customGrassColor", customGrassColor);
            obj.addProperty("grassColor", grassColor);
            obj.addProperty("customWaterColor", customWaterColor);
            obj.addProperty("waterColor", waterColor);

            obj.addProperty("hitSound", hitSound);
            obj.addProperty("hitSoundPitch", hitSoundPitch);
            obj.addProperty("hitSoundVolume", hitSoundVolume);
            obj.addProperty("hitSoundPreset", hitSoundPreset);
            obj.addProperty("bowHitSound", bowHitSound);
            obj.addProperty("killSound", killSound);
            obj.addProperty("critSound", critSound);
            obj.addProperty("headShotSound", headShotSound);
            obj.addProperty("comboSound", comboSound);
            obj.addProperty("totemSound", totemSound);
            obj.addProperty("buffSound", buffSound);
            obj.addProperty("cooldownReadySound", cooldownReadySound);
            obj.addProperty("lowHealthSound", lowHealthSound);
            obj.addProperty("lowHealthThreshold", lowHealthThreshold);
            obj.addProperty("itemSwapSound", itemSwapSound);

            obj.addProperty("pearlBind", pearlBind);
            obj.addProperty("chorusBind", chorusBind);
            obj.addProperty("rodBind", rodBind);
            obj.addProperty("shieldBind", shieldBind);
            obj.addProperty("gapBind", gapBind);
            obj.addProperty("swordSwapBind", swordSwapBind);

            obj.addProperty("toggleSneak", toggleSneak);
            obj.addProperty("toggleSprint", toggleSprint);
            obj.addProperty("antiBlind", antiBlind);
            obj.addProperty("noInvisible", noInvisible);
            obj.addProperty("chunkBorders", chunkBorders);
            obj.addProperty("hitboxes", hitboxes);

            obj.addProperty("language", language);
            obj.addProperty("discordPresence", discordPresence);

            obj.addProperty("swordX", swordX); obj.addProperty("swordY", swordY); obj.addProperty("swordZ", swordZ);
            obj.addProperty("swordRotX", swordRotX); obj.addProperty("swordRotY", swordRotY); obj.addProperty("swordRotZ", swordRotZ);
            obj.addProperty("swordScale", swordScale);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Files.write(configPath, gson.toJson(obj).getBytes());
        } catch (Exception e) {
            FrostVisuals.LOGGER.error("Failed to save FrostVisuals config: {}", e.getMessage());
        }
    }

    private static boolean getBool(String key, boolean def) {
        return config.has(key) ? config.get(key).getAsBoolean() : def;
    }
    private static int getInt(String key, int def) {
        return config.has(key) ? config.get(key).getAsInt() : def;
    }
    private static float getFloat(String key, float def) {
        return config.has(key) ? config.get(key).getAsFloat() : def;
    }
    private static String getString(String key, String def) {
        return config.has(key) ? config.get(key).getAsString() : def;
    }
}
