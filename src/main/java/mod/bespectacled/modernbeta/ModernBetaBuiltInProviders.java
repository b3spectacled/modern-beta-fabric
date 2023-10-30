package mod.bespectacled.modernbeta;

import mod.bespectacled.modernbeta.api.registry.ModernBetaRegistries;
import mod.bespectacled.modernbeta.api.world.blocksource.BlockSource;
import mod.bespectacled.modernbeta.api.world.chunk.noise.NoisePostProcessor;
import mod.bespectacled.modernbeta.api.world.chunk.surface.SurfaceConfig;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsPresets;
import mod.bespectacled.modernbeta.world.biome.HeightConfig;
import mod.bespectacled.modernbeta.world.biome.provider.*;
import mod.bespectacled.modernbeta.world.blocksource.BlockSourceDeepslate;
import mod.bespectacled.modernbeta.world.cavebiome.provider.CaveBiomeProviderNone;
import mod.bespectacled.modernbeta.world.cavebiome.provider.CaveBiomeProviderSingle;
import mod.bespectacled.modernbeta.world.cavebiome.provider.CaveBiomeProviderVoronoi;
import mod.bespectacled.modernbeta.world.chunk.provider.*;

/*
 * Registration of built-in providers for various things.
 *  
 */
public class ModernBetaBuiltInProviders {
    
    // Register default chunk providers
    public static void registerChunkProviders() {
        ModernBetaRegistries.CHUNK.register(ModernBetaBuiltInTypes.DEFAULT_ID, ChunkProviderBeta::new);
        ModernBetaRegistries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.BETA.id, ChunkProviderBeta::new);
        ModernBetaRegistries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.SKYLANDS.id, ChunkProviderSky::new);
        ModernBetaRegistries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.ALPHA.id, ChunkProviderAlpha::new);
        ModernBetaRegistries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.INFDEV_611.id, ChunkProviderInfdev611::new);
        ModernBetaRegistries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.INFDEV_420.id, ChunkProviderInfdev420::new);
        ModernBetaRegistries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.INFDEV_415.id, ChunkProviderInfdev415::new);
        ModernBetaRegistries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.INFDEV_227.id, ChunkProviderInfdev227::new);
        ModernBetaRegistries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.INDEV.id, ChunkProviderIndev::new);
        ModernBetaRegistries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.CLASSIC_0_30.id, ChunkProviderClassic030::new);
        ModernBetaRegistries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.PE.id, ChunkProviderPE::new);
        ModernBetaRegistries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.EARLY_RELEASE.id, ChunkProviderEarlyRelease::new);
    }
    
    // Register default biome providers
    public static void registerBiomeProviders() {
        ModernBetaRegistries.BIOME.register(ModernBetaBuiltInTypes.DEFAULT_ID, BiomeProviderBeta::new);
        ModernBetaRegistries.BIOME.register(ModernBetaBuiltInTypes.Biome.BETA.id, BiomeProviderBeta::new);
        ModernBetaRegistries.BIOME.register(ModernBetaBuiltInTypes.Biome.SINGLE.id, BiomeProviderSingle::new);
        ModernBetaRegistries.BIOME.register(ModernBetaBuiltInTypes.Biome.PE.id, BiomeProviderPE::new);
        ModernBetaRegistries.BIOME.register(ModernBetaBuiltInTypes.Biome.VORONOI.id, BiomeProviderVoronoi::new);
        ModernBetaRegistries.BIOME.register(ModernBetaBuiltInTypes.Biome.FRACTAL.id, BiomeProviderFractal::new);
    }
    
    // Register default cave biome providers
    public static void registerCaveBiomeProviders() {
        ModernBetaRegistries.CAVE_BIOME.register(ModernBetaBuiltInTypes.DEFAULT_ID, CaveBiomeProviderNone::new);
        ModernBetaRegistries.CAVE_BIOME.register(ModernBetaBuiltInTypes.CaveBiome.NONE.id, CaveBiomeProviderNone::new);
        ModernBetaRegistries.CAVE_BIOME.register(ModernBetaBuiltInTypes.CaveBiome.SINGLE.id, CaveBiomeProviderSingle::new);
        ModernBetaRegistries.CAVE_BIOME.register(ModernBetaBuiltInTypes.CaveBiome.VORONOI.id, CaveBiomeProviderVoronoi::new);
    }
    
    public static void registerNoisePostProcessors() {
        ModernBetaRegistries.NOISE_POST_PROCESSOR.register(ModernBetaBuiltInTypes.NoisePostProcessor.NONE.id, NoisePostProcessor.DEFAULT);
    }
    
    public static void registerSurfaceConfigs() {
        ModernBetaRegistries.SURFACE_CONFIG.register(ModernBetaBuiltInTypes.DEFAULT_ID, SurfaceConfig.DEFAULT);
        ModernBetaRegistries.SURFACE_CONFIG.register(ModernBetaBuiltInTypes.SurfaceConfig.SAND.id, SurfaceConfig.SAND);
        ModernBetaRegistries.SURFACE_CONFIG.register(ModernBetaBuiltInTypes.SurfaceConfig.RED_SAND.id, SurfaceConfig.RED_SAND);
        ModernBetaRegistries.SURFACE_CONFIG.register(ModernBetaBuiltInTypes.SurfaceConfig.BADLANDS.id, SurfaceConfig.BADLANDS);
        ModernBetaRegistries.SURFACE_CONFIG.register(ModernBetaBuiltInTypes.SurfaceConfig.NETHER.id, SurfaceConfig.NETHER);
        ModernBetaRegistries.SURFACE_CONFIG.register(ModernBetaBuiltInTypes.SurfaceConfig.WARPED_NYLIUM.id, SurfaceConfig.WARPED_NYLIUM);
        ModernBetaRegistries.SURFACE_CONFIG.register(ModernBetaBuiltInTypes.SurfaceConfig.CRIMSON_NYLIUM.id, SurfaceConfig.CRIMSON_NYLIUM);
        ModernBetaRegistries.SURFACE_CONFIG.register(ModernBetaBuiltInTypes.SurfaceConfig.BASALT.id, SurfaceConfig.BASALT);
        ModernBetaRegistries.SURFACE_CONFIG.register(ModernBetaBuiltInTypes.SurfaceConfig.SOUL_SOIL.id, SurfaceConfig.SOUL_SOIL);
        ModernBetaRegistries.SURFACE_CONFIG.register(ModernBetaBuiltInTypes.SurfaceConfig.THEEND.id, SurfaceConfig.THEEND);
        ModernBetaRegistries.SURFACE_CONFIG.register(ModernBetaBuiltInTypes.SurfaceConfig.GRASS.id, SurfaceConfig.GRASS);
        ModernBetaRegistries.SURFACE_CONFIG.register(ModernBetaBuiltInTypes.SurfaceConfig.MUD.id, SurfaceConfig.MUD);
        ModernBetaRegistries.SURFACE_CONFIG.register(ModernBetaBuiltInTypes.SurfaceConfig.MYCELIUM.id, SurfaceConfig.MYCELIUM);
        ModernBetaRegistries.SURFACE_CONFIG.register(ModernBetaBuiltInTypes.SurfaceConfig.PODZOL.id, SurfaceConfig.PODZOL);
        ModernBetaRegistries.SURFACE_CONFIG.register(ModernBetaBuiltInTypes.SurfaceConfig.STONE.id, SurfaceConfig.STONE);
        ModernBetaRegistries.SURFACE_CONFIG.register(ModernBetaBuiltInTypes.SurfaceConfig.SNOW.id, SurfaceConfig.SNOW);
        ModernBetaRegistries.SURFACE_CONFIG.register(ModernBetaBuiltInTypes.SurfaceConfig.SNOW_DIRT.id, SurfaceConfig.SNOW_DIRT);
        ModernBetaRegistries.SURFACE_CONFIG.register(ModernBetaBuiltInTypes.SurfaceConfig.SNOW_PACKED_ICE.id, SurfaceConfig.SNOW_PACKED_ICE);
        ModernBetaRegistries.SURFACE_CONFIG.register(ModernBetaBuiltInTypes.SurfaceConfig.SNOW_STONE.id, SurfaceConfig.SNOW_STONE);
    }

    public static void registerHeightConfigs() {
        ModernBetaRegistries.HEIGHT_CONFIG.register(ModernBetaBuiltInTypes.DEFAULT_ID, HeightConfig.DEFAULT);
        ModernBetaRegistries.HEIGHT_CONFIG.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_OCEAN.id, HeightConfig.OCEAN);
        ModernBetaRegistries.HEIGHT_CONFIG.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_DESERT.id, HeightConfig.DESERT);
        ModernBetaRegistries.HEIGHT_CONFIG.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_EXTREME_HILLS.id, HeightConfig.EXTREME_HILLS);
        ModernBetaRegistries.HEIGHT_CONFIG.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_BETA_HILLS.id, HeightConfig.BETA_HILLS);
        ModernBetaRegistries.HEIGHT_CONFIG.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_TAIGA.id, HeightConfig.TAIGA);
        ModernBetaRegistries.HEIGHT_CONFIG.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_SWAMPLAND.id, HeightConfig.SWAMPLAND);
        ModernBetaRegistries.HEIGHT_CONFIG.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_RIVER.id, HeightConfig.RIVER);
        ModernBetaRegistries.HEIGHT_CONFIG.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_MOUNTAINS.id, HeightConfig.MOUNTAINS);
        ModernBetaRegistries.HEIGHT_CONFIG.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_MUSHROOM_ISLAND.id, HeightConfig.MUSHROOM_ISLAND);
        ModernBetaRegistries.HEIGHT_CONFIG.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_MUSHROOM_ISLAND_SHORE.id, HeightConfig.MUSHROOM_ISLAND_SHORE);
        ModernBetaRegistries.HEIGHT_CONFIG.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_BEACH.id, HeightConfig.BEACH);
        ModernBetaRegistries.HEIGHT_CONFIG.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_HILLS.id, HeightConfig.HILLS);
        ModernBetaRegistries.HEIGHT_CONFIG.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_SHORT_HILLS.id, HeightConfig.SHORT_HILLS);
        ModernBetaRegistries.HEIGHT_CONFIG.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_EXTREME_HILLS_EDGE.id, HeightConfig.EXTREME_HILLS_EDGE);
        ModernBetaRegistries.HEIGHT_CONFIG.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_JUNGLE.id, HeightConfig.JUNGLE);
        ModernBetaRegistries.HEIGHT_CONFIG.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_JUNGLE_HILLS.id, HeightConfig.JUNGLE_HILLS);
        ModernBetaRegistries.HEIGHT_CONFIG.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_PLATEAU.id, HeightConfig.PLATEAU);
        ModernBetaRegistries.HEIGHT_CONFIG.register(ModernBetaBuiltInTypes.HeightConfig.HEIGHT_CONFIG_SWAMPLAND_HILLS.id, HeightConfig.SWAMPLAND_HILLS);
    }
    
    public static void registerBlockSources() {
        ModernBetaRegistries.BLOCKSOURCE.register(ModernBetaBuiltInTypes.DEFAULT_ID, (settings, deriver) -> BlockSource.DEFAULT);
        ModernBetaRegistries.BLOCKSOURCE.register(ModernBetaBuiltInTypes.BlockSource.DEEPSLATE.id, BlockSourceDeepslate::new);
    }
    
    public static void registerSettingsPresets() {
        ModernBetaRegistries.SETTINGS_PRESET.register(ModernBetaBuiltInTypes.DEFAULT_ID, ModernBetaSettingsPresets.PRESET_BETA);
        ModernBetaRegistries.SETTINGS_PRESET.register(ModernBetaBuiltInTypes.Preset.BETA.id, ModernBetaSettingsPresets.PRESET_BETA);
        ModernBetaRegistries.SETTINGS_PRESET.register(ModernBetaBuiltInTypes.Preset.SKYLANDS.id, ModernBetaSettingsPresets.PRESET_SKYLANDS);
        ModernBetaRegistries.SETTINGS_PRESET.register(ModernBetaBuiltInTypes.Preset.ALPHA.id, ModernBetaSettingsPresets.PRESET_ALPHA);
        ModernBetaRegistries.SETTINGS_PRESET.register(ModernBetaBuiltInTypes.Preset.INFDEV_611.id, ModernBetaSettingsPresets.PRESET_INFDEV_611);
        ModernBetaRegistries.SETTINGS_PRESET.register(ModernBetaBuiltInTypes.Preset.INFDEV_420.id, ModernBetaSettingsPresets.PRESET_INFDEV_420);
        ModernBetaRegistries.SETTINGS_PRESET.register(ModernBetaBuiltInTypes.Preset.INFDEV_415.id, ModernBetaSettingsPresets.PRESET_INFDEV_415);
        ModernBetaRegistries.SETTINGS_PRESET.register(ModernBetaBuiltInTypes.Preset.INFDEV_325.id, ModernBetaSettingsPresets.PRESET_INFDEV_325);
        ModernBetaRegistries.SETTINGS_PRESET.register(ModernBetaBuiltInTypes.Preset.INFDEV_227.id, ModernBetaSettingsPresets.PRESET_INFDEV_227);
        ModernBetaRegistries.SETTINGS_PRESET.register(ModernBetaBuiltInTypes.Preset.INDEV.id, ModernBetaSettingsPresets.PRESET_INDEV);
        ModernBetaRegistries.SETTINGS_PRESET.register(ModernBetaBuiltInTypes.Preset.CLASSIC_0_30.id, ModernBetaSettingsPresets.PRESET_CLASSIC);
        ModernBetaRegistries.SETTINGS_PRESET.register(ModernBetaBuiltInTypes.Preset.PE.id, ModernBetaSettingsPresets.PRESET_PE);
        ModernBetaRegistries.SETTINGS_PRESET.register(ModernBetaBuiltInTypes.Preset.BETA_1_8_1.id, ModernBetaSettingsPresets.PRESET_BETA_1_8_1);
        ModernBetaRegistries.SETTINGS_PRESET.register(ModernBetaBuiltInTypes.Preset.RELEASE_1_0_0.id, ModernBetaSettingsPresets.PRESET_RELEASE_1_0_0);
        ModernBetaRegistries.SETTINGS_PRESET.register(ModernBetaBuiltInTypes.Preset.RELEASE_1_1.id, ModernBetaSettingsPresets.PRESET_RELEASE_1_1);
        ModernBetaRegistries.SETTINGS_PRESET.register(ModernBetaBuiltInTypes.Preset.RELEASE_1_2_5.id, ModernBetaSettingsPresets.PRESET_RELEASE_1_2_5);
        ModernBetaRegistries.SETTINGS_PRESET.register(ModernBetaBuiltInTypes.Preset.RELEASE_1_6_4.id, ModernBetaSettingsPresets.PRESET_RELEASE_1_6_4);
    }
    
    public static void registerSettingsPresetAlts() {
        ModernBetaRegistries.SETTINGS_PRESET_ALT.register(ModernBetaBuiltInTypes.PresetAlt.BETA_SKYLANDS.id, ModernBetaSettingsPresets.PRESET_BETA_SKYLANDS);
        ModernBetaRegistries.SETTINGS_PRESET_ALT.register(ModernBetaBuiltInTypes.PresetAlt.BETA_ISLES.id, ModernBetaSettingsPresets.PRESET_BETA_ISLES);
        ModernBetaRegistries.SETTINGS_PRESET_ALT.register(ModernBetaBuiltInTypes.PresetAlt.BETA_ISLE_LAND.id, ModernBetaSettingsPresets.PRESET_BETA_ISLE_LAND);
        ModernBetaRegistries.SETTINGS_PRESET_ALT.register(ModernBetaBuiltInTypes.PresetAlt.BETA_CAVE_DELIGHT.id, ModernBetaSettingsPresets.PRESET_BETA_CAVE_DELIGHT);
        ModernBetaRegistries.SETTINGS_PRESET_ALT.register(ModernBetaBuiltInTypes.PresetAlt.BETA_CAVE_CHAOS.id, ModernBetaSettingsPresets.PRESET_BETA_CAVE_CHAOS);
        ModernBetaRegistries.SETTINGS_PRESET_ALT.register(ModernBetaBuiltInTypes.PresetAlt.BETA_LARGE_BIOMES.id, ModernBetaSettingsPresets.PRESET_BETA_LARGE_BIOMES);
        ModernBetaRegistries.SETTINGS_PRESET_ALT.register(ModernBetaBuiltInTypes.PresetAlt.BETA_XBOX_LEGACY.id, ModernBetaSettingsPresets.PRESET_BETA_XBOX_LEGACY);
        ModernBetaRegistries.SETTINGS_PRESET_ALT.register(ModernBetaBuiltInTypes.PresetAlt.BETA_SURVIVAL_ISLAND.id, ModernBetaSettingsPresets.PRESET_BETA_SURVIVAL_ISLAND);
        ModernBetaRegistries.SETTINGS_PRESET_ALT.register(ModernBetaBuiltInTypes.PresetAlt.BETA_VANILLA.id, ModernBetaSettingsPresets.PRESET_BETA_VANILLA);
        ModernBetaRegistries.SETTINGS_PRESET_ALT.register(ModernBetaBuiltInTypes.PresetAlt.BETA_HYBRID.id, ModernBetaSettingsPresets.PRESET_BETA_HYBRID);
        ModernBetaRegistries.SETTINGS_PRESET_ALT.register(ModernBetaBuiltInTypes.PresetAlt.RELEASE_HYBRID.id, ModernBetaSettingsPresets.PRESET_RELEASE_HYBRID);
        ModernBetaRegistries.SETTINGS_PRESET_ALT.register(ModernBetaBuiltInTypes.PresetAlt.ALPHA_WINTER.id, ModernBetaSettingsPresets.PRESET_ALPHA_WINTER);
        ModernBetaRegistries.SETTINGS_PRESET_ALT.register(ModernBetaBuiltInTypes.PresetAlt.INDEV_PARADISE.id, ModernBetaSettingsPresets.PRESET_INDEV_PARADISE);
        ModernBetaRegistries.SETTINGS_PRESET_ALT.register(ModernBetaBuiltInTypes.PresetAlt.INDEV_WOODS.id, ModernBetaSettingsPresets.PRESET_INDEV_WOODS);
        ModernBetaRegistries.SETTINGS_PRESET_ALT.register(ModernBetaBuiltInTypes.PresetAlt.INDEV_HELL.id, ModernBetaSettingsPresets.PRESET_INDEV_HELL);
    }
}
