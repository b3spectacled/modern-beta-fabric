package mod.bespectacled.modernbeta.world.biome.biomes.indev;

import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeColors;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeFeatures;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeMobs;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.PlacedFeature;

public class BiomeIndevParadise {
    public static Biome create(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        ModernBetaBiomeMobs.addCommonMobs(spawnSettings);
        ModernBetaBiomeMobs.addSquid(spawnSettings);
        
        GenerationSettings.LookupBackedBuilder genSettings = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        ModernBetaBiomeFeatures.addIndevParadiseFeatures(genSettings);
        
        return (new Biome.Builder())
            .precipitation(false)
            .temperature(0.6F)
            .downfall(0.6F)
            .effects((new BiomeEffects.Builder())
                .grassColor(ModernBetaBiomeColors.OLD_GRASS_COLOR)
                .foliageColor(ModernBetaBiomeColors.OLD_FOLIAGE_COLOR)
                .skyColor(ModernBetaBiomeColors.INDEV_PARADISE_SKY_COLOR)
                .fogColor(ModernBetaBiomeColors.INDEV_PARADISE_FOG_COLOR)
                .waterColor(ModernBetaBiomeColors.OLD_WATER_COLOR)
                .waterFogColor(ModernBetaBiomeColors.OLD_WATER_FOG_COLOR)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
