package com.bespectacled.modernbeta.world;

import com.bespectacled.modernbeta.api.WorldProvider;
import com.bespectacled.modernbeta.api.registry.BiomeProviderRegistry.BuiltInBiomeType;
import com.bespectacled.modernbeta.api.registry.ChunkProviderRegistry.BuiltInChunkType;
import com.bespectacled.modernbeta.api.registry.ChunkProviderSettingsRegistry.BuiltInChunkSettingsType;
import com.bespectacled.modernbeta.api.registry.WorldScreenProviderRegistry.BuiltInWorldScreenType;
import com.bespectacled.modernbeta.world.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.world.biome.classic.ClassicBiomes;
import com.bespectacled.modernbeta.world.biome.indev.IndevBiomes;
import com.bespectacled.modernbeta.world.gen.OldChunkGeneratorSettings;

public class BuiltInWorldProviders {
    public static final WorldProvider BETA;
    public static final WorldProvider SKYLANDS;
    public static final WorldProvider ALPHA;
    public static final WorldProvider INFDEV_415;
    public static final WorldProvider INFDEV_227;
    public static final WorldProvider INDEV;
    public static final WorldProvider BETA_ISLANDS;
    
    static {
        BETA = new WorldProvider(
            BuiltInChunkType.BETA.name, 
            BuiltInChunkSettingsType.BETA.name,
            OldChunkGeneratorSettings.BETA.toString(), 
            BuiltInWorldScreenType.INF.name,
            BuiltInBiomeType.BETA.name,
            BetaBiomes.FOREST_ID.toString()
        );
        
        SKYLANDS = new WorldProvider(
            BuiltInChunkType.SKYLANDS.name, 
            BuiltInChunkSettingsType.SKYLANDS.name,
            OldChunkGeneratorSettings.SKYLANDS.toString(), 
            BuiltInWorldScreenType.SKYLANDS.name, 
            BuiltInBiomeType.SINGLE.name,
            BetaBiomes.SKY_ID.toString()
        );
        
        ALPHA = new WorldProvider(
            BuiltInChunkType.ALPHA.name,
            BuiltInChunkSettingsType.ALPHA.name,
            OldChunkGeneratorSettings.ALPHA.toString(), 
            BuiltInWorldScreenType.INF.name, 
            BuiltInBiomeType.SINGLE.name,
            ClassicBiomes.ALPHA_ID.toString()
        );
        
        INFDEV_415 = new WorldProvider(
            BuiltInChunkType.INFDEV_415.name,
            BuiltInChunkSettingsType.INFDEV_415.name,
            OldChunkGeneratorSettings.INFDEV_415.toString(), 
            BuiltInWorldScreenType.INF.name, 
            BuiltInBiomeType.SINGLE.name,
            ClassicBiomes.INFDEV_415_ID.toString()
        );
        
        INFDEV_227 = new WorldProvider(
            BuiltInChunkType.INFDEV_227.name,
            BuiltInChunkSettingsType.INFDEV_227.name,
            OldChunkGeneratorSettings.INFDEV_227.toString(), 
            BuiltInWorldScreenType.INFDEV_OLD.name, 
            BuiltInBiomeType.SINGLE.name,
            ClassicBiomes.INFDEV_227_ID.toString()
        );
        
        INDEV = new WorldProvider(
            BuiltInChunkType.INDEV.name,
            BuiltInChunkSettingsType.INDEV.name,
            OldChunkGeneratorSettings.INDEV.toString(), 
            BuiltInWorldScreenType.INDEV.name, 
            BuiltInBiomeType.SINGLE.name,
            IndevBiomes.INDEV_NORMAL_ID.toString()
        );
        
        BETA_ISLANDS = new WorldProvider(
            BuiltInChunkType.BETA_ISLANDS.name,
            BuiltInChunkSettingsType.BETA_ISLANDS.name,
            OldChunkGeneratorSettings.BETA_ISLANDS.toString(),
            BuiltInWorldScreenType.ISLAND.name,
            BuiltInBiomeType.BETA.name,
            BetaBiomes.FOREST_ID.toString()
        );
    }
}
