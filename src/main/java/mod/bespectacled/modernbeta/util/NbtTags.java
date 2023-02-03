package mod.bespectacled.modernbeta.util;

public class NbtTags {
    /* Chunk Settings */
    
    public static final String CHUNK_PROVIDER = "chunkProvider";
    public static final String NOISE_POST_PROCESSOR = "noisePostProcessor";
    
    public static final String USE_DEEPSLATE = "useDeepslate";
    public static final String DEEPSLATE_MIN_Y = "deepslateMinY";
    public static final String DEEPSLATE_MAX_Y = "deepslateMaxY";
    public static final String DEEPSLATE_BLOCK = "deepslateBlock";
    
    // Noise
    public static final String COORDINATE_SCALE = "coordinateScale";
    public static final String HEIGHT_SCALE = "heightScale";
    public static final String UPPER_LIMIT_SCALE = "upperLimitScale";
    public static final String LOWER_LIMIT_SCALE = "lowerLimitScale";
    public static final String DEPTH_NOISE_SCALE_X = "depthNoiseScaleX";
    public static final String DEPTH_NOISE_SCALE_Z = "depthNoiseScaleZ";
    public static final String MAIN_NOISE_SCALE_X = "mainNoiseScaleX";
    public static final String MAIN_NOISE_SCALE_Y = "mainNoiseScaleY";
    public static final String MAIN_NOISE_SCALE_Z = "mainNoiseScaleZ";
    public static final String BASE_SIZE = "baseSize";
    public static final String STRETCH_Y = "stretchY";
    
    public static final String TOP_SLIDE_TARGET = "topSlideTarget";
    public static final String TOP_SLIDE_SIZE = "topSlideSize";
    public static final String TOP_SLIDE_OFFSET = "topSlideOffset";
    
    public static final String BOTTOM_SLIDE_TARGET = "bottomSlideTarget";
    public static final String BOTTOM_SLIDE_SIZE = "bottomSlideSize";
    public static final String BOTTOM_SLIDE_OFFSET = "bottomSlideOffset";
    
    // Infdev 227
    public static final String INFDEV_USE_PYRAMID = "infdevUsePyramid";
    public static final String INFDEV_USE_WALL = "infdevUseWall";
    
    // Indev
    public static final String INDEV_LEVEL_THEME = "indevLevelTheme";
    public static final String INDEV_LEVEL_TYPE = "indevLevelType";
    public static final String INDEV_LEVEL_WIDTH = "indevLevelWidth";
    public static final String INDEV_LEVEL_LENGTH = "indevLevelLength";
    public static final String INDEV_LEVEL_HEIGHT = "indevLevelHeight";
    public static final String INDEV_CAVE_RADIUS = "indevCaveRadius";
    
    // Islands
    public static final String ISLES_USE_ISLANDS = "islesUseIslands";
    public static final String ISLES_USE_OUTER_ISLANDS = "islesUseOuterIslands";
    public static final String ISLES_MAX_OCEAN_DEPTH = "islesMaxOceanDepth";
    public static final String ISLES_CENTER_ISLAND_FALLOFF = "islesCenterIslandFalloff";
    public static final String ISLES_CENTER_ISLAND_RADIUS = "islesCenterIslandRadius";
    public static final String ISLES_CENTER_OCEAN_FALLOFF_DIST = "islesCenterOceanFalloffDistance";
    public static final String ISLES_CENTER_OCEAN_RADIUS = "islesCenterOceanRadius";
    public static final String ISLES_OUTER_ISLAND_NOISE_SCALE = "islesOuterIslandNoiseScale";
    public static final String ISLES_OUTER_ISLAND_NOISE_OFFSET = "islesOuterIslandNoiseOffset";

    /* Climate/Voronoi Settings */
    
    public static final String BIOMES = "biomes";
    public static final String BIOME = "biome";
    public static final String OCEAN_BIOME = "oceanBiome";
    public static final String DEEP_OCEAN_BIOME = "deepOceanBiome";
    
    public static final String TEMP = "temp";
    public static final String RAIN = "rain";
    public static final String DEPTH = "depth";
    
    /* Biome Settings */
    
    public static final String BIOME_PROVIDER = "biomeProvider";
    public static final String SINGLE_BIOME = "singleBiome";
    public static final String REPLACE_OCEAN_BIOMES = "replaceOceanBiomes";
    
    // Beta Noise
    public static final String TEMP_NOISE_SCALE = "tempNoiseScale";
    public static final String RAIN_NOISE_SCALE = "rainNoiseScale";
    public static final String DETAIL_NOISE_SCALE = "detailNoiseScale";
    
    /* Cave Biome Settings */
    
    public static final String CAVE_BIOME_PROVIDER = "biomeProvider";
    
    // Cave Noise
    public static final String VORONOI_HORIZONTAL_NOISE_SCALE = "voronoiHorizontalNoiseScale";
    public static final String VORONOI_VERTICAL_NOISE_SCALE = "voronoiVerticalNoiseScale";
}
