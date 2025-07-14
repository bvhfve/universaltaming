package com.bvhfve.universaltaming.event;

import com.bvhfve.universaltaming.Universaltaming;
import com.bvhfve.universaltaming.config.UniversalTamingConfig;
import com.bvhfve.universaltaming.debug.DebugLogger;
import com.bvhfve.universaltaming.entity.TamedGenericEntity;
import com.bvhfve.universaltaming.init.ModItems;
import com.bvhfve.universaltaming.util.EntityConverter;
import com.bvhfve.universaltaming.util.EntityTypeHelper;
import com.bvhfve.universaltaming.util.TamingHelper;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class TamingEventHandler {
    
    public static void init() {
        UseEntityCallback.EVENT.register(TamingEventHandler::onUseEntity);
        Universaltaming.LOGGER.info("Registered taming event handlers");
    }
    
    private static ActionResult onUseEntity(PlayerEntity player, World world, net.minecraft.util.Hand hand, 
                                          net.minecraft.entity.Entity entity, 
                                          net.minecraft.util.hit.EntityHitResult hitResult) {
        
        if (world.isClient || !(entity instanceof MobEntity mob)) {
            return ActionResult.PASS;
        }
        
        ItemStack itemStack = player.getStackInHand(hand);
        UniversalTamingConfig config = UniversalTamingConfig.INSTANCE;
        
        // Check if holding the correct taming item
        if (!itemStack.isOf(ModItems.TAMING_TREAT)) {
            DebugLogger.logVerbose("TAMING", "Player " + player.getName().getString() + 
                " not holding taming item, has: " + itemStack.getItem());
            return ActionResult.PASS;
        }
        
        // Check if mob is tameable according to config
        Identifier entityTypeId = EntityType.getId(mob.getType());
        if (!config.isMobTameable(entityTypeId)) {
            DebugLogger.logTamingFailure(player, mob, "Mob type not enabled in config");
            return ActionResult.PASS;
        }
        
        // Don't tame already tamed entities
        if (mob instanceof TameableEntity tameable && tameable.isTamed()) {
            return ActionResult.PASS;
        }
        
        // Check if mob is already tamed by our system
        if (TamingHelper.isTamed(mob)) {
            DebugLogger.logTamingFailure(player, mob, "Mob already tamed");
            return ActionResult.PASS;
        }
        
        // Check if player has enough taming items
        if (itemStack.getCount() < config.tamingItemCount) {
            player.sendMessage(Text.translatable("message.universaltaming.not_enough_items", 
                config.tamingItemCount), true);
            return ActionResult.FAIL;
        }
        
        // Log taming attempt
        DebugLogger.logTamingAttempt(player, mob, "taming_treat");
        if (config.shouldLogTaming()) {
            Universaltaming.LOGGER.info("Player {} attempting to tame {} at {}", 
                player.getName().getString(), entityTypeId, mob.getBlockPos());
        }
        
        // Attempt taming (entity conversion approach)
        if (attemptEntityConversion(player, mob, (ServerWorld) world)) {
            // Consume taming items
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(config.tamingItemCount);
            }
            
            // Log successful taming
            if (config.shouldLogTaming()) {
                Universaltaming.LOGGER.info("Player {} successfully tamed {} at {}", 
                    player.getName().getString(), entityTypeId, mob.getBlockPos());
            }
            
            // Success feedback
            world.playSound(null, mob.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 
                mob.getSoundCategory(), 1.0f, 1.0f);
            
            if (world instanceof ServerWorld serverWorld) {
                // Spawn success particles
                for (int i = 0; i < 10; i++) {
                    double offsetX = mob.getRandom().nextGaussian() * 0.5;
                    double offsetY = mob.getRandom().nextGaussian() * 0.5;
                    double offsetZ = mob.getRandom().nextGaussian() * 0.5;
                    serverWorld.spawnParticles(ParticleTypes.HEART,
                        mob.getX() + offsetX,
                        mob.getY() + 1.0 + offsetY,
                        mob.getZ() + offsetZ,
                        1, 0, 0, 0, 0.1);
                }
            }
            
            return ActionResult.SUCCESS;
        }
        
        return ActionResult.FAIL;
    }
    
    private static boolean attemptEntityConversion(PlayerEntity player, MobEntity mob, ServerWorld world) {
        long startTime = System.currentTimeMillis();
        
        try {
            UniversalTamingConfig config = UniversalTamingConfig.INSTANCE;
            
            // Log entity analysis before taming
            DebugLogger.logEntityAnalysis(mob);
            
            // Store original health for logging
            double originalHealth = mob.getMaxHealth();
            
            // Convert the mob to a tamed entity
            TamedGenericEntity tamedEntity = EntityConverter.convertToTamedEntity(mob, player, world);
            if (tamedEntity == null) {
                DebugLogger.logError("ENTITY_CONVERSION", null, "Failed to convert mob to tamed entity");
                return false;
            }
            
            // Replace the original entity with the tamed version
            if (!EntityConverter.replaceEntityInWorld(mob, tamedEntity, world)) {
                DebugLogger.logError("ENTITY_REPLACEMENT", null, "Failed to replace entity in world");
                tamedEntity.discard(); // Clean up the created entity
                return false;
            }
            
            // Calculate new health for logging
            double newMaxHealth = originalHealth * config.healthMultiplier;
            
            DebugLogger.logDebugInfo("ENTITY_CONVERSION", String.format("Converted %s to tamed entity with health: %.1f -> %.1f", 
                EntityType.getId(mob.getType()), originalHealth, newMaxHealth));
            
            // Log successful taming
            DebugLogger.logTamingSuccess(player, mob, originalHealth, newMaxHealth);
            if (config.shouldLogTaming()) {
                Universaltaming.LOGGER.info("Player {} successfully tamed {} at {} (converted to custom entity)", 
                    player.getName().getString(), EntityType.getId(mob.getType()), mob.getBlockPos());
            }
            
            // Log performance
            long duration = System.currentTimeMillis() - startTime;
            DebugLogger.logPerformanceMetric("TAMING", duration, "Entity conversion completed");
            
            return true;
            
        } catch (Exception e) {
            DebugLogger.logError("TAMING", e, "Failed to tame mob for player " + player.getName().getString());
            Universaltaming.LOGGER.error("Failed to tame mob for player {}", player.getName().getString(), e);
            return false;
        }
    }
    
    private static void addTamingGoals(MobEntity mob) {
        String entityType = EntityType.getId(mob.getType()).toString();
        
        // Note: AI goal modification will be handled via mixins instead
        // Direct goal manipulation has access issues in 1.21.7
        DebugLogger.logDebugInfo("AI_SETUP", "Taming goals will be handled via mixins for " + entityType);
        
        if (EntityTypeHelper.isHostileMob(entityType)) {
            DebugLogger.logDebugInfo("AI_SETUP", "Hostile mob protection will be handled via mixins for " + entityType);
        } else {
            DebugLogger.logDebugInfo("AI_SETUP", "Passive mob behaviors will be handled via mixins for " + entityType);
        }
    }
    
}