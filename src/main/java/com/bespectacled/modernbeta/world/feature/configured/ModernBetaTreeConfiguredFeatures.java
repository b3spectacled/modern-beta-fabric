package com.bespectacled.modernbeta.world.feature.configured;

import com.bespectacled.modernbeta.world.feature.ModernBetaFeatures;

import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;

public class ModernBetaTreeConfiguredFeatures {
    public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> BETA_FANCY_OAK = ModernBetaConfiguredFeatures.register(
        "fancy_oak",
        ModernBetaFeatures.OLD_FANCY_OAK,
        FeatureConfig.DEFAULT
    );   
}   
