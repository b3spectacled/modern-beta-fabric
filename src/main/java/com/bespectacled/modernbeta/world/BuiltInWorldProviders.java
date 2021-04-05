package com.bespectacled.modernbeta.world;

import com.bespectacled.modernbeta.api.WorldProvider;
import com.bespectacled.modernbeta.api.registry.BiomeProviderRegistry.BuiltInBiomeType;
import com.bespectacled.modernbeta.api.registry.CaveBiomeProviderRegistry.BuiltInCaveBiomeType;
import com.bespectacled.modernbeta.api.registry.ChunkProviderRegistry.BuiltInChunkType;
import com.bespectacled.modernbeta.api.registry.ChunkProviderSettingsRegistry.BuiltInChunkSettingsType;
import com.bespectacled.modernbeta.api.registry.ScreenProviderRegistry.BuiltInScreenType;
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
            BuiltInChunkType.BETA.id, 
            BuiltInChunkSettingsType.BETA.id,
            OldChunkGeneratorSettings.BETA.toString(), 
            BuiltInScreenType.INF.id,
            BuiltInBiomeType.BETA.id, 
            BuiltInCaveBiomeType.VANILLA.id, 
            BetaBiomes.FOREST_ID.toString(),
            true
        );
        
        SKYLANDS = new WorldProvider(
            BuiltInChunkType.SKYLANDS.id, 
            BuiltInChunkSettingsType.SKYLANDS.id,
            OldChunkGeneratorSettings.SKYLANDS.toString(), 
            BuiltInScreenType.SKYLANDS.id, 
            BuiltInBiomeType.SINGLE.id, 
            BuiltInCaveBiomeType.NONE.id, 
            BetaBiomes.SKY_ID.toString(),
            true
        );
        
        ALPHA = new WorldProvider(
            BuiltInChunkType.ALPHA.id,
            BuiltInChunkSettingsType.ALPHA.id,
            OldChunkGeneratorSettings.ALPHA.toString(), 
            BuiltInScreenType.INF.id, 
            BuiltInBiomeType.SINGLE.id, 
            BuiltInCaveBiomeType.NONE.id, 
            ClassicBiomes.ALPHA_ID.toString(),
            true
        );
        
        INFDEV_415 = new WorldProvider(
            BuiltInChunkType.INFDEV_415.id,
            BuiltInChunkSettingsType.INFDEV_415.id,
            OldChunkGeneratorSettings.INFDEV_415.toString(), 
            BuiltInScreenType.INF.id, 
            BuiltInBiomeType.SINGLE.id, 
            BuiltInCaveBiomeType.NONE.id, 
            ClassicBiomes.INFDEV_415_ID.toString(),
            true
        );
        
        INFDEV_227 = new WorldProvider(
            BuiltInChunkType.INFDEV_227.id,
            BuiltInChunkSettingsType.INFDEV_227.id,
            OldChunkGeneratorSettings.INFDEV_227.toString(), 
            BuiltInScreenType.INFDEV_OLD.id, 
            BuiltInBiomeType.SINGLE.id, 
            BuiltInCaveBiomeType.NONE.id, 
            ClassicBiomes.INFDEV_227_ID.toString(),
            false
        );
        
        INDEV = new WorldProvider(
            BuiltInChunkType.INDEV.id,
            BuiltInChunkSettingsType.INDEV.id,
            OldChunkGeneratorSettings.INDEV.toString(), 
            BuiltInScreenType.INDEV.id, 
            BuiltInBiomeType.SINGLE.id, 
            BuiltInCaveBiomeType.NONE.id, 
            IndevBiomes.INDEV_NORMAL_ID.toString(),
            false
        );
        
        BETA_ISLANDS = new WorldProvider(
            BuiltInChunkType.BETA_ISLANDS.id,
            BuiltInChunkSettingsType.BETA_ISLANDS.id,
            OldChunkGeneratorSettings.BETA_ISLANDS.toString(),
            BuiltInScreenType.ISLAND.id,
            BuiltInBiomeType.BETA.id,
            BuiltInCaveBiomeType.VANILLA.id,
            BetaBiomes.FOREST_ID.toString(),
            true
        );
    }
}
