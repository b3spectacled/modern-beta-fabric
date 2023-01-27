package com.bespectacled.modernbeta.world.feature.placed;

import com.bespectacled.modernbeta.world.feature.configured.ModernBetaMiscConfiguredFeatures;

import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;

public class ModernBetaMiscPlacedFeatures {
    public static final RegistryEntry<PlacedFeature> BETA_FREEZE_TOP_LAYER = ModernBetaPlacedFeatures.register(
        "freeze_top_layer",
        ModernBetaMiscConfiguredFeatures.FREEZE_TOP_LAYER,
        new PlacementModifier[0]
    );
}
