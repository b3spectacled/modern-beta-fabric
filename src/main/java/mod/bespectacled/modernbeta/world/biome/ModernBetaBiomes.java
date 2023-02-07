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
    public static final RegistryKey<Biome> BETA_FOREST = keyOf(ModernBeta.createId(ModernBetaBiomeTags.BETA_FOREST));
    public static final RegistryKey<Biome> BETA_SHRUBLAND = keyOf(ModernBeta.createId(ModernBetaBiomeTags.BETA_SHRUBLAND));
    public static final RegistryKey<Biome> BETA_DESERT = keyOf(ModernBeta.createId(ModernBetaBiomeTags.BETA_DESERT));
    public static final RegistryKey<Biome> BETA_SAVANNA = keyOf(ModernBeta.createId(ModernBetaBiomeTags.BETA_SAVANNA));
    public static final RegistryKey<Biome> BETA_PLAINS = keyOf(ModernBeta.createId(ModernBetaBiomeTags.BETA_PLAINS));
    public static final RegistryKey<Biome> BETA_SEASONAL_FOREST = keyOf(ModernBeta.createId(ModernBetaBiomeTags.BETA_SEASONAL_FOREST));
    public static final RegistryKey<Biome> BETA_RAINFOREST = keyOf(ModernBeta.createId(ModernBetaBiomeTags.BETA_RAINFOREST));
    public static final RegistryKey<Biome> BETA_SWAMPLAND = keyOf(ModernBeta.createId(ModernBetaBiomeTags.BETA_SWAMPLAND));
    public static final RegistryKey<Biome> BETA_TAIGA = keyOf(ModernBeta.createId(ModernBetaBiomeTags.BETA_TAIGA));
    public static final RegistryKey<Biome> BETA_TUNDRA = keyOf(ModernBeta.createId(ModernBetaBiomeTags.BETA_TUNDRA));
    public static final RegistryKey<Biome> BETA_ICE_DESERT = keyOf(ModernBeta.createId(ModernBetaBiomeTags.BETA_ICE_DESERT));

    public static final RegistryKey<Biome> BETA_OCEAN = keyOf(ModernBeta.createId(ModernBetaBiomeTags.BETA_OCEAN));
    public static final RegistryKey<Biome> BETA_LUKEWARM_OCEAN = keyOf(ModernBeta.createId(ModernBetaBiomeTags.BETA_LUKEWARM_OCEAN));
    public static final RegistryKey<Biome> BETA_WARM_OCEAN = keyOf(ModernBeta.createId(ModernBetaBiomeTags.BETA_WARM_OCEAN));
    public static final RegistryKey<Biome> BETA_COLD_OCEAN = keyOf(ModernBeta.createId(ModernBetaBiomeTags.BETA_COLD_OCEAN));
    public static final RegistryKey<Biome> BETA_FROZEN_OCEAN = keyOf(ModernBeta.createId(ModernBetaBiomeTags.BETA_FROZEN_OCEAN));
    
    public static final RegistryKey<Biome> BETA_SKY = keyOf(ModernBeta.createId(ModernBetaBiomeTags.BETA_SKY));
    
    public static final RegistryKey<Biome> PE_FOREST = keyOf(ModernBeta.createId(ModernBetaBiomeTags.PE_FOREST));
    public static final RegistryKey<Biome> PE_SHRUBLAND = keyOf(ModernBeta.createId(ModernBetaBiomeTags.PE_SHRUBLAND));
    public static final RegistryKey<Biome> PE_DESERT = keyOf(ModernBeta.createId(ModernBetaBiomeTags.PE_DESERT));
    public static final RegistryKey<Biome> PE_SAVANNA = keyOf(ModernBeta.createId(ModernBetaBiomeTags.PE_SAVANNA));
    public static final RegistryKey<Biome> PE_PLAINS = keyOf(ModernBeta.createId(ModernBetaBiomeTags.PE_PLAINS));
    public static final RegistryKey<Biome> PE_SEASONAL_FOREST = keyOf(ModernBeta.createId(ModernBetaBiomeTags.PE_SEASONAL_FOREST));
    public static final RegistryKey<Biome> PE_RAINFOREST = keyOf(ModernBeta.createId(ModernBetaBiomeTags.PE_RAINFOREST));
    public static final RegistryKey<Biome> PE_SWAMPLAND = keyOf(ModernBeta.createId(ModernBetaBiomeTags.PE_SWAMPLAND));
    public static final RegistryKey<Biome> PE_TAIGA = keyOf(ModernBeta.createId(ModernBetaBiomeTags.PE_TAIGA));
    public static final RegistryKey<Biome> PE_TUNDRA = keyOf(ModernBeta.createId(ModernBetaBiomeTags.PE_TUNDRA));
    public static final RegistryKey<Biome> PE_ICE_DESERT = keyOf(ModernBeta.createId(ModernBetaBiomeTags.PE_ICE_DESERT));
    
    public static final RegistryKey<Biome> PE_OCEAN = keyOf(ModernBeta.createId(ModernBetaBiomeTags.PE_OCEAN));
    public static final RegistryKey<Biome> PE_LUKEWARM_OCEAN = keyOf(ModernBeta.createId(ModernBetaBiomeTags.PE_LUKEWARM_OCEAN));
    public static final RegistryKey<Biome> PE_WARM_OCEAN = keyOf(ModernBeta.createId(ModernBetaBiomeTags.PE_WARM_OCEAN));
    public static final RegistryKey<Biome> PE_COLD_OCEAN = keyOf(ModernBeta.createId(ModernBetaBiomeTags.PE_COLD_OCEAN));
    public static final RegistryKey<Biome> PE_FROZEN_OCEAN = keyOf(ModernBeta.createId(ModernBetaBiomeTags.PE_FROZEN_OCEAN));
    
    public static final RegistryKey<Biome> ALPHA = keyOf(ModernBeta.createId(ModernBetaBiomeTags.ALPHA));
    public static final RegistryKey<Biome> ALPHA_WINTER = keyOf(ModernBeta.createId(ModernBetaBiomeTags.ALPHA_WINTER));
    
    public static final RegistryKey<Biome> INFDEV_611 = keyOf(ModernBeta.createId(ModernBetaBiomeTags.INFDEV_611));
    public static final RegistryKey<Biome> INFDEV_420 = keyOf(ModernBeta.createId(ModernBetaBiomeTags.INFDEV_420));
    public static final RegistryKey<Biome> INFDEV_415 = keyOf(ModernBeta.createId(ModernBetaBiomeTags.INFDEV_415));
    public static final RegistryKey<Biome> INFDEV_227 = keyOf(ModernBeta.createId(ModernBetaBiomeTags.INFDEV_227));
    
    public static final RegistryKey<Biome> INDEV_NORMAL = keyOf(ModernBeta.createId(ModernBetaBiomeTags.INDEV_NORMAL));
    public static final RegistryKey<Biome> INDEV_HELL = keyOf(ModernBeta.createId(ModernBetaBiomeTags.INDEV_HELL));
    public static final RegistryKey<Biome> INDEV_PARADISE = keyOf(ModernBeta.createId(ModernBetaBiomeTags.INDEV_PARADISE));
    public static final RegistryKey<Biome> INDEV_WOODS = keyOf(ModernBeta.createId(ModernBetaBiomeTags.INDEV_WOODS));
    
    public static void bootstrap(Registerable<Biome> biomeRegisterable) {
        register(biomeRegisterable, BETA_FOREST, BiomeBetaForest::create);
        register(biomeRegisterable, BETA_SHRUBLAND, BiomeBetaShrubland::create);
        register(biomeRegisterable, BETA_DESERT, BiomeBetaDesert::create);
        register(biomeRegisterable, BETA_SAVANNA, BiomeBetaSavanna::create);
        register(biomeRegisterable, BETA_PLAINS, BiomeBetaPlains::create);
        register(biomeRegisterable, BETA_SEASONAL_FOREST, BiomeBetaSeasonalForest::create);
        register(biomeRegisterable, BETA_RAINFOREST, BiomeBetaRainforest::create);
        register(biomeRegisterable, BETA_SWAMPLAND, BiomeBetaSwampland::create);
        register(biomeRegisterable, BETA_TAIGA, BiomeBetaTaiga::create);
        register(biomeRegisterable, BETA_TUNDRA, BiomeBetaTundra::create);
        register(biomeRegisterable, BETA_ICE_DESERT, BiomeBetaIceDesert::create);

        register(biomeRegisterable, BETA_OCEAN, BiomeBetaOcean::create);
        register(biomeRegisterable, BETA_LUKEWARM_OCEAN, BiomeBetaLukewarmOcean::create);
        register(biomeRegisterable, BETA_WARM_OCEAN, BiomeBetaWarmOcean::create);
        register(biomeRegisterable, BETA_COLD_OCEAN, BiomeBetaColdOcean::create);
        register(biomeRegisterable, BETA_FROZEN_OCEAN, BiomeBetaFrozenOcean::create);
        
        register(biomeRegisterable, BETA_SKY, BiomeBetaSky::create);

        register(biomeRegisterable, PE_FOREST, BiomePEForest::create);
        register(biomeRegisterable, PE_SHRUBLAND, BiomePEShrubland::create);
        register(biomeRegisterable, PE_DESERT, BiomePEDesert::create);
        register(biomeRegisterable, PE_SAVANNA, BiomePESavanna::create);
        register(biomeRegisterable, PE_PLAINS, BiomePEPlains::create);
        register(biomeRegisterable, PE_SEASONAL_FOREST, BiomePESeasonalForest::create);
        register(biomeRegisterable, PE_RAINFOREST, BiomePERainforest::create);
        register(biomeRegisterable, PE_SWAMPLAND, BiomePESwampland::create);
        register(biomeRegisterable, PE_TAIGA, BiomePETaiga::create);
        register(biomeRegisterable, PE_TUNDRA, BiomePETundra::create);
        register(biomeRegisterable, PE_ICE_DESERT, BiomePEIceDesert::create);

        register(biomeRegisterable, PE_OCEAN, BiomePEOcean::create);
        register(biomeRegisterable, PE_LUKEWARM_OCEAN, BiomePELukewarmOcean::create);
        register(biomeRegisterable, PE_WARM_OCEAN, BiomePEWarmOcean::create);
        register(biomeRegisterable, PE_COLD_OCEAN, BiomePEColdOcean::create);
        register(biomeRegisterable, PE_FROZEN_OCEAN, BiomePEFrozenOcean::create);

        register(biomeRegisterable, ALPHA, BiomeAlpha::create);
        register(biomeRegisterable, ALPHA_WINTER, BiomeAlphaWinter::create);

        register(biomeRegisterable, INFDEV_611, BiomeInfdev611::create);
        register(biomeRegisterable, INFDEV_420, BiomeInfdev420::create);
        register(biomeRegisterable, INFDEV_415, BiomeInfdev415::create);
        register(biomeRegisterable, INFDEV_227, BiomeInfdev227::create);

        register(biomeRegisterable, INDEV_NORMAL, BiomeIndevNormal::create);
        register(biomeRegisterable, INDEV_HELL, BiomeIndevHell::create);
        register(biomeRegisterable, INDEV_PARADISE, BiomeIndevParadise::create);
        register(biomeRegisterable, INDEV_WOODS, BiomeIndevWoods::create);
    }
    
    private static void register(Registerable<Biome> biomeRegisterable, RegistryKey<Biome> biome, BiFunction<RegistryEntryLookup<PlacedFeature>, RegistryEntryLookup<ConfiguredCarver<?>>, Biome> biomeCreator) {
        RegistryEntryLookup<PlacedFeature> registryFeature = biomeRegisterable.getRegistryLookup(RegistryKeys.PLACED_FEATURE);
        RegistryEntryLookup<ConfiguredCarver<?>> registryCarver = biomeRegisterable.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER);
        
        biomeRegisterable.register(biome, biomeCreator.apply(registryFeature, registryCarver));
    }
    
    private static RegistryKey<Biome> keyOf(Identifier id) {
        return RegistryKey.of(RegistryKeys.BIOME, id);
    }
}
