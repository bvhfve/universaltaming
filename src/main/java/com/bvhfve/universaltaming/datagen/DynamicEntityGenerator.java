package com.bvhfve.universaltaming.datagen;

import com.bvhfve.universaltaming.Universaltaming;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

/**
 * Dynamically generates custom tamed entity types for each vanilla mob type.
 * Each vanilla mob gets a corresponding tamed version that renders with the original model/texture.
 */
public class DynamicEntityGenerator {
    
    // Map of vanilla entity type ID to custom tamed entity type
    private static final Map<String, EntityType<?>> TAMED_ENTITY_TYPES = new HashMap<>();
    
    // List of all vanilla mob types that can be tamed
    private static final String[] TAMEABLE_MOBS = {
        // Passive mobs
        "minecraft:cow", "minecraft:pig", "minecraft:sheep", "minecraft:chicken", "minecraft:rabbit",
        "minecraft:horse", "minecraft:donkey", "minecraft:mule", "minecraft:llama", "minecraft:trader_llama",
        "minecraft:villager", "minecraft:wandering_trader", "minecraft:mooshroom", "minecraft:goat",
        "minecraft:axolotl", "minecraft:glow_squid", "minecraft:squid", "minecraft:bat",
        
        // Neutral mobs
        "minecraft:wolf", "minecraft:cat", "minecraft:parrot", "minecraft:fox", "minecraft:bee",
        "minecraft:panda", "minecraft:polar_bear", "minecraft:iron_golem", "minecraft:snow_golem",
        "minecraft:dolphin", "minecraft:turtle", "minecraft:frog", "minecraft:allay",
        
        // Hostile mobs
        "minecraft:zombie", "minecraft:skeleton", "minecraft:creeper", "minecraft:spider", "minecraft:cave_spider",
        "minecraft:enderman", "minecraft:witch", "minecraft:vindicator", "minecraft:evoker", "minecraft:pillager",
        "minecraft:ravager", "minecraft:vex", "minecraft:zombie_villager", "minecraft:husk", "minecraft:stray",
        "minecraft:drowned", "minecraft:phantom", "minecraft:slime", "minecraft:magma_cube", "minecraft:blaze",
        "minecraft:ghast", "minecraft:wither_skeleton", "minecraft:piglin", "minecraft:piglin_brute",
        "minecraft:hoglin", "minecraft:zoglin", "minecraft:zombified_piglin", "minecraft:guardian",
        "minecraft:elder_guardian", "minecraft:shulker", "minecraft:endermite", "minecraft:silverfish",
        "minecraft:warden", "minecraft:breeze"
    };
    
    public static void generateTamedEntityTypes() {
        Universaltaming.LOGGER.info("Starting dynamic entity generation for {} mob types", TAMEABLE_MOBS.length);
        
        for (String vanillaMobId : TAMEABLE_MOBS) {
            try {
                generateTamedEntityType(vanillaMobId);
            } catch (Exception e) {
                Universaltaming.LOGGER.error("Failed to generate tamed entity type for {}: {}", vanillaMobId, e.getMessage());
            }
        }
        
        Universaltaming.LOGGER.info("Dynamic entity generation completed. Generated {} tamed entity types", 
            TAMED_ENTITY_TYPES.size());
    }
    
    private static void generateTamedEntityType(String vanillaMobId) {
        // Get the vanilla entity type
        Identifier vanillaId = Identifier.tryParse(vanillaMobId);
        if (vanillaId == null) {
            Universaltaming.LOGGER.warn("Invalid vanilla mob ID: {}", vanillaMobId);
            return;
        }
        
        EntityType<?> vanillaType = Registries.ENTITY_TYPE.get(vanillaId);
        if (vanillaType == null) {
            Universaltaming.LOGGER.warn("Vanilla entity type not found: {}", vanillaMobId);
            return;
        }
        
        // Create tamed version ID
        String mobName = vanillaId.getPath();
        Identifier tamedId = Universaltaming.id("tamed_" + mobName);
        
        // Get dimensions from vanilla entity
        EntityDimensions dimensions = vanillaType.getDimensions();
        
        // Create the tamed entity type using TamedGenericEntity
        EntityType<com.bvhfve.universaltaming.entity.TamedGenericEntity> tamedEntityType = 
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, 
                com.bvhfve.universaltaming.entity.TamedGenericEntity::new)
                .dimensions(dimensions)
                .trackRangeBlocks(80)
                .trackedUpdateRate(3)
                .build(net.minecraft.registry.RegistryKey.of(
                    net.minecraft.registry.Registries.ENTITY_TYPE.getKey(), tamedId));
        
        // Register the entity type
        Registry.register(Registries.ENTITY_TYPE, tamedId, tamedEntityType);
        
        // Store in our map
        TAMED_ENTITY_TYPES.put(vanillaMobId, tamedEntityType);
        
        Universaltaming.LOGGER.debug("Generated tamed entity type: {} -> {}", vanillaMobId, tamedId);
    }
    
    /**
     * Gets the tamed entity type for a given vanilla mob type
     */
    public static EntityType<?> getTamedEntityType(String vanillaMobId) {
        return TAMED_ENTITY_TYPES.get(vanillaMobId);
    }
    
    /**
     * Gets the tamed entity type for a given vanilla entity type
     */
    public static EntityType<?> getTamedEntityType(EntityType<?> vanillaType) {
        String vanillaMobId = EntityType.getId(vanillaType).toString();
        return getTamedEntityType(vanillaMobId);
    }
    
    /**
     * Checks if a vanilla mob type has a corresponding tamed entity type
     */
    public static boolean hasTamedEntityType(String vanillaMobId) {
        return TAMED_ENTITY_TYPES.containsKey(vanillaMobId);
    }
    
    /**
     * Gets all generated tamed entity types
     */
    public static Map<String, EntityType<?>> getAllTamedEntityTypes() {
        return new HashMap<>(TAMED_ENTITY_TYPES);
    }
}