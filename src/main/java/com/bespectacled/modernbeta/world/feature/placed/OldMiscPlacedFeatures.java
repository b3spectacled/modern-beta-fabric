package com.bespectacled.modernbeta.world.feature.placed;

import com.bespectacled.modernbeta.world.feature.configured.OldMiscConfiguredFeatures;

import net.minecraft.world.gen.decorator.PlacementModifier;
import net.minecraft.world.gen.feature.PlacedFeature;

public class OldMiscPlacedFeatures {
    public static final PlacedFeature BETA_FREEZE_TOP_LAYER = OldPlacedFeatures.register(
        "beta_freeze_top_layer", OldMiscConfiguredFeatures.BETA_FREEZE_TOP_LAYER.withPlacement(new PlacementModifier[0])
    );
}
