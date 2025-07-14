package com.bvhfve.universaltaming.entity;

import com.bvhfve.universaltaming.config.UniversalTamingConfig;
import com.bvhfve.universaltaming.entity.ai.TamedFollowOwnerGoal;
import com.bvhfve.universaltaming.entity.ai.TamedSitGoal;
import com.bvhfve.universaltaming.entity.ai.TamedAttackHostilesGoal;
import com.bvhfve.universaltaming.entity.ai.TamedRevengeGoal;
import com.bvhfve.universaltaming.entity.ai.TamedAttackWithOwnerGoal;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class TamedGenericEntity extends TameableEntity {
    
    private static final TrackedData<String> ORIGINAL_ENTITY_TYPE = DataTracker.registerData(
        TamedGenericEntity.class, TrackedDataHandlerRegistry.STRING
    );
    private static final TrackedData<Boolean> SITTING = DataTracker.registerData(
        TamedGenericEntity.class, TrackedDataHandlerRegistry.BOOLEAN
    );
    private static final TrackedData<Boolean> IS_HOSTILE = DataTracker.registerData(
        TamedGenericEntity.class, TrackedDataHandlerRegistry.BOOLEAN
    );
    private static final TrackedData<Boolean> IS_PASSIVE = DataTracker.registerData(
        TamedGenericEntity.class, TrackedDataHandlerRegistry.BOOLEAN
    );
    private static final TrackedData<Boolean> IS_UNDERWATER = DataTracker.registerData(
        TamedGenericEntity.class, TrackedDataHandlerRegistry.BOOLEAN
    );
    
    private int passiveDropTimer = 0;
    private final int passiveDropInterval;
    private int lastAttackTime = 0;
    
    public TamedGenericEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.setTamed(true, false);
        this.passiveDropInterval = UniversalTamingConfig.INSTANCE.passiveDropIntervalMinutes * 1200; // Convert minutes to ticks
    }
    
    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ORIGINAL_ENTITY_TYPE, "minecraft:pig");
        builder.add(SITTING, false);
        builder.add(IS_HOSTILE, false);
        builder.add(IS_PASSIVE, true);
        builder.add(IS_UNDERWATER, false);
    }
    
    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new TamedSitGoal(this));
        this.goalSelector.add(3, new TamedFollowOwnerGoal(this, 1.0, 10.0f, 2.0f, false));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(6, new LookAroundGoal(this));
        
        // Wolf-like AI goals for defending owner and coordinated attacks
        this.targetSelector.add(1, new TamedRevengeGoal(this));
        this.targetSelector.add(2, new TamedAttackWithOwnerGoal(this));
        this.targetSelector.add(3, new TamedAttackHostilesGoal(this));
    }
    
    public static DefaultAttributeContainer.Builder createTamedAttributes() {
        return MobEntity.createMobAttributes()
            .add(EntityAttributes.MAX_HEALTH, 20.0)
            .add(EntityAttributes.MOVEMENT_SPEED, 0.25)
            .add(EntityAttributes.ATTACK_DAMAGE, 2.0)
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
        
        // Handle passive drops for passive mobs
        if (!this.getWorld().isClient && this.isPassive()) {
            this.passiveDropTimer++;
            if (this.passiveDropTimer >= this.passiveDropInterval) {
                this.dropPassiveItem();
                this.passiveDropTimer = 0;
            }
        }
        
        // Handle underwater breathing for underwater mobs
        if (this.isUnderwater() && this.isSubmergedIn(net.minecraft.registry.tag.FluidTags.WATER)) {
            this.setAir(this.getMaxAir());
        }
    }
    
    private void dropPassiveItem() {
        String[] drops = UniversalTamingConfig.INSTANCE.getDropsForMob(
            Identifier.tryParse(this.getOriginalEntityType())
        );
        
        if (drops.length > 0) {
            String dropId = drops[this.random.nextInt(drops.length)];
            Identifier itemId = Identifier.tryParse(dropId);
            if (itemId != null) {
                ItemStack stack = new ItemStack(net.minecraft.registry.Registries.ITEM.get(itemId));
                if (this.getWorld() instanceof ServerWorld serverWorld) {
                    this.dropStack(serverWorld, stack);
                }
            }
        }
    }
    
    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        
        // Check if player is owner
        if (this.isOwner(player)) {
            // Handle sit/stand toggle (except for foxes)
            if (!this.getOriginalEntityType().equals("minecraft:fox")) {
                if (itemStack.isEmpty() || !itemStack.isOf(Items.GOLDEN_SWORD)) {
                    this.setSitting(!this.isSitting());
                    return ActionResult.SUCCESS;
                }
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
        
        // Wolf-like behavior: if damage is successful, trigger revenge
        boolean damaged = super.damage(world, source, amount);
        if (damaged && this.isTamed() && source.getAttacker() instanceof LivingEntity attacker) {
            // Update our last attack time to trigger revenge goals
            this.setLastAttackTime(this.age);
            
            // Set the attacker as our target if we're hostile and not sitting
            if (this.isHostile() && !this.isSitting()) {
                // Don't target other tamed mobs
                if (!(attacker instanceof TameableEntity tameableAttacker && tameableAttacker.isTamed()) &&
                    !(attacker instanceof TamedGenericEntity)) {
                    this.setTarget(attacker);
                }
            }
        }
        
        return damaged;
    }
    
    // Getters and setters for tracked data
    public String getOriginalEntityType() {
        return this.dataTracker.get(ORIGINAL_ENTITY_TYPE);
    }
    
    public void setOriginalEntityType(String entityType) {
        this.dataTracker.set(ORIGINAL_ENTITY_TYPE, entityType);
    }
    
    public boolean isSitting() {
        return this.dataTracker.get(SITTING);
    }
    
    public void setSitting(boolean sitting) {
        this.dataTracker.set(SITTING, sitting);
    }
    
    public boolean isHostile() {
        return this.dataTracker.get(IS_HOSTILE);
    }
    
    public void setHostile(boolean hostile) {
        this.dataTracker.set(IS_HOSTILE, hostile);
    }
    
    public boolean isPassive() {
        return this.dataTracker.get(IS_PASSIVE);
    }
    
    public void setPassive(boolean passive) {
        this.dataTracker.set(IS_PASSIVE, passive);
    }
    
    public boolean isUnderwater() {
        return this.dataTracker.get(IS_UNDERWATER);
    }
    
    public void setUnderwater(boolean underwater) {
        this.dataTracker.set(IS_UNDERWATER, underwater);
    }
    
    // Wolf-like attack time tracking for coordinated attacks
    public int getLastAttackTime() {
        return this.lastAttackTime;
    }
    
    public void setLastAttackTime(int lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }
    
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putString("OriginalEntityType", this.getOriginalEntityType());
        nbt.putBoolean("Sitting", this.isSitting());
        nbt.putBoolean("IsHostile", this.isHostile());
        nbt.putBoolean("IsPassive", this.isPassive());
        nbt.putBoolean("IsUnderwater", this.isUnderwater());
        nbt.putInt("PassiveDropTimer", this.passiveDropTimer);
        nbt.putInt("LastAttackTime", this.lastAttackTime);
    }
    
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.setOriginalEntityType(nbt.getString("OriginalEntityType", "minecraft:pig"));
        this.setSitting(nbt.getBoolean("Sitting", false));
        this.setHostile(nbt.getBoolean("IsHostile", false));
        this.setPassive(nbt.getBoolean("IsPassive", true));
        this.setUnderwater(nbt.getBoolean("IsUnderwater", false));
        this.passiveDropTimer = nbt.getInt("PassiveDropTimer", 0);
        this.lastAttackTime = nbt.getInt("LastAttackTime", 0);
    }
    
    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false; // Tamed entities don't breed
    }
    
    @Nullable
    public TamedGenericEntity createChild(ServerWorld world, AnimalEntity entity) {
        return null; // Tamed entities don't breed
    }
    
    @Nullable
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null; // Tamed entities don't breed
    }
}