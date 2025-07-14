package com.bvhfve.universaltaming.config;

import com.bvhfve.universaltaming.Universaltaming;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.MarkerManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UniversalTamingConfig {
    public static UniversalTamingConfig INSTANCE;
    
    //@Comment("The item required to tame mobs. Examples: \"minecraft:bone\", \"universaltaming:taming_treat\"")
    public final String tamingItem = "universaltaming:taming_treat";
    
    //@Comment("Number of taming items required per taming attempt.\nValid Range: 1 to 64")
    public final int tamingItemCount = 1;
    
    //@Comment("Health multiplier applied to tamed mobs.\nValid Range: 1.0 to 10.0")
    public final double healthMultiplier = 2.0;
    
    //@Comment("Distance at which pets will start following their owner.\nValid Range: 1 to 64")
    public final int followDistance = 16;
    
    //@Comment("Distance at which pets will teleport to their owner.\nValid Range: 16 to 128")
    public final int teleportDistance = 32;
    
    //@Comment("Range at which hostile tamed mobs will attack enemies.\nValid Range: 1 to 32")
    public final int hostileAttackRange = 16;
    
    //@Comment("Range at which tamed creepers will neutralize vanilla creepers.\nValid Range: 1 to 16")
    public final int creeperNeutralizationRange = 8;
    
    //@Comment("Interval in minutes between passive mob drops.\nValid Range: 1 to 60")
    public final int passiveDropIntervalMinutes = 5;
    
    //@Comment("Enable debug logging for taming events")
    public final boolean debugLogging = false;
    
    //@Comment("Log taming attempts and successes (Intended for server use)")
    public final boolean logTaming = false;
    
    //@Comment("Enable detailed debugging system with verbose output")
    public final boolean enableDetailedDebugging = false;
    
    //@Comment("Debug level: BASIC, DETAILED, VERBOSE")
    public final String debugLevel = "BASIC";
    
    //@Comment("Log AI goal modifications")
    public final boolean debugAI = false;
    
    //@Comment("Log mixin interactions")
    public final boolean debugMixins = false;
    
    //@Comment("Log data attachment operations")
    public final boolean debugDataAttachment = false;
    
    //@Comment("\nDefines drops for passive mobs when tamed.\nFormat: \"mob_id\": [\"item1\", \"item2\"]")
    private final Map<String, String[]> passiveMobDrops = createDefaultDrops();
    
    //@Comment("\nMobs that cannot be tamed (retain vanilla behavior).\nExamples: \"minecraft:cat\", \"minecraft:wolf\", \"minecraft:parrot\"")
    @SuppressWarnings("MismatchedReadAndWriteOfArray")
    private final Identifier[] excludedMobs = new Identifier[]{
        Identifier.of("minecraft:cat"),
        Identifier.of("minecraft:wolf"),
        Identifier.of("minecraft:parrot")
    };
    
    //@Comment("\nMobs that can be tamed. Set to false to disable taming for specific mobs.\nExamples: \"minecraft:zombie\": true, \"minecraft:creeper\": false")
    private final Map<String, Boolean> enabledMobs = createDefaultEnabledMobs();
    
    private static Map<String, String[]> createDefaultDrops() {
        Map<String, String[]> drops = new HashMap<>();
        drops.put("minecraft:cow", new String[]{"minecraft:leather", "minecraft:beef"});
        drops.put("minecraft:pig", new String[]{"minecraft:porkchop"});
        drops.put("minecraft:sheep", new String[]{"minecraft:wool", "minecraft:mutton"});
        drops.put("minecraft:chicken", new String[]{"minecraft:feather", "minecraft:chicken"});
        drops.put("minecraft:rabbit", new String[]{"minecraft:rabbit_hide", "minecraft:rabbit"});
        drops.put("minecraft:mooshroom", new String[]{"minecraft:leather", "minecraft:beef", "minecraft:red_mushroom"});
        return drops;
    }
    
    private static Map<String, Boolean> createDefaultEnabledMobs() {
        Map<String, Boolean> enabled = new HashMap<>();
        // Hostile mobs
        enabled.put("minecraft:zombie", true);
        enabled.put("minecraft:skeleton", true);
        enabled.put("minecraft:creeper", true);
        enabled.put("minecraft:spider", true);
        enabled.put("minecraft:enderman", true);
        enabled.put("minecraft:witch", true);
        enabled.put("minecraft:drowned", true);
        
        // Passive mobs
        enabled.put("minecraft:cow", true);
        enabled.put("minecraft:pig", true);
        enabled.put("minecraft:sheep", true);
        enabled.put("minecraft:chicken", true);
        enabled.put("minecraft:rabbit", true);
        enabled.put("minecraft:mooshroom", true);
        enabled.put("minecraft:fox", true);
        
        // Aquatic mobs
        enabled.put("minecraft:cod", true);
        enabled.put("minecraft:salmon", true);
        enabled.put("minecraft:tropical_fish", true);
        enabled.put("minecraft:squid", true);
        
        return enabled;
    }
    
    public static void initialize() {
        Gson gson = new GsonBuilder()
            .disableInnerClassSerialization()
            .registerTypeAdapter(Identifier.class, new IdentifierAdapter())
            .setPrettyPrinting()
            .create();
            
        Path configPath = getConfigPath();
        var logger = LogManager.getLogger("universaltaming-config");
        var marker = new MarkerManager.Log4jMarker("universaltaming");
        
        UniversalTamingConfig config = null;
        
        try {
            Files.createDirectories(configPath.getParent());
        } catch (IOException e) {
            logger.warn(marker, "Failed to create directory required for universaltaming config, using default config.");
            config = new UniversalTamingConfig();
        }
        
        if (config == null) {
            if (Files.exists(configPath)) {
                try (var reader = Files.newBufferedReader(configPath)) {
                    config = gson.fromJson(reader, UniversalTamingConfig.class);
                    logger.info(marker, "Loaded configuration from {}", configPath);
                } catch (IOException e) {
                    logger.warn(marker, "Failed to read universaltaming config file, using default config.");
                    config = new UniversalTamingConfig();
                }
            } else {
                config = new UniversalTamingConfig();
                try (var writer = Files.newBufferedWriter(configPath, StandardOpenOption.CREATE_NEW)) {
                    gson.toJson(config, writer);
                    logger.info(marker, "Created default configuration at {}", configPath);
                } catch (IOException e) {
                    logger.warn(marker, "Failed to save default universaltaming config file.");
                }
            }
        }
        
        INSTANCE = config;
        INSTANCE.onConfigLoaded();
    }
    
    private static Path getConfigPath() {
        // Use Fabric's config directory approach
        return net.fabricmc.loader.api.FabricLoader.getInstance()
            .getConfigDir()
            .resolve("universaltaming.json");
    }
    
    private void onConfigLoaded() {
        // Validate configuration values
        if (debugLogging) {
            Universaltaming.LOGGER.info("Debug logging enabled for Universal Taming");
        }
        
        if (logTaming) {
            Universaltaming.LOGGER.info("Taming event logging enabled");
        }
        
        // Log configuration summary
        Universaltaming.LOGGER.info("Universal Taming Config loaded - Taming item: {}, Health multiplier: {}", 
            tamingItem, healthMultiplier);
    }
    
    public boolean isMobTameable(Identifier entityType) {
        // Check if mob is explicitly excluded
        for (Identifier excluded : excludedMobs) {
            if (excluded.equals(entityType)) {
                return false;
            }
        }
        
        // Check if mob is enabled
        return enabledMobs.getOrDefault(entityType.toString(), false);
    }
    
    public String[] getDropsForMob(Identifier entityType) {
        return passiveMobDrops.getOrDefault(entityType.toString(), new String[0]);
    }
    
    public Identifier getTamingItem() {
        return Identifier.tryParse(tamingItem);
    }
    
    public boolean isDebugLogging() {
        return debugLogging;
    }
    
    public boolean shouldLogTaming() {
        return logTaming;
    }
}