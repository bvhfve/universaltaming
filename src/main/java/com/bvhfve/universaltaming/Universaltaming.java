package com.bvhfve.universaltaming;

import com.bvhfve.universaltaming.config.UniversalTamingConfig;
import com.bvhfve.universaltaming.datagen.DynamicEntityGenerator;
import com.bvhfve.universaltaming.event.TamingEventHandler;
import com.bvhfve.universaltaming.init.ModEntities;
import com.bvhfve.universaltaming.init.ModEntityAttributes;
import com.bvhfve.universaltaming.init.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Universaltaming implements ModInitializer {
	public static final String MOD_ID = "universaltaming";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Universal Taming Mod...");
		
		// Initialize configuration
		UniversalTamingConfig.initialize();
		
		// Register mod content
		ModItems.init();
		ModEntities.init();
		ModEntityAttributes.init();
		
		// Generate dynamic tamed entity types for all vanilla mobs
		DynamicEntityGenerator.generateTamedEntityTypes();
		
		// Register event handlers
		TamingEventHandler.init();
		
		LOGGER.info("Universal Taming Mod initialized successfully!");
	}
}