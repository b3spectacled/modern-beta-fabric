package com.bespectacled.modernbeta.world.gen.provider.settings;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import com.bespectacled.modernbeta.config.ModernBetaConfigGeneration;
import com.bespectacled.modernbeta.util.NbtCompoundBuilder;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.settings.ImmutableSettings;
import com.bespectacled.modernbeta.util.settings.Settings;

public class ChunkProviderSettings {
    private static final ModernBetaConfigGeneration CONFIG = ModernBeta.GEN_CONFIG;
    
    private static NbtCompoundBuilder createSettingsBase(String worldType) {
        return new NbtCompoundBuilder().putString(NbtTags.WORLD_TYPE, worldType);
    }
    
    private static NbtCompoundBuilder createSettingsNoise(String worldType) {
        return createSettingsBase(worldType)
            .putBoolean(NbtTags.GEN_DEEPSLATE, CONFIG.generateDeepslate)
            .putString(NbtTags.NOISE_POST_PROCESSOR, CONFIG.noisePostProcessor);
    }
    
    private static NbtCompoundBuilder createSettingsOcean(String worldType) {
        return createSettingsNoise(worldType)
            .putBoolean(NbtTags.GEN_OCEAN_SHRINES, CONFIG.generateOceanShrines)
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
                .putBoolean(NbtTags.GEN_DEEPSLATE, CONFIG.generateDeepslate)
                .putBoolean(NbtTags.GEN_OCEAN_SHRINES, CONFIG.generateOceanShrines)
                .putBoolean(NbtTags.GEN_MONUMENTS, CONFIG.generateMonuments)
                .putBoolean(NbtTags.GEN_INFDEV_PYRAMID, CONFIG.generateInfdevPyramid)
                .putBoolean(NbtTags.GEN_INFDEV_WALL, CONFIG.generateInfdevWall)
                .build()
        );
    }
    
    public static Settings createSettingsIndev() {
        return new ImmutableSettings(
            createSettingsBase(ModernBetaBuiltInTypes.Chunk.INDEV.name)
                .putBoolean(NbtTags.GEN_OCEAN_SHRINES, CONFIG.generateOceanShrines)
                .putBoolean(NbtTags.GEN_MONUMENTS, CONFIG.generateMonuments)
                .putInt(NbtTags.LEVEL_WIDTH, CONFIG.levelWidth)
                .putInt(NbtTags.LEVEL_LENGTH, CONFIG.levelLength)
                .putInt(NbtTags.LEVEL_HEIGHT, CONFIG.levelHeight)
                .putFloat(NbtTags.LEVEL_CAVE_RADIUS, CONFIG.caveRadius)
                .putString(NbtTags.LEVEL_TYPE, CONFIG.levelType)
                .putString(NbtTags.LEVEL_THEME, CONFIG.levelTheme)
                .build()
        );
    }
    
    public static Settings createSettingsClassic030() {
        return new ImmutableSettings(
            createSettingsBase(ModernBetaBuiltInTypes.Chunk.CLASSIC_0_30.name)
                .putBoolean(NbtTags.GEN_OCEAN_SHRINES, CONFIG.generateOceanShrines)
                .putBoolean(NbtTags.GEN_MONUMENTS, CONFIG.generateMonuments)
                .putInt(NbtTags.LEVEL_WIDTH, CONFIG.levelWidth)
                .putInt(NbtTags.LEVEL_LENGTH, CONFIG.levelLength)
                .putInt(NbtTags.LEVEL_HEIGHT, CONFIG.levelHeight)
                .putFloat(NbtTags.LEVEL_CAVE_RADIUS, CONFIG.caveRadius)
                .build()
        );
    }
    
    public static Settings createSettingsIslands() {
        return new ImmutableSettings(
            createSettingsOcean(ModernBetaBuiltInTypes.Chunk.BETA_ISLANDS.name)
                .putBoolean(NbtTags.SAMPLE_CLIMATE, CONFIG.sampleClimate)
                .putBoolean(NbtTags.GEN_OUTER_ISLANDS, CONFIG.generateOuterIslands)
                .putInt(NbtTags.CENTER_ISLAND_RADIUS, CONFIG.centerIslandRadius)
                .putFloat(NbtTags.CENTER_ISLAND_FALLOFF, CONFIG.centerIslandFalloff)
                .putInt(NbtTags.CENTER_OCEAN_LERP_DIST, CONFIG.centerOceanLerpDistance)
                .putInt(NbtTags.CENTER_OCEAN_RADIUS, CONFIG.centerOceanRadius)
                .putFloat(NbtTags.OUTER_ISLAND_NOISE_SCALE, CONFIG.outerIslandNoiseScale)
                .putFloat(NbtTags.OUTER_ISLAND_NOISE_OFFSET, CONFIG.outerIslandNoiseOffset)
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
