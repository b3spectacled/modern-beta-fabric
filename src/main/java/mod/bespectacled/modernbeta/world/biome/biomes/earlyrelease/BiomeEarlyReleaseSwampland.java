package mod.bespectacled.modernbeta.world.biome.biomes.earlyrelease;

import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeColors;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeFeatures;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeMobs;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.PlacedFeature;

public class BiomeEarlyReleaseSwampland {
    public static Biome create(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        ModernBetaBiomeMobs.addCommonMobs(spawnSettings);
        ModernBetaBiomeMobs.addSwamplandMobs(spawnSettings);

        GenerationSettings.LookupBackedBuilder genSettings = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        ModernBetaBiomeFeatures.addAdventureSwamplandFeatures(genSettings, true);

        return (new Biome.Builder())
            .precipitation(true)
            .temperature(0.8F)
            .downfall(0.9F)
            .effects((new BiomeEffects.Builder())
                .skyColor(OverworldBiomeCreator.getSkyColor(0.8F))
                .fogColor(ModernBetaBiomeColors.BETA_FOG_COLOR)
                .waterColor(ModernBetaBiomeColors.VANILLA_SWAMP_WATER_COLOR)
                .waterFogColor(ModernBetaBiomeColors.VANILLA_SWAMP_WATER_FOG_COLOR)
                .grassColor(0x5C694E)
                .foliageColor(0x496137)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
