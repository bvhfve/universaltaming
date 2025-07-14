package com.bvhfve.universaltaming.client;

import com.bvhfve.universaltaming.client.render.TamedCreeperEntityRenderer;
import com.bvhfve.universaltaming.client.render.TamedGenericEntityRenderer;
import com.bvhfve.universaltaming.datagen.DynamicEntityGenerator;
import com.bvhfve.universaltaming.init.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class UniversaltamingClient implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        // Register entity renderers using custom renderers that use vanilla models/textures
        EntityRendererRegistry.register(ModEntities.TAMED_GENERIC, TamedGenericEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.TAMED_CREEPER, TamedCreeperEntityRenderer::new);
        
        // Register renderers for all dynamically generated tamed entities
        registerDynamicEntityRenderers();
    }
    
    private void registerDynamicEntityRenderers() {
        // Register the existing TamedGenericEntityRenderer for all generated tamed entity types
        DynamicEntityGenerator.getAllTamedEntityTypes().forEach((vanillaMobId, entityType) -> {
            @SuppressWarnings("unchecked")
            net.minecraft.entity.EntityType<com.bvhfve.universaltaming.entity.TamedGenericEntity> castedType = 
                (net.minecraft.entity.EntityType<com.bvhfve.universaltaming.entity.TamedGenericEntity>) entityType;
            
            EntityRendererRegistry.register(castedType, TamedGenericEntityRenderer::new);
        });
    }
}