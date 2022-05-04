package com.bespectacled.modernbeta.world.biome.beta;

import com.bespectacled.modernbeta.world.biome.OldBiomeColors;
import com.bespectacled.modernbeta.world.biome.OldBiomeFeatures;
import com.bespectacled.modernbeta.world.biome.OldBiomeMobs;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;

public class Sky {
    public static final Biome BIOME = create();
    
    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        OldBiomeMobs.addSkyMobs(spawnSettings);
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        OldBiomeFeatures.addSkyFeatures(genSettings);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.NONE)
            .category(Biome.Category.PLAINS)
            .temperature(0.5F)
            .downfall(0.0F)
            .effects((new BiomeEffects.Builder())
                .skyColor(OldBiomeColors.SKYLANDS_SKY_COLOR)
                .fogColor(OldBiomeColors.SKYLANDS_FOG_COLOR)
                .waterColor(OldBiomeColors.VANILLA_WATER_COLOR)
                .waterFogColor(OldBiomeColors.VANILLA_WATER_FOG_COLOR)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
