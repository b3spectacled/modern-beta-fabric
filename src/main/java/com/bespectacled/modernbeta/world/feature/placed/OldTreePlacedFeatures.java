package com.bespectacled.modernbeta.world.feature.placed;

import com.bespectacled.modernbeta.world.feature.configured.OldTreeConfiguredFeatures;

import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.PlacedFeature;

public class OldTreePlacedFeatures {
    public static final PlacedFeature OLD_FANCY_OAK = OldPlacedFeatures.register(
        "old_fancy_oak", OldTreeConfiguredFeatures.OLD_FANCY_OAK.withWouldSurviveFilter(Blocks.OAK_SAPLING)
    );
}
