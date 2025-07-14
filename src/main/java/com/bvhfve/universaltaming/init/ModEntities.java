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
import net.minecraft.registry.RegistryKey;

public class ModEntities {

    public static final EntityType<TamedGenericEntity> TAMED_GENERIC = FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, TamedGenericEntity::new)
            .dimensions(EntityDimensions.changing(0.6f, 1.95f))
            .build(RegistryKey.of(Registries.ENTITY_TYPE.getKey(), Universaltaming.id("tamed_generic")));

    public static final EntityType<TamedCreeper> TAMED_CREEPER = FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, TamedCreeper::new)
            .dimensions(EntityDimensions.changing(0.6f, 1.7f))
            .build(RegistryKey.of(Registries.ENTITY_TYPE.getKey(), Universaltaming.id("tamed_creeper")));

    public static void init() {
        // The builders above now handle registration.
        // This method is called to ensure the static fields are initialized.
        Universaltaming.LOGGER.info("Mod entities loaded.");
    }
}