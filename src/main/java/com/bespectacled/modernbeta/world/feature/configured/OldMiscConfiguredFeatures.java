package com.bespectacled.modernbeta.world.feature.configured;

import com.bespectacled.modernbeta.world.feature.OldFeatures;

import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class OldMiscConfiguredFeatures {
    public static final ConfiguredFeature<?, ?> BETA_FREEZE_TOP_LAYER = OldConfiguredFeatures.register(
        "beta_freeze_top_layer", OldFeatures.BETA_FREEZE_TOP_LAYER.configure(FeatureConfig.DEFAULT)
    );
}
