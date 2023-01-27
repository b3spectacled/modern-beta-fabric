package com.bespectacled.modernbeta.world.biome.pe;

import com.bespectacled.modernbeta.world.biome.ModernBetaBiomeColors;
import com.bespectacled.modernbeta.world.biome.ModernBetaBiomeFeatures;
import com.bespectacled.modernbeta.world.biome.ModernBetaBiomeMobs;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;

public class PEIceDesert {
    public static final Biome BIOME = create();
    
    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        ModernBetaBiomeMobs.addTundraMobs(spawnSettings);
        ModernBetaBiomeMobs.addSquid(spawnSettings);
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        ModernBetaBiomeFeatures.addIceDesertFeatures(genSettings, true);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.SNOW)
            .category(Biome.Category.DESERT)
            .temperature(0.0F)
            .downfall(0.5F)
            .effects((new BiomeEffects.Builder())
                .skyColor(ModernBetaBiomeColors.PE_SKY_COLOR)
                .fogColor(ModernBetaBiomeColors.PE_FOG_COLOR)
                .waterColor(ModernBetaBiomeColors.OLD_WATER_COLOR)
                .waterFogColor(ModernBetaBiomeColors.OLD_WATER_FOG_COLOR)
                .grassColor(ModernBetaBiomeColors.PE_GRASS_COLOR)
                .foliageColor(ModernBetaBiomeColors.PE_FOLIAGE_COLOR)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
