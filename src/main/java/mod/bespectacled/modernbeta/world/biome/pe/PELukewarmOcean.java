package mod.bespectacled.modernbeta.world.biome.pe;

import mod.bespectacled.modernbeta.world.biome.OldBiomeColors;
import mod.bespectacled.modernbeta.world.biome.OldBiomeFeatures;
import mod.bespectacled.modernbeta.world.biome.OldBiomeMobs;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;

public class PELukewarmOcean {
    public static final Biome BIOME = create();
    
    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        OldBiomeMobs.addLukewarmOceanMobs(spawnSettings);
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        OldBiomeFeatures.addLukewarmOceanFeatures(genSettings, true);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.RAIN)
            .category(Biome.Category.OCEAN)
            .temperature(1.0F)
            .downfall(0.7F)
            .effects((new BiomeEffects.Builder())
                .skyColor(OldBiomeColors.PE_SKY_COLOR)
                .fogColor(OldBiomeColors.PE_FOG_COLOR)
                .waterColor(OldBiomeColors.OLD_WATER_COLOR)
                .waterFogColor(OldBiomeColors.OLD_WATER_FOG_COLOR)
                .grassColor(OldBiomeColors.PE_GRASS_COLOR)
                .foliageColor(OldBiomeColors.PE_FOLIAGE_COLOR)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}