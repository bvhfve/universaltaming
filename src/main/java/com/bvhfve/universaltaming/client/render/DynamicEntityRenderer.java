package com.bvhfve.universaltaming.client.render;

import com.bvhfve.universaltaming.entity.TamedGenericEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.render.entity.state.*;
import net.minecraft.util.Identifier;

/**
 * Dynamic Entity Renderer - Perfect Visual Preservation System
 * 
 * Core Philosophy: "A tamed cow must look like a vanilla cow"
 * 
 * This renderer dynamically selects the correct vanilla model and texture
 * based on the original entity type, ensuring perfect visual preservation.
 */
public class DynamicEntityRenderer {
    
    /**
     * Creates the appropriate renderer for a given entity type
     * This ensures each tamed entity uses its correct vanilla model and texture
     */
    public static LivingEntityRenderer<TamedGenericEntity, ?, ?> createRenderer(
            String entityType, EntityRendererFactory.Context context) {
        
        return switch (entityType) {
            case "minecraft:cow" -> new CowTamedRenderer(context);
            case "minecraft:pig" -> new PigTamedRenderer(context);
            case "minecraft:sheep" -> new SheepTamedRenderer(context);
            case "minecraft:chicken" -> new ChickenTamedRenderer(context);
            case "minecraft:wolf" -> new WolfTamedRenderer(context);
            case "minecraft:cat" -> new CatTamedRenderer(context);
            case "minecraft:zombie" -> new ZombieTamedRenderer(context);
            case "minecraft:skeleton" -> new SkeletonTamedRenderer(context);
            case "minecraft:creeper" -> new CreeperTamedRenderer(context);
            case "minecraft:spider" -> new SpiderTamedRenderer(context);
            case "minecraft:enderman" -> new EndermanTamedRenderer(context);
            default -> new ZombieTamedRenderer(context); // Fallback
        };
    }
    
    // Cow Renderer - Perfect Visual Preservation
    public static class CowTamedRenderer extends LivingEntityRenderer<TamedGenericEntity, CowEntityRenderState, CowEntityModel> {
        private static final Identifier TEXTURE = Identifier.of("minecraft", "textures/entity/cow/cow.png");
        
        public CowTamedRenderer(EntityRendererFactory.Context context) {
            super(context, new CowEntityModel(context.getPart(EntityModelLayers.COW)), 0.7f);
        }
        
        @Override
        public Identifier getTexture(CowEntityRenderState state) {
            return TEXTURE;
        }
        
        @Override
        public CowEntityRenderState createRenderState() {
            return new CowEntityRenderState();
        }
    }
    
    // Pig Renderer - Perfect Visual Preservation
    public static class PigTamedRenderer extends LivingEntityRenderer<TamedGenericEntity, PigEntityRenderState, PigEntityModel> {
        private static final Identifier TEXTURE = Identifier.of("minecraft", "textures/entity/pig/pig.png");
        
        public PigTamedRenderer(EntityRendererFactory.Context context) {
            super(context, new PigEntityModel(context.getPart(EntityModelLayers.PIG)), 0.7f);
        }
        
        @Override
        public Identifier getTexture(PigEntityRenderState state) {
            return TEXTURE;
        }
        
        @Override
        public PigEntityRenderState createRenderState() {
            return new PigEntityRenderState();
        }
    }
    
    // Sheep Renderer - Perfect Visual Preservation
    public static class SheepTamedRenderer extends LivingEntityRenderer<TamedGenericEntity, SheepEntityRenderState, SheepEntityModel> {
        private static final Identifier TEXTURE = Identifier.of("minecraft", "textures/entity/sheep/sheep.png");
        
        public SheepTamedRenderer(EntityRendererFactory.Context context) {
            super(context, new SheepEntityModel(context.getPart(EntityModelLayers.SHEEP)), 0.7f);
        }
        
        @Override
        public Identifier getTexture(SheepEntityRenderState state) {
            return TEXTURE;
        }
        
        @Override
        public SheepEntityRenderState createRenderState() {
            return new SheepEntityRenderState();
        }
    }
    
    // Chicken Renderer - Perfect Visual Preservation
    public static class ChickenTamedRenderer extends LivingEntityRenderer<TamedGenericEntity, ChickenEntityRenderState, ChickenEntityModel> {
        private static final Identifier TEXTURE = Identifier.of("minecraft", "textures/entity/chicken.png");
        
        public ChickenTamedRenderer(EntityRendererFactory.Context context) {
            super(context, new ChickenEntityModel(context.getPart(EntityModelLayers.CHICKEN)), 0.3f);
        }
        
        @Override
        public Identifier getTexture(ChickenEntityRenderState state) {
            return TEXTURE;
        }
        
        @Override
        public ChickenEntityRenderState createRenderState() {
            return new ChickenEntityRenderState();
        }
    }
    
    // Wolf Renderer - Perfect Visual Preservation
    public static class WolfTamedRenderer extends LivingEntityRenderer<TamedGenericEntity, WolfEntityRenderState, WolfEntityModel> {
        private static final Identifier TEXTURE = Identifier.of("minecraft", "textures/entity/wolf/wolf.png");
        
        public WolfTamedRenderer(EntityRendererFactory.Context context) {
            super(context, new WolfEntityModel(context.getPart(EntityModelLayers.WOLF)), 0.5f);
        }
        
        @Override
        public Identifier getTexture(WolfEntityRenderState state) {
            return TEXTURE;
        }
        
        @Override
        public WolfEntityRenderState createRenderState() {
            return new WolfEntityRenderState();
        }
    }
    
    // Cat Renderer - Perfect Visual Preservation
    public static class CatTamedRenderer extends LivingEntityRenderer<TamedGenericEntity, CatEntityRenderState, CatEntityModel> {
        private static final Identifier TEXTURE = Identifier.of("minecraft", "textures/entity/cat/tabby.png");
        
        public CatTamedRenderer(EntityRendererFactory.Context context) {
            super(context, new CatEntityModel(context.getPart(EntityModelLayers.CAT)), 0.4f);
        }
        
        @Override
        public Identifier getTexture(CatEntityRenderState state) {
            return TEXTURE;
        }
        
        @Override
        public CatEntityRenderState createRenderState() {
            return new CatEntityRenderState();
        }
    }
    
    // Zombie Renderer - Perfect Visual Preservation
    public static class ZombieTamedRenderer extends LivingEntityRenderer<TamedGenericEntity, ZombieEntityRenderState, ZombieEntityModel<ZombieEntityRenderState>> {
        private static final Identifier TEXTURE = Identifier.of("minecraft", "textures/entity/zombie/zombie.png");
        
        public ZombieTamedRenderer(EntityRendererFactory.Context context) {
            super(context, new ZombieEntityModel<>(context.getPart(EntityModelLayers.ZOMBIE)), 0.5f);
        }
        
        @Override
        public Identifier getTexture(ZombieEntityRenderState state) {
            return TEXTURE;
        }
        
        @Override
        public ZombieEntityRenderState createRenderState() {
            return new ZombieEntityRenderState();
        }
    }
    
    // Skeleton Renderer - Perfect Visual Preservation
    public static class SkeletonTamedRenderer extends LivingEntityRenderer<TamedGenericEntity, SkeletonEntityRenderState, SkeletonEntityModel<SkeletonEntityRenderState>> {
        private static final Identifier TEXTURE = Identifier.of("minecraft", "textures/entity/skeleton/skeleton.png");
        
        public SkeletonTamedRenderer(EntityRendererFactory.Context context) {
            super(context, new SkeletonEntityModel<>(context.getPart(EntityModelLayers.SKELETON)), 0.5f);
        }
        
        @Override
        public Identifier getTexture(SkeletonEntityRenderState state) {
            return TEXTURE;
        }
        
        @Override
        public SkeletonEntityRenderState createRenderState() {
            return new SkeletonEntityRenderState();
        }
    }
    
    // Creeper Renderer - Perfect Visual Preservation
    public static class CreeperTamedRenderer extends LivingEntityRenderer<TamedGenericEntity, CreeperEntityRenderState, CreeperEntityModel> {
        private static final Identifier TEXTURE = Identifier.of("minecraft", "textures/entity/creeper/creeper.png");
        
        public CreeperTamedRenderer(EntityRendererFactory.Context context) {
            super(context, new CreeperEntityModel(context.getPart(EntityModelLayers.CREEPER)), 0.5f);
        }
        
        @Override
        public Identifier getTexture(CreeperEntityRenderState state) {
            return TEXTURE;
        }
        
        @Override
        public CreeperEntityRenderState createRenderState() {
            return new CreeperEntityRenderState();
        }
    }
    
    // Spider Renderer - Perfect Visual Preservation
    public static class SpiderTamedRenderer extends LivingEntityRenderer<TamedGenericEntity, LivingEntityRenderState, SpiderEntityModel> {
        private static final Identifier TEXTURE = Identifier.of("minecraft", "textures/entity/spider/spider.png");
        
        public SpiderTamedRenderer(EntityRendererFactory.Context context) {
            super(context, new SpiderEntityModel(context.getPart(EntityModelLayers.SPIDER)), 1.0f);
        }
        
        @Override
        public Identifier getTexture(LivingEntityRenderState state) {
            return TEXTURE;
        }
        
        @Override
        public LivingEntityRenderState createRenderState() {
            return new LivingEntityRenderState();
        }
    }
    
    // Enderman Renderer - Perfect Visual Preservation
    public static class EndermanTamedRenderer extends LivingEntityRenderer<TamedGenericEntity, EndermanEntityRenderState, EndermanEntityModel<EndermanEntityRenderState>> {
        private static final Identifier TEXTURE = Identifier.of("minecraft", "textures/entity/enderman/enderman.png");
        
        public EndermanTamedRenderer(EntityRendererFactory.Context context) {
            super(context, new EndermanEntityModel<>(context.getPart(EntityModelLayers.ENDERMAN)), 0.5f);
        }
        
        @Override
        public Identifier getTexture(EndermanEntityRenderState state) {
            return TEXTURE;
        }
        
        @Override
        public EndermanEntityRenderState createRenderState() {
            return new EndermanEntityRenderState();
        }
    }
}