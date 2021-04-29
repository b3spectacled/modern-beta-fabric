package com.bespectacled.modernbeta.world.gen.provider.settings;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.config.ModernBetaGenerationConfig;

import net.minecraft.nbt.NbtCompound;

public class ChunkProviderSettings {
    protected static final ModernBetaGenerationConfig CONFIG = ModernBeta.BETA_CONFIG.generation_config;
    
    private static NbtCompound createSettingsBase(String worldType, boolean generateOceans) {
        NbtCompound settings = new NbtCompound();
        
        settings.putString("worldType", worldType);
        settings.putBoolean(worldType, generateOceans);
        
        return settings;
    }
    
    public static NbtCompound createSettingsBeta() {
        return createSettingsBase(BuiltInTypes.Chunk.BETA.name, CONFIG.generateOceans);
    }
    
    public static NbtCompound createSettingsAlpha() {
        return createSettingsBase(BuiltInTypes.Chunk.ALPHA.name, CONFIG.generateOceans);
    }
    
    public static NbtCompound createSettingsSkylands() {
        return createSettingsBase(BuiltInTypes.Chunk.SKYLANDS.name, false);
    }
    
    public static NbtCompound createSettingsInfdev415() {
        return createSettingsBase(BuiltInTypes.Chunk.INFDEV_415.name, CONFIG.generateOceans);
    }
    
    public static NbtCompound createSettingsInfdev227() {
        NbtCompound settings = createSettingsBase(BuiltInTypes.Chunk.INFDEV_227.name, CONFIG.generateOceans);
        
        settings.putBoolean("generateInfdevPyramid", CONFIG.generateInfdevPyramid);
        settings.putBoolean("generateInfdevWall", CONFIG.generateInfdevWall);
        
        return settings;
    }
    
    public static NbtCompound createSettingsIndev() {
        NbtCompound settings = createSettingsBase(BuiltInTypes.Chunk.INDEV.name, false);
        
        settings.putString("levelType", CONFIG.indevLevelType);
        settings.putString("levelTheme", CONFIG.indevLevelTheme);
        settings.putInt("levelWidth", CONFIG.indevLevelWidth);
        settings.putInt("levelLength", CONFIG.indevLevelLength);
        settings.putInt("levelHeight", CONFIG.indevLevelHeight);
        settings.putFloat("caveRadius", CONFIG.indevCaveRadius);
        
        return settings;
    }
    
    public static NbtCompound createSettingsBetaIslands() {
        NbtCompound settings = createSettingsBase(BuiltInTypes.Chunk.BETA_ISLANDS.name, CONFIG.generateOceans);
        
        settings.putBoolean("generateOuterIslands", CONFIG.generateOuterIslands);
        settings.putInt("centerOceanLerpDistance", CONFIG.centerOceanLerpDistance);
        settings.putInt("centerOceanRadius", CONFIG.centerOceanRadius);
        settings.putFloat("centerIslandFalloff", CONFIG.centerIslandFalloff);
        settings.putFloat("outerIslandNoiseScale", CONFIG.outerIslandNoiseScale);
        settings.putFloat("outerIslandNoiseOffset", CONFIG.outerIslandNoiseOffset);
        
        return settings;
    }
}
