package com.bvhfve.universaltaming.util;

import com.bvhfve.universaltaming.data.TamingData;
import com.bvhfve.universaltaming.debug.DebugLogger;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public class TamingHelper {
    private static final String TAMING_DATA_KEY = "UniversalTamingData";
    
    // Simple in-memory storage for taming data (will be lost on world reload, but functional for testing)
    private static final java.util.Map<java.util.UUID, TamingData> TAMING_DATA_MAP = new java.util.concurrent.ConcurrentHashMap<>();
    
    public static TamingData getTamingData(MobEntity mob) {
        try {
            java.util.UUID mobUuid = mob.getUuid();
            TamingData data = TAMING_DATA_MAP.get(mobUuid);
            
            if (data != null) {
                DebugLogger.logDataPersistence(mob, "READ_TAMING_DATA", true);
                return data;
            }
            
            DebugLogger.logDataPersistence(mob, "READ_TAMING_DATA", false);
            return new TamingData();
        } catch (Exception e) {
            DebugLogger.logError("DATA_ATTACHMENT", e, "Failed to read taming data");
            return new TamingData();
        }
    }
    
    public static void setTamingData(MobEntity mob, TamingData data) {
        try {
            java.util.UUID mobUuid = mob.getUuid();
            TAMING_DATA_MAP.put(mobUuid, data);
            
            DebugLogger.logDataPersistence(mob, "WRITE_TAMING_DATA", true);
        } catch (Exception e) {
            DebugLogger.logError("DATA_ATTACHMENT", e, "Failed to write taming data");
        }
    }
    
    public static boolean isTamed(MobEntity mob) {
        return getTamingData(mob).isTamed();
    }
    
    public static void setTamed(MobEntity mob, PlayerEntity owner) {
        TamingData data = getTamingData(mob);
        data.setTamed(true);
        data.setOwnerId(owner.getUuid());
        setTamingData(mob, data);
    }
    
    public static boolean isOwner(MobEntity mob, PlayerEntity player) {
        TamingData data = getTamingData(mob);
        return data.isTamed() && data.getOwnerId() != null && data.getOwnerId().equals(player.getUuid());
    }
    
    public static boolean isSitting(MobEntity mob) {
        return getTamingData(mob).isSitting();
    }
    
    public static void setSitting(MobEntity mob, boolean sitting) {
        TamingData data = getTamingData(mob);
        data.setSitting(sitting);
        setTamingData(mob, data);
    }
}