package com.bespectacled.modernbeta.world.feature.placed;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.PlacedFeature;

public class OldPlacedFeatures {
    public static PlacedFeature register(String id, PlacedFeature feature) {
        return Registry.register(BuiltinRegistries.PLACED_FEATURE, ModernBeta.createId(id), feature);
    }
}
