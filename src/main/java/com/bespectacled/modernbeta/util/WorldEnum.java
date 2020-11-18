package com.bespectacled.modernbeta.util;

public class WorldEnum {
    
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
            
            throw new IllegalArgumentException("No world type matching id: " + id);
        }
        
        public static WorldType fromName(String name) {
            for (WorldType w : WorldType.values()) {
                if (w.name.equalsIgnoreCase(name)) {
                    return w;
                }
            }
            
            throw new IllegalArgumentException("No world type matching name: " + name);
        }
    }
    
    public enum BetaBiomeType {
        CLASSIC(0, "classic"),
        ICE_DESERT(1, "ice_desert"),
        SKY(2, "sky"),
        VANILLA(3, "vanilla");
        
        private final int id;
        private final String name;
        
        private BetaBiomeType(int id, String name) {
            this.id = id;
            this.name = name;
        }
        
        public int getId() {
            return this.id;
        }
        
        public String getName() {
            return this.name;
        }
        
        public static BetaBiomeType fromId(int id) {
            for (BetaBiomeType t : BetaBiomeType.values()) {
                if (t.id == id) {
                    return t;
                }
            }
            
            throw new IllegalArgumentException("No Beta biome type matching id: " + id);
        }
        
        public static BetaBiomeType fromName(String name) {
            for (BetaBiomeType t : BetaBiomeType.values()) {
                if (t.name.equalsIgnoreCase(name)) {
                    return t;
                }
            }
            
            throw new IllegalArgumentException("No Beta biome type matching name: " + name);
        }
    }
    
    public enum PreBetaBiomeType {
        CLASSIC(0, "classic"),
        WINTER(1, "winter"),
        PLUS(2, "plus"),
        VANILLA(3, "vanilla");
        
        private final int id;
        private final String name;
        
        private PreBetaBiomeType(int id, String name) {
            this.id = id;
            this.name = name;
        }
        
        public int getId() {
            return this.id;
        }
        
        public String getName() {
            return this.name;
        }
        
        public static PreBetaBiomeType fromId(int id) {
            for (PreBetaBiomeType t : PreBetaBiomeType.values()) {
                if (t.id == id) {
                    return t;
                }
            }
            
            throw new IllegalArgumentException("No Pre-Beta biome type matching id: " + id);
        }
        
        public static PreBetaBiomeType fromName(String name) {
            for (PreBetaBiomeType t : PreBetaBiomeType.values()) {
                if (t.name.equalsIgnoreCase(name)) {
                    return t;
                }
            }
            
            throw new IllegalArgumentException("No Pre-Beta biome type matching name: " + name);
        }
    }
}
