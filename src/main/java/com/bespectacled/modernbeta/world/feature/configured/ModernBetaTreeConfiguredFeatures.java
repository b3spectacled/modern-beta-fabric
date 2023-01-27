package com.bespectacled.modernbeta.world.feature.configured;

import com.bespectacled.modernbeta.world.feature.ModernBetaFeatures;

import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;

public class ModernBetaTreeConfiguredFeatures {
    public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> OLD_FANCY_OAK = ModernBetaConfiguredFeatures.register(
        "old_fancy_oak",
        ModernBetaFeatures.OLD_FANCY_OAK,
        FeatureConfig.DEFAULT
    );   
}   
