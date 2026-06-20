package com.frostvisuals.utils;

public class ColorUtils {

    public static int withAlpha(int rgb, int alpha) {
        return (alpha << 24) | (rgb & 0x00FFFFFF);
    }

    public static int lerp(int colorA, int colorB, float t) {
        int ar = (colorA >> 16) & 0xFF, ag = (colorA >> 8) & 0xFF, ab = colorA & 0xFF;
        int br = (colorB >> 16) & 0xFF, bg = (colorB >> 8) & 0xFF, bb = colorB & 0xFF;
        int r = (int)(ar + (br - ar) * t);
        int g = (int)(ag + (bg - ag) * t);
        int b = (int)(ab + (bb - ab) * t);
        return (r << 16) | (g << 8) | b;
    }

    /** Parse #RRGGBB or 0xRRGGBB string */
    public static int parse(String s) {
        try {
            s = s.replace("#", "").replace("0x", "").replace("0X", "");
            return (int) Long.parseLong(s, 16);
        } catch (NumberFormatException e) {
            return 0xFFFFFF;
        }
    }

    public static String toHex(int color) {
        return String.format("%06X", color & 0xFFFFFF);
    }

    // Frost gradient: purple → blue, pulsing
    public static int frostGradient(float time) {
        float t = (float)(Math.sin(time * 2.0) * 0.5 + 0.5);
        return lerp(0x6A35FF, 0x3A8FFF, t);
    }
}
