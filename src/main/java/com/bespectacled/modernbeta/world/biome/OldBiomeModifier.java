package com.bespectacled.modernbeta.world.biome;

import com.google.common.collect.ImmutableList;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public class OldBiomeModifier {
    private static ImmutableList<RegistryKey<Biome>> VANILLA_OCEANS = ImmutableList.of(
        BiomeKeys.OCEAN,
        BiomeKeys.COLD_OCEAN,
        BiomeKeys.FROZEN_OCEAN,
        BiomeKeys.LUKEWARM_OCEAN,
        BiomeKeys.WARM_OCEAN
    );
    
    public static void addShrineToOceans() {
        //Predicate<BiomeSelectionContext> biomeSelector = BiomeSelectors.includeByKey(VANILLA_OCEANS);
        //BiomeModifications.addStructure(biomeSelector, OldStructures.OCEAN_SHRINE_KEY);
    }
}
