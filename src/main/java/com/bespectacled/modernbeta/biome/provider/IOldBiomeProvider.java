package com.bespectacled.modernbeta.biome.provider;

import java.util.List;

import com.bespectacled.modernbeta.biome.BetaBiomes;
import com.bespectacled.modernbeta.biome.IndevBiomes;
import com.bespectacled.modernbeta.biome.InfBiomes;
import com.bespectacled.modernbeta.util.BiomeUtil;
import com.bespectacled.modernbeta.util.WorldEnum;
import com.bespectacled.modernbeta.util.WorldEnum.BiomeType;
import com.bespectacled.modernbeta.util.WorldEnum.WorldType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public interface IOldBiomeProvider {
    public Biome getBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ);
    public Biome getOceanBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ);
    
    public static IOldBiomeProvider getBiomeProvider(long seed, CompoundTag settings) {
        WorldType worldType = WorldEnum.getWorldType(settings);
        
        if (worldType == WorldType.INDEV)
            return new IndevBiomeProvider(seed, settings);
        
        return new InfBiomeProvider(seed, settings);
    }
    
    public static List<RegistryKey<Biome>> getBiomeRegistryList(CompoundTag settings) {
        WorldType worldType = WorldEnum.getWorldType(settings);
        BiomeType biomeType = WorldEnum.getBiomeType(settings);
        
        if (worldType == WorldType.INDEV)
            return IndevBiomes.getBiomeList();
        
        if (biomeType == BiomeType.VANILLA)
            return BiomeUtil.VANILLA_BIOMES;
        
        if (biomeType == BiomeType.BETA || biomeType == BiomeType.SKY)
            return BetaBiomes.getBiomeRegistryList();
        
        return InfBiomes.getBiomeRegistryList(worldType);
    }
}
