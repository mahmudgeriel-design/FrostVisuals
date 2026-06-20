package com.frostvisuals.features.binds;

import com.frostvisuals.config.FrostConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.*;

/**
 * Helper to find items in hotbar (slots 0-8 ONLY) and activate them.
 * If item not found in hotbar, does NOTHING.
 */
public class ItemBindHelper {

    private static final Minecraft MC = Minecraft.getInstance();

    public static boolean activateIfInHotbar(Item item) {
        ClientPlayerEntity player = MC.player;
        if (player == null) return false;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.inventory.getItem(i);
            if (stack.getItem() == item) {
                player.inventory.selected = i;
                return true;
            }
        }
        return false; // Not in hotbar — do nothing
    }

    public static boolean isSword(ItemStack stack) {
        return stack.getItem() instanceof SwordItem;
    }

    public static boolean pearlInHotbar() {
        return findInHotbar(Items.ENDER_PEARL) >= 0;
    }

    public static boolean chorusInHotbar() {
        return findInHotbar(Items.CHORUS_FRUIT) >= 0;
    }

    public static int findInHotbar(Item item) {
        ClientPlayerEntity player = MC.player;
        if (player == null) return -1;
        for (int i = 0; i < 9; i++) {
            if (player.inventory.getItem(i).getItem() == item) return i;
        }
        return -1;
    }
}
