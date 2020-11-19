package com.bespectacled.modernbeta.biome.provider;

import java.util.Map;

import com.bespectacled.modernbeta.biome.BetaBiomes;
import com.bespectacled.modernbeta.biome.PreBetaBiomes;
import com.bespectacled.modernbeta.biome.BetaBiomes.BiomeProviderType;
import com.bespectacled.modernbeta.util.BiomeUtil;
import com.bespectacled.modernbeta.util.WorldEnum.BiomeType;
import com.bespectacled.modernbeta.util.WorldEnum.WorldType;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class OldBiomeProvider implements IOldBiomeProvider {
    
    private final WorldType worldType;
    private final BiomeType biomeType;
    
    private static final double[] TEMP_HUMID_POINT = new double[2];
    
    public OldBiomeProvider(long seed, WorldType worldType, BiomeType biomeType) {
        this.worldType = worldType;
        this.biomeType = biomeType;
        
        BiomeUtil.setSeed(seed);
    }

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        Biome biome = null;
        
        Map<BiomeType, Identifier> biomeMapping = PreBetaBiomes.getBiomeMap(this.worldType);
        
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        switch(this.biomeType) {
            case BETA:
                BiomeUtil.fetchTempHumidAtPoint(TEMP_HUMID_POINT, absX, absZ);
                biome = registry.get(BetaBiomes.getBiomeFromLookup(TEMP_HUMID_POINT[0], TEMP_HUMID_POINT[1], BiomeProviderType.LAND));
                break;
            case SKY:
                biome = registry.get(BetaBiomes.SKY_ID);
                break;
            case CLASSIC:
                biome = registry.get(biomeMapping.get(BiomeType.CLASSIC));
                break;
            case WINTER:
                biome = registry.get(biomeMapping.get(BiomeType.WINTER));
                break;
            case PLUS:
                BiomeUtil.fetchTempHumidAtPoint(TEMP_HUMID_POINT, absX, absZ);
                biome = TEMP_HUMID_POINT[0] < 0.5f ? 
                    registry.get(biomeMapping.get(BiomeType.WINTER)) : 
                    registry.get(biomeMapping.get(BiomeType.CLASSIC));
                break;
            default:
                biome = registry.get(biomeMapping.get(BiomeType.CLASSIC));
        }
        
        //System.out.println("GETTING BIOME! " + biome);
        
        return biome;
    }

    @Override
    public Biome getOceanBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        Biome biome = null;
        
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        if (this.biomeType == BiomeType.BETA) {
            BiomeUtil.fetchTempHumidAtPoint(TEMP_HUMID_POINT, absX, absZ);
            biome = registry.get(BetaBiomes.getBiomeFromLookup(TEMP_HUMID_POINT[0], TEMP_HUMID_POINT[1], BiomeProviderType.OCEAN));
        } else {
            biome = this.getBiomeForNoiseGen(registry, biomeX, 0, biomeZ);
        }
        
        return biome;
    }
}
