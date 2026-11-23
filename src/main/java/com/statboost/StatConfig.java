package com.statboost;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class StatConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.IntValue MAX_POINTS;
    public static final ForgeConfigSpec.DoubleValue PERCENT_PER_POINT;
    public static final ForgeConfigSpec.DoubleValue MAX_BONUS_PERCENT;
    public static final ForgeConfigSpec.IntValue BASE_COST;
    public static final ForgeConfigSpec.DoubleValue COST_GROWTH;
    public static final ForgeConfigSpec.BooleanValue FREE_IN_CREATIVE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> SPELL_POWER_ATTRIBUTES;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();
        b.push("stats");
        MAX_POINTS = b.comment("Max points per stat (default 50)")
                .defineInRange("maxPoints", 50, 1, 1000);
        PERCENT_PER_POINT = b.comment("Percent bonus per point (default 4 => 1 point = +4%)")
                .defineInRange("percentPerPoint", 4.0, 0.0, 1000.0);
        MAX_BONUS_PERCENT = b.comment("Hard cap of the total bonus in percent (default 200)")
                .defineInRange("maxBonusPercent", 200.0, 0.0, 10000.0);
        b.pop();

        b.push("cost");
        BASE_COST = b.comment("XP levels needed for the first point")
                .defineInRange("baseLevelCost", 1, 0, 1000);
        COST_GROWTH = b.comment("Extra XP levels per point already bought. cost = base + floor(points * growth)")
                .defineInRange("levelCostGrowth", 0.5, 0.0, 100.0);
        FREE_IN_CREATIVE = b.comment("Creative players upgrade for free")
                .define("freeInCreative", true);
        b.pop();

        b.push("compat");
        SPELL_POWER_ATTRIBUTES = b.comment(
                        "Attribute IDs that the 'Spell Power' stat boosts. Unknown IDs (mod not installed) are ignored.",
                        "Add attribute IDs from other mods here (e.g. To Magic and Extras) - one per entry.")
                .defineList("spellPowerAttributes", List.of("irons_spellbooks:spell_power"), o -> o instanceof String);
        b.pop();

        SPEC = b.build();
    }
}
