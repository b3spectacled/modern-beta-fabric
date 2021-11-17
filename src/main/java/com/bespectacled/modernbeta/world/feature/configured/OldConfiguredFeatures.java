package com.bespectacled.modernbeta.world.feature.configured;

import java.util.HashMap;
import java.util.Map;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class OldConfiguredFeatures {
    private static final Map<Identifier, ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = new HashMap<Identifier, ConfiguredFeature<?, ?>>();
    
    public static <F extends FeatureConfig> ConfiguredFeature<F, ?> register(String id, ConfiguredFeature<F, ?> feature) {
        CONFIGURED_FEATURES.put(ModernBeta.createId(id), feature);
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, ModernBeta.createId(id), feature);
    }
}
