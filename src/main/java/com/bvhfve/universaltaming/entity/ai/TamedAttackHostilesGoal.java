package com.bvhfve.universaltaming.entity.ai;

import com.bvhfve.universaltaming.config.UniversalTamingConfig;
import com.bvhfve.universaltaming.entity.TamedGenericEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.TameableEntity;

import java.util.List;

public class TamedAttackHostilesGoal extends TrackTargetGoal {
    private final TamedGenericEntity tameable;
    private LivingEntity attacking;
    private int lastAttackTime;

    public TamedAttackHostilesGoal(TamedGenericEntity tameable) {
        super(tameable, false);
        this.tameable = tameable;
    }

    @Override
    public boolean canStart() {
        // Only hostile tamed mobs should attack
        if (!this.tameable.isHostile() || this.tameable.isSitting()) {
            return false;
        }

        LivingEntity owner = this.tameable.getOwner();
        if (owner == null) {
            return false;
        }

        // Check if owner is being attacked
        this.attacking = owner.getAttacking();
        int ticksSinceLastAttack = owner.getLastAttackTime();
        
        if (this.attacking != null && ticksSinceLastAttack != this.lastAttackTime) {
            this.lastAttackTime = ticksSinceLastAttack;
            return this.canTrack(this.attacking, TargetPredicate.DEFAULT);
        }

        // Look for nearby hostile mobs
        double range = UniversalTamingConfig.INSTANCE.hostileAttackRange;
        
        // Find hostile entities manually since getClosestEntity API changed
        List<LivingEntity> nearbyEntities = this.tameable.getWorld().getEntitiesByClass(
            LivingEntity.class,
            this.tameable.getBoundingBox().expand(range, 4.0, range),
            entity -> entity instanceof HostileEntity && entity != this.tameable
        );
        
        LivingEntity nearestHostile = null;
        double closestDistance = Double.MAX_VALUE;
        
        for (LivingEntity entity : nearbyEntities) {
            double distance = this.tameable.squaredDistanceTo(entity);
            if (distance < closestDistance && this.canTrack(entity, TargetPredicate.DEFAULT)) {
                nearestHostile = entity;
                closestDistance = distance;
            }
        }

        if (nearestHostile != null) {
            this.attacking = nearestHostile;
            return true;
        }

        return false;
    }

    @Override
    public void start() {
        this.mob.setTarget(this.attacking);
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