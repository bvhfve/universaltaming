package com.bvhfve.universaltaming.client.render;

import com.bvhfve.universaltaming.entity.TamedCreeper;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.CreeperEntityModel;
import net.minecraft.client.render.entity.state.CreeperEntityRenderState;
import net.minecraft.util.Identifier;

public class TamedCreeperEntityRenderer extends MobEntityRenderer<TamedCreeper, CreeperEntityRenderState, CreeperEntityModel> {
    
    private static final Identifier TEXTURE = Identifier.of("minecraft", "textures/entity/creeper/creeper.png");
    
    public TamedCreeperEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new CreeperEntityModel(context.getPart(net.minecraft.client.render.entity.model.EntityModelLayers.CREEPER)), 0.5f);
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