package com.bvhfve.universaltaming.client;

import com.bvhfve.universaltaming.Universaltaming;
import com.bvhfve.universaltaming.client.render.TamedCreeperEntityRenderer;
import com.bvhfve.universaltaming.client.render.TamedGenericEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class UniversaltamingClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Universaltaming.TAMED_GENERIC, TamedGenericEntityRenderer::new);
        EntityRendererRegistry.register(Universaltaming.TAMED_CREEPER, TamedCreeperEntityRenderer::new);
    }
}