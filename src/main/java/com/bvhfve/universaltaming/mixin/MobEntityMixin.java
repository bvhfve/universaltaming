package com.bvhfve.universaltaming.mixin;

import com.bvhfve.universaltaming.debug.DebugLogger;
import com.bvhfve.universaltaming.util.TamingHelper;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public class MobEntityMixin {
    
    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void onInteractMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        MobEntity mob = (MobEntity) (Object) this;
        
        DebugLogger.logMixinInteraction(mob, "interactMob", "Player interaction detected");
        
        // Handle tamed mob interactions
        if (TamingHelper.isTamed(mob) && TamingHelper.isOwner(mob, player)) {
            DebugLogger.logMixinInteraction(mob, "interactMob", "Tamed mob owned by player");
            
            // Handle sit/stand toggle
            if (player.getStackInHand(hand).isEmpty() || !player.getStackInHand(hand).isOf(Items.GOLDEN_SWORD)) {
                // Don't allow foxes to sit (as specified)
                String entityType = net.minecraft.entity.EntityType.getId(mob.getType()).toString();
                if (!entityType.equals("minecraft:fox")) {
                    boolean wasSitting = TamingHelper.isSitting(mob);
                    TamingHelper.setSitting(mob, !wasSitting);
                    DebugLogger.logMixinInteraction(mob, "interactMob", 
                        "Toggled sitting: " + wasSitting + " -> " + !wasSitting);
                    cir.setReturnValue(ActionResult.SUCCESS);
                } else {
                    DebugLogger.logMixinInteraction(mob, "interactMob", "Fox interaction blocked (no sitting)");
                }
            } else {
                DebugLogger.logMixinInteraction(mob, "interactMob", "Golden sword detected - allowing default behavior");
            }
        } else if (TamingHelper.isTamed(mob)) {
            DebugLogger.logMixinInteraction(mob, "interactMob", "Tamed mob not owned by this player");
        }
    }
    
    @Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("HEAD"), cancellable = true)
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        MobEntity mob = (MobEntity) (Object) this;
        
        String damageSourceName = source.getName();
        DebugLogger.logMixinInteraction(mob, "damage", "Damage event: " + damageSourceName + " (" + amount + ")");
        
        // Prevent damage from owner unless using golden sword
        if (TamingHelper.isTamed(mob) && source.getAttacker() instanceof PlayerEntity player) {
            if (TamingHelper.isOwner(mob, player)) {
                // Allow damage only with golden sword
                if (!player.getMainHandStack().isOf(Items.GOLDEN_SWORD) && 
                    !player.getOffHandStack().isOf(Items.GOLDEN_SWORD)) {
                    DebugLogger.logDamageEvent(mob, damageSourceName, true, "Owner damage blocked (no golden sword)");
                    cir.setReturnValue(false);
                } else {
                    DebugLogger.logDamageEvent(mob, damageSourceName, false, "Owner damage allowed (golden sword)");
                }
            } else {
                DebugLogger.logDamageEvent(mob, damageSourceName, false, "Player damage allowed (not owner)");
            }
        }
        
        // Prevent damage from other tamed mobs
        if (TamingHelper.isTamed(mob) && source.getAttacker() instanceof MobEntity attacker) {
            if (TamingHelper.isTamed(attacker)) {
                DebugLogger.logDamageEvent(mob, damageSourceName, true, "Tamed mob friendly fire blocked");
                cir.setReturnValue(false);
            } else {
                DebugLogger.logDamageEvent(mob, damageSourceName, false, "Wild mob damage allowed");
            }
        }
    }
}