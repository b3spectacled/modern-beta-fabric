package com.bespectacled.modernbeta.biome.provider;

import java.util.List;

import com.bespectacled.modernbeta.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.biome.classic.ClassicBiomes;
import com.bespectacled.modernbeta.util.WorldEnum.BiomeType;
import com.bespectacled.modernbeta.util.WorldEnum.WorldType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public abstract class AbstractBiomeProvider {
    public abstract Biome getBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ);
    
    public Biome getOceanBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        return this.getBiomeForNoiseGen(registry, biomeX, biomeY, biomeZ);
    }
    
    public abstract List<RegistryKey<Biome>> getBiomesForRegistry();
    
    public static AbstractBiomeProvider getBiomeProvider(long seed, CompoundTag settings) {
        WorldType worldType = WorldType.getWorldType(settings);
        BiomeType biomeType = BiomeType.getBiomeType(settings);
        AbstractBiomeProvider biomeProvider;
        
        if (worldType == WorldType.INDEV)
            return new IndevBiomeProvider(seed, settings); 
        
        switch(biomeType) {
            case BETA:
                biomeProvider = new BetaBiomeProvider(seed);
                break;
            case SKY:
                biomeProvider = new SingleBiomeProvider(seed, BetaBiomes.SKY_ID);
                break;
            case PLUS:
                biomeProvider = new PlusBiomeProvider(seed, ClassicBiomes.getBiomeMap(worldType));
                break;
            case CLASSIC:
                biomeProvider = new SingleBiomeProvider(seed, ClassicBiomes.getBiomeMap(worldType).get(BiomeType.CLASSIC));
                break;
            case WINTER:
                biomeProvider = new SingleBiomeProvider(seed, ClassicBiomes.getBiomeMap(worldType).get(BiomeType.WINTER));
                break;
            case VANILLA:
                biomeProvider = new VanillaBiomeProvider(seed);
                break;
            default:
                biomeProvider = new SingleBiomeProvider(seed, ClassicBiomes.ALPHA_ID);
        }
        
        return biomeProvider;
    }
}
