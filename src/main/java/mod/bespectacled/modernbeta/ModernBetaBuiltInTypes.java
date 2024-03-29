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
        
        private Chunk(String id) {
            this.id = id;
        }
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
        
        private CaveBiome(String id) {
            this.id = id;
        }
    }
    
    public enum SurfaceConfig {
        SAND(ModernBetaTagProviderBiome.SURFACE_CONFIG_SAND.id().toString()),
        RED_SAND(ModernBetaTagProviderBiome.SURFACE_CONFIG_RED_SAND.id().toString()),
        BADLANDS(ModernBetaTagProviderBiome.SURFACE_CONFIG_BADLANDS.id().toString()),
        NETHER(ModernBetaTagProviderBiome.SURFACE_CONFIG_NETHER.id().toString()),
        WARPED_NYLIUM(ModernBetaTagProviderBiome.SURFACE_CONFIG_WARPED_NYLIUM.id().toString()),
        CRIMSON_NYLIUM(ModernBetaTagProviderBiome.SURFACE_CONFIG_CRIMSON_NYLIUM.id().toString()),
        BASALT(ModernBetaTagProviderBiome.SURFACE_CONFIG_BASALT.id().toString()),
        SOUL_SOIL(ModernBetaTagProviderBiome.SURFACE_CONFIG_SOUL_SOIL.id().toString()),
        THEEND(ModernBetaTagProviderBiome.SURFACE_CONFIG_END.id().toString()),
        GRASS(ModernBetaTagProviderBiome.SURFACE_CONFIG_GRASS.id().toString()),
        MUD(ModernBetaTagProviderBiome.SURFACE_CONFIG_MUD.id().toString()),
        MYCELIUM(ModernBetaTagProviderBiome.SURFACE_CONFIG_MYCELIUM.id().toString()),
        PODZOL(ModernBetaTagProviderBiome.SURFACE_CONFIG_PODZOL.id().toString()),
        STONE(ModernBetaTagProviderBiome.SURFACE_CONFIG_STONE.id().toString()),
        SNOW(ModernBetaTagProviderBiome.SURFACE_CONFIG_SNOW.id().toString()),
        SNOW_DIRT(ModernBetaTagProviderBiome.SURFACE_CONFIG_SNOW_DIRT.id().toString()),
        SNOW_PACKED_ICE(ModernBetaTagProviderBiome.SURFACE_CONFIG_SNOW_PACKED_ICE.id().toString()),
        SNOW_STONE(ModernBetaTagProviderBiome.SURFACE_CONFIG_SNOW_STONE.id().toString())
        ;
        
        public final String id;
        
        private SurfaceConfig(String id) {
            this.id = id;
        }
    }
    
    public enum NoisePostProcessor {
        NONE("none")
        ;
        
        public final String id;
        
        private NoisePostProcessor(String id) {
            this.id = id;
        }
    }
    
    public enum BlockSource {
        DEEPSLATE("deepslate")
        ;
        
        public final String id;
        
        private BlockSource(String id) {
            this.id = id;
        }
    }
    
    public enum Preset {
        BETA(Chunk.BETA.id),
        SKYLANDS(Chunk.SKYLANDS.id),
        ALPHA(Chunk.ALPHA.id),
        INFDEV_611(Chunk.INFDEV_611.id),
        INFDEV_420(Chunk.INFDEV_420.id),
        INFDEV_415(Chunk.INFDEV_415.id),
        INFDEV_227(Chunk.INFDEV_227.id),
        INDEV(Chunk.INDEV.id),
        CLASSIC_0_30(Chunk.CLASSIC_0_30.id),
        PE(Chunk.PE.id)
        ;
        
        public final String id;
        
        private Preset(String id) {
            this.id = id;
        }
    }
    
    public enum PresetAlt {
        BETA_SKYLANDS("beta_skylands"),
        BETA_ISLES("beta_isles"),
        BETA_ISLE_LAND("beta_isle_land"),
        BETA_CAVE_DELIGHT("beta_cave_delight"),
        BETA_CAVE_CHAOS("beta_cave_chaos"),
        BETA_LARGE_BIOMES("beta_large_biomes"),
        BETA_XBOX_LEGACY("beta_xbox_legacy"),
        BETA_SURVIVAL_ISLAND("beta_survival_island"),
        BETA_VANILLA("beta_vanilla"),
        ALPHA_WINTER("alpha_winter"),
        INDEV_PARADISE("indev_paradise"),
        INDEV_WOODS("indev_woods"),
        INDEV_HELL("indev_hell")
        ;
        
        public final String id;
        
        private PresetAlt(String id) {
            this.id = id;
        }
    }
}
