package com.bespectacled.modernbeta.world.gen.provider.settings;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.config.ModernBetaGenerationConfig;
import com.bespectacled.modernbeta.util.NbtTags;

import net.minecraft.nbt.NbtCompound;

public class ChunkProviderSettings {
    protected static final ModernBetaGenerationConfig CONFIG = ModernBeta.BETA_CONFIG.generation_config;
    
    public static NbtCompound createSettingsBase(String worldType) {
        NbtCompound settings = new NbtCompound();
        
        settings.putString(NbtTags.WORLD_TYPE, worldType);
        
        return settings;
    }
    
    public static NbtCompound createSettingsInf(String worldType) {
        NbtCompound settings = createSettingsBase(worldType);
        
        settings.putBoolean(NbtTags.GEN_OCEANS, CONFIG.generateOceans);
        settings.putBoolean(NbtTags.GEN_OCEAN_SHRINES, CONFIG.generateOceanShrines);
        
        return settings;
    }
    
    public static NbtCompound createSettingsBeta() {
        return createSettingsInf(BuiltInTypes.Chunk.BETA.name);
    }
    
    public static NbtCompound createSettingsSkylands() {
        return createSettingsBase(BuiltInTypes.Chunk.SKYLANDS.name);
    }
    
    public static NbtCompound createSettingsAlpha() {
        return createSettingsInf(BuiltInTypes.Chunk.ALPHA.name);
    }
    
    public static NbtCompound createSettingsInfdev611() {
        return createSettingsInf(BuiltInTypes.Chunk.INFDEV_611.name);
    }
    
    public static NbtCompound createSettingsInfdev415() {
        return createSettingsInf(BuiltInTypes.Chunk.INFDEV_415.name);
    }
    
    public static NbtCompound createSettingsInfdev227() {
        NbtCompound settings = createSettingsInf(BuiltInTypes.Chunk.INFDEV_227.name);
        settings.putBoolean(NbtTags.GEN_INFDEV_PYRAMID, CONFIG.generateInfdevPyramid);
        settings.putBoolean(NbtTags.GEN_INFDEV_WALL, CONFIG.generateInfdevWall);
        
        return settings;
    }
    
    public static NbtCompound createSettingsIndev() {
        NbtCompound settings = createSettingsInf(BuiltInTypes.Chunk.INDEV.name);
        settings.putString(NbtTags.LEVEL_TYPE, CONFIG.indevLevelType);
        settings.putString(NbtTags.LEVEL_THEME, CONFIG.indevLevelTheme);
        settings.putInt(NbtTags.LEVEL_WIDTH, CONFIG.indevLevelWidth);
        settings.putInt(NbtTags.LEVEL_LENGTH, CONFIG.indevLevelLength);
        settings.putInt(NbtTags.LEVEL_HEIGHT, CONFIG.indevLevelHeight);
        settings.putFloat(NbtTags.LEVEL_CAVE_RADIUS, CONFIG.indevCaveRadius);
        
        return settings;
    }
    
    public static NbtCompound createSettingsIslands() {
        NbtCompound settings = createSettingsInf(BuiltInTypes.Chunk.BETA_ISLANDS.name);
        settings.putBoolean(NbtTags.GEN_OUTER_ISLANDS, CONFIG.generateOuterIslands);
        settings.putInt(NbtTags.CENTER_ISLAND_RADIUS, CONFIG.centerIslandRadius);
        settings.putFloat(NbtTags.CENTER_ISLAND_FALLOFF, CONFIG.centerIslandFalloff);
        settings.putInt(NbtTags.CENTER_OCEAN_LERP_DIST, CONFIG.centerOceanLerpDistance);
        settings.putInt(NbtTags.CENTER_OCEAN_RADIUS, CONFIG.centerOceanRadius);
        settings.putFloat(NbtTags.OUTER_ISLAND_NOISE_SCALE, CONFIG.outerIslandNoiseScale);
        settings.putFloat(NbtTags.OUTER_ISLAND_NOISE_OFFSET, CONFIG.outerIslandNoiseOffset);
        
        return settings;
    }
    
    public static NbtCompound createSettingsAll(String worldType) {
        NbtCompound settings = createSettingsBase(worldType);
        
        settings.putBoolean(NbtTags.GEN_OCEANS, CONFIG.generateOceans);
        settings.putBoolean(NbtTags.GEN_OCEAN_SHRINES, CONFIG.generateOceanShrines);
        
        settings.putBoolean(NbtTags.GEN_INFDEV_PYRAMID, CONFIG.generateInfdevPyramid);
        settings.putBoolean(NbtTags.GEN_INFDEV_WALL, CONFIG.generateInfdevWall);
        
        settings.putString(NbtTags.LEVEL_TYPE, CONFIG.indevLevelType);
        settings.putString(NbtTags.LEVEL_THEME, CONFIG.indevLevelTheme);
        settings.putInt(NbtTags.LEVEL_WIDTH, CONFIG.indevLevelWidth);
        settings.putInt(NbtTags.LEVEL_LENGTH, CONFIG.indevLevelLength);
        settings.putInt(NbtTags.LEVEL_HEIGHT, CONFIG.indevLevelHeight);
        settings.putFloat(NbtTags.LEVEL_CAVE_RADIUS, CONFIG.indevCaveRadius);
        
        settings.putBoolean(NbtTags.GEN_OUTER_ISLANDS, CONFIG.generateOuterIslands);
        settings.putInt(NbtTags.CENTER_ISLAND_RADIUS, CONFIG.centerIslandRadius);
        settings.putFloat(NbtTags.CENTER_ISLAND_FALLOFF, CONFIG.centerIslandFalloff);
        settings.putInt(NbtTags.CENTER_OCEAN_LERP_DIST, CONFIG.centerOceanLerpDistance);
        settings.putInt(NbtTags.CENTER_OCEAN_RADIUS, CONFIG.centerOceanRadius);
        settings.putFloat(NbtTags.OUTER_ISLAND_NOISE_SCALE, CONFIG.outerIslandNoiseScale);
        settings.putFloat(NbtTags.OUTER_ISLAND_NOISE_OFFSET, CONFIG.outerIslandNoiseOffset);
        
        return settings;
    }
}
