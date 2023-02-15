package mod.bespectacled.modernbeta;

import mod.bespectacled.modernbeta.data.ModernBetaTagProviderBiome;

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
        PE("pe")
        ;
        
        public final String id;
        
        private Chunk(String id) { this.id = id; }
    }
    
    public enum Biome {
        BETA("beta"),
        SINGLE("single"),
        PE("pe"),
        VORONOI("voronoi")
        ;
        
        public final String id;
        
        private Biome(String id) { this.id = id; }
    }
    
    public enum CaveBiome {
        NONE("none"),
        VORONOI("voronoi"),
        BETA("beta"),
        SINGLE("single")
        ;
        
        public final String id;
        
        private CaveBiome(String id) { this.id = id; }
    }
    
    public enum SurfaceConfig {
        SAND(ModernBetaTagProviderBiome.SURFACE_CONFIG_SAND.id().toString()),
        RED_SAND(ModernBetaTagProviderBiome.SURFACE_CONFIG_RED_SAND.id().toString()),
        BADLANDS(ModernBetaTagProviderBiome.SURFACE_CONFIG_BADLANDS.id().toString()),
        NETHER(ModernBetaTagProviderBiome.SURFACE_CONFIG_NETHER.id().toString()),
        THEEND(ModernBetaTagProviderBiome.SURFACE_CONFIG_END.id().toString())
        ;
        
        public final String id;
        
        private SurfaceConfig(String id) { this.id = id; }
    }
    
    public enum NoisePostProcessor {
        NONE("none")
        ;
        
        public final String id;
        
        private NoisePostProcessor(String id) { this.id = id; }
    }
    
    public enum BlockSource {
        DEEPSLATE("deepslate")
        ;
        
        public final String id;
        
        private BlockSource(String id) { this.id = id; }
    }
}
