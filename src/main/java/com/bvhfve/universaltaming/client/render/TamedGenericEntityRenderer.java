package com.bvhfve.universaltaming.client.render;

import com.bvhfve.universaltaming.entity.TamedGenericEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.render.entity.state.*;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class TamedGenericEntityRenderer extends MobEntityRenderer<TamedGenericEntity, ZombieEntityRenderState, ZombieEntityModel<ZombieEntityRenderState>> {
    
    private static final Identifier DEFAULT_TEXTURE = Identifier.of("minecraft", "textures/entity/zombie/zombie.png");
    private final EntityRendererFactory.Context context;
    
    // Cache for textures based on original entity type
    private static final Map<String, Identifier> TEXTURE_CACHE = new HashMap<>();
    
    public TamedGenericEntityRenderer(EntityRendererFactory.Context context) {
        // Use zombie model with proper ZombieEntityRenderState
        super(context, new ZombieEntityModel<>(context.getPart(EntityModelLayers.ZOMBIE)), 0.5f);
        this.context = context;
    }
    
    @Override
    public Identifier getTexture(ZombieEntityRenderState state) {
        if (state instanceof TamedEntityRenderState tamedState) {
            String originalType = tamedState.originalEntityType;
            if (originalType != null && !originalType.isEmpty()) {
                return getTextureForEntityType(originalType);
            }
        }
        return DEFAULT_TEXTURE;
    }
    
    @Override
    public ZombieEntityRenderState createRenderState() {
        return new TamedEntityRenderState();
    }
    
    @Override
    public void updateRenderState(TamedGenericEntity entity, ZombieEntityRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        if (state instanceof TamedEntityRenderState tamedState) {
            tamedState.originalEntityType = entity.getOriginalEntityType();
        }
    }
    
    private Identifier getTextureForEntityType(String entityType) {
        return TEXTURE_CACHE.computeIfAbsent(entityType, this::computeTextureForEntityType);
    }
    
    private Identifier computeTextureForEntityType(String entityType) {
        // Map entity types to their textures
        switch (entityType) {
            case "minecraft:cow" -> {
                return Identifier.of("minecraft", "textures/entity/cow/cow.png");
            }
            case "minecraft:pig" -> {
                return Identifier.of("minecraft", "textures/entity/pig/pig.png");
            }
            case "minecraft:sheep" -> {
                return Identifier.of("minecraft", "textures/entity/sheep/sheep.png");
            }
            case "minecraft:chicken" -> {
                return Identifier.of("minecraft", "textures/entity/chicken.png");
            }
            case "minecraft:rabbit" -> {
                return Identifier.of("minecraft", "textures/entity/rabbit/brown.png");
            }
            case "minecraft:horse" -> {
                return Identifier.of("minecraft", "textures/entity/horse/horse_brown.png");
            }
            case "minecraft:wolf" -> {
                return Identifier.of("minecraft", "textures/entity/wolf/wolf.png");
            }
            case "minecraft:cat" -> {
                return Identifier.of("minecraft", "textures/entity/cat/tabby.png");
            }
            case "minecraft:fox" -> {
                return Identifier.of("minecraft", "textures/entity/fox/fox.png");
            }
            case "minecraft:zombie" -> {
                return Identifier.of("minecraft", "textures/entity/zombie/zombie.png");
            }
            case "minecraft:skeleton" -> {
                return Identifier.of("minecraft", "textures/entity/skeleton/skeleton.png");
            }
            case "minecraft:creeper" -> {
                return Identifier.of("minecraft", "textures/entity/creeper/creeper.png");
            }
            case "minecraft:spider" -> {
                return Identifier.of("minecraft", "textures/entity/spider/spider.png");
            }
            case "minecraft:enderman" -> {
                return Identifier.of("minecraft", "textures/entity/enderman/enderman.png");
            }
            case "minecraft:villager" -> {
                return Identifier.of("minecraft", "textures/entity/villager/villager.png");
            }
            case "minecraft:iron_golem" -> {
                return Identifier.of("minecraft", "textures/entity/iron_golem/iron_golem.png");
            }
            case "minecraft:bee" -> {
                return Identifier.of("minecraft", "textures/entity/bee/bee.png");
            }
            case "minecraft:panda" -> {
                return Identifier.of("minecraft", "textures/entity/panda/panda.png");
            }
            case "minecraft:polar_bear" -> {
                return Identifier.of("minecraft", "textures/entity/bear/polarbear.png");
            }
            // Add more mappings as needed
            default -> {
                // Try to infer texture path from entity type
                Identifier entityId = Identifier.tryParse(entityType);
                if (entityId != null) {
                    String path = entityId.getPath();
                    return Identifier.of("minecraft", "textures/entity/" + path + "/" + path + ".png");
                }
                return DEFAULT_TEXTURE;
            }
        }
    }
    
    // Custom render state to store original entity type
    public static class TamedEntityRenderState extends ZombieEntityRenderState {
        public String originalEntityType = "";
    }
}