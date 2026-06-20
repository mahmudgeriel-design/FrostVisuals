package com.frostvisuals.keys;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {

    public static final String CATEGORY = "key.category.frostvisuals";

    public static KeyBinding openMenu;
    public static KeyBinding pearlBind;
    public static KeyBinding chorusBind;
    public static KeyBinding rodBind;
    public static KeyBinding shieldBind;
    public static KeyBinding gapBind;
    public static KeyBinding swordSwapBind;
    public static KeyBinding freelookBind;
    public static KeyBinding zoomBind;
    public static KeyBinding toggleSneakBind;
    public static KeyBinding toggleSprintBind;

    public static void register() {
        openMenu       = reg("key.frostvisuals.open_menu",    GLFW.GLFW_KEY_RIGHT_SHIFT);
        pearlBind      = reg("key.frostvisuals.pearl",        GLFW.GLFW_KEY_UNKNOWN);
        chorusBind     = reg("key.frostvisuals.chorus",       GLFW.GLFW_KEY_UNKNOWN);
        rodBind        = reg("key.frostvisuals.rod",          GLFW.GLFW_KEY_UNKNOWN);
        shieldBind     = reg("key.frostvisuals.shield",       GLFW.GLFW_KEY_UNKNOWN);
        gapBind        = reg("key.frostvisuals.gap",          GLFW.GLFW_KEY_UNKNOWN);
        swordSwapBind  = reg("key.frostvisuals.sword_swap",   GLFW.GLFW_KEY_UNKNOWN);
        freelookBind   = reg("key.frostvisuals.freelook",     GLFW.GLFW_KEY_LEFT_ALT);
        zoomBind       = reg("key.frostvisuals.zoom",         GLFW.GLFW_KEY_C);
        toggleSneakBind  = reg("key.frostvisuals.toggle_sneak",  GLFW.GLFW_KEY_UNKNOWN);
        toggleSprintBind = reg("key.frostvisuals.toggle_sprint", GLFW.GLFW_KEY_UNKNOWN);
    }

    private static KeyBinding reg(String desc, int key) {
        KeyBinding kb = new KeyBinding(desc, key, CATEGORY);
        ClientRegistry.registerKeyBinding(kb);
        return kb;
    }
}
