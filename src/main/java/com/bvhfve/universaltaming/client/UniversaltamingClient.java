package com.bvhfve.universaltaming.client;

import com.bvhfve.universaltaming.Universaltaming;
import com.bvhfve.universaltaming.client.render.TamedCreeperEntityRenderer;
import com.bvhfve.universaltaming.client.render.TamedGenericEntityRenderer;
import com.bvhfve.universaltaming.datagen.DynamicEntityGenerator;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.entity.EntityType;

import java.util.Map;

public class UniversaltamingClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Register renderers for the main static entity types
        EntityRendererRegistry.register(Universaltaming.TAMED_GENERIC, TamedGenericEntityRenderer::new);
        // EntityRendererRegistry.register(Universaltaming.TAMED_CREEPER, TamedCreeperEntityRenderer::new); // Removed - entity doesn't exist
        
        // Register renderers for all dynamically generated entity types
        registerDynamicEntityRenderers();
    }
    
    private void registerDynamicEntityRenderers() {
        Map<String, EntityType<?>> tamedEntityTypes = DynamicEntityGenerator.getAllTamedEntityTypes();
        
        for (Map.Entry<String, EntityType<?>> entry : tamedEntityTypes.entrySet()) {
            String vanillaMobId = entry.getKey();
            EntityType<?> tamedEntityType = entry.getValue();
            
            // Get the original vanilla entity type to use its renderer
            net.minecraft.util.Identifier vanillaId = net.minecraft.util.Identifier.tryParse(vanillaMobId);
            if (vanillaId != null) {
                EntityType<?> vanillaEntityType = net.minecraft.registry.Registries.ENTITY_TYPE.get(vanillaId);
                
                if (vanillaEntityType != null) {
                    // Register the ORIGINAL vanilla renderer for our tamed entity type
                    // This preserves the exact original appearance (model + texture + animations)
                    @SuppressWarnings("unchecked")
                    EntityType<com.bvhfve.universaltaming.entity.TamedGenericEntity> castedType = 
                        (EntityType<com.bvhfve.universaltaming.entity.TamedGenericEntity>) tamedEntityType;
                    
                    // Use dynamic renderer that preserves perfect visual appearance
                    // Each entity type gets its correct vanilla model and texture
                    EntityRendererRegistry.register(castedType, (context) -> {
                        return com.bvhfve.universaltaming.client.render.DynamicEntityRenderer.createRenderer(vanillaMobId, context);
                    });
                    
                    Universaltaming.LOGGER.debug("Registered dynamic renderer for tamed entity type: {} -> perfect visual preservation", vanillaMobId);
                } else {
                    Universaltaming.LOGGER.warn("Vanilla entity type not found for: {}", vanillaMobId);
                }
            }
        }
        
        Universaltaming.LOGGER.info("Registered compatible renderers for {} entity types - texture preservation enabled", 
            tamedEntityTypes.size());
    }
    
    @SuppressWarnings("unchecked")
    private net.minecraft.client.render.entity.EntityRenderer<com.bvhfve.universaltaming.entity.TamedGenericEntity, ?> getVanillaRenderer(
            EntityType<?> vanillaType, net.minecraft.client.render.entity.EntityRendererFactory.Context context) {
        
        // Create a specialized renderer that uses the original entity's model and texture
        // but works with our TamedGenericEntity
        String entityId = EntityType.getId(vanillaType).toString();
        
        return switch (entityId) {
            case "minecraft:cow" -> new com.bvhfve.universaltaming.client.render.OriginalAppearanceRenderer<>(
                context, entityId, new net.minecraft.client.render.entity.model.CowEntityModel(context.getPart(net.minecraft.client.render.entity.model.EntityModelLayers.COW)));
            case "minecraft:pig" -> new com.bvhfve.universaltaming.client.render.OriginalAppearanceRenderer<>(
                context, entityId, new net.minecraft.client.render.entity.model.PigEntityModel(context.getPart(net.minecraft.client.render.entity.model.EntityModelLayers.PIG)));
            case "minecraft:sheep" -> new com.bvhfve.universaltaming.client.render.OriginalAppearanceRenderer<>(
                context, entityId, new net.minecraft.client.render.entity.model.SheepEntityModel(context.getPart(net.minecraft.client.render.entity.model.EntityModelLayers.SHEEP)));
            case "minecraft:chicken" -> new com.bvhfve.universaltaming.client.render.OriginalAppearanceRenderer<>(
                context, entityId, new net.minecraft.client.render.entity.model.ChickenEntityModel(context.getPart(net.minecraft.client.render.entity.model.EntityModelLayers.CHICKEN)));
            case "minecraft:wolf" -> new com.bvhfve.universaltaming.client.render.OriginalAppearanceRenderer<>(
                context, entityId, new net.minecraft.client.render.entity.model.WolfEntityModel(context.getPart(net.minecraft.client.render.entity.model.EntityModelLayers.WOLF)));
            case "minecraft:cat" -> new com.bvhfve.universaltaming.client.render.OriginalAppearanceRenderer<>(
                context, entityId, new net.minecraft.client.render.entity.model.CatEntityModel(context.getPart(net.minecraft.client.render.entity.model.EntityModelLayers.CAT)));
            case "minecraft:zombie" -> new com.bvhfve.universaltaming.client.render.OriginalAppearanceRenderer<>(
                context, entityId, new net.minecraft.client.render.entity.model.ZombieEntityModel(context.getPart(net.minecraft.client.render.entity.model.EntityModelLayers.ZOMBIE)));
            case "minecraft:skeleton" -> new com.bvhfve.universaltaming.client.render.OriginalAppearanceRenderer<>(
                context, entityId, new net.minecraft.client.render.entity.model.SkeletonEntityModel(context.getPart(net.minecraft.client.render.entity.model.EntityModelLayers.SKELETON)));
            case "minecraft:creeper" -> new com.bvhfve.universaltaming.client.render.OriginalAppearanceRenderer<>(
                context, entityId, new net.minecraft.client.render.entity.model.CreeperEntityModel(context.getPart(net.minecraft.client.render.entity.model.EntityModelLayers.CREEPER)));
            case "minecraft:spider" -> new com.bvhfve.universaltaming.client.render.OriginalAppearanceRenderer<>(
                context, entityId, new net.minecraft.client.render.entity.model.SpiderEntityModel(context.getPart(net.minecraft.client.render.entity.model.EntityModelLayers.SPIDER)));
            case "minecraft:enderman" -> new com.bvhfve.universaltaming.client.render.OriginalAppearanceRenderer<>(
                context, entityId, new net.minecraft.client.render.entity.model.EndermanEntityModel(context.getPart(net.minecraft.client.render.entity.model.EntityModelLayers.ENDERMAN)));
            // Add more as needed
            default -> new TamedGenericEntityRenderer(context); // Fallback
        };
    }
}