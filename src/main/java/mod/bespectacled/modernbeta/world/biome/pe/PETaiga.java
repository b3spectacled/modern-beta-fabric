package mod.bespectacled.modernbeta.world.biome.pe;

import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeColors;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeFeatures;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeMobs;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;

public class PETaiga {
    public static final Biome BIOME = create();
    
    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        ModernBetaBiomeMobs.addCommonMobs(spawnSettings);
        ModernBetaBiomeMobs.addSquid(spawnSettings);
        ModernBetaBiomeMobs.addTaigaMobs(spawnSettings);
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        ModernBetaBiomeFeatures.addTaigaFeatures(genSettings, true);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.SNOW)
            .category(Biome.Category.TAIGA)
            //.temperature(0.4F) TODO: Re-add this later as it looks more accurate; for some reason precipitation currently does not work properly.
            //.downfall(0.8F)
            .temperature(0.0F)
            .downfall(0.5F)
            .effects((new BiomeEffects.Builder())
                //.skyColor(8756991) TODO: Re-add when above fixed
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
