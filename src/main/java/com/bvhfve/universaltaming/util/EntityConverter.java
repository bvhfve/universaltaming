package com.bvhfve.universaltaming.util;

import com.bvhfve.universaltaming.Universaltaming;
import com.bvhfve.universaltaming.datagen.DynamicEntityGenerator;
import com.bvhfve.universaltaming.entity.TamedGenericEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

/**
 * Handles conversion between vanilla mobs and their tamed counterparts
 */
public class EntityConverter {
    
    /**
     * Converts a vanilla mob to its tamed equivalent
     */
    public static TamedGenericEntity convertToTamedEntity(MobEntity originalMob, PlayerEntity owner, ServerWorld world) {
        try {
            // Get the vanilla mob's entity type
            String vanillaMobId = EntityType.getId(originalMob.getType()).toString();
            
            // Get the corresponding tamed entity type
            EntityType<?> tamedEntityType = DynamicEntityGenerator.getTamedEntityType(vanillaMobId);
            if (tamedEntityType == null) {
                Universaltaming.LOGGER.error("No tamed entity type found for: {}", vanillaMobId);
                return null;
            }
            
            // Create the new tamed entity
            @SuppressWarnings("unchecked")
            EntityType<TamedGenericEntity> castedType = (EntityType<TamedGenericEntity>) tamedEntityType;
            TamedGenericEntity tamedEntity = new TamedGenericEntity(castedType, world);
            
            // Copy position and rotation
            tamedEntity.refreshPositionAndAngles(
                originalMob.getX(), originalMob.getY(), originalMob.getZ(),
                originalMob.getYaw(), originalMob.getPitch()
            );
            
            // Copy basic properties
            tamedEntity.setVelocity(originalMob.getVelocity());
            tamedEntity.setOnGround(originalMob.isOnGround());
            
            // Set taming properties
            tamedEntity.setTamed(true, false);
            tamedEntity.setOwner(owner);
            tamedEntity.setOriginalEntityType(vanillaMobId);
            
            // Configure entity type-specific properties
            configureEntityProperties(tamedEntity, vanillaMobId);
            
            // Copy health (with boost)
            double originalMaxHealth = originalMob.getMaxHealth();
            double boostedHealth = originalMaxHealth * com.bvhfve.universaltaming.config.UniversalTamingConfig.INSTANCE.healthMultiplier;
            tamedEntity.getAttributeInstance(net.minecraft.entity.attribute.EntityAttributes.MAX_HEALTH)
                .setBaseValue(boostedHealth);
            tamedEntity.setHealth((float) boostedHealth);
            
            // Copy custom name if present
            if (originalMob.hasCustomName()) {
                tamedEntity.setCustomName(originalMob.getCustomName());
            }
            
            Universaltaming.LOGGER.debug("Successfully converted {} to tamed entity", vanillaMobId);
            return tamedEntity;
            
        } catch (Exception e) {
            Universaltaming.LOGGER.error("Failed to convert mob to tamed entity", e);
            return null;
        }
    }
    
    /**
     * Configures entity-specific properties based on the original mob type
     */
    private static void configureEntityProperties(TamedGenericEntity tamedEntity, String originalMobType) {
        // Set behavioral flags based on original mob type
        if (EntityTypeHelper.isHostileMob(originalMobType)) {
            tamedEntity.setHostile(true);
            tamedEntity.setPassive(false);
        } else if (EntityTypeHelper.isPassiveMob(originalMobType)) {
            tamedEntity.setHostile(false);
            tamedEntity.setPassive(true);
        } else {
            // Neutral mobs
            tamedEntity.setHostile(false);
            tamedEntity.setPassive(false);
        }
        
        // Set aquatic flag for water mobs
        if (EntityTypeHelper.isAquaticMob(originalMobType)) {
            tamedEntity.setUnderwater(true);
        }
        
        // Special cases for specific mobs
        switch (originalMobType) {
            case "minecraft:fox" -> {
                // Foxes can't sit (as per original design)
                tamedEntity.setSitting(false);
            }
            case "minecraft:creeper" -> {
                // Creepers might need special handling
                tamedEntity.setHostile(true);
            }
            case "minecraft:enderman" -> {
                // Endermen are neutral but can be aggressive
                tamedEntity.setHostile(false);
            }
        }
    }
    
    /**
     * Safely replaces the original mob with the tamed version in the world
     */
    public static boolean replaceEntityInWorld(MobEntity originalMob, TamedGenericEntity tamedEntity, ServerWorld world) {
        try {
            // Store the position for logging
            BlockPos pos = originalMob.getBlockPos();
            
            // Add the new entity to the world
            world.spawnEntity(tamedEntity);
            
            // Remove the original entity
            originalMob.discard();
            
            Universaltaming.LOGGER.debug("Successfully replaced entity at {}", pos);
            return true;
            
        } catch (Exception e) {
            Universaltaming.LOGGER.error("Failed to replace entity in world", e);
            return false;
        }
    }
}