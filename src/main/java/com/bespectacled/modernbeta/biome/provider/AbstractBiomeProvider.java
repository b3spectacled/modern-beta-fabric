package com.bespectacled.modernbeta.biome.provider;

import java.util.List;

import com.bespectacled.modernbeta.biome.BetaBiomes;
import com.bespectacled.modernbeta.biome.IndevBiomes;
import com.bespectacled.modernbeta.biome.InfBiomes;
import com.bespectacled.modernbeta.biome.VanillaBiomes;
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
    
    public static AbstractBiomeProvider getBiomeProvider(long seed, CompoundTag settings) {
        WorldType worldType = WorldType.getWorldType(settings);
        BiomeType biomeType = BiomeType.getBiomeType(settings);
        
        if (worldType == WorldType.INDEV)
            return new IndevBiomeProvider(seed, settings);
        
        switch(biomeType) {
            case BETA:
                return new BetaBiomeProvider(seed);
            case SKY:
                return new SingleBiomeProvider(seed, BetaBiomes.SKY_ID);
            case PLUS:
                return new PlusBiomeProvider(seed, InfBiomes.getBiomeMap(worldType));
            case CLASSIC:
                return new SingleBiomeProvider(seed, InfBiomes.getBiomeMap(worldType).get(BiomeType.CLASSIC));
            case WINTER:
                return new SingleBiomeProvider(seed, InfBiomes.getBiomeMap(worldType).get(BiomeType.WINTER));
            case VANILLA:
                return new VanillaBiomeProvider(seed);
        }
        
        return new SingleBiomeProvider(seed, InfBiomes.ALPHA_ID);
    }
    
    public static List<RegistryKey<Biome>> getBiomeRegistryList(CompoundTag settings) {
        WorldType worldType = WorldType.getWorldType(settings);
        BiomeType biomeType = BiomeType.getBiomeType(settings);
        
        if (worldType == WorldType.INDEV)
            return IndevBiomes.INDEV_BIOME_KEYS;
        
        if (biomeType == BiomeType.VANILLA)
            return VanillaBiomes.VANILLA_BIOME_KEYS;
        
        if (biomeType == BiomeType.BETA || biomeType == BiomeType.SKY)
            return BetaBiomes.BETA_BIOME_KEYS;
        
        return InfBiomes.getBiomeRegistryList(worldType);
    }
}
