package mod.bespectacled.modernbeta;

import mod.bespectacled.modernbeta.api.registry.ModernBetaRegistries;
import mod.bespectacled.modernbeta.api.world.blocksource.BlockSource;
import mod.bespectacled.modernbeta.api.world.chunk.noise.NoisePostProcessor;
import mod.bespectacled.modernbeta.api.world.chunk.surface.SurfaceConfig;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsPresets;
import mod.bespectacled.modernbeta.world.biome.provider.BiomeProviderBeta;
import mod.bespectacled.modernbeta.world.biome.provider.BiomeProviderPE;
import mod.bespectacled.modernbeta.world.biome.provider.BiomeProviderSingle;
import mod.bespectacled.modernbeta.world.biome.provider.BiomeProviderVoronoi;
import mod.bespectacled.modernbeta.world.blocksource.BlockSourceDeepslate;
import mod.bespectacled.modernbeta.world.cavebiome.provider.CaveBiomeProviderNone;
import mod.bespectacled.modernbeta.world.cavebiome.provider.CaveBiomeProviderSingle;
import mod.bespectacled.modernbeta.world.cavebiome.provider.CaveBiomeProviderVoronoi;
import mod.bespectacled.modernbeta.world.chunk.provider.ChunkProviderAlpha;
import mod.bespectacled.modernbeta.world.chunk.provider.ChunkProviderBeta;
import mod.bespectacled.modernbeta.world.chunk.provider.ChunkProviderClassic030;
import mod.bespectacled.modernbeta.world.chunk.provider.ChunkProviderIndev;
import mod.bespectacled.modernbeta.world.chunk.provider.ChunkProviderInfdev227;
import mod.bespectacled.modernbeta.world.chunk.provider.ChunkProviderInfdev415;
import mod.bespectacled.modernbeta.world.chunk.provider.ChunkProviderInfdev420;
import mod.bespectacled.modernbeta.world.chunk.provider.ChunkProviderInfdev611;
import mod.bespectacled.modernbeta.world.chunk.provider.ChunkProviderPE;
import mod.bespectacled.modernbeta.world.chunk.provider.ChunkProviderSky;

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
    }
    
    // Register default biome providers
    public static void registerBiomeProviders() {
        ModernBetaRegistries.BIOME.register(ModernBetaBuiltInTypes.DEFAULT_ID, BiomeProviderBeta::new);
        ModernBetaRegistries.BIOME.register(ModernBetaBuiltInTypes.Biome.BETA.id, BiomeProviderBeta::new);
        ModernBetaRegistries.BIOME.register(ModernBetaBuiltInTypes.Biome.SINGLE.id, BiomeProviderSingle::new);
        ModernBetaRegistries.BIOME.register(ModernBetaBuiltInTypes.Biome.PE.id, BiomeProviderPE::new);
        ModernBetaRegistries.BIOME.register(ModernBetaBuiltInTypes.Biome.VORONOI.id, BiomeProviderVoronoi::new);
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
        ModernBetaRegistries.SETTINGS_PRESET.register(ModernBetaBuiltInTypes.Preset.INFDEV_227.id, ModernBetaSettingsPresets.PRESET_INFDEV_227);
        ModernBetaRegistries.SETTINGS_PRESET.register(ModernBetaBuiltInTypes.Preset.INDEV.id, ModernBetaSettingsPresets.PRESET_INDEV);
        ModernBetaRegistries.SETTINGS_PRESET.register(ModernBetaBuiltInTypes.Preset.CLASSIC_0_30.id, ModernBetaSettingsPresets.PRESET_CLASSIC);
        ModernBetaRegistries.SETTINGS_PRESET.register(ModernBetaBuiltInTypes.Preset.PE.id, ModernBetaSettingsPresets.PRESET_PE);
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
        ModernBetaRegistries.SETTINGS_PRESET_ALT.register(ModernBetaBuiltInTypes.PresetAlt.ALPHA_WINTER.id, ModernBetaSettingsPresets.PRESET_ALPHA_WINTER);
        ModernBetaRegistries.SETTINGS_PRESET_ALT.register(ModernBetaBuiltInTypes.PresetAlt.INDEV_PARADISE.id, ModernBetaSettingsPresets.PRESET_INDEV_PARADISE);
        ModernBetaRegistries.SETTINGS_PRESET_ALT.register(ModernBetaBuiltInTypes.PresetAlt.INDEV_WOODS.id, ModernBetaSettingsPresets.PRESET_INDEV_WOODS);
        ModernBetaRegistries.SETTINGS_PRESET_ALT.register(ModernBetaBuiltInTypes.PresetAlt.INDEV_HELL.id, ModernBetaSettingsPresets.PRESET_INDEV_HELL);
    }
}
