package com.bespectacled.modernbeta;

public final class ModernBetaBuiltInTypes {
    public static final String DEFAULT_ID = "default";
    
    public enum Chunk {
        BETA("beta"),
        SKYLANDS("skylands"),
        ALPHA("alpha"),
        INFDEV_611("infdev_611"),
        INFDEV_420("infdev_420"),
        INFDEV_415("infdev_415"),
        INFDEV_227("infdev_227"),
        INDEV("indev"),
        CLASSIC_0_30("classic_0_30"),
        BETA_ISLANDS("beta_islands"),
        PE("pe")
        ;
        
        public final String name;
        
        private Chunk(String name) { this.name = name; }
    }
    
    public enum Biome {
        BETA("beta"),
        SINGLE("single"),
        VANILLA("vanilla"),
        PE("pe")
        ;
        
        public final String name;
        
        private Biome(String name) { this.name = name; }
    }
    
    public enum CaveBiome {
        NONE("none"),
        VORONOI("voronoi"),
        BETA("beta"),
        SINGLE("single")
        ;
        
        public final String name;
        
        private CaveBiome(String name) { this.name = name; }
    }
    
    public enum WorldScreen {
        BETA("beta"),
        NOISE("noise"),
        INFDEV_227("infdev_227"),
        INDEV("indev"),
        CLASSIC("classic"),
        ISLAND("island")
        ;
        
        public final String name;
        
        private WorldScreen(String name) { this.name = name; }
    }
    
    public enum NoisePostProcessor {
        NONE("none")
        ;
        
        public final String name;
        
        private NoisePostProcessor(String name) { this.name = name; }
    }
}
