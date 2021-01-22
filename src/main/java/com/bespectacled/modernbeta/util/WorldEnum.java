package com.bespectacled.modernbeta.util;

import net.minecraft.nbt.CompoundTag;

public class WorldEnum {
    
    public enum WorldType {
        BETA(0, "beta", true),
        SKYLANDS(1, "skylands", false),
        ALPHA(2, "alpha", true),
        INFDEV(3, "infdev", true),
        INFDEV_OLD(4, "infdev_old", true),
        INDEV(5, "indev", false);
        
        private final int id;
        private final String name;
        private final boolean hasOceans;
        
        private WorldType(int id, String name, boolean hasOceans) {
            this.id = id;
            this.name = name;
            this.hasOceans = hasOceans;
        }
        
        public int getId() {
            return this.id;
        }
        
        public String getName() {
            return this.name;
        }
        
        public boolean hasOceans() {
            return this.hasOceans;
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
}
