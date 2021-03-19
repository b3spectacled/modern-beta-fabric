package com.bespectacled.modernbeta.biome.indev;

import net.minecraft.util.Identifier;

public class IndevUtil {
    public enum IndevTheme {
        NORMAL(0, "normal", IndevBiomes.INDEV_NORMAL_ID),
        HELL(1, "hell", IndevBiomes.INDEV_HELL_ID),
        PARADISE(2, "paradise", IndevBiomes.INDEV_PARADISE_ID),
        WOODS(3, "woods", IndevBiomes.INDEV_WOODS_ID),
        SNOWY(4, "snowy", IndevBiomes.INDEV_SNOWY_ID);
        
        private final int id;
        private final String name;
        private final Identifier defaultBiome;
        
        private IndevTheme(int id, String name, Identifier defaultBiome) {
            this.id = id;
            this.name = name;
            this.defaultBiome = defaultBiome;
        }
        
        public int getId() {
            return this.id;
        }
        
        public String getName() {
            return this.name;
        }
        
        public Identifier getDefaultBiome() {
            return this.defaultBiome;
        }
        
        public static IndevTheme fromId(int id) {
            for (IndevTheme t : IndevTheme.values()) {
                if (t.id == id) {
                    return t;
                }
            }
            
            throw new IllegalArgumentException("No Indev Theme matching id: " + id);
        }
        
        public static IndevTheme fromName(String name) {
            for (IndevTheme t : IndevTheme.values()) {
                if (t.name.equalsIgnoreCase(name)) {
                    return t;
                }
            }
            
            throw new IllegalArgumentException("No Indev Theme matching name: " + name);
        }
        
        public static IndevTheme fromString(String str) {
            IndevTheme theme;
            
            if (str.matches("-?\\d+")) {
                theme = fromId(Integer.parseInt(str));
            } else {
                theme = fromName(str);
            }
            
            return theme;
        }
    }
    
    public enum IndevType {
        ISLAND(0, "island"),
        FLOATING(1, "floating"),
        INLAND(2, "inland");
        
        private final int id;
        private final String name;
        
        private IndevType(int id, String name) {
            this.id = id;
            this.name = name;
        }
        
        public int getId() {
            return this.id;
        }
        
        public String getName() {
            return this.name;
        }
        
        public static IndevType fromId(int id) {
            for (IndevType t : IndevType.values()) {
                if (t.id == id) {
                    return t;
                }
            }
            
            throw new IllegalArgumentException("No Indev Type matching id: " + id);
        }
        
        public static IndevType fromName(String name) {
            for (IndevType t : IndevType.values()) {
                if (t.name.equalsIgnoreCase(name)) {
                    return t;
                }
            }
            
            throw new IllegalArgumentException("No Indev Type matching name: " + name);
        }
        
        public static IndevType fromString(String str) {
            IndevType type;
            
            if (str.matches("-?\\d+")) {
                type = fromId(Integer.parseInt(str));
            } else {
                type = fromName(str);
            }
            
            return type;
        }
    }
    
    public static boolean inIndevRegion(int x, int z, int width, int length) {
        int halfWidth = width / 2;
        int halfLength = length / 2;
        
        if (x >= -halfWidth && x < halfWidth && z >= -halfLength && z < halfLength)
            return true;
        
        return false;
    }
}
