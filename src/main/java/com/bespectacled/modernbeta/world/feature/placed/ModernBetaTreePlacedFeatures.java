package com.bespectacled.modernbeta.world.feature.placed;

import com.bespectacled.modernbeta.world.feature.configured.ModernBetaTreeConfiguredFeatures;

import net.minecraft.block.Blocks;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;

public class ModernBetaTreePlacedFeatures {
    public static final RegistryEntry<PlacedFeature> BETA_FANCY_OAK = ModernBetaPlacedFeatures.register(
        "fancy_oak",
        ModernBetaTreeConfiguredFeatures.BETA_FANCY_OAK,
        PlacedFeatures.wouldSurvive(Blocks.OAK_SAPLING)
    );
}
