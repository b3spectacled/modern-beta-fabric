package mod.bespectacled.modernbeta.util;

public class NbtTags {
    /* Chunk Settings */
    
    public static final String CHUNK_PROVIDER = "chunkProvider";
    
    public static final String USE_DEEPSLATE = "useDeepslate";
    public static final String DEEPSLATE_MIN_Y = "deepslateMinY";
    public static final String DEEPSLATE_MAX_Y = "deepslateMaxY";
    public static final String DEEPSLATE_BLOCK = "deepslateBlock";
    
    // Noise
    public static final String NOISE_POST_PROCESSOR = "noisePostProcessor";
    public static final String NOISE_COORDINATE_SCALE = "noiseCoordinateScale";
    public static final String NOISE_HEIGHT_SCALE = "noiseHeightScale";
    public static final String NOISE_UPPER_LIMIT_SCALE = "noiseUpperLimitScale";
    public static final String NOISE_LOWER_LIMIT_SCALE = "noiseLowerLimitScale";
    public static final String NOISE_DEPTH_NOISE_SCALE_X = "noiseDepthNoiseScaleX";
    public static final String NOISE_DEPTH_NOISE_SCALE_Z = "noiseDepthNoiseScaleZ";
    public static final String NOISE_MAIN_NOISE_SCALE_X = "noiseMainNoiseScaleX";
    public static final String NOISE_MAIN_NOISE_SCALE_Y = "noiseMainNoiseScaleY";
    public static final String NOISE_MAIN_NOISE_SCALE_Z = "noiseMainNoiseScaleZ";
    public static final String NOISE_BASE_SIZE = "noiseBaseSize";
    public static final String NOISE_STRETCH_Y = "noiseStretchY";
    
    public static final String NOISE_TOP_SLIDE_TARGET = "noiseTopSlideTarget";
    public static final String NOISE_TOP_SLIDE_SIZE = "noiseTopSlideSize";
    public static final String NOISE_TOP_SLIDE_OFFSET = "noiseTopSlideOffset";
    
    public static final String NOISE_BOTTOM_SLIDE_TARGET = "noiseBottomSlideTarget";
    public static final String NOISE_BOTTOM_SLIDE_SIZE = "noiseBottomSlideSize";
    public static final String NOISE_BOTTOM_SLIDE_OFFSET = "noiseBottomSlideOffset";
    
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
    public static final String BETA_TEMP_NOISE_SCALE = "betaTempNoiseScale";
    public static final String BETA_RAIN_NOISE_SCALE = "betaRainNoiseScale";
    public static final String BETA_DETAIL_NOISE_SCALE = "betaDetailNoiseScale";
    
    /* Cave Biome Settings */
    
    public static final String CAVE_BIOME_PROVIDER = "biomeProvider";
    
    // Cave Noise
    public static final String VORONOI_HORIZONTAL_NOISE_SCALE = "voronoiHorizontalNoiseScale";
    public static final String VORONOI_VERTICAL_NOISE_SCALE = "voronoiVerticalNoiseScale";
    public static final String VORONOI_DEPTH_MIN_Y = "voronoiDepthMinY";
    public static final String VORONOI_DEPTH_MAX_Y = "voronoiDepthMaxY";
}
