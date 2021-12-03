package com.bespectacled.modernbeta.world.gen.provider.settings;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.config.ModernBetaConfigGeneration;
import com.bespectacled.modernbeta.util.NbtCompoundBuilder;
import com.bespectacled.modernbeta.util.NbtTags;

import net.minecraft.nbt.NbtCompound;

public class ChunkProviderSettings {
    private static final ModernBetaConfigGeneration CONFIG = ModernBeta.GEN_CONFIG;
    
    private static NbtCompoundBuilder createSettingsBase(String worldType) {
        return new NbtCompoundBuilder().putString(NbtTags.WORLD_TYPE, worldType);
    }
    
    private static NbtCompoundBuilder createSettingsInf(String worldType) {
        return createSettingsBase(worldType)
            .putBoolean(NbtTags.GEN_OCEAN_SHRINES, CONFIG.generateOceanShrines)
            .putBoolean(NbtTags.GEN_MONUMENTS, CONFIG.generateMonuments);
    }
    
    private static NbtCompoundBuilder createSettingsPreInf(String worldType) {
        return createSettingsBase(worldType)
            .putInt(NbtTags.LEVEL_WIDTH, CONFIG.levelWidth)
            .putInt(NbtTags.LEVEL_LENGTH, CONFIG.levelLength)
            .putInt(NbtTags.LEVEL_HEIGHT, CONFIG.levelHeight)
            .putFloat(NbtTags.LEVEL_CAVE_RADIUS, CONFIG.caveRadius);
    }
    
    private static NbtCompoundBuilder createSettingsClimate(String worldType) {
        return createSettingsInf(worldType)
            .putBoolean(NbtTags.SAMPLE_CLIMATE, CONFIG.sampleClimate);
    }
    
    public static NbtCompound createSettingsDefault(String worldType) {
        return createSettingsBase(worldType).build();
    }
    
    public static NbtCompound createSettingsBeta() {
        return createSettingsClimate(BuiltInTypes.Chunk.BETA.name).build();
    }
    
    public static NbtCompound createSettingsSkylands() {
        return createSettingsBase(BuiltInTypes.Chunk.SKYLANDS.name)
            .putBoolean(NbtTags.GEN_OCEANS, false)
            .build();
    }
    
    public static NbtCompound createSettingsAlpha() {
        return createSettingsInf(BuiltInTypes.Chunk.ALPHA.name).build();
    }
    
    public static NbtCompound createSettingsInfdev611() {
        return createSettingsInf(BuiltInTypes.Chunk.INFDEV_611.name).build();
    }
    
    public static NbtCompound createSettingsInfdev415() {
        return createSettingsInf(BuiltInTypes.Chunk.INFDEV_415.name).build();
    }
    
    public static NbtCompound createSettingsInfdev420() {
        return createSettingsInf(BuiltInTypes.Chunk.INFDEV_420.name).build();
    }
    
    public static NbtCompound createSettingsInfdev227() {
        return createSettingsInf(BuiltInTypes.Chunk.INFDEV_227.name)
            .putBoolean(NbtTags.GEN_INFDEV_PYRAMID, CONFIG.generateInfdevPyramid)
            .putBoolean(NbtTags.GEN_INFDEV_WALL, CONFIG.generateInfdevWall)
            .build();
    }
    
    public static NbtCompound createSettingsIndev() {
        return createSettingsPreInf(BuiltInTypes.Chunk.INDEV.name)
            .putString(NbtTags.LEVEL_TYPE, CONFIG.levelType)
            .putString(NbtTags.LEVEL_THEME, CONFIG.levelTheme)
            .build();
    }
    
    public static NbtCompound createSettingsClassic030() {
        return createSettingsPreInf(BuiltInTypes.Chunk.CLASSIC_0_30.name).build();
    }
    
    public static NbtCompound createSettingsIslands() {
        return createSettingsClimate(BuiltInTypes.Chunk.BETA_ISLANDS.name)
            .putBoolean(NbtTags.GEN_OUTER_ISLANDS, CONFIG.generateOuterIslands)
            .putInt(NbtTags.CENTER_ISLAND_RADIUS, CONFIG.centerIslandRadius)
            .putFloat(NbtTags.CENTER_ISLAND_FALLOFF, CONFIG.centerIslandFalloff)
            .putInt(NbtTags.CENTER_OCEAN_LERP_DIST, CONFIG.centerOceanLerpDistance)
            .putInt(NbtTags.CENTER_OCEAN_RADIUS, CONFIG.centerOceanRadius)
            .putFloat(NbtTags.OUTER_ISLAND_NOISE_SCALE, CONFIG.outerIslandNoiseScale)
            .putFloat(NbtTags.OUTER_ISLAND_NOISE_OFFSET, CONFIG.outerIslandNoiseOffset)
            .build();
    }
    
    public static NbtCompound createSettingsPE() {
        return createSettingsClimate(BuiltInTypes.Chunk.PE.name).build();
    }
}