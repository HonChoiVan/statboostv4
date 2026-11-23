package com.statboost;

import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;

@Mod.EventBusSubscriber(modid = StatBoostMod.MODID)
public class ServerEvents {

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent e) {
        if (e.getEntity() instanceof ServerPlayer p) {
            StatManager.apply(p);
            StatManager.sync(p);
        }
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone e) {
        e.getEntity().getPersistentData().putIntArray(StatManager.KEY,
                e.getOriginal().getPersistentData().getIntArray(StatManager.KEY));
        if (e.getEntity() instanceof ServerPlayer p) {
            StatManager.apply(p);
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent e) {
        if (e.getEntity() instanceof ServerPlayer p) {
            StatManager.apply(p);
            if (!e.isEndConquered()) p.setHealth(p.getMaxHealth());
            StatManager.sync(p);
        }
    }

    @SubscribeEvent
    public static void onDimension(PlayerEvent.PlayerChangedDimensionEvent e) {
        if (e.getEntity() instanceof ServerPlayer p) {
            StatManager.apply(p);
            StatManager.sync(p);
        }
    }

    @SubscribeEvent
    public static void onCommands(RegisterCommandsEvent e) {
        e.getDispatcher().register(Commands.literal("statboost")
                .requires(s -> s.hasPermission(2))
                .then(Commands.literal("reset")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .executes(ctx -> {
                                    Collection<ServerPlayer> list = EntityArgument.getPlayers(ctx, "targets");
                                    for (ServerPlayer p : list) StatManager.reset(p);
                                    ctx.getSource().sendSuccess(
                                            () -> Component.translatable("msg.statboost.reset", list.size()), true);
                                    return list.size();
                                }))));
    }
}
