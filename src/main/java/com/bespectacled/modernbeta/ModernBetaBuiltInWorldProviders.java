package com.bespectacled.modernbeta;

import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.api.world.WorldProvider;
import com.bespectacled.modernbeta.world.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.world.biome.indev.IndevBiomes;
import com.bespectacled.modernbeta.world.biome.inf.InfBiomes;
import com.bespectacled.modernbeta.world.biome.pe.PEBiomes;
import com.bespectacled.modernbeta.world.gen.OldChunkGeneratorSettings;

public class ModernBetaBuiltInWorldProviders {
    public static final WorldProvider DEFAULT;
    public static final WorldProvider BETA;
    public static final WorldProvider SKYLANDS;
    public static final WorldProvider ALPHA;
    public static final WorldProvider INFDEV_611;
    public static final WorldProvider INFDEV_420;
    public static final WorldProvider INFDEV_415;
    public static final WorldProvider INFDEV_227;
    public static final WorldProvider INDEV;
    public static final WorldProvider CLASSIC_0_30;
    public static final WorldProvider BETA_ISLANDS;
    public static final WorldProvider PE;
    
    static {
        DEFAULT = new WorldProvider(
            BuiltInTypes.Chunk.BETA.name, 
            OldChunkGeneratorSettings.BETA.toString(), 
            BuiltInTypes.Biome.BETA.name,
            BetaBiomes.FOREST_ID.toString(), 
            BuiltInTypes.WorldScreen.BASE.name,
            true,
            false
        );
        
        BETA = new WorldProvider(
            BuiltInTypes.Chunk.BETA.name, 
            OldChunkGeneratorSettings.BETA.toString(), 
            BuiltInTypes.Biome.BETA.name,
            BetaBiomes.FOREST_ID.toString(), 
            BuiltInTypes.WorldScreen.INF_CLIMATE.name,
            true,
            false
        );
        
        SKYLANDS = new WorldProvider(
            BuiltInTypes.Chunk.SKYLANDS.name, 
            OldChunkGeneratorSettings.SKYLANDS.toString(), 
            BuiltInTypes.Biome.SINGLE.name,
            BetaBiomes.SKY_ID.toString(), 
            BuiltInTypes.DEFAULT_ID,
            false,
            false
        );
        
        ALPHA = new WorldProvider(
            BuiltInTypes.Chunk.ALPHA.name,
            OldChunkGeneratorSettings.ALPHA.toString(), 
            BuiltInTypes.Biome.SINGLE.name, 
            InfBiomes.ALPHA_ID.toString(), 
            BuiltInTypes.WorldScreen.INF.name,
            true,
            false
        );
        
        INFDEV_611 = new WorldProvider(
            BuiltInTypes.Chunk.INFDEV_611.name,
            OldChunkGeneratorSettings.INFDEV_611.toString(),
            BuiltInTypes.Biome.SINGLE.name,
            InfBiomes.INFDEV_611_ID.toString(),
            BuiltInTypes.WorldScreen.INF.name,
            false,
            true
        );
        
        INFDEV_420 = new WorldProvider(
            BuiltInTypes.Chunk.INFDEV_420.name,
            OldChunkGeneratorSettings.INFDEV_420.toString(), 
            BuiltInTypes.Biome.SINGLE.name,
            InfBiomes.INFDEV_420_ID.toString(), 
            BuiltInTypes.WorldScreen.INF.name,
            true,
            false
        );
        
        INFDEV_415 = new WorldProvider(
            BuiltInTypes.Chunk.INFDEV_415.name,
            OldChunkGeneratorSettings.INFDEV_415.toString(), 
            BuiltInTypes.Biome.SINGLE.name,
            InfBiomes.INFDEV_415_ID.toString(), 
            BuiltInTypes.WorldScreen.INF.name,
            true,
            false
        );
        
        INFDEV_227 = new WorldProvider(
            BuiltInTypes.Chunk.INFDEV_227.name,
            OldChunkGeneratorSettings.INFDEV_227.toString(), 
            BuiltInTypes.Biome.SINGLE.name,
            InfBiomes.INFDEV_227_ID.toString(), 
            BuiltInTypes.WorldScreen.INFDEV_227.name,
            true,
            false
        );
        
        INDEV = new WorldProvider(
            BuiltInTypes.Chunk.INDEV.name,
            OldChunkGeneratorSettings.INDEV.toString(), 
            BuiltInTypes.Biome.SINGLE.name,
            IndevBiomes.INDEV_NORMAL_ID.toString(), 
            BuiltInTypes.WorldScreen.INDEV.name,
            true,
            false
        );
        
        CLASSIC_0_30 = new WorldProvider(
            BuiltInTypes.Chunk.CLASSIC_0_30.name,
            OldChunkGeneratorSettings.CLASSIC_0_30.toString(), 
            BuiltInTypes.Biome.SINGLE.name,
            IndevBiomes.INDEV_NORMAL_ID.toString(), 
            BuiltInTypes.WorldScreen.PRE_INF.name,
            true,
            false
        );
        
        BETA_ISLANDS = new WorldProvider(
            BuiltInTypes.Chunk.BETA_ISLANDS.name,
            OldChunkGeneratorSettings.BETA_ISLANDS.toString(),
            BuiltInTypes.Biome.BETA.name,
            BetaBiomes.FOREST_ID.toString(),
            BuiltInTypes.WorldScreen.ISLAND.name,
            false,
            true
        );
        
        PE = new WorldProvider(
            BuiltInTypes.Chunk.PE.name,
            OldChunkGeneratorSettings.PE.toString(),
            BuiltInTypes.Biome.PE.name,
            PEBiomes.PE_FOREST_ID.toString(),
            BuiltInTypes.WorldScreen.INF_CLIMATE.name,
            true,
            false
        );
    }
}
