package com.bespectacled.modernbeta.biome;

import java.util.function.BiFunction;

import com.bespectacled.modernbeta.biome.provider.AbstractBiomeProvider;
import com.bespectacled.modernbeta.biome.indev.IndevUtil.IndevTheme;
import com.bespectacled.modernbeta.biome.provider.*;

import net.minecraft.nbt.CompoundTag;

public enum BiomeType {
    BETA("beta", BetaBiomeProvider::new),
    SINGLE("single", SingleBiomeProvider::new),
    VANILLA("vanilla", VanillaBiomeProvider::new),
    
    // Legacy biome types
    SKY("sky", SingleBiomeProvider::new), 
    CLASSIC("classic", SingleBiomeProvider::new),
    WINTER("winter", SingleBiomeProvider::new),
    PLUS("plus", SingleBiomeProvider::new);
    
    private final String name;
    private final BiFunction<Long, CompoundTag, AbstractBiomeProvider> biomeProvider;
    
    private BiomeType(String name, BiFunction<Long, CompoundTag, AbstractBiomeProvider> biomeProvider) {
        this.name = name;
        this.biomeProvider = biomeProvider;
    }
    
    public String getName() {
        return this.name;
    }
    
    public AbstractBiomeProvider createBiomeProvider(Long seed, CompoundTag settings) {
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
    
    public static BiomeType getBiomeType(CompoundTag settings) {
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