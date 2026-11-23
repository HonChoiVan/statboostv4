package com.statboost;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public enum Stat {
    STRENGTH("strength", "a1b2c3d4-0001-4000-8000-5b0a3d2c1001"),
    HEALTH("health", "a1b2c3d4-0002-4000-8000-5b0a3d2c1002"),
    SPEED("speed", "a1b2c3d4-0003-4000-8000-5b0a3d2c1003"),
    SPELL_POWER("spell_power", "a1b2c3d4-0004-4000-8000-5b0a3d2c1004");

    public final String id;
    public final UUID uuid;

    Stat(String id, String uuid) {
        this.id = id;
        this.uuid = UUID.fromString(uuid);
    }

    public List<Attribute> attributes() {
        List<Attribute> list = new ArrayList<>();
        switch (this) {
            case STRENGTH -> list.add(Attributes.ATTACK_DAMAGE);
            case HEALTH -> list.add(Attributes.MAX_HEALTH);
            case SPEED -> list.add(Attributes.MOVEMENT_SPEED);
            case SPELL_POWER -> {
                for (String s : StatConfig.SPELL_POWER_ATTRIBUTES.get()) {
                    ResourceLocation rl = ResourceLocation.tryParse(s.trim());
                    if (rl != null && ForgeRegistries.ATTRIBUTES.containsKey(rl)) {
                        Attribute a = ForgeRegistries.ATTRIBUTES.getValue(rl);
                        if (a != null) list.add(a);
                    }
                }
            }
        }
        return list;
    }
}
