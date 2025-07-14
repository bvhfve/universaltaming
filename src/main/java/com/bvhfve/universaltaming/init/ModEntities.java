package com.bvhfve.universaltaming.init;

import com.bvhfve.universaltaming.Universaltaming;
import com.bvhfve.universaltaming.entity.TamedCreeper;
import com.bvhfve.universaltaming.entity.TamedGenericEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEntities {
    
    // Generic tamed entity for most mobs - using zombie dimensions as default
    public static final EntityType<TamedGenericEntity> TAMED_GENERIC = Registry.register(
        Registries.ENTITY_TYPE,
        Universaltaming.id("tamed_generic"),
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, TamedGenericEntity::new)
            .dimensions(EntityDimensions.changing(0.6f, 1.95f)) // Zombie dimensions
            .trackRangeBlocks(80)
            .trackedUpdateRate(3)
            .build()
    );
    
    // Special tamed creeper with unique behavior - using creeper dimensions
    public static final EntityType<TamedCreeper> TAMED_CREEPER = Registry.register(
        Registries.ENTITY_TYPE,
        Universaltaming.id("tamed_creeper"),
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, TamedCreeper::new)
            .dimensions(EntityDimensions.changing(0.6f, 1.7f)) // Creeper dimensions
            .trackRangeBlocks(80)
            .trackedUpdateRate(3)
            .build()
    );
    
    public static void init() {
        // Entities are now registered during static initialization above
        // This method just ensures the class is loaded and entities are registered
        Universaltaming.LOGGER.info("Registered mod entities: {} and {}", 
            EntityType.getId(TAMED_GENERIC), EntityType.getId(TAMED_CREEPER));
    }
}