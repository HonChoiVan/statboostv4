package com.statboost.client;

import com.statboost.ClientStatData;
import com.statboost.Stat;
import com.statboost.network.NetworkHandler;
import com.statboost.network.UpgradePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class StatScreen extends Screen {
    private static final int W = 300;
    private static final int ROW = 26;
    private final int H = 40 + Stat.values().length * ROW + 24;
    private int left, top;
    private final Button[] buttons = new Button[Stat.values().length];

    public StatScreen() {
        super(Component.translatable("screen.statboost.title"));
    }

    @Override
    protected void init() {
        left = (width - W) / 2;
        top = (height - H) / 2;
        for (Stat s : Stat.values()) {
            final int idx = s.ordinal();
            buttons[idx] = addRenderableWidget(Button.builder(Component.literal("+"),
                            b -> NetworkHandler.CHANNEL.sendToServer(new UpgradePacket(idx)))
                    .bounds(left + W - 34, top + 30 + idx * ROW, 24, 20).build());
        }
        refresh();
    }

    private void refresh() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        for (Stat s : Stat.values()) {
            int i = s.ordinal();
            boolean maxed = ClientStatData.points[i] >= ClientStatData.max;
            boolean afford = mc.player.isCreative() || mc.player.experienceLevel >= ClientStatData.costs[i];
            if (buttons[i] != null) buttons[i].active = !maxed && afford;
        }
    }

    @Override
    public void tick() {
        super.tick();
        refresh();
    }

    private static String fmt(double v) {
        return v == Math.floor(v) ? String.valueOf((int) v) : String.format("%.1f", v);
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float pt) {
        renderBackground(g);
        g.fill(left - 2, top - 2, left + W + 2, top + H + 2, 0xFF555555);
        g.fill(left, top, left + W, top + H, 0xEE101018);
        g.drawCenteredString(font, title, width / 2, top + 10, 0xFFFFFF);

        for (Stat s : Stat.values()) {
            int i = s.ordinal();
            int y = top + 30 + i * ROW;
            int pts = ClientStatData.points[i];
            boolean maxed = pts >= ClientStatData.max;
            double bonus = Math.min(pts * ClientStatData.percent, ClientStatData.maxBonus);
            g.drawString(font, Component.translatable("stat.statboost." + s.id), left + 10, y + 6, 0xFFFFFF);
            g.drawString(font, pts + "/" + ClientStatData.max, left + 120, y + 6, 0xFFD27F);
            g.drawString(font, "+" + fmt(bonus) + "%", left + 165, y + 6, 0x7FFF7F);
            Component c = maxed ? Component.translatable("screen.statboost.max")
                    : Component.translatable("screen.statboost.cost", ClientStatData.costs[i]);
            g.drawString(font, c, left + 220, y + 6, 0x7FD4FF);
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            g.drawCenteredString(font,
                    Component.translatable("screen.statboost.level", mc.player.experienceLevel),
                    width / 2, top + H - 16, 0x80FF20);
        }
        super.render(g, mx, my, pt);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
