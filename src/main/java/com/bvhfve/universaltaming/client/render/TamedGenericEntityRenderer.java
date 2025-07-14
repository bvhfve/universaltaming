package com.bvhfve.universaltaming.client.render;

import com.bvhfve.universaltaming.entity.TamedGenericEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.ZombieEntityModel;
import net.minecraft.client.render.entity.state.ZombieEntityRenderState;
import net.minecraft.util.Identifier;

public class TamedGenericEntityRenderer extends MobEntityRenderer<TamedGenericEntity, ZombieEntityRenderState, ZombieEntityModel<ZombieEntityRenderState>> {
    
    private static final Identifier TEXTURE = Identifier.of("minecraft", "textures/entity/zombie/zombie.png");
    
    public TamedGenericEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new ZombieEntityModel<>(context.getPart(net.minecraft.client.render.entity.model.EntityModelLayers.ZOMBIE)), 0.5f);
    }
    
    @Override
    public Identifier getTexture(ZombieEntityRenderState state) {
        // For now, use the default texture - dynamic texturing will need a different approach
        return TEXTURE;
    }
    
    @Override
    public ZombieEntityRenderState createRenderState() {
        return new ZombieEntityRenderState();
    }
}