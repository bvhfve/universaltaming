package com.bvhfve.universaltaming.mixin;

import com.bvhfve.universaltaming.util.TamingHelper;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public class MobEntityMixin {

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void onInteractMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        MobEntity mob = (MobEntity) (Object) this;
        if (TamingHelper.isTamed(mob) && TamingHelper.isOwner(mob, player)) {
            if (player.getStackInHand(hand).isEmpty() || !player.getStackInHand(hand).isOf(Items.GOLDEN_SWORD)) {
                String entityType = net.minecraft.entity.EntityType.getId(mob.getType()).toString();
                if (!entityType.equals("minecraft:fox")) {
                    boolean wasSitting = TamingHelper.isSitting(mob);
                    TamingHelper.setSitting(mob, !wasSitting);
                    cir.setReturnValue(ActionResult.SUCCESS);
                }
            }
        }
    }
}