package com.bespectacled.modernbeta.api.registry;

public final class BuiltInTypes {
    public static final String DEFAULT_ID = "default";
    
    public enum Chunk {
        BETA("beta"),
        SKYLANDS("skylands"),
        ALPHA("alpha"),
        INFDEV_611("infdev_611"),
        INFDEV_415("infdev_415"),
        INFDEV_227("infdev_227"),
        INDEV("indev"),
        BETA_ISLANDS("beta_islands")
        ;
        
        public final String name;
        
        private Chunk(String name) { this.name = name; }
    }
    
    public enum Biome {
        BETA("beta"),
        SINGLE("single"),
        VANILLA("vanilla")
        ;
        
        public final String name;
        
        private Biome(String name) { this.name = name; }
    }
    
    public enum CaveBiome {
        NONE("none"),
        VANILLA("vanilla"),
        BETA("beta"),
        SINGLE("single")
        ;
        
        public final String name;
        
        private CaveBiome(String name) { this.name = name; }
    }
    
    public enum WorldScreen {
        BASE("base"),
        INF("inf"),
        INFDEV_227("infdev_227"),
        INDEV("indev"),
        ISLAND("island")
        ;
        
        public final String name;
        
        private WorldScreen(String name) { this.name = name; }
    }
}
