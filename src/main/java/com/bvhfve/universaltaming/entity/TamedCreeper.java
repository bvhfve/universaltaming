package com.bvhfve.universaltaming.entity;

import com.bvhfve.universaltaming.config.UniversalTamingConfig;
import com.bvhfve.universaltaming.entity.ai.TamedFollowOwnerGoal;
import com.bvhfve.universaltaming.entity.ai.TamedSitGoal;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TamedCreeper extends TameableEntity {
    
    private static final TrackedData<Boolean> SITTING = DataTracker.registerData(
        TamedCreeper.class, TrackedDataHandlerRegistry.BOOLEAN
    );
    
    private int neutralizationCooldown = 0;
    
    public TamedCreeper(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.setTamed(true, false);
    }
    
    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(SITTING, false);
    }
    
    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new TamedSitGoal(this));
        this.goalSelector.add(3, new TamedFollowOwnerGoal(this, 1.0, 10.0f, 2.0f, false));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(6, new LookAroundGoal(this));
    }
    
    public static DefaultAttributeContainer.Builder createTamedCreeperAttributes() {
        return MobEntity.createMobAttributes()
            .add(EntityAttributes.MAX_HEALTH, 40.0) // Double the original 20 health
            .add(EntityAttributes.MOVEMENT_SPEED, 0.25)
            .add(EntityAttributes.FOLLOW_RANGE, 16.0);
    }
    
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, 
                                @Nullable EntityData entityData) {
        return super.initialize(world, difficulty, spawnReason, entityData);
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if (this.neutralizationCooldown > 0) {
            this.neutralizationCooldown--;
        }
        
        // Check for nearby vanilla creepers to neutralize
        if (!this.getWorld().isClient && this.neutralizationCooldown <= 0) {
            this.neutralizeNearbyCreepers();
        }
    }
    
    private void neutralizeNearbyCreepers() {
        double range = UniversalTamingConfig.INSTANCE.creeperNeutralizationRange;
        Box searchBox = this.getBoundingBox().expand(range);
        List<CreeperEntity> nearbyCreepers = this.getWorld().getEntitiesByClass(
            CreeperEntity.class, searchBox, creeper -> !creeper.isRemoved()
        );
        
        for (CreeperEntity creeper : nearbyCreepers) {
            if (creeper.squaredDistanceTo(this) <= range * range) {
                this.neutralizeCreeper(creeper);
                this.neutralizationCooldown = 60; // 3 second cooldown
                break; // Only neutralize one at a time
            }
        }
    }
    
    private void neutralizeCreeper(CreeperEntity creeper) {
        if (!this.getWorld().isClient) {
            // Create fireworks effect
            ServerWorld serverWorld = (ServerWorld) this.getWorld();
            for (int i = 0; i < 20; i++) {
                double offsetX = this.random.nextGaussian() * 0.5;
                double offsetY = this.random.nextGaussian() * 0.5;
                double offsetZ = this.random.nextGaussian() * 0.5;
                serverWorld.spawnParticles(
                    ParticleTypes.FIREWORK,
                    creeper.getX() + offsetX,
                    creeper.getY() + 1.0 + offsetY,
                    creeper.getZ() + offsetZ,
                    1, 0, 0, 0, 0.1
                );
            }
            
            // Play sound
            this.getWorld().playSound(null, creeper.getBlockPos(), SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST, 
                this.getSoundCategory(), 1.0f, 1.0f);
            
            // Remove the creeper
            creeper.discard();
        }
    }
    
    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        
        // Check if player is owner
        if (this.isOwner(player)) {
            // Handle sit/stand toggle
            if (itemStack.isEmpty() || !itemStack.isOf(Items.GOLDEN_SWORD)) {
                this.setSitting(!this.isSitting());
                return ActionResult.SUCCESS;
            }
            
            // Handle golden sword for removal
            if (itemStack.isOf(Items.GOLDEN_SWORD)) {
                if (!this.getWorld().isClient) {
                    this.damage((ServerWorld)this.getWorld(), this.getDamageSources().playerAttack(player), Float.MAX_VALUE);
                }
                return ActionResult.SUCCESS;
            }
        }
        
        return super.interactMob(player, hand);
    }
    
    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        // Prevent damage from owner unless using golden sword
        if (source.getAttacker() instanceof PlayerEntity player && this.isOwner(player)) {
            if (player.getMainHandStack().isOf(Items.GOLDEN_SWORD) || 
                player.getOffHandStack().isOf(Items.GOLDEN_SWORD)) {
                return super.damage(world, source, amount);
            }
            return false;
        }
        
        // Prevent damage from other tamed mobs
        if (source.getAttacker() instanceof TamedGenericEntity || 
            source.getAttacker() instanceof TameableEntity tamedAttacker && tamedAttacker.isTamed()) {
            return false;
        }
        
        return super.damage(world, source, amount);
    }
    
    public boolean isSitting() {
        return this.dataTracker.get(SITTING);
    }
    
    public void setSitting(boolean sitting) {
        this.dataTracker.set(SITTING, sitting);
    }
    
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putBoolean("Sitting", this.isSitting());
        nbt.putInt("NeutralizationCooldown", this.neutralizationCooldown);
    }
    
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.setSitting(nbt.getBoolean("Sitting", false));
        this.neutralizationCooldown = nbt.getInt("NeutralizationCooldown", 0);
    }
    
    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false; // Tamed entities don't breed
    }
    
    @Nullable
    public TamedCreeper createChild(ServerWorld world, AnimalEntity entity) {
        return null; // Tamed entities don't breed
    }
    
    @Nullable
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null; // Tamed entities don't breed
    }
}