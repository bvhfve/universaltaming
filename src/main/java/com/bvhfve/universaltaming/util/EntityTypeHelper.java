package com.bvhfve.universaltaming.util;

import java.util.Set;

public class EntityTypeHelper {
    
    private static final Set<String> AQUATIC_MOBS = Set.of(
        "minecraft:cod",
        "minecraft:salmon", 
        "minecraft:tropical_fish",
        "minecraft:pufferfish",
        "minecraft:squid",
        "minecraft:glow_squid",
        "minecraft:dolphin",
        "minecraft:turtle",
        "minecraft:drowned",
        "minecraft:guardian",
        "minecraft:elder_guardian"
    );
    
    private static final Set<String> PASSIVE_MOBS = Set.of(
        "minecraft:cow",
        "minecraft:pig", 
        "minecraft:sheep",
        "minecraft:chicken",
        "minecraft:rabbit",
        "minecraft:horse",
        "minecraft:donkey",
        "minecraft:mule",
        "minecraft:llama",
        "minecraft:trader_llama",
        "minecraft:villager",
        "minecraft:wandering_trader",
        "minecraft:cod",
        "minecraft:salmon",
        "minecraft:tropical_fish",
        "minecraft:pufferfish",
        "minecraft:squid",
        "minecraft:glow_squid",
        "minecraft:bat",
        "minecraft:mooshroom"
    );
    
    private static final Set<String> HOSTILE_MOBS = Set.of(
        "minecraft:zombie",
        "minecraft:skeleton",
        "minecraft:creeper",
        "minecraft:spider",
        "minecraft:cave_spider",
        "minecraft:enderman",
        "minecraft:witch",
        "minecraft:vindicator",
        "minecraft:evoker",
        "minecraft:pillager",
        "minecraft:ravager",
        "minecraft:vex",
        "minecraft:zombie_villager",
        "minecraft:husk",
        "minecraft:stray",
        "minecraft:drowned",
        "minecraft:phantom",
        "minecraft:slime",
        "minecraft:magma_cube",
        "minecraft:blaze",
        "minecraft:ghast",
        "minecraft:wither_skeleton",
        "minecraft:piglin",
        "minecraft:piglin_brute",
        "minecraft:hoglin",
        "minecraft:zoglin",
        "minecraft:zombified_piglin",
        "minecraft:guardian",
        "minecraft:elder_guardian",
        "minecraft:shulker",
        "minecraft:endermite",
        "minecraft:silverfish"
    );
    
    public static boolean isAquaticMob(String entityType) {
        return AQUATIC_MOBS.contains(entityType);
    }
    
    public static boolean isPassiveMob(String entityType) {
        return PASSIVE_MOBS.contains(entityType);
    }
    
    public static boolean isHostileMob(String entityType) {
        return HOSTILE_MOBS.contains(entityType);
    }
    
    public static boolean isNeutralMob(String entityType) {
        return !isPassiveMob(entityType) && !isHostileMob(entityType);
    }
}