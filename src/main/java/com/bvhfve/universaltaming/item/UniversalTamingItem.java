package com.bvhfve.universaltaming.item;

import com.bvhfve.universaltaming.Universaltaming;
import com.bvhfve.universaltaming.entity.TamedGenericEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/**
 * Universal Taming Item - Perfect Visual Preservation System
 * 
 * Core Philosophy: "A tamed cow must look like a vanilla cow"
 * 
 * This item enables taming of any supported entity while preserving their exact original appearance.
 */
public class UniversalTamingItem extends Item {
    
    public UniversalTamingItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        World world = user.getWorld();
        
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        
        // Check if entity is already tamed
        if (entity instanceof TameableEntity tameable && tameable.isTamed()) {
            user.sendMessage(Text.literal("This entity is already tamed!"), true);
            return ActionResult.FAIL;
        }
        
        // Check if entity is already our tamed entity
        if (entity instanceof TamedGenericEntity) {
            user.sendMessage(Text.literal("This entity is already tamed!"), true);
            return ActionResult.FAIL;
        }
        
        // Get the entity type for perfect visual preservation
        String entityTypeId = net.minecraft.registry.Registries.ENTITY_TYPE.getId(entity.getType()).toString();
        
        // Check if this entity type is supported
        if (!Universaltaming.isTamingEnabled(entityTypeId)) {
            user.sendMessage(Text.literal("This entity type cannot be tamed yet!"), true);
            return ActionResult.FAIL;
        }
        
        // Create tamed version with perfect visual preservation
        TamedGenericEntity tamedEntity = new TamedGenericEntity(Universaltaming.TAMED_GENERIC, world, entityTypeId);
        
        // Copy position and properties
        tamedEntity.setPosition(entity.getX(), entity.getY(), entity.getZ());
        tamedEntity.setYaw(entity.getYaw());
        tamedEntity.setPitch(entity.getPitch());
        
        // Set owner for taming
        tamedEntity.setOwner(user);
        
        // Copy health if possible
        tamedEntity.setHealth(entity.getHealth());
        
        // Remove original entity and spawn tamed version
        entity.discard();
        world.spawnEntity(tamedEntity);
        
        // Success message emphasizing visual preservation
        user.sendMessage(Text.literal("Tamed! The entity maintains its original appearance."), true);
        
        // Consume item if not in creative mode
        if (!user.getAbilities().creativeMode) {
            stack.decrement(1);
        }
        
        return ActionResult.SUCCESS;
    }
}