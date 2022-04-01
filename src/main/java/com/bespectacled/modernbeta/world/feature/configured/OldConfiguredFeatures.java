package com.bespectacled.modernbeta.world.feature.configured;

import java.util.HashMap;
import java.util.Map;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class OldConfiguredFeatures {
    private static final Map<Identifier, ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = new HashMap<Identifier, ConfiguredFeature<?, ?>>();
    
    public static <FC extends FeatureConfig, F extends Feature<FC>> RegistryEntry<ConfiguredFeature<FC, ?>> register(String id, F feature, FC featureConfig) {
        Identifier featureId = ModernBeta.createId(id);
        ConfiguredFeature<FC, F> configuredFeature = new ConfiguredFeature<FC, F>(feature, featureConfig);
        
        CONFIGURED_FEATURES.put(featureId, configuredFeature);
        return BuiltinRegistries.method_40360(BuiltinRegistries.CONFIGURED_FEATURE, id, configuredFeature);
    }
}
