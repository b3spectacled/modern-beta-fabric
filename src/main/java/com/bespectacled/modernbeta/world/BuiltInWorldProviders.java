package com.bespectacled.modernbeta.world;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.WorldProvider;
import com.bespectacled.modernbeta.api.registry.BiomeProviderRegistry.BuiltInBiomeType;
import com.bespectacled.modernbeta.api.registry.CaveBiomeProviderRegistry.BuiltInCaveBiomeType;
import com.bespectacled.modernbeta.api.registry.ChunkProviderRegistry.BuiltInChunkType;
import com.bespectacled.modernbeta.api.registry.ChunkProviderSettingsRegistry.BuiltInChunkSettingsType;
import com.bespectacled.modernbeta.api.registry.ScreenProviderRegistry.BuiltInScreenType;
import com.bespectacled.modernbeta.world.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.world.biome.classic.ClassicBiomes;
import com.bespectacled.modernbeta.world.biome.indev.IndevBiomes;

public class BuiltInWorldProviders {
    public static final WorldProvider BETA;
    public static final WorldProvider SKYLANDS;
    public static final WorldProvider ALPHA;
    public static final WorldProvider INFDEV_415;
    public static final WorldProvider INFDEV_227;
    public static final WorldProvider INDEV;
    
    static {
        BETA = new WorldProvider(
            BuiltInChunkType.BETA.id, 
            BuiltInChunkSettingsType.BETA.id,
            ModernBeta.createId(BuiltInChunkType.BETA.id).toString(), 
            BuiltInScreenType.INF.id,
            BuiltInBiomeType.BETA.id, 
            BuiltInCaveBiomeType.VANILLA.id, 
            BetaBiomes.FOREST_ID.toString(),
            true
        );
        
        SKYLANDS = new WorldProvider(
            BuiltInChunkType.SKYLANDS.id, 
            BuiltInChunkSettingsType.SKYLANDS.id,
            ModernBeta.createId(BuiltInChunkType.SKYLANDS.id).toString(), 
            BuiltInScreenType.SKYLANDS.id, 
            BuiltInBiomeType.SINGLE.id, 
            BuiltInCaveBiomeType.NONE.id, 
            BetaBiomes.SKY_ID.toString(),
            true
        );
        
        ALPHA = new WorldProvider(
            BuiltInChunkType.ALPHA.id,
            BuiltInChunkSettingsType.ALPHA.id,
            ModernBeta.createId(BuiltInChunkType.ALPHA.id).toString(), 
            BuiltInScreenType.INF.id, 
            BuiltInBiomeType.SINGLE.id, 
            BuiltInCaveBiomeType.NONE.id, 
            ClassicBiomes.ALPHA_ID.toString(),
            true
        );
        
        INFDEV_415 = new WorldProvider(
            BuiltInChunkType.INFDEV_415.id,
            BuiltInChunkSettingsType.INFDEV_415.id,
            ModernBeta.createId(BuiltInChunkType.INFDEV_415.id).toString(), 
            BuiltInScreenType.INF.id, 
            BuiltInBiomeType.SINGLE.id, 
            BuiltInCaveBiomeType.NONE.id, 
            ClassicBiomes.INFDEV_415_ID.toString(),
            true
        );
        
        INFDEV_227 = new WorldProvider(
            BuiltInChunkType.INFDEV_227.id,
            BuiltInChunkSettingsType.INFDEV_227.id,
            ModernBeta.createId(BuiltInChunkType.INFDEV_227.id).toString(), 
            BuiltInScreenType.INFDEV_OLD.id, 
            BuiltInBiomeType.SINGLE.id, 
            BuiltInCaveBiomeType.NONE.id, 
            ClassicBiomes.INFDEV_227_ID.toString(),
            false
        );
        
        INDEV = new WorldProvider(
            BuiltInChunkType.INDEV.id,
            BuiltInChunkSettingsType.INDEV.id,
            ModernBeta.createId(BuiltInChunkType.INDEV.id).toString(), 
            BuiltInScreenType.INDEV.id, 
            BuiltInBiomeType.SINGLE.id, 
            BuiltInCaveBiomeType.NONE.id, 
            IndevBiomes.INDEV_NORMAL_ID.toString(),
            false
        );
    }
}
