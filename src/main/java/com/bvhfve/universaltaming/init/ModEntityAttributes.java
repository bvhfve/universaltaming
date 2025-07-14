package com.bvhfve.universaltaming.init;

import com.bvhfve.universaltaming.Universaltaming;
import com.bvhfve.universaltaming.entity.TamedCreeper;
import com.bvhfve.universaltaming.entity.TamedGenericEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class ModEntityAttributes {
    
    public static void init() {
        // Register default attributes for our custom entities
        FabricDefaultAttributeRegistry.register(ModEntities.TAMED_GENERIC, TamedGenericEntity.createTamedAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.TAMED_CREEPER, TamedCreeper.createTamedCreeperAttributes());
        
        Universaltaming.LOGGER.info("Registered entity attributes");
    }
}