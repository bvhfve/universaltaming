package com.bvhfve.universaltaming.client.render;

import com.bvhfve.universaltaming.entity.TamedGenericEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;

/**
 * A renderer that preserves the original appearance of any entity type
 * while working with our TamedGenericEntity system
 */
public class OriginalAppearanceRenderer<S extends LivingEntityRenderState> extends LivingEntityRenderer<TamedGenericEntity, S, EntityModel<S>> {
    
    private final String originalEntityType;
    
    public OriginalAppearanceRenderer(EntityRendererFactory.Context context, String originalEntityType, EntityModel<S> model) {
        super(context, model, 0.5f);
        this.originalEntityType = originalEntityType;
    }
    
    @Override
    public Identifier getTexture(S state) {
        // Return the texture for the original entity type
        return getTextureForEntityType(this.originalEntityType);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public S createRenderState() {
        // Create appropriate render state based on entity type
        return (S) createRenderStateForEntityType(this.originalEntityType);
    }
    
    @Override
    public void updateRenderState(TamedGenericEntity entity, S state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        // Additional state updates can be added here if needed
    }
    
    private Identifier getTextureForEntityType(String entityType) {
        return switch (entityType) {
            case "minecraft:cow" -> Identifier.of("minecraft", "textures/entity/cow/cow.png");
            case "minecraft:pig" -> Identifier.of("minecraft", "textures/entity/pig/pig.png");
            case "minecraft:sheep" -> Identifier.of("minecraft", "textures/entity/sheep/sheep.png");
            case "minecraft:chicken" -> Identifier.of("minecraft", "textures/entity/chicken.png");
            case "minecraft:wolf" -> Identifier.of("minecraft", "textures/entity/wolf/wolf.png");
            case "minecraft:cat" -> Identifier.of("minecraft", "textures/entity/cat/tabby.png");
            case "minecraft:zombie" -> Identifier.of("minecraft", "textures/entity/zombie/zombie.png");
            case "minecraft:skeleton" -> Identifier.of("minecraft", "textures/entity/skeleton/skeleton.png");
            case "minecraft:creeper" -> Identifier.of("minecraft", "textures/entity/creeper/creeper.png");
            case "minecraft:spider" -> Identifier.of("minecraft", "textures/entity/spider/spider.png");
            case "minecraft:enderman" -> Identifier.of("minecraft", "textures/entity/enderman/enderman.png");
            default -> {
                // Try to infer texture path from entity type
                Identifier entityId = Identifier.tryParse(entityType);
                if (entityId != null) {
                    String path = entityId.getPath();
                    yield Identifier.of("minecraft", "textures/entity/" + path + "/" + path + ".png");
                }
                yield Identifier.of("minecraft", "textures/entity/zombie/zombie.png");
            }
        };
    }
    
    private LivingEntityRenderState createRenderStateForEntityType(String entityType) {
        // Create the appropriate render state for each entity type
        return switch (entityType) {
            case "minecraft:cow" -> new net.minecraft.client.render.entity.state.CowEntityRenderState();
            case "minecraft:pig" -> new net.minecraft.client.render.entity.state.PigEntityRenderState();
            case "minecraft:sheep" -> new net.minecraft.client.render.entity.state.SheepEntityRenderState();
            case "minecraft:chicken" -> new net.minecraft.client.render.entity.state.ChickenEntityRenderState();
            case "minecraft:wolf" -> new net.minecraft.client.render.entity.state.WolfEntityRenderState();
            case "minecraft:cat" -> new net.minecraft.client.render.entity.state.CatEntityRenderState();
            case "minecraft:zombie" -> new net.minecraft.client.render.entity.state.ZombieEntityRenderState();
            case "minecraft:skeleton" -> new net.minecraft.client.render.entity.state.SkeletonEntityRenderState();
            case "minecraft:creeper" -> new net.minecraft.client.render.entity.state.CreeperEntityRenderState();
            case "minecraft:spider" -> new net.minecraft.client.render.entity.state.SpiderEntityRenderState();
            case "minecraft:enderman" -> new net.minecraft.client.render.entity.state.EndermanEntityRenderState();
            default -> new LivingEntityRenderState(); // Generic fallback
        };
    }
}