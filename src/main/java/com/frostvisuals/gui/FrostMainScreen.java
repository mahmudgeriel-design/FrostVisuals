package com.frostvisuals.gui;

import com.frostvisuals.config.FrostConfig;
import com.frostvisuals.gui.widgets.ContextMenu;
import com.frostvisuals.gui.widgets.FrostButton;
import com.frostvisuals.utils.ColorUtils;
import com.frostvisuals.utils.RenderUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;

public class FrostMainScreen extends Screen {

    private static final String[] TABS = {"VISUALS", "HUD", "WORLD", "SOUNDS", "BINDS", "UTILITIES", "SETTINGS"};
    private int selectedTab = 0;

    // Sub-screens
    private Screen[] tabScreens;

    // Snowflake particles for background
    private final List<float[]> snowflakes = new ArrayList<>();
    private long lastSnowUpdate = 0;

    private ContextMenu contextMenu = new ContextMenu();

    public FrostMainScreen() {
        super(new StringTextComponent("FrostVisuals"));
        for (int i = 0; i < 60; i++) {
            snowflakes.add(new float[]{
                (float)(Math.random() * 800),
                (float)(Math.random() * 500),
                (float)(Math.random() * 1.5 + 0.3f),
                (float)(Math.random() * 0.4 + 0.6f)
            });
        }
    }

    @Override
    protected void init() {
        tabScreens = new Screen[]{
            new VisualsScreen(this),
            new HudScreen(this),
            new WorldScreen(this),
            new SoundsScreen(this),
            new BindsScreen(this),
            new UtilitiesScreen(this),
            new SettingsScreen(this)
        };
        for (Screen s : tabScreens) { s.init(minecraft, width, height); }
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partial) {
        // Dark overlay
        RenderUtils.fillRect(ms, 0, 0, width, height, ColorUtils.withAlpha(0x000000, 180));

        // Background panel
        int panelX = width / 2 - 220, panelY = 20;
        int panelW = 440, panelH = height - 50;
        RenderUtils.fillRect(ms, panelX, panelY, panelW, panelH, ColorUtils.withAlpha(0x0A0E1A, 245));

        // Frost outline
        long time = System.currentTimeMillis();
        int outlineColor = ColorUtils.withAlpha(ColorUtils.frostGradient(time / 1000f), 230);
        RenderUtils.drawRoundedOutline(ms, panelX, panelY, panelW, panelH, outlineColor);

        // Inner top bar
        RenderUtils.fillRect(ms, panelX, panelY, panelW, 28, ColorUtils.withAlpha(0x06091A, 220));
        RenderUtils.fillRect(ms, panelX, panelY + 27, panelW, 1, ColorUtils.withAlpha(ColorUtils.frostGradient(time / 1000f), 180));

        // ❄ FrostVisuals title
        String title = "\u2744 FrostVisuals";
        int titleW = font.width(title);
        font.draw(ms, title, panelX + panelW / 2f - titleW / 2f, panelY + 10, ColorUtils.frostGradient(time / 1000f));

        // Animate snowflakes in background
        updateAndDrawSnowflakes(ms, panelX, panelY + 28, panelW, panelH - 28);

        // Tab bar
        int tabY = panelY + 32;
        int tabW = panelW / TABS.length;
        for (int i = 0; i < TABS.length; i++) {
            int tx = panelX + i * tabW;
            boolean sel = i == selectedTab;
            boolean hov = mouseX >= tx && mouseX <= tx + tabW && mouseY >= tabY && mouseY <= tabY + 16;
            int tabBg = sel ? ColorUtils.withAlpha(0x1A0A3A, 220) : (hov ? ColorUtils.withAlpha(0x0F1228, 200) : 0);
            if (tabBg != 0) RenderUtils.fillRect(ms, tx, tabY, tabW, 16, tabBg);
            if (sel) {
                RenderUtils.fillRect(ms, tx, tabY + 14, tabW, 2, ColorUtils.withAlpha(ColorUtils.frostGradient(time / 1000f), 220));
            }
            int textColor = sel ? ColorUtils.frostGradient(time / 1000f) : (hov ? 0xDDDDFF : 0x888899);
            int tw = font.width(TABS[i]);
            font.draw(ms, TABS[i], tx + tabW / 2f - tw / 2f, tabY + 4, textColor);
        }

        // Render active tab content
        if (tabScreens != null && selectedTab < tabScreens.length && tabScreens[selectedTab] != null) {
            tabScreens[selectedTab].render(ms, mouseX, mouseY, partial);
        }

        // Context menu on top
        contextMenu.render(ms, mouseX, mouseY);
    }

    private void updateAndDrawSnowflakes(MatrixStack ms, int ox, int oy, int w, int h) {
        long now = System.currentTimeMillis();
        float dt = Math.min((now - lastSnowUpdate) / 1000f, 0.1f);
        lastSnowUpdate = now;
        for (float[] sf : snowflakes) {
            sf[1] += sf[2] * dt * 30f;
            sf[0] += (float)Math.sin(sf[1] * 0.01f) * dt * 5f;
            if (sf[1] > h) { sf[1] = 0; sf[0] = (float)(Math.random() * w); }
            if (sf[0] < 0) sf[0] += w; if (sf[0] > w) sf[0] -= w;
            int alpha = (int)(80 * sf[3]);
            RenderUtils.fillRect(ms, ox + sf[0], oy + sf[1], sf[2], sf[2], ColorUtils.withAlpha(0xCCDDFF, alpha));
        }
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        // Context menu
        if (button == 0 && contextMenu.isVisible()) {
            if (contextMenu.mouseClicked(mx, my, button)) return true;
        }

        // Tab click
        int panelX = width / 2 - 220, panelY = 20;
        int panelW = 440;
        int tabY = panelY + 32;
        int tabW = panelW / TABS.length;
        if (my >= tabY && my <= tabY + 16) {
            for (int i = 0; i < TABS.length; i++) {
                int tx = panelX + i * tabW;
                if (mx >= tx && mx <= tx + tabW) {
                    selectedTab = i;
                    if (tabScreens != null) tabScreens[selectedTab].init(minecraft, width, height);
                    return true;
                }
            }
        }

        // Delegate to active tab
        if (tabScreens != null && selectedTab < tabScreens.length && tabScreens[selectedTab] != null) {
            return tabScreens[selectedTab].mouseClicked(mx, my, button);
        }
        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double delta) {
        if (tabScreens != null && selectedTab < tabScreens.length && tabScreens[selectedTab] != null) {
            return tabScreens[selectedTab].mouseScrolled(mx, my, delta);
        }
        return super.mouseScrolled(mx, my, delta);
    }

    @Override
    public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
        if (tabScreens != null && selectedTab < tabScreens.length && tabScreens[selectedTab] != null) {
            return tabScreens[selectedTab].mouseDragged(mx, my, button, dx, dy);
        }
        return super.mouseDragged(mx, my, button, dx, dy);
    }

    @Override
    public void onClose() {
        FrostConfig.save();
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() { return false; }

    public ContextMenu getContextMenu() { return contextMenu; }
    public int getPanelX() { return width / 2 - 220; }
    public int getPanelY() { return 20; }
    public int getPanelW() { return 440; }
    public int getPanelH() { return height - 50; }
    public int getContentY() { return getPanelY() + 50; }
}
