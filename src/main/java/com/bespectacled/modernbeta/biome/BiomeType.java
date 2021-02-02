package com.bespectacled.modernbeta.biome;

import net.minecraft.nbt.CompoundTag;

public enum BiomeType {
    BETA("beta"),
    BETA_ICE_DESERT("beta_ice_desert"),
    SKY("sky"),
    CLASSIC("classic"),
    WINTER("winter"),
    PLUS("plus"),
    VANILLA("vanilla");
    //NETHER("nether");
    
    private final String name;
    
    private BiomeType(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
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
}