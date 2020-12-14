package com.bespectacled.modernbeta.biome;

import java.util.function.Predicate;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.structure.BetaStructure;
import com.google.common.collect.ImmutableList;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.StructureFeature;

@SuppressWarnings("deprecation")
public class VanillaBiomeModifier {
    private static ImmutableList<RegistryKey<Biome>> VANILLA_OCEANS = ImmutableList.of(
        BiomeKeys.OCEAN,
        BiomeKeys.COLD_OCEAN,
        BiomeKeys.FROZEN_OCEAN,
        BiomeKeys.LUKEWARM_OCEAN,
        BiomeKeys.WARM_OCEAN
    );
    
    public static void addShrineToOceans() {
        Predicate<BiomeSelectionContext> biomeSelector = BiomeSelectors.includeByKey(VANILLA_OCEANS);
        BiomeModifications.addStructure(biomeSelector, BetaStructure.OCEAN_SHRINE_KEY);
    }
    
   
    public static void addMonumentToOceans() {
        Predicate<BiomeSelectionContext> biomeSelector = BiomeSelectors.includeByKey(VANILLA_OCEANS);
        BiomeModifications.addStructure(biomeSelector, RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_WORLDGEN, new Identifier("monument")));
    }
    
}
