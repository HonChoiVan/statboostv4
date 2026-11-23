package com.statboost.client;

import com.statboost.StatBoostMod;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientModEvents {

    @Mod.EventBusSubscriber(modid = StatBoostMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBus {
        @SubscribeEvent
        public static void onKeys(RegisterKeyMappingsEvent e) {
            e.register(ClientKeys.OPEN);
        }
    }

    @Mod.EventBusSubscriber(modid = StatBoostMod.MODID, value = Dist.CLIENT)
    public static class ForgeBus {
        @SubscribeEvent
        public static void onTick(TickEvent.ClientTickEvent e) {
            if (e.phase != TickEvent.Phase.END) return;
            Minecraft mc = Minecraft.getInstance();
            while (ClientKeys.OPEN.consumeClick()) {
                if (mc.player != null && mc.screen == null) {
                    mc.setScreen(new StatScreen());
                }
            }
        }
    }
}
