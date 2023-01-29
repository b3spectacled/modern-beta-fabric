package mod.bespectacled.modernbeta.world.chunk.provider.settings;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import mod.bespectacled.modernbeta.config.ModernBetaConfigChunk;
import mod.bespectacled.modernbeta.util.NbtCompoundBuilder;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.settings.ImmutableSettings;
import mod.bespectacled.modernbeta.util.settings.Settings;

public class ChunkProviderSettings {
    private static final ModernBetaConfigChunk CONFIG = ModernBeta.CHUNK_CONFIG;
    
    private static NbtCompoundBuilder createSettingsBase(String worldType) {
        return new NbtCompoundBuilder().putString(NbtTags.CHUNK_PROVIDER, worldType);
    }
    
    private static NbtCompoundBuilder createSettingsNoise(String worldType) {
        return createSettingsBase(worldType)
            .putBoolean(NbtTags.USE_DEEPSLATE, CONFIG.useDeepslate)
            .putString(NbtTags.NOISE_POST_PROCESSOR, CONFIG.noisePostProcessor);
    }
    
    private static NbtCompoundBuilder createSettingsOcean(String worldType) {
        return createSettingsNoise(worldType)
            .putBoolean(NbtTags.GEN_MONUMENTS, CONFIG.generateMonuments);
    }
    
    public static Settings createSettingsDefault(String worldType) {
        return new ImmutableSettings(
            createSettingsBase(worldType).build()
        );
    }
    
    public static Settings createSettingsBeta() {
        return new ImmutableSettings(
            createSettingsOcean(ModernBetaBuiltInTypes.Chunk.BETA.name)
                .putBoolean(NbtTags.SAMPLE_CLIMATE, CONFIG.sampleClimate)
                .build()
        );
    }
    
    public static Settings createSettingsSkylands() {
        return new ImmutableSettings(
            createSettingsNoise(ModernBetaBuiltInTypes.Chunk.SKYLANDS.name)
                .build()
        );
    }
    
    public static Settings createSettingsAlpha() {
        return new ImmutableSettings(
            createSettingsOcean(ModernBetaBuiltInTypes.Chunk.ALPHA.name)
                .build()
        );
    }
    
    public static Settings createSettingsInfdev611() {
        return new ImmutableSettings(
            createSettingsOcean(ModernBetaBuiltInTypes.Chunk.INFDEV_611.name)
                .build()
        );
    }
    
    public static Settings createSettingsInfdev415() {
        return new ImmutableSettings(
            createSettingsOcean(ModernBetaBuiltInTypes.Chunk.INFDEV_415.name)
                .build()
        );
    }
    
    public static Settings createSettingsInfdev420() {
        return new ImmutableSettings(
            createSettingsOcean(ModernBetaBuiltInTypes.Chunk.INFDEV_420.name)
                .build()
        );
    }
    
    public static Settings createSettingsInfdev227() {
        return new ImmutableSettings(
            createSettingsBase(ModernBetaBuiltInTypes.Chunk.INFDEV_227.name)
                .putBoolean(NbtTags.USE_DEEPSLATE, CONFIG.useDeepslate)
                .putBoolean(NbtTags.GEN_MONUMENTS, CONFIG.generateMonuments)
                .putBoolean(NbtTags.INFDEV_USE_PYRAMID, CONFIG.infdevUsePyramid)
                .putBoolean(NbtTags.INFDEV_USE_WALL, CONFIG.infdevUseWall)
                .build()
        );
    }
    
    public static Settings createSettingsIndev() {
        return new ImmutableSettings(
            createSettingsBase(ModernBetaBuiltInTypes.Chunk.INDEV.name)
                .putBoolean(NbtTags.GEN_MONUMENTS, CONFIG.generateMonuments)
                .putInt(NbtTags.INDEV_LEVEL_WIDTH, CONFIG.indevLevelWidth)
                .putInt(NbtTags.INDEV_LEVEL_LENGTH, CONFIG.indevLevelLength)
                .putInt(NbtTags.INDEV_LEVEL_HEIGHT, CONFIG.indevLevelHeight)
                .putFloat(NbtTags.INDEV_CAVE_RADIUS, CONFIG.indevCaveRadius)
                .putString(NbtTags.INDEV_LEVEL_TYPE, CONFIG.indevLevelType)
                .putString(NbtTags.INDEV_LEVEL_THEME, CONFIG.indevLevelTheme)
                .build()
        );
    }
    
    public static Settings createSettingsClassic030() {
        return new ImmutableSettings(
            createSettingsBase(ModernBetaBuiltInTypes.Chunk.CLASSIC_0_30.name)
                .putBoolean(NbtTags.GEN_MONUMENTS, CONFIG.generateMonuments)
                .putInt(NbtTags.INDEV_LEVEL_WIDTH, CONFIG.indevLevelWidth)
                .putInt(NbtTags.INDEV_LEVEL_LENGTH, CONFIG.indevLevelLength)
                .putInt(NbtTags.INDEV_LEVEL_HEIGHT, CONFIG.indevLevelHeight)
                .putFloat(NbtTags.INDEV_CAVE_RADIUS, CONFIG.indevCaveRadius)
                .build()
        );
    }
    
    public static Settings createSettingsIslands() {
        return new ImmutableSettings(
            createSettingsOcean(ModernBetaBuiltInTypes.Chunk.BETA_ISLANDS.name)
                .putBoolean(NbtTags.SAMPLE_CLIMATE, CONFIG.sampleClimate)
                .putBoolean(NbtTags.ISLANDS_USE_OUTER_ISLANDS, CONFIG.islandsUseOuterIslands)
                .putInt(NbtTags.ISLANDS_CENTER_ISLAND_RADIUS, CONFIG.islandsCenterIslandRadius)
                .putFloat(NbtTags.ISLANDS_CENTER_ISLAND_FALLOFF, CONFIG.islandsCenterIslandFalloff)
                .putInt(NbtTags.ISLANDS_CENTER_OCEAN_FALLOFF_DIST, CONFIG.islandsCenterOceanFalloffDistance)
                .putInt(NbtTags.ISLANDS_CENTER_OCEAN_RADIUS, CONFIG.islandsCenterOceanRadius)
                .putFloat(NbtTags.ISLANDS_OUTER_ISLAND_NOISE_SCALE, CONFIG.islandsOuterIslandNoiseScale)
                .putFloat(NbtTags.ISLANDS_OUTER_ISLAND_NOISE_OFFSET, CONFIG.islandsOuterIslandNoiseOffset)
                .build()
        );
    }
    
    public static Settings createSettingsPE() {
        return new ImmutableSettings(
            createSettingsOcean(ModernBetaBuiltInTypes.Chunk.PE.name)
                .putBoolean(NbtTags.SAMPLE_CLIMATE, CONFIG.sampleClimate)
                .build()
        );
    }
}
