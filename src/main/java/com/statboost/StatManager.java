package com.statboost;

import com.statboost.network.NetworkHandler;
import com.statboost.network.SyncPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.network.PacketDistributor;

import java.util.Arrays;

public class StatManager {
    public static final String KEY = "statboost_points";

    public static int[] get(Player p) {
        CompoundTag t = p.getPersistentData();
        return Arrays.copyOf(t.getIntArray(KEY), Stat.values().length);
    }

    public static void save(Player p, int[] pts) {
        p.getPersistentData().putIntArray(KEY, pts);
    }

    public static int cost(int points) {
        return Math.max(0, StatConfig.BASE_COST.get() + (int) Math.floor(points * StatConfig.COST_GROWTH.get()));
    }

    public static double bonusPercent(int points) {
        return Math.min(points * StatConfig.PERCENT_PER_POINT.get(), StatConfig.MAX_BONUS_PERCENT.get());
    }

    public static void apply(ServerPlayer p) {
        int[] pts = get(p);
        for (Stat s : Stat.values()) {
            double amount = bonusPercent(pts[s.ordinal()]) / 100.0;
            for (Attribute a : s.attributes()) {
                AttributeInstance inst = p.getAttribute(a);
                if (inst == null) continue;
                inst.removeModifier(s.uuid);
                if (amount > 0) {
                    inst.addPermanentModifier(new AttributeModifier(
                            s.uuid, "statboost_" + s.id, amount, AttributeModifier.Operation.MULTIPLY_BASE));
                }
            }
        }
    }

    public static void sync(ServerPlayer p) {
        int[] pts = get(p);
        int[] costs = new int[pts.length];
        for (int i = 0; i < pts.length; i++) costs[i] = cost(pts[i]);
        NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> p),
                new SyncPacket(pts, costs, StatConfig.MAX_POINTS.get(),
                        StatConfig.PERCENT_PER_POINT.get().floatValue(),
                        StatConfig.MAX_BONUS_PERCENT.get().floatValue()));
    }

    public static void tryUpgrade(ServerPlayer p, int statIndex) {
        if (statIndex < 0 || statIndex >= Stat.values().length) return;
        int[] pts = get(p);
        int cur = pts[statIndex];
        if (cur >= StatConfig.MAX_POINTS.get()) {
            p.displayClientMessage(Component.translatable("msg.statboost.maxed"), true);
            sync(p);
            return;
        }
        int cost = cost(cur);
        boolean free = p.isCreative() && StatConfig.FREE_IN_CREATIVE.get();
        if (!free) {
            if (p.experienceLevel < cost) {
                p.displayClientMessage(Component.translatable("msg.statboost.not_enough_xp", cost), true);
                sync(p);
                return;
            }
            p.giveExperienceLevels(-cost);
        }
        pts[statIndex] = cur + 1;
        save(p, pts);
        float oldMax = p.getMaxHealth();
        apply(p);
        float diff = p.getMaxHealth() - oldMax;
        if (diff > 0) p.heal(diff);
        sync(p);
    }

    public static void reset(ServerPlayer p) {
        save(p, new int[Stat.values().length]);
        apply(p);
        if (p.getHealth() > p.getMaxHealth()) p.setHealth(p.getMaxHealth());
        sync(p);
    }
}
