package com.bespectacled.modernbeta.world.gen.provider.settings;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.config.ConfigGeneration;
import com.bespectacled.modernbeta.util.NbtTags;

import net.minecraft.nbt.NbtCompound;

public class ChunkProviderSettings {
    protected static final ConfigGeneration CONFIG = ModernBeta.GEN_CONFIG;
    
    public static NbtCompound createSettingsBase(String worldType) {
        NbtCompound settings = new NbtCompound();
        
        settings.putString(NbtTags.WORLD_TYPE, worldType);
        
        return settings;
    }
    
    public static NbtCompound createSettingsInf(String worldType) {
        NbtCompound settings = createSettingsBase(worldType);
        
        settings.putBoolean(NbtTags.GEN_OCEANS, CONFIG.infGenConfig.generateOceans);
        settings.putBoolean(NbtTags.GEN_OCEAN_SHRINES, CONFIG.infGenConfig.generateOceanShrines);
        settings.putBoolean(NbtTags.GEN_MONUMENTS, CONFIG.infGenConfig.generateMonuments);
        
        return settings;
    }
    
    public static NbtCompound createSettingsPreInf(String worldType) {
        NbtCompound settings = createSettingsInf(worldType);
        
        settings.putInt(NbtTags.LEVEL_WIDTH, CONFIG.preInfGenConfig.levelWidth);
        settings.putInt(NbtTags.LEVEL_LENGTH, CONFIG.preInfGenConfig.levelLength);
        settings.putInt(NbtTags.LEVEL_HEIGHT, CONFIG.preInfGenConfig.levelHeight);
        settings.putFloat(NbtTags.LEVEL_CAVE_RADIUS, CONFIG.preInfGenConfig.caveRadius);
        
        return settings;
    }
    
    public static NbtCompound createSettingsBeta() {
        NbtCompound settings = createSettingsInf(BuiltInTypes.Chunk.BETA.name);
        
        settings.putBoolean(NbtTags.SAMPLE_CLIMATE, CONFIG.infGenConfig.sampleClimate);
        
        return settings;
    }
    
    public static NbtCompound createSettingsSkylands() {
        NbtCompound settings = createSettingsBase(BuiltInTypes.Chunk.SKYLANDS.name);
        
        settings.putBoolean(NbtTags.GEN_OCEANS, false);
        
        return settings;
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
    
    public static NbtCompound createSettingsInfdev420() {
        return createSettingsInf(BuiltInTypes.Chunk.INFDEV_420.name);
    }
    
    public static NbtCompound createSettingsInfdev227() {
        NbtCompound settings = createSettingsInf(BuiltInTypes.Chunk.INFDEV_227.name);
        
        settings.putBoolean(NbtTags.GEN_INFDEV_PYRAMID, CONFIG.inf227GenConfig.generateInfdevPyramid);
        settings.putBoolean(NbtTags.GEN_INFDEV_WALL, CONFIG.inf227GenConfig.generateInfdevWall);
        
        return settings;
    }
    
    public static NbtCompound createSettingsIndev() {
        NbtCompound settings = createSettingsPreInf(BuiltInTypes.Chunk.INDEV.name);
        
        settings.putString(NbtTags.LEVEL_TYPE, CONFIG.indevGenConfig.indevLevelType);
        settings.putString(NbtTags.LEVEL_THEME, CONFIG.indevGenConfig.indevLevelTheme);
        
        return settings;
    }
    
    public static NbtCompound createSettingsClassic030() {
        return createSettingsPreInf(BuiltInTypes.Chunk.CLASSIC_0_30.name);
    }
    
    public static NbtCompound createSettingsIslands() {
        NbtCompound settings = createSettingsInf(BuiltInTypes.Chunk.BETA_ISLANDS.name);
        
        settings.putBoolean(NbtTags.SAMPLE_CLIMATE, CONFIG.infGenConfig.sampleClimate);
        settings.putBoolean(NbtTags.GEN_OUTER_ISLANDS, CONFIG.islandGenConfig.generateOuterIslands);
        settings.putInt(NbtTags.CENTER_ISLAND_RADIUS, CONFIG.islandGenConfig.centerIslandRadius);
        settings.putFloat(NbtTags.CENTER_ISLAND_FALLOFF, CONFIG.islandGenConfig.centerIslandFalloff);
        settings.putInt(NbtTags.CENTER_OCEAN_LERP_DIST, CONFIG.islandGenConfig.centerOceanLerpDistance);
        settings.putInt(NbtTags.CENTER_OCEAN_RADIUS, CONFIG.islandGenConfig.centerOceanRadius);
        settings.putFloat(NbtTags.OUTER_ISLAND_NOISE_SCALE, CONFIG.islandGenConfig.outerIslandNoiseScale);
        settings.putFloat(NbtTags.OUTER_ISLAND_NOISE_OFFSET, CONFIG.islandGenConfig.outerIslandNoiseOffset);
        
        return settings;
    }
    
    public static NbtCompound createSettingsPE() {
        NbtCompound settings = createSettingsInf(BuiltInTypes.Chunk.PE.name);
        
        settings.putBoolean(NbtTags.SAMPLE_CLIMATE, CONFIG.infGenConfig.sampleClimate);
        
        return settings;
    }
}
