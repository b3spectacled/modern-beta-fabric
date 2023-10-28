package mod.bespectacled.modernbeta.world.biome;

public final class ModernBetaBiomeColors {
    private static final boolean USE_OLD_WATER_COLORS = false;
    public static final boolean USE_DEBUG_OCEAN_COLOR = false;
    
    public static final int OLD_GRASS_COLOR = 11272039;
    public static final int OLD_FOLIAGE_COLOR = 5242667;
    public static final int OLD_WATER_COLOR = 4610815;
    public static final int OLD_WATER_FOG_COLOR = 329011; // Vanilla color is apparently the same as original water fog color
    
    public static final int PE_GRASS_COLOR = 3709744;
    public static final int PE_FOLIAGE_COLOR = 3207444;

    public static final int VANILLA_WATER_COLOR = USE_OLD_WATER_COLORS ? OLD_WATER_COLOR : 4159204;
    public static final int VANILLA_COLD_WATER_COLOR = USE_OLD_WATER_COLORS ? OLD_WATER_COLOR : 4159204;
    public static final int VANILLA_FROZEN_WATER_COLOR = USE_OLD_WATER_COLORS ? OLD_WATER_COLOR : 4159204;
    public static final int VANILLA_LUKEWARM_WATER_COLOR = USE_OLD_WATER_COLORS ? OLD_WATER_COLOR : 4159204;
    public static final int VANILLA_WARM_WATER_COLOR = USE_OLD_WATER_COLORS ? OLD_WATER_COLOR : 4159204;
    public static final int VANILLA_SWAMP_WATER_COLOR = USE_OLD_WATER_COLORS ? 4020848 : 3634788;

    public static final int VANILLA_WATER_FOG_COLOR = USE_OLD_WATER_COLORS ? OLD_WATER_FOG_COLOR : 329011;
    public static final int VANILLA_COLD_WATER_FOG_COLOR = USE_OLD_WATER_COLORS ? OLD_WATER_FOG_COLOR : 329011;
    public static final int VANILLA_FROZEN_WATER_FOG_COLOR = USE_OLD_WATER_COLORS ? OLD_WATER_FOG_COLOR : 329011;
    public static final int VANILLA_LUKEWARM_WATER_FOG_COLOR = USE_OLD_WATER_COLORS ? OLD_WATER_FOG_COLOR : 329011;
    public static final int VANILLA_WARM_WATER_FOG_COLOR = USE_OLD_WATER_COLORS ? OLD_WATER_FOG_COLOR : 329011;
    public static final int VANILLA_SWAMP_WATER_FOG_COLOR = USE_OLD_WATER_COLORS ? OLD_WATER_FOG_COLOR : 329011;

    // Made-up colors based on biome temperature and own custom sky colormap.
    public static final int BETA_WARM_SKY_COLOR = 6733055;
    public static final int BETA_TEMP_SKY_COLOR = 7777023;
    public static final int BETA_COOL_SKY_COLOR = 8430079;
    public static final int BETA_COLD_SKY_COLOR = 10263039;
    
    // All colors below extracted from respective original versions, so they should be accurate.
    public static final int BETA_FOG_COLOR = 12638463;
    
    public static final int PE_SKY_COLOR = 2380991;
    public static final int PE_FOG_COLOR = 8444671;
    
    public static final int SKYLANDS_SKY_COLOR = 12632319;
    public static final int SKYLANDS_FOG_COLOR = 8421536;
    
    public static final int ALPHA_SKY_COLOR = 8961023;
    public static final int ALPHA_FOG_COLOR = 12638463;
    
    public static final int INFDEV_611_SKY_COLOR = 10079487;
    public static final int INFDEV_611_FOG_COLOR = 11587839;
    
    public static final int INFDEV_420_SKY_COLOR = 10079487;
    public static final int INFDEV_420_FOG_COLOR = 11587839;
    
    public static final int INFDEV_415_SKY_COLOR = 10079487;
    public static final int INFDEV_415_FOG_COLOR = 11587839;
    
    public static final int INFDEV_227_SKY_COLOR = 255;
    public static final int INFDEV_227_FOG_COLOR = 11908351;
    
    public static final int INDEV_NORMAL_SKY_COLOR = 10079487;
    public static final int INDEV_NORMAL_FOG_COLOR = 16777215;
    
    public static final int INDEV_HELL_SKY_COLOR = 1049600;
    public static final int INDEV_HELL_FOG_COLOR = 1049600;
    
    public static final int INDEV_PARADISE_SKY_COLOR = 13033215;
    public static final int INDEV_PARADISE_FOG_COLOR = 13033215;
    
    public static final int INDEV_WOODS_SKY_COLOR = 7699847;
    public static final int INDEV_WOODS_FOG_COLOR = 5069403;
    
    // Not an original biome, use Indev Normal colors
    public static final int INDEV_SNOWY_SKY_COLOR = INDEV_NORMAL_SKY_COLOR;
    public static final int INDEV_SNOWY_FOG_COLOR = INDEV_NORMAL_FOG_COLOR;
}
