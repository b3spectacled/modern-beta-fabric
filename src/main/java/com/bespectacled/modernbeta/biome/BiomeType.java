package com.bespectacled.modernbeta.biome;

import net.minecraft.nbt.CompoundTag;

public enum BiomeType {
    BETA(0, "beta"),
    //ICE_DESERT(1, "ice_desert"),
    SKY(2, "sky"),
    CLASSIC(3, "classic"),
    WINTER(4, "winter"),
    PLUS(5, "plus"),
    VANILLA(6, "vanilla");
    
    private final int id;
    private final String name;
    
    private BiomeType(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public static BiomeType fromId(int id) {
        for (BiomeType t : BiomeType.values()) {
            if (t.id == id) {
                return t;
            }
        }
        
        throw new IllegalArgumentException("[Modern Beta] No biome type matching id: " + id);
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