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
        PE("pe"),
        EARLY_RELEASE("early_release")
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
        VORONOI("voronoi"),
        FRACTAL("fractal")
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

    public enum HeightConfig {
        HEIGHT_CONFIG_OCEAN(ModernBetaTagProviderBiome.HEIGHT_CONFIG_OCEAN.id().toString()),
        HEIGHT_CONFIG_BETA_OCEAN(ModernBetaTagProviderBiome.HEIGHT_CONFIG_BETA_OCEAN.id().toString()),
        HEIGHT_CONFIG_DESERT(ModernBetaTagProviderBiome.HEIGHT_CONFIG_DESERT.id().toString()),
        HEIGHT_CONFIG_EXTREME_HILLS(ModernBetaTagProviderBiome.HEIGHT_CONFIG_EXTREME_HILLS.id().toString()),
        HEIGHT_CONFIG_BETA_HILLS(ModernBetaTagProviderBiome.HEIGHT_CONFIG_BETA_HILLS.id().toString()),
        HEIGHT_CONFIG_TAIGA(ModernBetaTagProviderBiome.HEIGHT_CONFIG_TAIGA.id().toString()),
        HEIGHT_CONFIG_SWAMPLAND(ModernBetaTagProviderBiome.HEIGHT_CONFIG_SWAMPLAND.id().toString()),
        HEIGHT_CONFIG_RIVER(ModernBetaTagProviderBiome.HEIGHT_CONFIG_RIVER.id().toString()),
        HEIGHT_CONFIG_MOUNTAINS(ModernBetaTagProviderBiome.HEIGHT_CONFIG_MOUNTAINS.id().toString()),
        HEIGHT_CONFIG_MUSHROOM_ISLAND(ModernBetaTagProviderBiome.HEIGHT_CONFIG_MUSHROOM_ISLAND.id().toString()),
        HEIGHT_CONFIG_MUSHROOM_ISLAND_SHORE(ModernBetaTagProviderBiome.HEIGHT_CONFIG_MUSHROOM_ISLAND_SHORE.id().toString()),
        HEIGHT_CONFIG_BEACH(ModernBetaTagProviderBiome.HEIGHT_CONFIG_BEACH.id().toString()),
        HEIGHT_CONFIG_HILLS(ModernBetaTagProviderBiome.HEIGHT_CONFIG_HILLS.id().toString()),
        HEIGHT_CONFIG_SHORT_HILLS(ModernBetaTagProviderBiome.HEIGHT_CONFIG_SHORT_HILLS.id().toString()),
        HEIGHT_CONFIG_EXTREME_HILLS_EDGE(ModernBetaTagProviderBiome.HEIGHT_CONFIG_EXTREME_HILLS_EDGE.id().toString()),
        HEIGHT_CONFIG_JUNGLE(ModernBetaTagProviderBiome.HEIGHT_CONFIG_JUNGLE.id().toString()),
        HEIGHT_CONFIG_JUNGLE_HILLS(ModernBetaTagProviderBiome.HEIGHT_CONFIG_JUNGLE_HILLS.id().toString()),
        HEIGHT_CONFIG_PLATEAU(ModernBetaTagProviderBiome.HEIGHT_CONFIG_PLATEAU.id().toString()),
        HEIGHT_CONFIG_SWAMPLAND_HILLS(ModernBetaTagProviderBiome.HEIGHT_CONFIG_SWAMPLAND_HILLS.id().toString())
        ;

        public final String id;

        private HeightConfig(String id) {
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
        PE(Chunk.PE.id),
        BETA_1_8_1("beta_1_8_1"),
        RELEASE_1_0_0("release_1_0_0"),
        RELEASE_1_1("release_1_1"),
        RELEASE_1_2_5("release_1_2_5"),
        RELEASE_1_6_4("release_1_6_4")
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
        RELEASE_HYBRID("release_hybrid"),
        ALPHA_WINTER("alpha_winter"),
        INDEV_PARADISE("indev_paradise"),
        INDEV_WOODS("indev_woods"),
        INDEV_HELL("indev_hell"),
        ;
        
        public final String id;
        
        private PresetAlt(String id) {
            this.id = id;
        }
    }
}
