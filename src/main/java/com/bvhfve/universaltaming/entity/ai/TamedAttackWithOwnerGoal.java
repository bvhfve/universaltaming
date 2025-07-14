package com.bvhfve.universaltaming.entity.ai;

import com.bvhfve.universaltaming.entity.TamedGenericEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.passive.TameableEntity;

/**
 * Wolf-like attack goal that makes tamed mobs attack what their owner is attacking.
 * Based on AttackWithOwnerGoal from vanilla Minecraft wolves.
 */
public class TamedAttackWithOwnerGoal extends TrackTargetGoal {
    private final TamedGenericEntity tameable;
    private LivingEntity attacking;
    private int lastAttackTime;

    public TamedAttackWithOwnerGoal(TamedGenericEntity tameable) {
        super(tameable, false);
        this.tameable = tameable;
    }

    @Override
    public boolean canStart() {
        // Only start if the mob is tamed, not sitting, and is hostile type
        if (!this.tameable.isTamed() || this.tameable.isSitting() || !this.tameable.isHostile()) {
            return false;
        }

        LivingEntity owner = this.tameable.getOwner();
        if (owner == null) {
            return false;
        }

        // Check if owner is attacking something
        this.attacking = owner.getAttacking();
        int ownerLastAttackTime = owner.getLastAttackTime();
        
        if (this.attacking != null && ownerLastAttackTime != this.lastAttackTime) {
            this.lastAttackTime = ownerLastAttackTime;
            
            // Don't attack other tamed mobs
            if (this.attacking instanceof TameableEntity tameableTarget && tameableTarget.isTamed()) {
                return false;
            }
            if (this.attacking instanceof TamedGenericEntity) {
                return false;
            }
            
            return this.canTrack(this.attacking, TargetPredicate.DEFAULT);
        }

        return false;
    }

    @Override
    public void start() {
        this.mob.setTarget(this.attacking);
        LivingEntity owner = this.tameable.getOwner();
        if (owner != null) {
            this.tameable.setLastAttackTime(owner.getLastAttackTime());
        }
        super.start();
    }

    @Override
    protected boolean canTrack(LivingEntity target, TargetPredicate targetPredicate) {
        // Don't attack other tamed mobs
        if (target instanceof TameableEntity tameableTarget && tameableTarget.isTamed()) {
            return false;
        }
        if (target instanceof TamedGenericEntity) {
            return false;
        }
        
        return super.canTrack(target, targetPredicate);
    }
}