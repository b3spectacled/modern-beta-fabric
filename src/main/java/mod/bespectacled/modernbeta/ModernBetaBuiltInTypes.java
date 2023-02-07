package mod.bespectacled.modernbeta;

import mod.bespectacled.modernbeta.data.ModernBetaBiomeTagProvider;
import net.minecraft.registry.tag.BiomeTags;

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
        
        public final String name;
        
        private Chunk(String name) { this.name = name; }
    }
    
    public enum Biome {
        BETA("beta"),
        SINGLE("single"),
        PE("pe"),
        VORONOI("voronoi")
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
    
    public enum SurfaceConfig {
        DESERT(ModernBetaBiomeTagProvider.IS_DESERT.id().toString()),
        BADLANDS(BiomeTags.IS_BADLANDS.id().toString()),
        NETHER(BiomeTags.IS_NETHER.id().toString()),
        THEEND(BiomeTags.IS_END.id().toString())
        ;
        
        public final String id;
        
        private SurfaceConfig(String name) { this.id = name; }
    }
    
    public enum NoisePostProcessor {
        NONE("none")
        ;
        
        public final String name;
        
        private NoisePostProcessor(String name) { this.name = name; }
    }
}
