package mod.bespectacled.modernbeta.settings;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.config.ModernBetaConfigChunk;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import net.minecraft.nbt.NbtCompound;

public class ModernBetaSettingsChunk {
    private static final ModernBetaConfigChunk CONFIG = ModernBeta.CHUNK_CONFIG;
    
    public final String chunkProvider;
    
    public final boolean useDeepslate;
    public final int deepslateMinY;
    public final int deepslateMaxY;
    public final String deepslateBlock;
    
    public final String noisePostProcessor;
    public final float noiseCoordinateScale;
    public final float noiseHeightScale;
    public final float noiseUpperLimitScale;
    public final float noiseLowerLimitScale;
    public final float noiseDepthNoiseScaleX;
    public final float noiseDepthNoiseScaleZ;
    public final float noiseMainNoiseScaleX;
    public final float noiseMainNoiseScaleY;
    public final float noiseMainNoiseScaleZ;
    public final float noiseBaseSize;
    public final float noiseStretchY;
    
    public final int noiseTopSlideTarget;
    public final int noiseTopSlideSize;
    public final int noiseTopSlideOffset;
    
    public final int noiseBottomSlideTarget;
    public final int noiseBottomSlideSize;
    public final int noiseBottomSlideOffset;
    
    public final boolean infdevUsePyramid;
    public final boolean infdevUseWall;
    
    public final String indevLevelType;
    public final String indevLevelTheme;
    public final int indevLevelWidth;
    public final int indevLevelLength;
    public final int indevLevelHeight;
    public final float indevCaveRadius;

    public final boolean islesUseIslands;
    public final boolean islesUseOuterIslands;
    public final float islesMaxOceanDepth;
    public final float islesCenterIslandFalloff;
    public final int islesCenterIslandRadius;
    public final int islesCenterOceanFalloffDistance;
    public final int islesCenterOceanRadius;
    public final float islesOuterIslandNoiseScale;
    public final float islesOuterIslandNoiseOffset;
    
    public ModernBetaSettingsChunk() {
        this(new Builder());
    }
    
    public ModernBetaSettingsChunk(ModernBetaSettingsChunk.Builder builder) {
        this.chunkProvider = builder.chunkProvider;
        
        this.useDeepslate = builder.useDeepslate;
        this.deepslateMinY = builder.deepslateMinY;
        this.deepslateMaxY = builder.deepslateMaxY;
        this.deepslateBlock = builder.deepslateBlock;
        
        this.noisePostProcessor = builder.noisePostProcessor;
        this.noiseCoordinateScale = builder.noiseCoordinateScale;
        this.noiseHeightScale = builder.noiseHeightScale;
        this.noiseUpperLimitScale = builder.noiseUpperLimitScale;
        this.noiseLowerLimitScale = builder.noiseLowerLimitScale;
        this.noiseDepthNoiseScaleX = builder.noiseDepthNoiseScaleX;
        this.noiseDepthNoiseScaleZ = builder.noiseDepthNoiseScaleZ;
        this.noiseMainNoiseScaleX = builder.noiseMainNoiseScaleX;
        this.noiseMainNoiseScaleY = builder.noiseMainNoiseScaleY;
        this.noiseMainNoiseScaleZ = builder.noiseMainNoiseScaleZ;
        this.noiseBaseSize = builder.noiseBaseSize;
        this.noiseStretchY = builder.noiseStretchY;

        this.noiseTopSlideTarget = builder.noiseTopSlideTarget;
        this.noiseTopSlideSize = builder.noiseTopSlideSize;
        this.noiseTopSlideOffset = builder.noiseTopSlideOffset;
        
        this.noiseBottomSlideTarget = builder.noiseBottomSlideTarget;
        this.noiseBottomSlideSize = builder.noiseBottomSlideSize;
        this.noiseBottomSlideOffset = builder.noiseBottomSlideOffset;
        
        this.infdevUsePyramid = builder.infdevUsePyramid;
        this.infdevUseWall = builder.infdevUseWall;
        
        this.indevLevelType = builder.indevLevelType;
        this.indevLevelTheme = builder.indevLevelTheme;
        this.indevLevelWidth = builder.indevLevelWidth;
        this.indevLevelLength = builder.indevLevelLength;
        this.indevLevelHeight = builder.indevLevelHeight;
        this.indevCaveRadius = builder.indevCaveRadius;

        this.islesUseIslands = builder.islesUseIslands;
        this.islesUseOuterIslands = builder.islesUseOuterIslands;
        this.islesMaxOceanDepth = builder.islesMaxOceanDepth;
        this.islesCenterIslandFalloff = builder.islesCenterIslandFalloff;
        this.islesCenterIslandRadius = builder.islesCenterIslandRadius;
        this.islesCenterOceanFalloffDistance = builder.islesCenterOceanFalloffDistance;
        this.islesCenterOceanRadius = builder.islesCenterOceanRadius;
        this.islesOuterIslandNoiseScale = builder.islesOuterIslandNoiseScale;
        this.islesOuterIslandNoiseOffset = builder.islesOuterIslandNoiseOffset;
    }
    
    public NbtCompound toCompound() {
        NbtCompound compound = new NbtCompound();
        
        compound.putString(NbtTags.CHUNK_PROVIDER, this.chunkProvider);
        
        compound.putBoolean(NbtTags.USE_DEEPSLATE, this.useDeepslate);
        compound.putInt(NbtTags.DEEPSLATE_MIN_Y, this.deepslateMinY);
        compound.putInt(NbtTags.DEEPSLATE_MAX_Y, this.deepslateMaxY);
        compound.putString(NbtTags.DEEPSLATE_BLOCK, this.deepslateBlock);
        
        //compound.putString(NbtTags.NOISE_POST_PROCESSOR, this.noisePostProcessor);
        compound.putFloat(NbtTags.NOISE_COORDINATE_SCALE, this.noiseCoordinateScale);
        compound.putFloat(NbtTags.NOISE_HEIGHT_SCALE, this.noiseHeightScale);
        compound.putFloat(NbtTags.NOISE_UPPER_LIMIT_SCALE, this.noiseUpperLimitScale);
        compound.putFloat(NbtTags.NOISE_LOWER_LIMIT_SCALE, this.noiseLowerLimitScale);
        compound.putFloat(NbtTags.NOISE_DEPTH_NOISE_SCALE_X, this.noiseDepthNoiseScaleX);
        compound.putFloat(NbtTags.NOISE_DEPTH_NOISE_SCALE_Z, this.noiseDepthNoiseScaleZ);
        compound.putFloat(NbtTags.NOISE_MAIN_NOISE_SCALE_X, this.noiseMainNoiseScaleX);
        compound.putFloat(NbtTags.NOISE_MAIN_NOISE_SCALE_Y, this.noiseMainNoiseScaleY);
        compound.putFloat(NbtTags.NOISE_MAIN_NOISE_SCALE_Z, this.noiseMainNoiseScaleZ);
        compound.putFloat(NbtTags.NOISE_BASE_SIZE, this.noiseBaseSize);
        compound.putFloat(NbtTags.NOISE_STRETCH_Y, this.noiseStretchY);
        
        compound.putInt(NbtTags.NOISE_TOP_SLIDE_TARGET, this.noiseTopSlideTarget);
        compound.putInt(NbtTags.NOISE_TOP_SLIDE_SIZE, this.noiseTopSlideSize);
        compound.putInt(NbtTags.NOISE_TOP_SLIDE_OFFSET, this.noiseTopSlideOffset);
        
        compound.putInt(NbtTags.NOISE_BOTTOM_SLIDE_TARGET, this.noiseBottomSlideTarget);
        compound.putInt(NbtTags.NOISE_BOTTOM_SLIDE_SIZE, this.noiseBottomSlideSize);
        compound.putInt(NbtTags.NOISE_BOTTOM_SLIDE_OFFSET, this.noiseBottomSlideOffset);
        
        compound.putBoolean(NbtTags.INFDEV_USE_PYRAMID, this.infdevUsePyramid);
        compound.putBoolean(NbtTags.INFDEV_USE_WALL, this.infdevUseWall);
        
        compound.putString(NbtTags.INDEV_LEVEL_THEME, this.indevLevelTheme);
        compound.putString(NbtTags.INDEV_LEVEL_TYPE, this.indevLevelType);
        compound.putInt(NbtTags.INDEV_LEVEL_WIDTH, this.indevLevelWidth);
        compound.putInt(NbtTags.INDEV_LEVEL_LENGTH, this.indevLevelLength);
        compound.putInt(NbtTags.INDEV_LEVEL_HEIGHT, this.indevLevelHeight);
        compound.putFloat(NbtTags.INDEV_CAVE_RADIUS, this.indevCaveRadius);

        compound.putBoolean(NbtTags.ISLES_USE_ISLANDS, this.islesUseIslands);
        compound.putBoolean(NbtTags.ISLES_USE_OUTER_ISLANDS, this.islesUseOuterIslands);
        compound.putFloat(NbtTags.ISLES_MAX_OCEAN_DEPTH, this.islesMaxOceanDepth);
        compound.putFloat(NbtTags.ISLES_CENTER_ISLAND_FALLOFF, this.islesCenterIslandFalloff);
        compound.putInt(NbtTags.ISLES_CENTER_ISLAND_RADIUS, this.islesCenterIslandRadius);
        compound.putInt(NbtTags.ISLES_CENTER_OCEAN_FALLOFF_DIST, this.islesCenterOceanFalloffDistance);
        compound.putInt(NbtTags.ISLES_CENTER_OCEAN_RADIUS, this.islesCenterOceanRadius);
        compound.putFloat(NbtTags.ISLES_OUTER_ISLAND_NOISE_SCALE, this.islesOuterIslandNoiseScale);
        compound.putFloat(NbtTags.ISLES_OUTER_ISLAND_NOISE_OFFSET, this.islesOuterIslandNoiseOffset);
        
        return compound;
    }

    public static class Builder {
        public String chunkProvider;
        
        public boolean useDeepslate;
        public int deepslateMinY;
        public int deepslateMaxY;
        public String deepslateBlock;
        
        public String noisePostProcessor;
        public float noiseCoordinateScale;
        public float noiseHeightScale;
        public float noiseUpperLimitScale;
        public float noiseLowerLimitScale;
        public float noiseDepthNoiseScaleX;
        public float noiseDepthNoiseScaleZ;
        public float noiseMainNoiseScaleX;
        public float noiseMainNoiseScaleY;
        public float noiseMainNoiseScaleZ;
        public float noiseBaseSize;
        public float noiseStretchY;
        
        public int noiseTopSlideTarget;
        public int noiseTopSlideSize;
        public int noiseTopSlideOffset;
        
        public int noiseBottomSlideTarget;
        public int noiseBottomSlideSize;
        public int noiseBottomSlideOffset;

        public boolean infdevUsePyramid;
        public boolean infdevUseWall;
        
        public String indevLevelType;
        public String indevLevelTheme;
        public int indevLevelWidth;
        public int indevLevelLength;
        public int indevLevelHeight;
        public float indevCaveRadius;

        public boolean islesUseIslands;
        public boolean islesUseOuterIslands;
        public float islesMaxOceanDepth;
        public float islesCenterIslandFalloff;
        public int islesCenterIslandRadius;
        public int islesCenterOceanFalloffDistance;
        public int islesCenterOceanRadius;
        public float islesOuterIslandNoiseScale;
        public float islesOuterIslandNoiseOffset;
        
        public Builder() {
            this(new NbtCompound());
        }
        
        public Builder(NbtCompound compound) {
            this.chunkProvider = NbtUtil.readString(NbtTags.CHUNK_PROVIDER, compound, CONFIG.chunkProvider);
            
            this.useDeepslate = NbtUtil.readBoolean(NbtTags.USE_DEEPSLATE, compound, CONFIG.useDeepslate);
            this.deepslateMinY = NbtUtil.readInt(NbtTags.DEEPSLATE_MIN_Y, compound, CONFIG.deepslateMinY);
            this.deepslateMaxY = NbtUtil.readInt(NbtTags.DEEPSLATE_MAX_Y, compound, CONFIG.deepslateMaxY);
            this.deepslateBlock = NbtUtil.readString(NbtTags.DEEPSLATE_BLOCK, compound, CONFIG.deepslateBlock);
        
            //this.noisePostProcessor = NbtUtil.readString(NbtTags.NOISE_POST_PROCESSOR, compound, CONFIG.noisePostProcessor);
            this.noiseCoordinateScale = NbtUtil.readFloat(NbtTags.NOISE_COORDINATE_SCALE, compound, CONFIG.noiseCoordinateScale);
            this.noiseHeightScale = NbtUtil.readFloat(NbtTags.NOISE_HEIGHT_SCALE, compound, CONFIG.noiseHeightScale);
            this.noiseUpperLimitScale = NbtUtil.readFloat(NbtTags.NOISE_UPPER_LIMIT_SCALE, compound, CONFIG.noiseUpperLimitScale);
            this.noiseLowerLimitScale = NbtUtil.readFloat(NbtTags.NOISE_LOWER_LIMIT_SCALE, compound, CONFIG.noiseLowerLimitScale);
            this.noiseDepthNoiseScaleX = NbtUtil.readFloat(NbtTags.NOISE_DEPTH_NOISE_SCALE_X, compound, CONFIG.noiseDepthNoiseScaleX);
            this.noiseDepthNoiseScaleZ = NbtUtil.readFloat(NbtTags.NOISE_DEPTH_NOISE_SCALE_Z, compound, CONFIG.noiseDepthNoiseScaleZ);
            this.noiseMainNoiseScaleX = NbtUtil.readFloat(NbtTags.NOISE_MAIN_NOISE_SCALE_X, compound, CONFIG.noiseMainNoiseScaleX);
            this.noiseMainNoiseScaleY = NbtUtil.readFloat(NbtTags.NOISE_MAIN_NOISE_SCALE_Y, compound, CONFIG.noiseMainNoiseScaleY);
            this.noiseMainNoiseScaleZ = NbtUtil.readFloat(NbtTags.NOISE_MAIN_NOISE_SCALE_Z, compound, CONFIG.noiseMainNoiseScaleZ);
            this.noiseBaseSize = NbtUtil.readFloat(NbtTags.NOISE_BASE_SIZE, compound, CONFIG.noiseBaseSize);
            this.noiseStretchY = NbtUtil.readFloat(NbtTags.NOISE_STRETCH_Y, compound, CONFIG.noiseStretchY);
            
            this.noiseTopSlideTarget = NbtUtil.readInt(NbtTags.NOISE_TOP_SLIDE_TARGET, compound, CONFIG.noiseTopSlideTarget);
            this.noiseTopSlideSize = NbtUtil.readInt(NbtTags.NOISE_TOP_SLIDE_SIZE, compound, CONFIG.noiseTopSlideSize);
            this.noiseTopSlideOffset = NbtUtil.readInt(NbtTags.NOISE_TOP_SLIDE_OFFSET, compound, CONFIG.noiseTopSlideOffset);
            
            this.noiseBottomSlideTarget = NbtUtil.readInt(NbtTags.NOISE_BOTTOM_SLIDE_TARGET, compound, CONFIG.noiseBottomSlideTarget);
            this.noiseBottomSlideSize = NbtUtil.readInt(NbtTags.NOISE_BOTTOM_SLIDE_SIZE, compound, CONFIG.noiseBottomSlideSize);
            this.noiseBottomSlideOffset = NbtUtil.readInt(NbtTags.NOISE_BOTTOM_SLIDE_OFFSET, compound, CONFIG.noiseBottomSlideOffset);
            
            this.infdevUsePyramid = NbtUtil.readBoolean(NbtTags.INFDEV_USE_PYRAMID, compound, CONFIG.infdevUsePyramid);
            this.infdevUseWall = NbtUtil.readBoolean(NbtTags.INFDEV_USE_WALL, compound, CONFIG.infdevUseWall);
            
            this.indevLevelType = NbtUtil.readString(NbtTags.INDEV_LEVEL_TYPE, compound, CONFIG.indevLevelType);
            this.indevLevelTheme = NbtUtil.readString(NbtTags.INDEV_LEVEL_THEME, compound, CONFIG.indevLevelTheme);
            this.indevLevelWidth = NbtUtil.readInt(NbtTags.INDEV_LEVEL_WIDTH, compound, CONFIG.indevLevelWidth);
            this.indevLevelLength = NbtUtil.readInt(NbtTags.INDEV_LEVEL_LENGTH, compound, CONFIG.indevLevelLength);
            this.indevLevelHeight = NbtUtil.readInt(NbtTags.INDEV_LEVEL_HEIGHT, compound, CONFIG.indevLevelHeight);
            this.indevCaveRadius = NbtUtil.readFloat(NbtTags.INDEV_CAVE_RADIUS, compound, CONFIG.indevCaveRadius);

            this.islesUseIslands = NbtUtil.readBoolean(NbtTags.ISLES_USE_ISLANDS, compound, CONFIG.islesUseIslands);
            this.islesUseOuterIslands = NbtUtil.readBoolean(NbtTags.ISLES_USE_OUTER_ISLANDS, compound, CONFIG.islesUseOuterIslands);
            this.islesMaxOceanDepth = NbtUtil.readFloat(NbtTags.ISLES_MAX_OCEAN_DEPTH, compound, CONFIG.islesMaxOceanDepth);
            this.islesCenterIslandFalloff = NbtUtil.readFloat(NbtTags.ISLES_CENTER_ISLAND_FALLOFF, compound, CONFIG.islesCenterIslandFalloff);
            this.islesCenterIslandRadius = NbtUtil.readInt(NbtTags.ISLES_CENTER_ISLAND_RADIUS, compound, CONFIG.islesCenterIslandRadius);
            this.islesCenterOceanFalloffDistance = NbtUtil.readInt(NbtTags.ISLES_CENTER_OCEAN_FALLOFF_DIST, compound, CONFIG.islesCenterOceanFalloffDistance);
            this.islesCenterOceanRadius = NbtUtil.readInt(NbtTags.ISLES_CENTER_OCEAN_RADIUS, compound, CONFIG.islesCenterOceanRadius);
            this.islesOuterIslandNoiseScale = NbtUtil.readFloat(NbtTags.ISLES_OUTER_ISLAND_NOISE_SCALE, compound, CONFIG.islesOuterIslandNoiseScale);
            this.islesOuterIslandNoiseOffset = NbtUtil.readFloat(NbtTags.ISLES_OUTER_ISLAND_NOISE_OFFSET, compound, CONFIG.islesOuterIslandNoiseOffset);
            
            this.loadDeprecated(compound);
        }
        
        public ModernBetaSettingsChunk build() {
            return new ModernBetaSettingsChunk(this);
        }
        
        private void loadDeprecated(NbtCompound compound) {}
    }
}