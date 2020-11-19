package com.bespectacled.modernbeta.biome.provider;

import java.util.List;

import com.bespectacled.modernbeta.biome.BetaBiomes;
import com.bespectacled.modernbeta.util.BiomeUtil;
import com.bespectacled.modernbeta.util.WorldEnum.BiomeType;
import com.bespectacled.modernbeta.util.WorldEnum.WorldType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class OldBiomeProviderUtil {
    
    public static IOldBiomeProvider getBiomeProvider(long seed, WorldType worldType, BiomeType biomeType) {
        return new OldBiomeProvider(seed, worldType, biomeType);
    }
    
    public static List<RegistryKey<Biome>> getBiomeRegistryList(CompoundTag settings) {
        WorldType worldType = getWorldType(settings);
        BiomeType biomeType = getBiomeType(settings);
        List<RegistryKey<Biome>> biomeRegistry = null;
        
        if (biomeType == BiomeType.VANILLA)
            return BiomeUtil.VANILLA_BIOMES;
        
        switch(biomeType) {
            case BETA:
                biomeRegistry = BetaBiomes.getBiomeRegistryList(settings);
                break;
            default:
                biomeRegistry = BiomeUtil.VANILLA_BIOMES;
        }
        
        //return BiomeUtil.VANILLA_BIOMES;
        return biomeRegistry;
    }
    
    public static WorldType getWorldType(CompoundTag settings) {
        WorldType type = WorldType.BETA;
        
        if (settings.contains("worldType"))
            type = WorldType.fromName(settings.getString("worldType"));
        
        return type;
    }
    
    public static BiomeType getBiomeType(CompoundTag settings) {
        BiomeType type = BiomeType.BETA;
        
        if (settings.contains("biomeType")) 
            type = BiomeType.fromName(settings.getString("biomeType"));
        
        return type;
    }
}
