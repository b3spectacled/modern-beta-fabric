package com.bespectacled.modernbeta;

import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.api.world.WorldProvider;
import com.bespectacled.modernbeta.world.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.world.biome.indev.IndevBiomes;
import com.bespectacled.modernbeta.world.biome.inf.InfBiomes;
import com.bespectacled.modernbeta.world.biome.pe.PEBiomes;

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
            BuiltInTypes.Biome.BETA.name,
            BuiltInTypes.CaveBiome.VANILLA.name, 
            BetaBiomes.FOREST_ID.toString(), 
            BuiltInTypes.WorldScreen.BASE.name,
            true,
            false,
            true,
            true
        );
        
        BETA = new WorldProvider(
            BuiltInTypes.Chunk.BETA.name,
            BuiltInTypes.Biome.BETA.name,
            BuiltInTypes.CaveBiome.VANILLA.name, 
            BetaBiomes.FOREST_ID.toString(), 
            BuiltInTypes.WorldScreen.INF_CLIMATE.name,
            true,
            false,
            true,
            true
        );
        
        
        SKYLANDS = new WorldProvider(
            BuiltInTypes.Chunk.SKYLANDS.name,
            BuiltInTypes.Biome.SINGLE.name, 
            BuiltInTypes.CaveBiome.NONE.name, 
            BetaBiomes.SKY_ID.toString(), 
            BuiltInTypes.WorldScreen.BASE.name,
            false,
            false,
            false,
            true
        );
        
        ALPHA = new WorldProvider(
            BuiltInTypes.Chunk.ALPHA.name,
            BuiltInTypes.Biome.SINGLE.name, 
            BuiltInTypes.CaveBiome.NONE.name, 
            InfBiomes.ALPHA_ID.toString(), 
            BuiltInTypes.WorldScreen.INF.name,
            true,
            false,
            false,
            true
        );
        
        INFDEV_611 = new WorldProvider(
            BuiltInTypes.Chunk.INFDEV_611.name,
            BuiltInTypes.Biome.SINGLE.name,
            BuiltInTypes.CaveBiome.NONE.name,
            InfBiomes.INFDEV_611_ID.toString(),
            BuiltInTypes.WorldScreen.INF.name,
            false,
            true,
            false,
            true
        );
        
        INFDEV_420 = new WorldProvider(
            BuiltInTypes.Chunk.INFDEV_420.name,
            BuiltInTypes.Biome.SINGLE.name, 
            BuiltInTypes.CaveBiome.NONE.name, 
            InfBiomes.INFDEV_420_ID.toString(), 
            BuiltInTypes.WorldScreen.INF.name,
            true,
            false,
            false,
            true
        );
        
        INFDEV_415 = new WorldProvider(
            BuiltInTypes.Chunk.INFDEV_415.name,
            BuiltInTypes.Biome.SINGLE.name, 
            BuiltInTypes.CaveBiome.NONE.name, 
            InfBiomes.INFDEV_415_ID.toString(), 
            BuiltInTypes.WorldScreen.INF.name,
            true,
            false,
            false, true
        );
        
        INFDEV_227 = new WorldProvider(
            BuiltInTypes.Chunk.INFDEV_227.name, 
            BuiltInTypes.Biome.SINGLE.name, 
            BuiltInTypes.CaveBiome.NONE.name, 
            InfBiomes.INFDEV_227_ID.toString(), 
            BuiltInTypes.WorldScreen.INFDEV_227.name,
            true,
            false,
            false,
            true
        );
        
        INDEV = new WorldProvider(
            BuiltInTypes.Chunk.INDEV.name,
            BuiltInTypes.Biome.SINGLE.name, 
            BuiltInTypes.CaveBiome.NONE.name, 
            IndevBiomes.INDEV_NORMAL_ID.toString(), 
            BuiltInTypes.WorldScreen.INDEV.name,
            true,
            false,
            false,
            false
        );
        
        CLASSIC_0_30 = new WorldProvider(
            BuiltInTypes.Chunk.CLASSIC_0_30.name,
            BuiltInTypes.Biome.SINGLE.name, 
            BuiltInTypes.CaveBiome.NONE.name,
            IndevBiomes.INDEV_NORMAL_ID.toString(), 
            BuiltInTypes.WorldScreen.PRE_INF.name,
            true,
            false,
            false,
            false
        );
        
        BETA_ISLANDS = new WorldProvider(
            BuiltInTypes.Chunk.BETA_ISLANDS.name,
            BuiltInTypes.Biome.BETA.name,
            BuiltInTypes.CaveBiome.NONE.name,
            BetaBiomes.FOREST_ID.toString(),
            BuiltInTypes.WorldScreen.ISLAND.name,
            false,
            true,
            true,
            true
        );
        
        PE = new WorldProvider(
            BuiltInTypes.Chunk.PE.name,
            BuiltInTypes.Biome.PE.name,
            BuiltInTypes.CaveBiome.NONE.name,
            PEBiomes.PE_FOREST_ID.toString(),
            BuiltInTypes.WorldScreen.INF_CLIMATE.name,
            true,
            false,
            false,
            true
        );
    }
}
