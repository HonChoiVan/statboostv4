package com.statboost;

import com.statboost.network.NetworkHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(StatBoostMod.MODID)
public class StatBoostMod {
    public static final String MODID = "statboost";

    public StatBoostMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, StatConfig.SPEC);
        NetworkHandler.register();
    }
}
