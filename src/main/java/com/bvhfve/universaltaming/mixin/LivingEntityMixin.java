package com.bvhfve.universaltaming.mixin;

import com.bvhfve.universaltaming.util.TamingHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (!(livingEntity instanceof MobEntity mob)) {
            return;
        }

        if (TamingHelper.isTamed(mob)) {
            if (source.getAttacker() instanceof PlayerEntity player && TamingHelper.isOwner(mob, player)) {
                if (!player.getMainHandStack().isOf(Items.GOLDEN_SWORD) && !player.getOffHandStack().isOf(Items.GOLDEN_SWORD)) {
                    cir.setReturnValue(false); // Cancel damage
                }
            } else if (source.getAttacker() instanceof MobEntity attacker && TamingHelper.isTamed(attacker)) {
                cir.setReturnValue(false); // Cancel friendly fire
            }
        }
    }
}