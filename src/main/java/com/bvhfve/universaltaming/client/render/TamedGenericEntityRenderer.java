package com.bvhfve.universaltaming.client.render;

import com.bvhfve.universaltaming.entity.TamedGenericEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.render.entity.state.*;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class TamedGenericEntityRenderer extends MobEntityRenderer<TamedGenericEntity, WolfEntityRenderState, WolfEntityModel> {
    
    private static final Identifier DEFAULT_TEXTURE = Identifier.of("minecraft", "textures/entity/wolf/wolf.png");
    private final EntityRendererFactory.Context context;
    
    // Cache for textures based on original entity type
    private static final Map<String, Identifier> TEXTURE_CACHE = new HashMap<>();
    
    public TamedGenericEntityRenderer(EntityRendererFactory.Context context) {
        // Use wolf model as temporary fix - looks better than zombie
        super(context, new WolfEntityModel(context.getPart(EntityModelLayers.WOLF)), 0.5f);
        this.context = context;
    }
    
    @Override
    public Identifier getTexture(WolfEntityRenderState state) {
        if (state instanceof TamedEntityRenderState tamedState) {
            String originalType = tamedState.originalEntityType;
            if (originalType != null && !originalType.isEmpty()) {
                return getTextureForEntityType(originalType);
            }
        }
        return DEFAULT_TEXTURE;
    }
    
    @Override
    public WolfEntityRenderState createRenderState() {
        return new TamedEntityRenderState();
    }
    
    @Override
    public void updateRenderState(TamedGenericEntity entity, WolfEntityRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        if (state instanceof TamedEntityRenderState tamedState) {
            tamedState.originalEntityType = entity.getOriginalEntityType();
            
            // Log the entity type for debugging
            if (entity.getOriginalEntityType() != null && !entity.getOriginalEntityType().equals("minecraft:pig")) {
                com.bvhfve.universaltaming.Universaltaming.LOGGER.debug("Rendering tamed entity: {} with original type: {}", 
                    entity.getClass().getSimpleName(), entity.getOriginalEntityType());
            }
        }
    }
    
    private Identifier getTextureForEntityType(String entityType) {
        return TEXTURE_CACHE.computeIfAbsent(entityType, this::computeTextureForEntityType);
    }
    
    private Identifier computeTextureForEntityType(String entityType) {
        // Use the centralized texture mapping for perfect visual preservation
        return com.bvhfve.universaltaming.Universaltaming.getEntityTexture(entityType);
    }
    
    // Custom render state to store original entity type
    public static class TamedEntityRenderState extends WolfEntityRenderState {
        public String originalEntityType = "";
    }
}