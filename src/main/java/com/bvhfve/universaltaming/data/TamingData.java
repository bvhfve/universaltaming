package com.bvhfve.universaltaming.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class TamingData {
    private boolean isTamed = false;
    private UUID ownerId = null;
    private boolean isSitting = false;
    private int passiveDropTimer = 0;
    private double healthMultiplier = 2.0;
    
    public TamingData() {}
    
    public TamingData(boolean isTamed, UUID ownerId) {
        this.isTamed = isTamed;
        this.ownerId = ownerId;
    }
    
    // Getters and setters
    public boolean isTamed() { return isTamed; }
    public void setTamed(boolean tamed) { this.isTamed = tamed; }
    
    public UUID getOwnerId() { return ownerId; }
    public void setOwnerId(UUID ownerId) { this.ownerId = ownerId; }
    
    public boolean isSitting() { return isSitting; }
    public void setSitting(boolean sitting) { this.isSitting = sitting; }
    
    public int getPassiveDropTimer() { return passiveDropTimer; }
    public void setPassiveDropTimer(int timer) { this.passiveDropTimer = timer; }
    
    public double getHealthMultiplier() { return healthMultiplier; }
    public void setHealthMultiplier(double multiplier) { this.healthMultiplier = multiplier; }
    
    // NBT serialization
    public NbtCompound writeToNbt(NbtCompound nbt) {
        nbt.putBoolean("IsTamed", isTamed);
        if (ownerId != null) {
            nbt.putString("OwnerId", ownerId.toString());
        }
        nbt.putBoolean("IsSitting", isSitting);
        nbt.putInt("PassiveDropTimer", passiveDropTimer);
        nbt.putDouble("HealthMultiplier", healthMultiplier);
        return nbt;
    }
    
    public void readFromNbt(NbtCompound nbt) {
        isTamed = nbt.getBoolean("IsTamed", false);
        if (nbt.contains("OwnerId")) {
            try {
                ownerId = UUID.fromString(nbt.getString("OwnerId", ""));
            } catch (IllegalArgumentException e) {
                ownerId = null;
            }
        }
        isSitting = nbt.getBoolean("IsSitting", false);
        passiveDropTimer = nbt.getInt("PassiveDropTimer", 0);
        healthMultiplier = nbt.getDouble("HealthMultiplier", 2.0);
    }
}