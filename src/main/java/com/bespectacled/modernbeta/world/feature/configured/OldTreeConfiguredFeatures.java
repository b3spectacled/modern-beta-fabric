package com.bespectacled.modernbeta.world.feature.configured;

import com.bespectacled.modernbeta.world.feature.OldFeatures;

import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class OldTreeConfiguredFeatures {
    public static final ConfiguredFeature<?, ?> OLD_FANCY_OAK = OldConfiguredFeatures.register(
        "old_fancy_oak", OldFeatures.OLD_FANCY_OAK.configure(FeatureConfig.DEFAULT)
    );   
}   
