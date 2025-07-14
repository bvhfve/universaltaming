package com.bvhfve.universaltaming.entity.ai;

import com.bvhfve.universaltaming.entity.TamedGenericEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.TameableEntity;

/**
 * Wolf-like revenge goal that makes tamed mobs attack entities that hurt their owner.
 * Based on WolfEntity.WolfRevengeGoal from vanilla Minecraft.
 */
public class TamedRevengeGoal extends RevengeGoal {
    private final TamedGenericEntity tameable;

    public TamedRevengeGoal(TamedGenericEntity tameable) {
        super(tameable);
        this.tameable = tameable;
    }

    @Override
    public boolean canStart() {
        // Only start if the mob is tamed and not sitting
        if (!this.tameable.isTamed() || this.tameable.isSitting()) {
            return false;
        }

        LivingEntity owner = this.tameable.getOwner();
        if (owner == null) {
            return false;
        }

        // Check if owner was recently hurt
        LivingEntity attacker = owner.getAttacker();
        if (attacker != null && owner.getLastAttackTime() != this.tameable.getLastAttackTime()) {
            // Don't attack other tamed mobs
            if (attacker instanceof TameableEntity tameableAttacker && tameableAttacker.isTamed()) {
                return false;
            }
            if (attacker instanceof TamedGenericEntity) {
                return false;
            }
            
            // Set the attacker as our target
            this.setMobEntityTarget(this.tameable, attacker);
            return super.canStart();
        }

        return super.canStart();
    }

    @Override
    public void start() {
        super.start();
        // Update our last attack time to match owner's
        LivingEntity owner = this.tameable.getOwner();
        if (owner != null) {
            this.tameable.setLastAttackTime(owner.getLastAttackTime());
        }
    }

    @Override
    protected void setMobEntityTarget(MobEntity mob, LivingEntity target) {
        // Only set target if it's not a tamed mob
        if (target instanceof TameableEntity tameableTarget && tameableTarget.isTamed()) {
            return;
        }
        if (target instanceof TamedGenericEntity) {
            return;
        }
        
        super.setMobEntityTarget(mob, target);
    }
}