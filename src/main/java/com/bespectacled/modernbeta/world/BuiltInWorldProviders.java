package com.bespectacled.modernbeta.world;

import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.api.world.WorldProvider;
import com.bespectacled.modernbeta.world.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.world.biome.indev.IndevBiomes;
import com.bespectacled.modernbeta.world.biome.inf.InfBiomes;
import com.bespectacled.modernbeta.world.biome.pe.PEBiomes;
import com.bespectacled.modernbeta.world.gen.OldChunkGeneratorSettings;

public class BuiltInWorldProviders {
    public static final WorldProvider DEFAULT;
    public static final WorldProvider BETA;
    public static final WorldProvider SKYLANDS;
    public static final WorldProvider ALPHA;
    public static final WorldProvider INFDEV_611;
    public static final WorldProvider INFDEV_415;
    public static final WorldProvider INFDEV_227;
    public static final WorldProvider INDEV;
    public static final WorldProvider BETA_ISLANDS;
    public static final WorldProvider PE;
    
    static {
        DEFAULT = new WorldProvider(
            BuiltInTypes.Chunk.BETA.name, 
            OldChunkGeneratorSettings.BETA.toString(), 
            BuiltInTypes.Biome.BETA.name,
            BuiltInTypes.CaveBiome.SINGLE.name, 
            BetaBiomes.FOREST_ID.toString(), 
            BuiltInTypes.WorldScreen.BASE.name,
            true,
            false
        );
        
        BETA = new WorldProvider(
            BuiltInTypes.Chunk.BETA.name, 
            OldChunkGeneratorSettings.BETA.toString(), 
            BuiltInTypes.Biome.BETA.name,
            BuiltInTypes.CaveBiome.SINGLE.name, 
            BetaBiomes.FOREST_ID.toString(), 
            BuiltInTypes.WorldScreen.INF.name,
            true,
            false
        );
        
        
        SKYLANDS = new WorldProvider(
            BuiltInTypes.Chunk.SKYLANDS.name, 
            OldChunkGeneratorSettings.SKYLANDS.toString(), 
            BuiltInTypes.Biome.SINGLE.name, 
            BuiltInTypes.CaveBiome.NONE.name, 
            BetaBiomes.SKY_ID.toString(), 
            BuiltInTypes.WorldScreen.BASE.name,
            false,
            false
        );
        
        ALPHA = new WorldProvider(
            BuiltInTypes.Chunk.ALPHA.name,
            OldChunkGeneratorSettings.ALPHA.toString(), 
            BuiltInTypes.Biome.SINGLE.name, 
            BuiltInTypes.CaveBiome.NONE.name, 
            InfBiomes.ALPHA_ID.toString(), 
            BuiltInTypes.WorldScreen.INF.name,
            true,
            false
        );
        
        INFDEV_611 = new WorldProvider(
            BuiltInTypes.Chunk.INFDEV_611.name,
            OldChunkGeneratorSettings.INFDEV_611.toString(),
            BuiltInTypes.Biome.SINGLE.name,
            BuiltInTypes.CaveBiome.NONE.name,
            InfBiomes.ALPHA_ID.toString(),
            BuiltInTypes.WorldScreen.INF.name,
            false,
            true
        );
        
        INFDEV_415 = new WorldProvider(
            BuiltInTypes.Chunk.INFDEV_415.name,
            OldChunkGeneratorSettings.INFDEV_415.toString(), 
            BuiltInTypes.Biome.SINGLE.name, 
            BuiltInTypes.CaveBiome.NONE.name, 
            InfBiomes.INFDEV_415_ID.toString(), 
            BuiltInTypes.WorldScreen.INF.name,
            true,
            false
        );
        
        INFDEV_227 = new WorldProvider(
            BuiltInTypes.Chunk.INFDEV_227.name,
            OldChunkGeneratorSettings.INFDEV_227.toString(), 
            BuiltInTypes.Biome.SINGLE.name, 
            BuiltInTypes.CaveBiome.NONE.name, 
            InfBiomes.INFDEV_227_ID.toString(), 
            BuiltInTypes.WorldScreen.INFDEV_227.name,
            true,
            false
        );
        
        INDEV = new WorldProvider(
            BuiltInTypes.Chunk.INDEV.name,
            OldChunkGeneratorSettings.INDEV.toString(), 
            BuiltInTypes.Biome.SINGLE.name, 
            BuiltInTypes.CaveBiome.NONE.name, 
            IndevBiomes.INDEV_NORMAL_ID.toString(), 
            BuiltInTypes.WorldScreen.INDEV.name,
            true,
            false
        );
        
        BETA_ISLANDS = new WorldProvider(
            BuiltInTypes.Chunk.BETA_ISLANDS.name,
            OldChunkGeneratorSettings.BETA_ISLANDS.toString(),
            BuiltInTypes.Biome.BETA.name,
            BuiltInTypes.CaveBiome.SINGLE.name,
            BetaBiomes.FOREST_ID.toString(),
            BuiltInTypes.WorldScreen.ISLAND.name,
            false,
            true
        );
        
        PE = new WorldProvider(
            BuiltInTypes.Chunk.PE.name,
            OldChunkGeneratorSettings.PE.toString(),
            BuiltInTypes.Biome.PE.name,
            BuiltInTypes.CaveBiome.NONE.name,
            PEBiomes.PE_FOREST_ID.toString(),
            BuiltInTypes.WorldScreen.INF.name,
            true,
            false
        );
    }
}
