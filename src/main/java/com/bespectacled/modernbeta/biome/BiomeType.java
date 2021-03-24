package com.bespectacled.modernbeta.biome;

import java.util.function.BiFunction;

import com.bespectacled.modernbeta.biome.provider.AbstractBiomeProvider;
import com.bespectacled.modernbeta.biome.provider.*;

import net.minecraft.nbt.NbtCompound;

public enum BiomeType {
    BETA("beta", true, BetaBiomeProvider::new),
    SINGLE("single", true, SingleBiomeProvider::new),
    VANILLA("vanilla", false, VanillaBiomeProvider::new),
    
    // Legacy biome types
    SKY("sky", false, SingleBiomeProvider::new), 
    CLASSIC("classic", false, SingleBiomeProvider::new),
    WINTER("winter", false, SingleBiomeProvider::new),
    PLUS("plus", false, SingleBiomeProvider::new);
    
    private final String name;
    private final boolean hasOptions;
    private final BiFunction<Long, NbtCompound, AbstractBiomeProvider> biomeProvider;
    
    private BiomeType(String name, boolean hasOptions, BiFunction<Long, NbtCompound, AbstractBiomeProvider> biomeProvider) {
        this.name = name;
        this.hasOptions = hasOptions;
        this.biomeProvider = biomeProvider;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean hasOptions() {
        return this.hasOptions;
    }
    
    public AbstractBiomeProvider createBiomeProvider(Long seed, NbtCompound settings) {
        return this.biomeProvider.apply(seed, settings);
    }
    
    public static BiomeType fromName(String name) {
        for (BiomeType t : BiomeType.values()) {
            if (t.name.equalsIgnoreCase(name)) {
                return t;
            }
        }
        
        throw new IllegalArgumentException("[Modern Beta] No biome type matching name: " + name);
    }
    
    public static BiomeType getBiomeType(NbtCompound settings) {
        BiomeType type = BiomeType.BETA;
        
        if (settings.contains("biomeType")) 
            type = BiomeType.fromName(settings.getString("biomeType"));
        
        return type;
    }
    
    public static boolean getExclusions(BiomeType biomeType) {
        if (biomeType == BiomeType.SKY ||
            biomeType == BiomeType.CLASSIC ||
            biomeType == BiomeType.WINTER ||
            biomeType == BiomeType.PLUS) {
            return false;
        }
        
        return true;
    }
}