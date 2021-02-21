package com.bespectacled.modernbeta.gen;

import net.minecraft.nbt.CompoundTag;

public enum WorldType {
    BETA(0, "beta"),
    SKYLANDS(1, "skylands"),
    ALPHA(2, "alpha"),
    INFDEV(3, "infdev"),
    INFDEV_OLD(4, "infdev_old"),
    INDEV(5, "indev");
    
    private final int id;
    private final String name;
    
    private WorldType(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public static WorldType fromId(int id) {
        for (WorldType w : WorldType.values()) {
            if (w.id == id) {
                return w;
            }
        }
        
        throw new IllegalArgumentException("[Modern Beta] No world type matching id: " + id);
    }
    
    public static WorldType fromName(String name) {
        for (WorldType w : WorldType.values()) {
            if (w.name.equalsIgnoreCase(name)) {
                return w;
            }
        }
        
        throw new IllegalArgumentException("[Modern Beta] No world type matching name: " + name);
    }
    
    public static WorldType getWorldType(CompoundTag settings) {
        WorldType type = WorldType.BETA;
        
        if (settings.contains("worldType"))
            type = WorldType.fromName(settings.getString("worldType"));
        
        return type;
    }
}