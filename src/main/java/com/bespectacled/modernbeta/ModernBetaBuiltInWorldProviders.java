package com.bespectacled.modernbeta;

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
            ModernBetaBuiltInTypes.Chunk.BETA.name,
            ModernBetaBuiltInTypes.Biome.BETA.name,
            ModernBetaBuiltInTypes.CaveBiome.VORONOI.name, 
            BetaBiomes.FOREST_KEY.getValue().toString(), 
            ModernBetaBuiltInTypes.WorldScreen.BASE.name,
            true,
            false,
            true,
            true
        );
        
        BETA = new WorldProvider(
            ModernBetaBuiltInTypes.Chunk.BETA.name,
            ModernBetaBuiltInTypes.Biome.BETA.name,
            ModernBetaBuiltInTypes.CaveBiome.VORONOI.name, 
            BetaBiomes.FOREST_KEY.getValue().toString(), 
            ModernBetaBuiltInTypes.WorldScreen.INF_CLIMATE.name,
            true,
            false,
            true,
            true
        );
        
        SKYLANDS = new WorldProvider(
            ModernBetaBuiltInTypes.Chunk.SKYLANDS.name,
            ModernBetaBuiltInTypes.Biome.SINGLE.name, 
            ModernBetaBuiltInTypes.CaveBiome.NONE.name, 
            BetaBiomes.SKY_KEY.getValue().toString(), 
            ModernBetaBuiltInTypes.DEFAULT_ID,
            false,
            false,
            false,
            true
        );
        
        ALPHA = new WorldProvider(
            ModernBetaBuiltInTypes.Chunk.ALPHA.name,
            ModernBetaBuiltInTypes.Biome.SINGLE.name, 
            ModernBetaBuiltInTypes.CaveBiome.NONE.name, 
            InfBiomes.ALPHA_KEY.getValue().toString(), 
            ModernBetaBuiltInTypes.WorldScreen.INF.name,
            true,
            false,
            false,
            true
        );
        
        INFDEV_611 = new WorldProvider(
            ModernBetaBuiltInTypes.Chunk.INFDEV_611.name,
            ModernBetaBuiltInTypes.Biome.SINGLE.name,
            ModernBetaBuiltInTypes.CaveBiome.NONE.name,
            InfBiomes.INFDEV_611_KEY.getValue().toString(),
            ModernBetaBuiltInTypes.WorldScreen.INF.name,
            false,
            true,
            false,
            true
        );
        
        INFDEV_420 = new WorldProvider(
            ModernBetaBuiltInTypes.Chunk.INFDEV_420.name,
            ModernBetaBuiltInTypes.Biome.SINGLE.name, 
            ModernBetaBuiltInTypes.CaveBiome.NONE.name, 
            InfBiomes.INFDEV_420_KEY.getValue().toString(), 
            ModernBetaBuiltInTypes.WorldScreen.INF.name,
            true,
            false,
            false,
            true
        );
        
        INFDEV_415 = new WorldProvider(
            ModernBetaBuiltInTypes.Chunk.INFDEV_415.name,
            ModernBetaBuiltInTypes.Biome.SINGLE.name, 
            ModernBetaBuiltInTypes.CaveBiome.NONE.name, 
            InfBiomes.INFDEV_415_KEY.getValue().toString(), 
            ModernBetaBuiltInTypes.WorldScreen.INF.name,
            true,
            false,
            false, true
        );
        
        INFDEV_227 = new WorldProvider(
            ModernBetaBuiltInTypes.Chunk.INFDEV_227.name, 
            ModernBetaBuiltInTypes.Biome.SINGLE.name, 
            ModernBetaBuiltInTypes.CaveBiome.NONE.name, 
            InfBiomes.INFDEV_227_KEY.getValue().toString(), 
            ModernBetaBuiltInTypes.WorldScreen.INFDEV_227.name,
            true,
            false,
            false,
            true
        );
        
        INDEV = new WorldProvider(
            ModernBetaBuiltInTypes.Chunk.INDEV.name,
            ModernBetaBuiltInTypes.Biome.SINGLE.name, 
            ModernBetaBuiltInTypes.CaveBiome.NONE.name, 
            IndevBiomes.INDEV_NORMAL_KEY.getValue().toString(), 
            ModernBetaBuiltInTypes.WorldScreen.INDEV.name,
            true,
            false,
            false,
            false
        );
        
        CLASSIC_0_30 = new WorldProvider(
            ModernBetaBuiltInTypes.Chunk.CLASSIC_0_30.name,
            ModernBetaBuiltInTypes.Biome.SINGLE.name, 
            ModernBetaBuiltInTypes.CaveBiome.NONE.name,
            IndevBiomes.INDEV_NORMAL_KEY.getValue().toString(), 
            ModernBetaBuiltInTypes.WorldScreen.PRE_INF.name,
            true,
            false,
            false,
            false
        );
        
        BETA_ISLANDS = new WorldProvider(
            ModernBetaBuiltInTypes.Chunk.BETA_ISLANDS.name,
            ModernBetaBuiltInTypes.Biome.BETA.name,
            ModernBetaBuiltInTypes.CaveBiome.VORONOI.name,
            BetaBiomes.FOREST_KEY.getValue().toString(),
            ModernBetaBuiltInTypes.WorldScreen.ISLAND.name,
            false,
            true,
            true,
            true
        );
        
        PE = new WorldProvider(
            ModernBetaBuiltInTypes.Chunk.PE.name,
            ModernBetaBuiltInTypes.Biome.PE.name,
            ModernBetaBuiltInTypes.CaveBiome.NONE.name,
            PEBiomes.PE_FOREST_KEY.getValue().toString(),
            ModernBetaBuiltInTypes.WorldScreen.INF_CLIMATE.name,
            true,
            false,
            false,
            true
        );
    }
}
