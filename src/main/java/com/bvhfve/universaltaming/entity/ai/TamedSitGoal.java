package com.bvhfve.universaltaming.entity.ai;

import com.bvhfve.universaltaming.entity.TamedGenericEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class TamedSitGoal extends Goal {
    private final TameableEntity tameable;

    public TamedSitGoal(TameableEntity tameable) {
        this.tameable = tameable;
        this.setControls(EnumSet.of(Goal.Control.JUMP, Goal.Control.MOVE));
    }

    @Override
    public boolean shouldContinue() {
        if (this.tameable instanceof TamedGenericEntity tamedGeneric) {
            return tamedGeneric.isSitting();
        } else if (this.tameable instanceof com.bvhfve.universaltaming.entity.TamedCreeper tamedCreeper) {
            return tamedCreeper.isSitting();
        }
        return false;
    }

    @Override
    public boolean canStart() {
        if (!this.tameable.isTamed()) {
            return false;
        } else if (this.tameable.isTouchingWater()) {
            return false;
        } else if (!this.tameable.isOnGround()) {
            return false;
        } else {
            // Check if it's our custom entity with sitting capability
            if (this.tameable instanceof TamedGenericEntity tamedGeneric) {
                return tamedGeneric.isSitting();
            } else if (this.tameable instanceof com.bvhfve.universaltaming.entity.TamedCreeper tamedCreeper) {
                return tamedCreeper.isSitting();
            }
            return false;
        }
    }

    @Override
    public void start() {
        this.tameable.getNavigation().stop();
        this.tameable.setTarget(null);
    }
}