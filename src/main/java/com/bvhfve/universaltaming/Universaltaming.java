package com.bvhfve.universaltaming;

import com.bvhfve.universaltaming.datagen.DynamicEntityGenerator;
import com.bvhfve.universaltaming.entity.TamedGenericEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Universal Taming Mod - Perfect Visual Preservation System
 * 
 * Core Philosophy: "A tamed cow must look like a vanilla cow"
 * 
 * This mod enables taming of any entity while preserving their exact original appearance.
 * Players experience seamless taming without visual disruption - tamed entities are 
 * indistinguishable from their vanilla counterparts while gaining enhanced AI behaviors.
 * 
 * Features:
 * - Perfect Visual Preservation: Original textures maintained for authentic appearance
 * - Universal Taming System: One item tames all supported entities
 * - Enhanced AI: Improved following, protection, and loyalty behaviors
 * - Dynamic Entity Generation: Automatic support for new entity types
 * - Type-Safe Implementation: Robust error handling and performance optimization
 * 
 * Phase 1 (COMPLETE): 11 entity types with perfect visual preservation
 * Phase 2 (IN PROGRESS): Extended coverage for 50+ entity types
 */
public class Universaltaming implements ModInitializer {
    public static final String MOD_ID = "universaltaming";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    // Mod version and metadata
    public static final String VERSION = "1.0.0";
    public static final String DESCRIPTION = "Perfect Visual Preservation Taming System";

    // Core entity types with perfect visual preservation
    public static final EntityType<TamedGenericEntity> TAMED_GENERIC = Registry.register(
        Registries.ENTITY_TYPE,
        Identifier.of(MOD_ID, "tamed_generic"),
        EntityType.Builder.<TamedGenericEntity>create((entityType, world) -> new TamedGenericEntity(entityType, world), SpawnGroup.CREATURE)
            .dimensions(0.6f, 1.95f)
            .maxTrackingRange(10)
            .trackingTickInterval(3)
            .build(net.minecraft.registry.RegistryKey.of(Registries.ENTITY_TYPE.getKey(), Identifier.of(MOD_ID, "tamed_generic")))
    );

    // Universal taming item - will be initialized in onInitialize()
    public static net.minecraft.item.Item UNIVERSAL_TAMING_ITEM;

    @Override
    public void onInitialize() {
        LOGGER.info("=== Universal Taming Mod v{} ===", VERSION);
        LOGGER.info("Initializing Perfect Visual Preservation System");
        LOGGER.info("Core Philosophy: 'A tamed cow must look like a vanilla cow'");

        // Register items first
        registerItems();

        // Register core entity attributes
        registerEntityAttributes();

        // Initialize dynamic entity generation system
        initializeDynamicEntitySystem();

        // Add items to creative inventory
        registerCreativeItems();

        // Register server lifecycle events
        registerServerEvents();

        // Log successful initialization
        int totalEntityTypes = DynamicEntityGenerator.getAllTamedEntityTypes().size() + 1;
        LOGGER.info("=== Universal Taming Initialized Successfully ===");
        LOGGER.info("Total entity types with perfect visual preservation: {}", totalEntityTypes);
        LOGGER.info("Phase 1 (COMPLETE): 11 entity types");
        LOGGER.info("Phase 2 (IN PROGRESS): Extended coverage planned");
        LOGGER.info("Ready for seamless taming with authentic appearance!");
    }

    /**
     * Register items during initialization
     */
    private void registerItems() {
        LOGGER.debug("Registering Universal Taming items");
        
        // Register the universal taming item with proper registry key
        UNIVERSAL_TAMING_ITEM = Registry.register(
            Registries.ITEM,
            net.minecraft.registry.RegistryKey.of(net.minecraft.registry.RegistryKeys.ITEM, Identifier.of(MOD_ID, "universal_taming_item")),
            new com.bvhfve.universaltaming.item.UniversalTamingItem(new net.minecraft.item.Item.Settings()
                .maxCount(1)
                .registryKey(net.minecraft.registry.RegistryKey.of(net.minecraft.registry.RegistryKeys.ITEM, Identifier.of(MOD_ID, "universal_taming_item"))))
        );
        
        LOGGER.debug("Universal Taming Item registered successfully");
    }

    /**
     * Register entity attributes for perfect visual preservation
     */
    private void registerEntityAttributes() {
        LOGGER.debug("Registering entity attributes for perfect visual preservation");
        
        // Register core entity attributes
        FabricDefaultAttributeRegistry.register(TAMED_GENERIC, TamedGenericEntity.createLivingAttributes());
        
        LOGGER.debug("Core entity attributes registered successfully");
    }

    /**
     * Initialize the dynamic entity generation system
     * This creates tamed versions of all supported vanilla entities
     */
    private void initializeDynamicEntitySystem() {
        LOGGER.info("Initializing Dynamic Entity Generation System");
        
        try {
            // Generate dynamic entity types for all supported mobs
            DynamicEntityGenerator.generateTamedEntityTypes();
            
            int generatedTypes = DynamicEntityGenerator.getAllTamedEntityTypes().size();
            LOGGER.info("Dynamic entity generation complete: {} entity types created", generatedTypes);
            
            // Log supported entity types for debugging
            if (LOGGER.isDebugEnabled()) {
                DynamicEntityGenerator.getAllTamedEntityTypes().keySet().forEach(entityType -> 
                    LOGGER.debug("Generated tamed entity type: {}", entityType));
            }
            
        } catch (Exception e) {
            LOGGER.error("Failed to initialize dynamic entity system", e);
            throw new RuntimeException("Critical error in dynamic entity generation", e);
        }
    }

    /**
     * Add items to creative inventory
     */
    private void registerCreativeItems() {
        LOGGER.debug("Registering creative inventory items");
        
        // Add universal taming item to tools tab
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.addAfter(Items.NAME_TAG, UNIVERSAL_TAMING_ITEM);
        });
        
        LOGGER.debug("Universal Taming Item added to creative inventory");
    }

    /**
     * Register server lifecycle events
     */
    private void registerServerEvents() {
        LOGGER.debug("Registering server lifecycle events");
        
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            LOGGER.info("Server started - Universal Taming active with perfect visual preservation");
        });
        
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            LOGGER.info("Server stopping - Universal Taming cleanup");
        });
    }

    /**
     * Get supported entity types for Phase 1 (complete)
     */
    public static String[] getPhase1EntityTypes() {
        return new String[] {
            "minecraft:cow", "minecraft:pig", "minecraft:sheep", "minecraft:chicken",
            "minecraft:wolf", "minecraft:cat", "minecraft:zombie", "minecraft:skeleton",
            "minecraft:creeper", "minecraft:spider", "minecraft:enderman"
        };
    }

    /**
     * Get planned entity types for Phase 2 (in progress)
     */
    public static String[] getPhase2EntityTypes() {
        return new String[] {
            "minecraft:horse", "minecraft:donkey", "minecraft:mule", "minecraft:llama",
            "minecraft:rabbit", "minecraft:fox", "minecraft:panda", "minecraft:polar_bear",
            "minecraft:ocelot", "minecraft:parrot", "minecraft:turtle", "minecraft:bee",
            "minecraft:goat", "minecraft:axolotl", "minecraft:frog", "minecraft:allay"
        };
    }

    /**
     * Utility method to create entity identifier
     */
    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    /**
     * Check if taming is enabled for a specific entity type
     */
    public static boolean isTamingEnabled(String entityType) {
        // Check if entity type is in Phase 1 (complete)
        for (String supportedType : getPhase1EntityTypes()) {
            if (supportedType.equals(entityType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the texture identifier for a specific entity type
     * This ensures perfect visual preservation by mapping to correct textures
     */
    public static Identifier getEntityTexture(String entityType) {
        return switch (entityType) {
            case "minecraft:cow" -> Identifier.of("minecraft", "textures/entity/cow/cow.png");
            case "minecraft:pig" -> Identifier.of("minecraft", "textures/entity/pig/pig.png");
            case "minecraft:sheep" -> Identifier.of("minecraft", "textures/entity/sheep/sheep.png");
            case "minecraft:chicken" -> Identifier.of("minecraft", "textures/entity/chicken.png");
            case "minecraft:wolf" -> Identifier.of("minecraft", "textures/entity/wolf/wolf.png");
            case "minecraft:cat" -> Identifier.of("minecraft", "textures/entity/cat/tabby.png");
            case "minecraft:zombie" -> Identifier.of("minecraft", "textures/entity/zombie/zombie.png");
            case "minecraft:skeleton" -> Identifier.of("minecraft", "textures/entity/skeleton/skeleton.png");
            case "minecraft:creeper" -> Identifier.of("minecraft", "textures/entity/creeper/creeper.png");
            case "minecraft:spider" -> Identifier.of("minecraft", "textures/entity/spider/spider.png");
            case "minecraft:enderman" -> Identifier.of("minecraft", "textures/entity/enderman/enderman.png");
            default -> {
                // Try to infer texture path from entity type
                Identifier entityId = Identifier.tryParse(entityType);
                if (entityId != null) {
                    String path = entityId.getPath();
                    yield Identifier.of("minecraft", "textures/entity/" + path + "/" + path + ".png");
                }
                yield Identifier.of("minecraft", "textures/entity/zombie/zombie.png");
            }
        };
    }
}