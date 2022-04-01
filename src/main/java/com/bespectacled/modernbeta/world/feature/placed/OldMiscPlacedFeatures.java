package com.bespectacled.modernbeta.world.feature.placed;

import com.bespectacled.modernbeta.world.feature.configured.OldMiscConfiguredFeatures;

import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;

public class OldMiscPlacedFeatures {
    public static final RegistryEntry<PlacedFeature> BETA_FREEZE_TOP_LAYER = OldPlacedFeatures.register(
        "beta_freeze_top_layer",
        OldMiscConfiguredFeatures.BETA_FREEZE_TOP_LAYER,
        new PlacementModifier[0]
    );
}
