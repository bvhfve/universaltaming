# Universal Taming Mod - Master Development Plan

## 🎯 REVOLUTIONARY NEW ARCHITECTURE PLAN

Based on deep analysis of wolf examples and current system limitations, we're implementing a **complete architectural overhaul** to create the most advanced universal taming system ever built.

### 🔥 CORE PHILOSOPHY: "Every Mob Gets Its Own Perfect Renderer"

**Problem Solved**: Dynamic rendering cannot perfectly preserve complex entity behaviors, animations, and visual states.

**Solution**: Create **unique rendering and entity classes for EVERY single mob** in the game, each with:
- Perfect model preservation using original vanilla models
- Perfect texture preservation using original vanilla textures  
- Perfect animation preservation using original vanilla animation systems
- Wolf-like AI system with full vanilla wolf data compatibility
- Complete `/data get entity` compatibility showing all wolf-like properties

## 🏗️ NEW MASTER ARCHITECTURE

### Phase 1: Individual Entity System (PRIORITY: CRITICAL)
**Goal**: Replace dynamic system with individual entity classes for each mob type

#### 1.1 Entity Class Generation
- **TamedCowEntity** extends TameableEntity with cow model/texture/animations
- **TamedPigEntity** extends TameableEntity with pig model/texture/animations  
- **TamedSheepEntity** extends TameableEntity with sheep model/texture/animations
- **TamedChickenEntity** extends TameableEntity with chicken model/texture/animations
- **TamedZombieEntity** extends TameableEntity with zombie model/texture/animations
- **TamedSkeletonEntity** extends TameableEntity with skeleton model/texture/animations
- **TamedCreeperEntity** extends TameableEntity with creeper model/texture/animations
- **TamedSpiderEntity** extends TameableEntity with spider model/texture/animations
- **TamedEndermanEntity** extends TameableEntity with enderman model/texture/animations
- **[Continue for ALL 50+ mob types]**

#### 1.2 Renderer Class Generation  
- **TamedCowEntityRenderer** using vanilla CowEntityRenderer patterns
- **TamedPigEntityRenderer** using vanilla PigEntityRenderer patterns
- **TamedSheepEntityRenderer** using vanilla SheepEntityRenderer patterns
- **TamedChickenEntityRenderer** using vanilla ChickenEntityRenderer patterns
- **TamedZombieEntityRenderer** using vanilla ZombieEntityRenderer patterns
- **TamedSkeletonEntityRenderer** using vanilla SkeletonEntityRenderer patterns
- **TamedCreeperEntityRenderer** using vanilla CreeperEntityRenderer patterns
- **TamedSpiderEntityRenderer** using vanilla SpiderEntityRenderer patterns
- **TamedEndermanEntityRenderer** using vanilla EndermanEntityRenderer patterns
- **[Continue for ALL 50+ mob types]**

#### 1.3 Model Class Generation
- **TamedCowEntityModel** extending vanilla CowEntityModel
- **TamedPigEntityModel** extending vanilla PigEntityModel
- **TamedSheepEntityModel** extending vanilla SheepEntityModel
- **[Continue for ALL 50+ mob types]**

### Phase 2: Wolf-Like AI Implementation (PRIORITY: HIGH)
**Goal**: Every tamed entity behaves exactly like a vanilla wolf

#### 2.1 Universal Wolf AI System
Based on analysis of wolf examples (DoggyTalents, ImprovedWolves, Companions), implement:

```java
// Universal Wolf-like data structure for ALL tamed entities
public class TamedEntityData {
    // Core wolf properties that ALL tamed entities will have
    private UUID owner;
    private boolean sitting = false;
    private int collarColor = 14; // Red by default like wolves
    private int angerTime = 0;
    private float health = 40.0f; // Wolf-like health
    private float maxHealth = 40.0f;
    private float movementSpeed = 0.3f; // Wolf-like speed
    private float followRange = 16.0f; // Wolf-like follow range
    
    // Wolf-like brain/memory system
    private Brain<TamedEntity> brain;
    private Map<String, Object> memories = new HashMap<>();
    
    // Wolf-like attributes that show in /data get entity
    private boolean persistenceRequired = false;
    private boolean invulnerable = false;
    private boolean fallFlying = false;
    private int forcedAge = 0;
    private int deathTime = 0;
    private boolean leftHanded = false;
    private boolean canPickUpLoot = false;
    private List<AttributeModifier> attributes = new ArrayList<>();
}
```

#### 2.2 Wolf-Compatible Data Output
When using `/data get entity` on ANY tamed mob, it should show:
```
Wolf has the following entity data: {
    Brain: {memories: {}}, 
    HurtByTimestamp: 0, 
    Owner: [I; -798559876, -823380661, -1815467092, 1532164499], 
    Sitting: 1b, 
    Invulnerable: 0b, 
    FallFlying: 0b, 
    ForcedAge: 0, 
    PortalCooldown: 0, 
    AbsorptionAmount: 0.0f, 
    InLove: 0, 
    DeathTime: 0s, 
    PersistenceRequired: 0b, 
    UUID: [I; 1774636305, 36717396, -1692411697, -402704169], 
    Age: 0, 
    CollarColor: 14b, 
    AngerTime: 0, 
    Motion: [0.0d, -0.0784000015258789d, 0.0d], 
    Health: 40.0f, 
    LeftHanded: 0b, 
    fall_distance: 0.0d, 
    Air: 300s, 
    OnGround: 1b, 
    Rotation: [0.0f, 0.0f], 
    Pos: [-9.655578847900273d, 109.0d, -32.013277301114876d], 
    Fire: 0s, 
    CanPickUpLoot: 0b, 
    attributes: [
        {id: "minecraft:max_health", base: 40.0d}, 
        {id: "minecraft:movement_speed", base: 0.30000001192092896d}, 
        {id: "minecraft:follow_range", modifiers: [{amount: 0.018497530560997278d, id: "minecraft:random_spawn_bonus", operation: "add_multiplied_base"}], base: 16.0d}
    ], 
    HurtTime: 0s
}
```

### Phase 3: Dynamic Wolf Model Removal (PRIORITY: MEDIUM)
**Goal**: Remove the old dynamic system completely

#### 3.1 Cleanup Tasks
- Remove `OriginalAppearanceRenderer.java`
- Remove `DynamicEntityRenderer.java` 
- Remove `TamedGenericEntity.java` (replace with individual entities)
- Remove dynamic texture mapping system
- Remove dynamic model system

#### 3.2 Migration System
- Convert existing tamed entities to new individual entity types
- Preserve all existing tamed entity data during migration
- Ensure no data loss during the transition

### Phase 4: Advanced Wolf AI Features (PRIORITY: LOW)
**Goal**: Implement advanced wolf-like behaviors from knowledgebase examples

#### 4.1 Enhanced Behaviors (from DoggyTalents analysis)
- **Sitting/Standing**: Right-click to toggle like vanilla wolves
- **Following**: Smart following with teleportation when too far
- **Combat**: Attack hostiles, defend owner, coordinated pack attacks
- **Loyalty**: Never despawn, always return to owner
- **Health**: Wolf-like health system with regeneration

#### 4.2 Advanced Features (from ImprovedWolves analysis)  
- **Gift System**: Tamed entities occasionally bring gifts to owner
- **Stat Tracking**: Kill count, death count, experience gained
- **Classes**: Hunter/Gatherer specializations
- **Breeding**: Tamed entities can breed with each other

## 🛠️ IMPLEMENTATION STRATEGY

### Step 1: Entity Class Generation (Week 1-2)
1. Create template entity class based on wolf examples
2. Generate individual entity classes for all 50+ mob types
3. Implement wolf-like AI goals for each entity type
4. Add proper NBT data serialization for wolf compatibility

### Step 2: Renderer System (Week 3-4)  
1. Create template renderer class based on vanilla patterns
2. Generate individual renderer classes for all 50+ mob types
3. Ensure perfect model/texture preservation
4. Test rendering performance and visual accuracy

### Step 3: Model Integration (Week 5-6)
1. Create template model class extending vanilla models
2. Generate individual model classes for all 50+ mob types  
3. Preserve all original animations and visual states
4. Test model accuracy and animation fidelity

### Step 4: Wolf AI Implementation (Week 7-8)
1. Implement universal wolf-like data structure
2. Add wolf-compatible NBT serialization
3. Implement wolf-like AI goals and behaviors
4. Test `/data get entity` compatibility

### Step 5: Testing & Polish (Week 9-10)
1. Comprehensive testing of all entity types
2. Performance optimization
3. Bug fixes and edge case handling
4. Documentation and user guides

## 📊 SUCCESS METRICS

### Technical Metrics
- ✅ 50+ individual entity classes with perfect visual preservation
- ✅ 50+ individual renderer classes using vanilla patterns  
- ✅ 50+ individual model classes extending vanilla models
- ✅ 100% wolf-like AI compatibility for all tamed entities
- ✅ 100% `/data get entity` compatibility showing wolf data
- ✅ Zero dynamic rendering (all static, type-safe)
- ✅ Zero visual differences from vanilla entities

### User Experience Metrics  
- ✅ Seamless taming experience (no visual changes)
- ✅ Perfect wolf-like behavior for all tamed mobs
- ✅ Complete data compatibility with vanilla wolf systems
- ✅ Professional-quality mod experience
- ✅ Zero crashes or performance issues

## 🎯 FINAL VISION

**The Ultimate Universal Taming Mod**: Every single mob in Minecraft can be tamed and will behave exactly like a vanilla wolf, while looking exactly like their original vanilla appearance. No compromises, no limitations, just perfect universal taming with perfect visual preservation.

This represents the most ambitious and technically advanced universal taming system ever attempted in Minecraft modding.
- **Perfect Visual Preservation**: Tamed entities look exactly like their original counterparts
- **Model Accuracy**: Each entity type uses its correct model (cow model, pig model, etc.)
- **Texture Accuracy**: Each entity type uses its correct texture
- **Animation Preservation**: Original animations maintained
- **Type Safety**: Proper render state handling for each entity type

#### 1.1 Original Appearance Renderer ✅
- **File**: `src/main/java/com/bvhfve/universaltaming/client/render/OriginalAppearanceRenderer.java`
- **Purpose**: Preserve exact original entity appearance (model + texture + animations)
- **Status**: COMPLETE - Supports major entity types with perfect visual preservation
- **Implementation**:
  ```java
  // Original appearance preservation with correct models and textures
  case "minecraft:cow" -> new OriginalAppearanceRenderer<>(
      context, entityId, new CowEntityModel(context.getPart(EntityModelLayers.COW)));
  case "minecraft:pig" -> new OriginalAppearanceRenderer<>(
      context, entityId, new PigEntityModel(context.getPart(EntityModelLayers.PIG)));
  // ... supports all major entity types with perfect visual preservation
  ```

### Phase 2: Extended Entity Support (Priority: MEDIUM) 🚧
**Goal**: Add support for remaining entity types and special cases
**Status**: PLANNED - Core system complete, expanding coverage

#### Current Coverage:
- ✅ **Major Animals**: cow, pig, sheep, chicken, wolf, cat
- ✅ **Common Hostiles**: zombie, skeleton, creeper, spider, enderman
- 🚧 **Remaining Entities**: ~40+ additional entity types to add

#### Next Steps:
- **Aquatic Entities**: squid, dolphin, turtle, axolotl, guardian
- **Flying Entities**: bat, bee, phantom, parrot
- **Nether Entities**: piglin, hoglin, blaze, ghast
- **Special Cases**: villager, iron_golem, slime variants
      private static final Map<String, EntityModelLayers> MODEL_LAYERS = Map.of(
          "minecraft:cow", EntityModelLayers.COW,
          "minecraft:pig", EntityModelLayers.PIG,
          "minecraft:sheep", EntityModelLayers.SHEEP,
          // ... 50+ mappings
      );
      
      public static EntityModel<?> createModel(String entityType, EntityRendererFactory.Context context) {
          return switch (entityType) {
              case "minecraft:cow" -> new CowEntityModel<>(context.getPart(EntityModelLayers.COW));
              case "minecraft:pig" -> new PigEntityModel<>(context.getPart(EntityModelLayers.PIG));
              // ... all entity types
          };
      }
  }
  ```

#### 1.2 Dynamic Renderer Enhancement
- **File**: `src/main/java/com/bvhfve/universaltaming/client/render/DynamicTamedEntityRenderer.java`
- **Purpose**: Render tamed entities with correct models and textures
- **Key Features**:
  - Model switching based on `originalEntityType`
  - Texture mapping to vanilla resources
  - Animation state preservation
  - Render state management for MC 1.21.7

#### 1.3 Render State System
- **Challenge**: MC 1.21.7 uses render states instead of direct entity access
- **Solution**: Custom render state that includes original entity type
- **Files**:
  - `TamedEntityRenderState.java` - Custom render state
  - `TamedEntityRenderer.java` - Updated renderer using render states

### Phase 2: Visual Taming Indicators (Priority: Medium)
**Goal**: Add visual cues to distinguish tamed entities from wild ones

#### 2.1 Collar System
- **Implementation**: Overlay rendering on neck area
- **Features**:
  - Color-coded by owner (UUID-based hashing)
  - Configurable visibility (always/nearby/never)
  - Different collar styles for different mob types
- **Files**:
  - `CollarRenderer.java` - Collar overlay system
  - `CollarTextures.java` - Collar texture management

#### 2.2 Particle Effects
- **Taming Status Particles**:
  - Heart particles when near owner
  - Sparkle effects when following commands
  - Angry particles when defending owner
- **Implementation**:
  - Custom particle types
  - Conditional spawning based on behavior state
  - Performance-optimized (limited particle count)

#### 2.3 Name Tag Enhancements
- **Features**:
  - Owner name display option
  - Taming status indicators
  - Behavior state display (sitting, following, attacking)
- **Configuration**: Client-side settings for visibility

### Phase 3: Advanced Visual Features (Priority: Low)
**Goal**: Polish and advanced visual enhancements

#### 3.1 Animation Enhancements
- **Sitting Animations**: Custom sitting poses for different mob types
- **Following Behavior**: Tail wagging, ear perking for applicable mobs
- **Combat Stance**: Aggressive postures when defending owner

#### 3.2 Texture Variations
- **Tamed Variants**: Slightly different textures for tamed versions
- **Health Indicators**: Subtle color shifts based on health percentage
- **Mood System**: Visual changes based on happiness/loyalty level

#### 3.3 UI Integration
- **Pet Management Screen**: GUI for managing all tamed entities
- **Status Overlays**: Health bars, behavior indicators
- **Command Interface**: Visual command selection system

## Implementation Timeline 📅

### Week 1-2: Model System Foundation
1. **Day 1-3**: Research MC 1.21.7 render state system
2. **Day 4-7**: Implement `DynamicModelFactory`
3. **Day 8-10**: Create custom render states
4. **Day 11-14**: Test basic model switching

### Week 3-4: Core Visual Implementation
1. **Day 15-18**: Implement dynamic texture mapping
2. **Day 19-21**: Add model-specific animations
3. **Day 22-25**: Test all 50+ mob types
4. **Day 26-28**: Performance optimization

### Week 5-6: Visual Indicators
1. **Day 29-32**: Implement collar system
2. **Day 33-35**: Add particle effects
3. **Day 36-38**: Name tag enhancements
4. **Day 39-42**: Integration testing

## Technical Challenges & Solutions 🔧

### Challenge 1: Render State Complexity
**Problem**: MC 1.21.7 render states don't directly expose entity data
**Solution**: 
- Custom `TamedEntityRenderState` extending base render states
- Store `originalEntityType` in render state
- Update render state in `updateRenderState()` method

### Challenge 2: Model Memory Management
**Problem**: 50+ models could cause memory issues
**Solution**:
- Lazy loading of models
- Model caching with LRU eviction
- Shared model instances where possible

### Challenge 3: Animation Compatibility
**Problem**: Different mob types have different animation systems
**Solution**:
- Animation adapter pattern
- Fallback to basic animations for complex mobs
- Custom animation blending for tamed behaviors

## Configuration Options ⚙️

### Client-Side Settings
```json
{
  "visual_enhancements": {
    "dynamic_models": true,
    "collar_visibility": "nearby",
    "particle_effects": true,
    "name_tag_enhancements": true,
    "performance_mode": false
  }
}
```

### Server-Side Settings
```json
{
  "visual_sync": {
    "force_collar_visibility": false,
    "allow_custom_textures": true,
    "particle_limit_per_player": 50
  }
}
```

## Testing Strategy 🧪

### Phase 1 Testing
- **Model Accuracy**: Verify each mob type renders correctly
- **Performance**: FPS impact with multiple tamed entities
- **Memory Usage**: Monitor model loading and caching

### Phase 2 Testing
- **Visual Clarity**: Ensure indicators don't obstruct gameplay
- **Multiplayer**: Test collar colors and ownership display
- **Accessibility**: Color-blind friendly options

### Phase 3 Testing
- **Integration**: All features working together
- **Edge Cases**: Unusual mob types, modded entities
- **Performance**: Final optimization and profiling

## Success Metrics 📊

### Technical Metrics
- **Render Performance**: <5% FPS impact with 20+ tamed entities
- **Memory Usage**: <100MB additional for all models
- **Compatibility**: Works with 95%+ of vanilla mob types

### User Experience Metrics
- **Visual Clarity**: Easy to distinguish tamed vs wild mobs
- **Immersion**: Feels natural and integrated with vanilla
- **Customization**: Flexible configuration options

## Future Enhancements 🚀

### Mod Compatibility
- **JEI Integration**: Recipe viewing for taming treats
- **REI Support**: Alternative recipe viewer support
- **Modded Mobs**: Framework for supporting modded entities

### Advanced Features
- **Breeding System**: Visual indicators for tamed mob breeding
- **Evolution System**: Visual changes as mobs gain experience
- **Seasonal Variants**: Holiday-themed visual modifications

---

## Getting Started 🏁

### For Developers
1. **Study Current Code**: Understand existing entity conversion system
2. **MC 1.21.7 Research**: Learn render state system changes
3. **Model Analysis**: Examine vanilla entity models and animations
4. **Prototype**: Start with single mob type (pig) for proof of concept

### For Contributors
1. **Art Assets**: Create collar textures and particle designs
2. **Testing**: Help test visual features across different scenarios
3. **Documentation**: Improve user guides and configuration docs
4. **Feedback**: Provide UX feedback on visual clarity and usability

This roadmap provides a comprehensive path to transform the Universal Taming mod from a functional but visually basic system into a polished, professional-quality experience that rivals the best pet mods available.