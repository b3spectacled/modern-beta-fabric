package mod.bespectacled.modernbeta.world.biome;

import java.util.function.BiFunction;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.world.biome.biomes.alpha.BiomeAlpha;
import mod.bespectacled.modernbeta.world.biome.biomes.alpha.BiomeAlphaWinter;
import mod.bespectacled.modernbeta.world.biome.biomes.beta.BiomeBetaColdOcean;
import mod.bespectacled.modernbeta.world.biome.biomes.beta.BiomeBetaDesert;
import mod.bespectacled.modernbeta.world.biome.biomes.beta.BiomeBetaForest;
import mod.bespectacled.modernbeta.world.biome.biomes.beta.BiomeBetaFrozenOcean;
import mod.bespectacled.modernbeta.world.biome.biomes.beta.BiomeBetaIceDesert;
import mod.bespectacled.modernbeta.world.biome.biomes.beta.BiomeBetaLukewarmOcean;
import mod.bespectacled.modernbeta.world.biome.biomes.beta.BiomeBetaOcean;
import mod.bespectacled.modernbeta.world.biome.biomes.beta.BiomeBetaPlains;
import mod.bespectacled.modernbeta.world.biome.biomes.beta.BiomeBetaRainforest;
import mod.bespectacled.modernbeta.world.biome.biomes.beta.BiomeBetaSavanna;
import mod.bespectacled.modernbeta.world.biome.biomes.beta.BiomeBetaSeasonalForest;
import mod.bespectacled.modernbeta.world.biome.biomes.beta.BiomeBetaShrubland;
import mod.bespectacled.modernbeta.world.biome.biomes.beta.BiomeBetaSky;
import mod.bespectacled.modernbeta.world.biome.biomes.beta.BiomeBetaSwampland;
import mod.bespectacled.modernbeta.world.biome.biomes.beta.BiomeBetaTaiga;
import mod.bespectacled.modernbeta.world.biome.biomes.beta.BiomeBetaTundra;
import mod.bespectacled.modernbeta.world.biome.biomes.beta.BiomeBetaWarmOcean;
import mod.bespectacled.modernbeta.world.biome.biomes.indev.BiomeIndevHell;
import mod.bespectacled.modernbeta.world.biome.biomes.indev.BiomeIndevNormal;
import mod.bespectacled.modernbeta.world.biome.biomes.indev.BiomeIndevParadise;
import mod.bespectacled.modernbeta.world.biome.biomes.indev.BiomeIndevWoods;
import mod.bespectacled.modernbeta.world.biome.biomes.infdev.BiomeInfdev227;
import mod.bespectacled.modernbeta.world.biome.biomes.infdev.BiomeInfdev415;
import mod.bespectacled.modernbeta.world.biome.biomes.infdev.BiomeInfdev420;
import mod.bespectacled.modernbeta.world.biome.biomes.infdev.BiomeInfdev611;
import mod.bespectacled.modernbeta.world.biome.biomes.pe.BiomePEColdOcean;
import mod.bespectacled.modernbeta.world.biome.biomes.pe.BiomePEDesert;
import mod.bespectacled.modernbeta.world.biome.biomes.pe.BiomePEForest;
import mod.bespectacled.modernbeta.world.biome.biomes.pe.BiomePEFrozenOcean;
import mod.bespectacled.modernbeta.world.biome.biomes.pe.BiomePEIceDesert;
import mod.bespectacled.modernbeta.world.biome.biomes.pe.BiomePELukewarmOcean;
import mod.bespectacled.modernbeta.world.biome.biomes.pe.BiomePEOcean;
import mod.bespectacled.modernbeta.world.biome.biomes.pe.BiomePEPlains;
import mod.bespectacled.modernbeta.world.biome.biomes.pe.BiomePERainforest;
import mod.bespectacled.modernbeta.world.biome.biomes.pe.BiomePESavanna;
import mod.bespectacled.modernbeta.world.biome.biomes.pe.BiomePESeasonalForest;
import mod.bespectacled.modernbeta.world.biome.biomes.pe.BiomePEShrubland;
import mod.bespectacled.modernbeta.world.biome.biomes.pe.BiomePESwampland;
import mod.bespectacled.modernbeta.world.biome.biomes.pe.BiomePETaiga;
import mod.bespectacled.modernbeta.world.biome.biomes.pe.BiomePETundra;
import mod.bespectacled.modernbeta.world.biome.biomes.pe.BiomePEWarmOcean;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.PlacedFeature;

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
    
    public static final Identifier BETA_SKY_ID = ModernBeta.createId("beta_sky");
    
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
        register(biomeRegisterable, BETA_FOREST_ID, BiomeBetaForest::create);
        register(biomeRegisterable, BETA_SHRUBLAND_ID, BiomeBetaShrubland::create);
        register(biomeRegisterable, BETA_DESERT_ID, BiomeBetaDesert::create);
        register(biomeRegisterable, BETA_SAVANNA_ID, BiomeBetaSavanna::create);
        register(biomeRegisterable, BETA_PLAINS_ID, BiomeBetaPlains::create);
        register(biomeRegisterable, BETA_SEASONAL_FOREST_ID, BiomeBetaSeasonalForest::create);
        register(biomeRegisterable, BETA_RAINFOREST_ID, BiomeBetaRainforest::create);
        register(biomeRegisterable, BETA_SWAMPLAND_ID, BiomeBetaSwampland::create);
        register(biomeRegisterable, BETA_TAIGA_ID, BiomeBetaTaiga::create);
        register(biomeRegisterable, BETA_TUNDRA_ID, BiomeBetaTundra::create);
        register(biomeRegisterable, BETA_ICE_DESERT_ID, BiomeBetaIceDesert::create);

        register(biomeRegisterable, BETA_OCEAN_ID, BiomeBetaOcean::create);
        register(biomeRegisterable, BETA_LUKEWARM_OCEAN_ID, BiomeBetaLukewarmOcean::create);
        register(biomeRegisterable, BETA_WARM_OCEAN_ID, BiomeBetaWarmOcean::create);
        register(biomeRegisterable, BETA_COLD_OCEAN_ID, BiomeBetaColdOcean::create);
        register(biomeRegisterable, BETA_FROZEN_OCEAN_ID, BiomeBetaFrozenOcean::create);
        
        register(biomeRegisterable, BETA_SKY_ID, BiomeBetaSky::create);

        register(biomeRegisterable, PE_FOREST_ID, BiomePEForest::create);
        register(biomeRegisterable, PE_SHRUBLAND_ID, BiomePEShrubland::create);
        register(biomeRegisterable, PE_DESERT_ID, BiomePEDesert::create);
        register(biomeRegisterable, PE_SAVANNA_ID, BiomePESavanna::create);
        register(biomeRegisterable, PE_PLAINS_ID, BiomePEPlains::create);
        register(biomeRegisterable, PE_SEASONAL_FOREST_ID, BiomePESeasonalForest::create);
        register(biomeRegisterable, PE_RAINFOREST_ID, BiomePERainforest::create);
        register(biomeRegisterable, PE_SWAMPLAND_ID, BiomePESwampland::create);
        register(biomeRegisterable, PE_TAIGA_ID, BiomePETaiga::create);
        register(biomeRegisterable, PE_TUNDRA_ID, BiomePETundra::create);
        register(biomeRegisterable, PE_ICE_DESERT_ID, BiomePEIceDesert::create);

        register(biomeRegisterable, PE_OCEAN_ID, BiomePEOcean::create);
        register(biomeRegisterable, PE_LUKEWARM_OCEAN_ID, BiomePELukewarmOcean::create);
        register(biomeRegisterable, PE_WARM_OCEAN_ID, BiomePEWarmOcean::create);
        register(biomeRegisterable, PE_COLD_OCEAN_ID, BiomePEColdOcean::create);
        register(biomeRegisterable, PE_FROZEN_OCEAN_ID, BiomePEFrozenOcean::create);

        register(biomeRegisterable, ALPHA_ID, BiomeAlpha::create);
        register(biomeRegisterable, ALPHA_WINTER_ID, BiomeAlphaWinter::create);

        register(biomeRegisterable, INFDEV_611_ID, BiomeInfdev611::create);
        register(biomeRegisterable, INFDEV_420_ID, BiomeInfdev420::create);
        register(biomeRegisterable, INFDEV_415_ID, BiomeInfdev415::create);
        register(biomeRegisterable, INFDEV_227_ID, BiomeInfdev227::create);

        register(biomeRegisterable, INDEV_NORMAL_ID, BiomeIndevNormal::create);
        register(biomeRegisterable, INDEV_HELL_ID, BiomeIndevHell::create);
        register(biomeRegisterable, INDEV_PARADISE_ID, BiomeIndevParadise::create);
        register(biomeRegisterable, INDEV_WOODS_ID, BiomeIndevWoods::create);
    }
    
    private static void register(Registerable<Biome> biomeRegisterable, Identifier id, BiFunction<RegistryEntryLookup<PlacedFeature>, RegistryEntryLookup<ConfiguredCarver<?>>, Biome> biomeCreator) {
        RegistryEntryLookup<PlacedFeature> registryFeature = biomeRegisterable.getRegistryLookup(RegistryKeys.PLACED_FEATURE);
        RegistryEntryLookup<ConfiguredCarver<?>> registryCarver = biomeRegisterable.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER);
        
        biomeRegisterable.register(keyOf(id), biomeCreator.apply(registryFeature, registryCarver));
    }
    
    private static RegistryKey<Biome> keyOf(Identifier id) {
        return RegistryKey.of(RegistryKeys.BIOME, id);
    }
}
