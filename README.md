# Universal Taming Mod

A comprehensive Fabric mod for Minecraft 1.21.7 that allows players to tame nearly any mob in the game, transforming them into loyal custom pet entities with enhanced behaviors and abilities.

![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.7-brightgreen)
![Fabric Loader](https://img.shields.io/badge/Fabric%20Loader-0.16.14+-blue)
![Fabric API](https://img.shields.io/badge/Fabric%20API-0.129.0+-blue)
![Java](https://img.shields.io/badge/Java-21+-orange)

## Overview

Transform your Minecraft experience with the Universal Taming mod! This comprehensive mod allows you to tame virtually any mob in the game, creating loyal companions with enhanced abilities and unique behaviors. Each tamed mob becomes a custom entity with doubled health, special abilities, and unwavering loyalty to their owner.

## Key Features

### Universal Taming System
- **Taming Treats**: Craft special treats using bones and cooked beef
- **One-Click Taming**: Right-click any compatible mob with a Taming Treat
- **Smart Exclusions**: Cats, wolves, and parrots retain their vanilla taming mechanics
- **Configurable Requirements**: Customize taming items and quantities

### Custom Pet Entities
- **Distinct Entities**: Tamed mobs become custom entities to prevent vanilla conflicts
- **Health Boost**: All tamed mobs gain **2x their original health** (configurable)
- **Attribute Preservation**: Retains original movement speed, attack damage, and characteristics
- **Visual Consistency**: Maintains original mob appearance and animations

### Advanced Pet Behaviors

#### Following & Movement
- **Smart Following**: Pets follow within configurable distance (default: 16 blocks)
- **Auto-Teleportation**: Pets teleport when too far behind (default: 32 blocks)
- **Pathfinding**: Intelligent navigation around obstacles

#### Commands & Control
- **Sit/Stand Toggle**: Right-click to make pets sit or stand (except foxes)
- **Owner Recognition**: Pets only respond to their owner's commands
- **Golden Sword Removal**: Player must use a golden sword to "remove" unwanted pets

#### Protection & Combat
- **Owner Immunity**: Pets cannot be harmed by their owner (except with golden sword)
- **No Friendly Fire**: Tamed mobs never attack other tamed mobs
- **Threat Response**: Hostile pets protect their owner from dangers

### Mob-Specific Features

#### Passive Mobs (Cow, Pig, Sheep, Chicken, etc.)
- **Resource Generation**: Periodically drop items from their loot tables
- **Configurable Drops**: Customize what items each mob type drops
- **Drop Intervals**: Set how often pets provide resources (default: 5 minutes)
- **Peaceful Behavior**: Remain calm and follow their owner

#### Hostile Mobs (Zombie, Skeleton, Spider, etc.)
- **Guardian Mode**: Attack hostile mobs within 16 blocks of owner
- **Threat Prioritization**: Focus on mobs attacking the owner first
- **Combat Effectiveness**: Retain original attack damage and abilities
- **Defensive Positioning**: Stay close to owner during combat

#### Tamed Creepers (Special)
- **Explosion Prevention**: **Never explode** - completely safe companions
- **Creeper Neutralization**: Automatically neutralize nearby vanilla creepers
- **Fireworks Display**: Neutralization creates harmless fireworks effect
- **Base Defense**: Excellent for protecting your builds from creeper damage

#### Underwater Mobs (Cod, Salmon, Drowned, etc.)
- **Land Adaptation**: No suffocation damage when following owner on land
- **Aquatic Behavior**: Behave as if always in water
- **Amphibious Following**: Seamlessly follow between water and land

### Professional Configuration System

#### Comprehensive Settings
- **Taming Items**: Customize required items and quantities
- **Health Multipliers**: Adjust health boost for tamed mobs
- **Distance Settings**: Configure follow and teleport ranges
- **Combat Settings**: Set attack ranges for hostile pets
- **Drop Rates**: Customize resource generation intervals

#### Advanced Options
- **Debug Logging**: Enable detailed logging for troubleshooting
- **Server Logging**: Track taming events for server administration
- **Individual Mob Control**: Enable/disable taming for specific mobs
- **Performance Tuning**: Optimize settings for server performance

## Installation

### Prerequisites
- **Minecraft**: 1.21.7
- **Fabric Loader**: 0.16.14 or later
- **Fabric API**: 0.129.0 or later
- **Java**: 21 or later

### Installation Steps
1. Download and install [Fabric Loader](https://fabricmc.net/use/)
2. Download [Fabric API](https://modrinth.com/mod/fabric-api)
3. Download the Universal Taming mod JAR
4. Place both JAR files in your `mods` folder
5. Launch Minecraft with the Fabric profile

## How to Use

### Crafting Taming Treats
```
 B     (B = Bone)
BMB    (M = Cooked Beef)
 B
```
**Result**: 4 Taming Treats

### Taming Process
1. **Craft Taming Treats** using the recipe above
2. **Find a mob** you want to tame (check compatibility list)
3. **Right-click** the mob with a Taming Treat in hand
4. **Watch the transformation** with heart particles and sound effects
5. **Enjoy your new pet** with doubled health and special abilities!

### Pet Management
- **Sit/Stand**: Right-click your pet to toggle sitting (except foxes)
- **Following**: Pets automatically follow within configured distance
- **Removal**: Use a golden sword to safely remove pets if needed
- **Health**: Pets start with 2x their original maximum health

## Configuration

The mod creates `config/universaltaming.json` with extensive customization options:

### Basic Settings
```json
{
  "tamingItem": "universaltaming:taming_treat",
  "tamingItemCount": 1,
  "healthMultiplier": 2.0,
  "followDistance": 16,
  "teleportDistance": 32
}
```

### Behavior Settings
```json
{
  "hostileAttackRange": 16,
  "creeperNeutralizationRange": 8,
  "passiveDropIntervalMinutes": 5,
  "debugLogging": false,
  "logTaming": false
}
```

### Mob Configuration
```json
{
  "enabledMobs": {
    "minecraft:zombie": true,
    "minecraft:creeper": true,
    "minecraft:cow": true
  },
  "excludedMobs": [
    "minecraft:cat",
    "minecraft:wolf",
    "minecraft:parrot"
  ]
}
```

## Compatibility

### Tameable Mobs
- **Passive**: Cow, Pig, Sheep, Chicken, Rabbit, Mooshroom, Fox
- **Hostile**: Zombie, Skeleton, Creeper, Spider, Enderman, Witch, Drowned
- **Aquatic**: Cod, Salmon, Tropical Fish, Squid
- **And many more!**

### Excluded Mobs (Retain Vanilla Behavior)
- **Cat**: Uses vanilla fish taming
- **Wolf**: Uses vanilla bone taming  
- **Parrot**: Uses vanilla seed taming

### Mod Compatibility
- **Fabric API**: Required dependency
- **Other Pet Mods**: Generally compatible
- **Server Friendly**: Full multiplayer support
- **Performance Optimized**: Minimal impact on game performance

## Building from Source

### Development Setup
```bash
git clone <repository-url>
cd universaltaming
./gradlew build
```

### Build Output
- **Main JAR**: `build/libs/universaltaming-1.0.0.jar`
- **Sources JAR**: `build/libs/universaltaming-1.0.0-sources.jar`

### Development Requirements
- **JDK 21** or later
- **Gradle 8.12+** (included via wrapper)
- **Fabric Development Kit**

## Troubleshooting

### Common Issues
- **Pets not following**: Check follow distance in config
- **Taming not working**: Verify mob is enabled in config
- **Performance issues**: Reduce pet count or adjust intervals
- **Config not loading**: Check JSON syntax and file permissions

### Debug Mode
Enable `debugLogging: true` in config for detailed information about:
- Taming attempts and results
- Pet behavior and AI decisions
- Configuration loading and validation
- Performance metrics

## Documentation

- **[Complete User Guide](USER_GUIDE.md)**: Detailed instructions and tips
- **[Configuration Reference](CONFIG_REFERENCE.md)**: All configuration options
- **[API Documentation](API_DOCS.md)**: For mod developers

## Changelog

### Version 1.0.0
- Initial release with full feature set
- Universal taming system implementation
- Custom pet entities with enhanced behaviors
- Comprehensive configuration system
- Minecraft 1.21.7 compatibility
- Professional-grade code architecture

## Contributing

We welcome contributions! Please:
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- **Fabric Team**: For the excellent modding framework
- **Minecraft Community**: For inspiration and feedback
- **Torcherino Mod**: For configuration system architecture reference
- **Beta Testers**: For helping refine the mod

---

**Transform your Minecraft world with loyal companions! Download Universal Taming today and experience the joy of having any mob as your faithful pet.**