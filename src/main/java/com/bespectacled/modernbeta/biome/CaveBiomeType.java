package com.bespectacled.modernbeta.biome;

import net.minecraft.nbt.CompoundTag;

public enum CaveBiomeType {
    VANILLA("vanilla"),
    NONE("none");
    
    private final String name;
    
    private CaveBiomeType(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public static CaveBiomeType fromName(String name) {
        for (CaveBiomeType t : CaveBiomeType.values()) {
            if (t.name.equalsIgnoreCase(name)) {
                return t;
            }
        }
        
        throw new IllegalArgumentException("[Modern Beta] No cave biome type matching name: " + name);
    }
    
    public static CaveBiomeType getCaveBiomeType(CompoundTag settings) {
        CaveBiomeType type = CaveBiomeType.VANILLA;
        
        if (settings.contains("caveBiomeType")) 
            type = CaveBiomeType.fromName(settings.getString("caveBiomeType"));
        
        return type;
    }
}
