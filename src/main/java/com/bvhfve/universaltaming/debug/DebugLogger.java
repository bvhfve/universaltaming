package com.bvhfve.universaltaming.debug;

import com.bvhfve.universaltaming.Universaltaming;
import com.bvhfve.universaltaming.config.UniversalTamingConfig;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger("UniversalTaming-Debug");
    
    public enum DebugLevel {
        BASIC, DETAILED, VERBOSE
    }
    
    public static boolean isDebugEnabled() {
        return UniversalTamingConfig.INSTANCE != null && 
               UniversalTamingConfig.INSTANCE.enableDetailedDebugging;
    }
    
    public static DebugLevel getDebugLevel() {
        if (UniversalTamingConfig.INSTANCE == null) return DebugLevel.BASIC;
        
        try {
            return DebugLevel.valueOf(UniversalTamingConfig.INSTANCE.debugLevel.toUpperCase());
        } catch (IllegalArgumentException e) {
            return DebugLevel.BASIC;
        }
    }
    
    // Taming Events
    public static void logTamingAttempt(PlayerEntity player, MobEntity mob, String itemUsed) {
        if (!isDebugEnabled()) return;
        
        String entityType = net.minecraft.entity.EntityType.getId(mob.getType()).toString();
        LOGGER.info("[TAMING-ATTEMPT] Player: {} | Mob: {} | Item: {} | Position: {}", 
            player.getName().getString(), entityType, itemUsed, mob.getBlockPos());
    }
    
    public static void logTamingSuccess(PlayerEntity player, MobEntity mob, double healthBefore, double healthAfter) {
        if (!isDebugEnabled()) return;
        
        String entityType = net.minecraft.entity.EntityType.getId(mob.getType()).toString();
        LOGGER.info("[TAMING-SUCCESS] Player: {} | Mob: {} | Health: {}->{} | Position: {}", 
            player.getName().getString(), entityType, healthBefore, healthAfter, mob.getBlockPos());
    }
    
    public static void logTamingFailure(PlayerEntity player, MobEntity mob, String reason) {
        if (!isDebugEnabled()) return;
        
        String entityType = net.minecraft.entity.EntityType.getId(mob.getType()).toString();
        LOGGER.warn("[TAMING-FAILURE] Player: {} | Mob: {} | Reason: {} | Position: {}", 
            player.getName().getString(), entityType, reason, mob.getBlockPos());
    }
    
    // AI System
    public static void logAIGoalAdded(MobEntity mob, String goalType, int priority) {
        if (!isDebugEnabled() || !UniversalTamingConfig.INSTANCE.debugAI) return;
        
        String entityType = net.minecraft.entity.EntityType.getId(mob.getType()).toString();
        LOGGER.debug("[AI-GOAL-ADDED] Mob: {} | Goal: {} | Priority: {} | Position: {}", 
            entityType, goalType, priority, mob.getBlockPos());
    }
    
    public static void logAIGoalExecution(MobEntity mob, String goalType, String action) {
        if (!isDebugEnabled() || !UniversalTamingConfig.INSTANCE.debugAI) return;
        if (getDebugLevel() != DebugLevel.VERBOSE) return;
        
        String entityType = net.minecraft.entity.EntityType.getId(mob.getType()).toString();
        LOGGER.debug("[AI-GOAL-EXEC] Mob: {} | Goal: {} | Action: {}", 
            entityType, goalType, action);
    }
    
    // Mixin Events
    public static void logMixinInteraction(MobEntity mob, String mixinMethod, String action) {
        if (!isDebugEnabled() || !UniversalTamingConfig.INSTANCE.debugMixins) return;
        
        String entityType = net.minecraft.entity.EntityType.getId(mob.getType()).toString();
        LOGGER.debug("[MIXIN-INTERACTION] Mob: {} | Method: {} | Action: {}", 
            entityType, mixinMethod, action);
    }
    
    public static void logDamageEvent(MobEntity mob, String damageSource, boolean prevented, String reason) {
        if (!isDebugEnabled() || !UniversalTamingConfig.INSTANCE.debugMixins) return;
        
        String entityType = net.minecraft.entity.EntityType.getId(mob.getType()).toString();
        LOGGER.debug("[DAMAGE-EVENT] Mob: {} | Source: {} | Prevented: {} | Reason: {}", 
            entityType, damageSource, prevented, reason);
    }
    
    // Data Attachment
    public static void logDataAttachment(MobEntity mob, String operation, String data) {
        if (!isDebugEnabled() || !UniversalTamingConfig.INSTANCE.debugDataAttachment) return;
        
        String entityType = net.minecraft.entity.EntityType.getId(mob.getType()).toString();
        LOGGER.debug("[DATA-ATTACHMENT] Mob: {} | Operation: {} | Data: {}", 
            entityType, operation, data);
    }
    
    public static void logDataPersistence(MobEntity mob, String operation, boolean success) {
        if (!isDebugEnabled() || !UniversalTamingConfig.INSTANCE.debugDataAttachment) return;
        
        String entityType = net.minecraft.entity.EntityType.getId(mob.getType()).toString();
        LOGGER.debug("[DATA-PERSISTENCE] Mob: {} | Operation: {} | Success: {}", 
            entityType, operation, success);
    }
    
    // Configuration
    public static void logConfigLoad(String configFile, boolean success, String details) {
        if (!isDebugEnabled()) return;
        
        LOGGER.info("[CONFIG-LOAD] File: {} | Success: {} | Details: {}", 
            configFile, success, details);
    }
    
    // Entity Analysis
    public static void logEntityAnalysis(MobEntity mob) {
        if (!isDebugEnabled() || getDebugLevel() != DebugLevel.VERBOSE) return;
        
        String entityType = net.minecraft.entity.EntityType.getId(mob.getType()).toString();
        LOGGER.debug("[ENTITY-ANALYSIS] Type: {} | Health: {}/{} | Position: {}", 
            entityType, 
            mob.getHealth(), 
            mob.getMaxHealth(),
            mob.getBlockPos());
    }
    
    // Performance Monitoring
    public static void logPerformanceMetric(String operation, long timeMs, String details) {
        if (!isDebugEnabled() || getDebugLevel() != DebugLevel.VERBOSE) return;
        
        LOGGER.debug("[PERFORMANCE] Operation: {} | Time: {}ms | Details: {}", 
            operation, timeMs, details);
    }
    
    // Error Tracking
    public static void logError(String operation, Exception e, String context) {
        if (!isDebugEnabled()) return;
        
        LOGGER.error("[ERROR] Operation: {} | Context: {} | Exception: {}", 
            operation, context, e.getMessage(), e);
    }
    
    // Utility Methods
    public static void logDebugInfo(String category, String message) {
        if (!isDebugEnabled()) return;
        
        LOGGER.debug("[{}] {}", category.toUpperCase(), message);
    }
    
    public static void logVerbose(String category, String message) {
        if (!isDebugEnabled() || getDebugLevel() != DebugLevel.VERBOSE) return;
        
        LOGGER.debug("[{}-VERBOSE] {}", category.toUpperCase(), message);
    }
}