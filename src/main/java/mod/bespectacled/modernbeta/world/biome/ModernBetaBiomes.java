package mod.bespectacled.modernbeta.world.biome;

import mod.bespectacled.modernbeta.ModernBeta;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;

public class ModernBetaBiomes {
    public static final Identifier BETA_FOREST_ID = ModernBeta.createId("beta_forest");
    public static final Identifier BETA_SHRUBLAND_ID = ModernBeta.createId("beta_shrubland");
    public static final Identifier BETA_DESERT_ID = ModernBeta.createId("beta_desert");
    public static final Identifier BETA_SAVANNA_ID = ModernBeta.createId("beta_savanna");
    public static final Identifier BETA_PLAINS_ID = ModernBeta.createId("beta_plains");
    public static final Identifier BETA_SEASONAL_FOREST_ID = ModernBeta.createId("beta_seasonal_forest");
    public static final Identifier BETA_RAINFOREST_ID = ModernBeta.createId("beta_rainforest");
    public static final Identifier BETA_SWAMPLAND_ID = ModernBeta.createId("beta_swampland");
    public static final Identifier BETA_TAIGA_ID = ModernBeta.createId("beta_taiga");
    public static final Identifier BETA_TUNDRA_ID = ModernBeta.createId("beta_tundra");
    public static final Identifier BETA_ICE_DESERT_ID = ModernBeta.createId("beta_ice_desert");

    public static final Identifier BETA_OCEAN_ID = ModernBeta.createId("beta_ocean");
    public static final Identifier BETA_LUKEWARM_OCEAN_ID = ModernBeta.createId("beta_lukewarm_ocean");
    public static final Identifier BETA_WARM_OCEAN_ID = ModernBeta.createId("beta_warm_ocean");
    public static final Identifier BETA_COLD_OCEAN_ID = ModernBeta.createId("beta_cold_ocean");
    public static final Identifier BETA_FROZEN_OCEAN_ID = ModernBeta.createId("beta_frozen_ocean");
    
    public static final Identifier PE_FOREST_ID = ModernBeta.createId("pe_forest");
    public static final Identifier PE_SHRUBLAND_ID = ModernBeta.createId("pe_shrubland");
    public static final Identifier PE_DESERT_ID = ModernBeta.createId("pe_desert");
    public static final Identifier PE_SAVANNA_ID = ModernBeta.createId("pe_savanna");
    public static final Identifier PE_PLAINS_ID = ModernBeta.createId("pe_plains");
    public static final Identifier PE_SEASONAL_FOREST_ID = ModernBeta.createId("pe_seasonal_forest");
    public static final Identifier PE_RAINFOREST_ID = ModernBeta.createId("pe_rainforest");
    public static final Identifier PE_SWAMPLAND_ID = ModernBeta.createId("pe_swampland");
    public static final Identifier PE_TAIGA_ID = ModernBeta.createId("pe_taiga");
    public static final Identifier PE_TUNDRA_ID = ModernBeta.createId("pe_tundra");
    public static final Identifier PE_ICE_DESERT_ID = ModernBeta.createId("pe_ice_desert");
    
    public static final Identifier PE_OCEAN_ID = ModernBeta.createId("pe_ocean");
    public static final Identifier PE_LUKEWARM_OCEAN_ID = ModernBeta.createId("pe_lukewarm_ocean");
    public static final Identifier PE_WARM_OCEAN_ID = ModernBeta.createId("pe_warm_ocean");
    public static final Identifier PE_COLD_OCEAN_ID = ModernBeta.createId("pe_cold_ocean");
    public static final Identifier PE_FROZEN_OCEAN_ID = ModernBeta.createId("pe_frozen_ocean");
    
    public static final Identifier ALPHA_ID = ModernBeta.createId("alpha");
    public static final Identifier ALPHA_WINTER_ID = ModernBeta.createId("alpha_winter");
    
    public static final Identifier INFDEV_611_ID = ModernBeta.createId("infdev_611");
    public static final Identifier INFDEV_420_ID = ModernBeta.createId("infdev_420");
    public static final Identifier INFDEV_415_ID = ModernBeta.createId("infdev_415");
    public static final Identifier INFDEV_227_ID = ModernBeta.createId("infdev_227");
    
    public static final Identifier INDEV_NORMAL_ID = ModernBeta.createId("indev_normal");
    public static final Identifier INDEV_HELL_ID = ModernBeta.createId("indev_hell");
    public static final Identifier INDEV_PARADISE_ID = ModernBeta.createId("indev_paradise");
    public static final Identifier INDEV_WOODS_ID = ModernBeta.createId("indev_woods");
    
    public static void bootstrap(Registerable<Biome> biomeRegisterable) {
        register(biomeRegisterable, BETA_FOREST_ID);
        register(biomeRegisterable, BETA_SHRUBLAND_ID);
        register(biomeRegisterable, BETA_DESERT_ID);
        register(biomeRegisterable, BETA_SAVANNA_ID);
        register(biomeRegisterable, BETA_PLAINS_ID);
        register(biomeRegisterable, BETA_SEASONAL_FOREST_ID);
        register(biomeRegisterable, BETA_RAINFOREST_ID);
        register(biomeRegisterable, BETA_SWAMPLAND_ID);
        register(biomeRegisterable, BETA_TAIGA_ID);
        register(biomeRegisterable, BETA_TUNDRA_ID);
        register(biomeRegisterable, BETA_ICE_DESERT_ID);

        register(biomeRegisterable, BETA_OCEAN_ID);
        register(biomeRegisterable, BETA_LUKEWARM_OCEAN_ID);
        register(biomeRegisterable, BETA_WARM_OCEAN_ID);
        register(biomeRegisterable, BETA_COLD_OCEAN_ID);
        register(biomeRegisterable, BETA_FROZEN_OCEAN_ID);

        register(biomeRegisterable, PE_FOREST_ID);
        register(biomeRegisterable, PE_SHRUBLAND_ID);
        register(biomeRegisterable, PE_DESERT_ID);
        register(biomeRegisterable, PE_SAVANNA_ID);
        register(biomeRegisterable, PE_PLAINS_ID);
        register(biomeRegisterable, PE_SEASONAL_FOREST_ID);
        register(biomeRegisterable, PE_RAINFOREST_ID);
        register(biomeRegisterable, PE_SWAMPLAND_ID);
        register(biomeRegisterable, PE_TAIGA_ID);
        register(biomeRegisterable, PE_TUNDRA_ID);
        register(biomeRegisterable, PE_ICE_DESERT_ID);

        register(biomeRegisterable, PE_OCEAN_ID);
        register(biomeRegisterable, PE_LUKEWARM_OCEAN_ID);
        register(biomeRegisterable, PE_WARM_OCEAN_ID);
        register(biomeRegisterable, PE_COLD_OCEAN_ID);
        register(biomeRegisterable, PE_FROZEN_OCEAN_ID);

        register(biomeRegisterable, ALPHA_ID);
        register(biomeRegisterable, ALPHA_WINTER_ID);

        register(biomeRegisterable, INFDEV_611_ID);
        register(biomeRegisterable, INFDEV_420_ID);
        register(biomeRegisterable, INFDEV_415_ID);
        register(biomeRegisterable, INFDEV_227_ID);

        register(biomeRegisterable, INDEV_NORMAL_ID);
        register(biomeRegisterable, INDEV_HELL_ID);
        register(biomeRegisterable, INDEV_PARADISE_ID);
        register(biomeRegisterable, INDEV_WOODS_ID);
    }
    
    public static void register() { }
    
    private static Biome createPlaceholderBiome() {
        GenerationSettings.Builder builder = new GenerationSettings.Builder();
        SpawnSettings.Builder builder2 = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addPlainsMobs(builder2);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.NONE)
            .temperature(0.5F)
            .downfall(0.5F)
            .effects((new BiomeEffects.Builder())
                .waterColor(4159204)
                .waterFogColor(329011)
                .fogColor(10518688)
                .skyColor(0)
                .moodSound(BiomeMoodSound.CAVE)
                .build()
            )
            .spawnSettings(builder2.build())
            .generationSettings(builder.build())
            .build();
    }
    
    private static void register(Registerable<Biome> biomeRegisterable, Identifier id) {
        biomeRegisterable.register(keyOf(id), createPlaceholderBiome());
    }
    
    private static RegistryKey<Biome> keyOf(Identifier id) {
        return RegistryKey.of(RegistryKeys.BIOME, id);
    }
}
