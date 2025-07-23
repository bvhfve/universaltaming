package com.bvhfve.universaltaming;

import com.bvhfve.universaltaming.config.UniversalTamingConfig;
import com.bvhfve.universaltaming.datagen.DynamicEntityGenerator;
import com.bvhfve.universaltaming.entity.TamedCreeper;
import com.bvhfve.universaltaming.entity.TamedGenericEntity;
import com.bvhfve.universaltaming.event.TamingEventHandler;
import com.bvhfve.universaltaming.init.ModEntityAttributes;
import com.bvhfve.universaltaming.init.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Universaltaming implements ModInitializer {
	public static final String MOD_ID = "universaltaming";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final RegistryKey<EntityType<?>> TAMED_GENERIC_KEY = RegistryKey.of(Registries.ENTITY_TYPE.getKey(), id("tamed_generic"));
	public static EntityType<TamedGenericEntity> TAMED_GENERIC;

	public static final RegistryKey<EntityType<?>> TAMED_CREEPER_KEY = RegistryKey.of(Registries.ENTITY_TYPE.getKey(), id("tamed_creeper"));
	public static EntityType<TamedCreeper> TAMED_CREEPER;

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Universal Taming Mod...");

				TAMED_GENERIC = Registry.register(Registries.ENTITY_TYPE, TAMED_GENERIC_KEY, FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, TamedGenericEntity::new)
				.dimensions(EntityDimensions.changing(0.6f, 1.95f))
				.build(TAMED_GENERIC_KEY));

		TAMED_CREEPER = Registry.register(Registries.ENTITY_TYPE, TAMED_CREEPER_KEY, FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, TamedCreeper::new)
				.dimensions(EntityDimensions.changing(0.6f, 1.7f))
				.build(TAMED_CREEPER_KEY));

		// Initialize configuration
		UniversalTamingConfig.initialize();
		
		// Register mod content
		ModItems.init();
		ModEntityAttributes.init();
		
		// Generate dynamic tamed entity types for all vanilla mobs
		DynamicEntityGenerator.generateTamedEntityTypes();
		
		// Register event handlers
		TamingEventHandler.init();
		
		LOGGER.info("Universal Taming Mod initialized successfully!");
	}
}