package mod.bespectacled.modernbeta.world.biome.inf;

import mod.bespectacled.modernbeta.world.biome.OldBiomeColors;
import mod.bespectacled.modernbeta.world.biome.OldBiomeFeatures;
import mod.bespectacled.modernbeta.world.biome.OldBiomeMobs;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;

public class Infdev420Winter {
    public static final Biome BIOME = create();
    
    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        OldBiomeMobs.addCommonMobs(spawnSettings);
        OldBiomeMobs.addSquid(spawnSettings);
        OldBiomeMobs.addWolves(spawnSettings);
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        OldBiomeFeatures.addInfdev420Features(genSettings);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.SNOW)
            .category(Biome.Category.FOREST)
            .temperature(0.0F)
            .downfall(0.0F)
            .effects((new BiomeEffects.Builder())
                .grassColor(OldBiomeColors.OLD_GRASS_COLOR)
                .foliageColor(OldBiomeColors.OLD_FOLIAGE_COLOR)
                .skyColor(OldBiomeColors.INFDEV_420_SKY_COLOR)
                .fogColor(OldBiomeColors.INFDEV_420_FOG_COLOR)
                .waterColor(OldBiomeColors.OLD_WATER_COLOR)
                .waterFogColor(OldBiomeColors.OLD_WATER_FOG_COLOR)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}