package com.bespectacled.modernbeta.biome.indev;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

public class IndevSnowyEdge {
    public static final Biome BIOME = create();
    
    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        genSettings.surfaceBuilder(ConfiguredSurfaceBuilders.GRASS);

        DefaultBiomeFeatures.addFrozenTopLayer(genSettings);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.RAIN)
            .category(Biome.Category.FOREST)
            .depth(0.37F)
            .scale(0.4F)
            .temperature(0.0F)
            .downfall(0.0F)
            .effects((new BiomeEffects.Builder())
                .grassColor(11272039)
                .foliageColor(5242667)
                .skyColor(10079487)
                .fogColor(16777215)
                .waterColor(2835199)
                .waterFogColor(329011)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
