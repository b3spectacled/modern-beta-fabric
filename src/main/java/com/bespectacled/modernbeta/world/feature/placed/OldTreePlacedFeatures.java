package com.bespectacled.modernbeta.world.feature.placed;

import com.bespectacled.modernbeta.world.feature.configured.OldTreeConfiguredFeatures;

import net.minecraft.block.Blocks;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;

public class OldTreePlacedFeatures {
    public static final RegistryEntry<PlacedFeature> OLD_FANCY_OAK = OldPlacedFeatures.register(
        "old_fancy_oak",
        OldTreeConfiguredFeatures.OLD_FANCY_OAK,
        PlacedFeatures.wouldSurvive(Blocks.OAK_SAPLING)
    );
}
