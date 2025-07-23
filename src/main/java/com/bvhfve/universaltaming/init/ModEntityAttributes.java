package com.bvhfve.universaltaming.init;

import com.bvhfve.universaltaming.Universaltaming;
import com.bvhfve.universaltaming.entity.TamedCreeper;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class ModEntityAttributes {

    public static void init() {
        FabricDefaultAttributeRegistry.register(Universaltaming.TAMED_GENERIC, TamedCreeper.createTamedCreeperAttributes());
    }
}