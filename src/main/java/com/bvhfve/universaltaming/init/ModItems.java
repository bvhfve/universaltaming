package com.bvhfve.universaltaming.init;

import com.bvhfve.universaltaming.Universaltaming;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItems {
    
    // Taming treat item - universal taming item
    public static Item TAMING_TREAT;
    
    // Golden sword for pet management (already exists in vanilla, but we'll reference it)
    public static final Item GOLDEN_SWORD = Items.GOLDEN_SWORD;
    
    // Creative tab for our mod
    public static final RegistryKey<ItemGroup> UNIVERSAL_TAMING_GROUP = RegistryKey.of(
        Registries.ITEM_GROUP.getKey(), 
        Universaltaming.id("universal_taming")
    );
    
    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, Universaltaming.id(name), item);
    }
    
    public static void init() {
        // Register items during init() when registry is ready
        RegistryKey<Item> tamingTreatKey = RegistryKey.of(Registries.ITEM.getKey(), Universaltaming.id("taming_treat"));
        TAMING_TREAT = Registry.register(Registries.ITEM, tamingTreatKey, 
            new Item(new Item.Settings().registryKey(tamingTreatKey)));
        
        // Register creative tab
        Registry.register(Registries.ITEM_GROUP, UNIVERSAL_TAMING_GROUP, FabricItemGroup.builder()
            .icon(() -> new ItemStack(TAMING_TREAT))
            .displayName(Text.translatable("itemgroup.universaltaming.universal_taming"))
            .build());
        
        // Add items to creative tab
        ItemGroupEvents.modifyEntriesEvent(UNIVERSAL_TAMING_GROUP).register(entries -> {
            entries.add(TAMING_TREAT);
        });
        
        Universaltaming.LOGGER.info("Registered mod items");
    }
}