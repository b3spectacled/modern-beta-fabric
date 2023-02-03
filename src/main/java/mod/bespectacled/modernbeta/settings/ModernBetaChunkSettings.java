package mod.bespectacled.modernbeta.settings;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import net.minecraft.nbt.NbtCompound;

public class ModernBetaChunkSettings {
    public final String chunkProvider;
    
    public final boolean useDeepslate;
    public final int deepslateMinY;
    public final int deepslateMaxY;
    public final String deepslateBlock;
    
    public final float coordinateScale;
    public final float heightScale;
    public final float upperLimitScale;
    public final float lowerLimitScale;
    public final float depthNoiseScaleX;
    public final float depthNoiseScaleZ;
    public final float mainNoiseScaleX;
    public final float mainNoiseScaleY;
    public final float mainNoiseScaleZ;
    public final float baseSize;
    public final float stretchY;
    
    public final int topSlideTarget;
    public final int topSlideSize;
    public final int topSlideOffset;
    
    public final int bottomSlideTarget;
    public final int bottomSlideSize;
    public final int bottomSlideOffset;
    
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
    
    public ModernBetaChunkSettings() {
        this(new Builder());
    }
    
    public ModernBetaChunkSettings(ModernBetaChunkSettings.Builder builder) {
        this.chunkProvider = builder.chunkProvider;
        
        this.useDeepslate = builder.useDeepslate;
        this.deepslateMinY = builder.deepslateMinY;
        this.deepslateMaxY = builder.deepslateMaxY;
        this.deepslateBlock = builder.deepslateBlock;
        
        this.coordinateScale = builder.coordinateScale;
        this.heightScale = builder.heightScale;
        this.upperLimitScale = builder.upperLimitScale;
        this.lowerLimitScale = builder.lowerLimitScale;
        this.depthNoiseScaleX = builder.depthNoiseScaleX;
        this.depthNoiseScaleZ = builder.depthNoiseScaleZ;
        this.mainNoiseScaleX = builder.mainNoiseScaleX;
        this.mainNoiseScaleY = builder.mainNoiseScaleY;
        this.mainNoiseScaleZ = builder.mainNoiseScaleZ;
        this.baseSize = builder.baseSize;
        this.stretchY = builder.stretchY;

        this.topSlideTarget = builder.topSlideTarget;
        this.topSlideSize = builder.topSlideSize;
        this.topSlideOffset = builder.topSlideOffset;
        
        this.bottomSlideTarget = builder.bottomSlideTarget;
        this.bottomSlideSize = builder.bottomSlideSize;
        this.bottomSlideOffset = builder.bottomSlideOffset;
        
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
        
        compound.putFloat(NbtTags.COORDINATE_SCALE, this.coordinateScale);
        compound.putFloat(NbtTags.HEIGHT_SCALE, this.heightScale);
        compound.putFloat(NbtTags.UPPER_LIMIT_SCALE, this.upperLimitScale);
        compound.putFloat(NbtTags.LOWER_LIMIT_SCALE, this.lowerLimitScale);
        compound.putFloat(NbtTags.DEPTH_NOISE_SCALE_X, this.depthNoiseScaleX);
        compound.putFloat(NbtTags.DEPTH_NOISE_SCALE_Z, this.depthNoiseScaleZ);
        compound.putFloat(NbtTags.MAIN_NOISE_SCALE_X, this.mainNoiseScaleX);
        compound.putFloat(NbtTags.MAIN_NOISE_SCALE_Y, this.mainNoiseScaleY);
        compound.putFloat(NbtTags.MAIN_NOISE_SCALE_Z, this.mainNoiseScaleZ);
        compound.putFloat(NbtTags.BASE_SIZE, this.baseSize);
        compound.putFloat(NbtTags.STRETCH_Y, this.stretchY);
        
        compound.putInt(NbtTags.TOP_SLIDE_TARGET, this.topSlideTarget);
        compound.putInt(NbtTags.TOP_SLIDE_SIZE, this.topSlideSize);
        compound.putInt(NbtTags.TOP_SLIDE_OFFSET, this.topSlideOffset);
        
        compound.putInt(NbtTags.BOTTOM_SLIDE_TARGET, this.bottomSlideTarget);
        compound.putInt(NbtTags.BOTTOM_SLIDE_SIZE, this.bottomSlideSize);
        compound.putInt(NbtTags.BOTTOM_SLIDE_OFFSET, this.bottomSlideOffset);
        
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
        
        public float coordinateScale;
        public float heightScale;
        public float upperLimitScale;
        public float lowerLimitScale;
        public float depthNoiseScaleX;
        public float depthNoiseScaleZ;
        public float mainNoiseScaleX;
        public float mainNoiseScaleY;
        public float mainNoiseScaleZ;
        public float baseSize;
        public float stretchY;
        
        public int topSlideTarget;
        public int topSlideSize;
        public int topSlideOffset;
        
        public int bottomSlideTarget;
        public int bottomSlideSize;
        public int bottomSlideOffset;

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
            this.chunkProvider = NbtUtil.readString(NbtTags.CHUNK_PROVIDER, compound, ModernBeta.CHUNK_CONFIG.chunkProvider);
            
            this.useDeepslate = NbtUtil.readBoolean(NbtTags.USE_DEEPSLATE, compound, ModernBeta.CHUNK_CONFIG.useDeepslate);
            this.deepslateMinY = NbtUtil.readInt(NbtTags.DEEPSLATE_MIN_Y, compound, ModernBeta.CHUNK_CONFIG.deepslateMinY);
            this.deepslateMaxY = NbtUtil.readInt(NbtTags.DEEPSLATE_MAX_Y, compound, ModernBeta.CHUNK_CONFIG.deepslateMaxY);
            this.deepslateBlock = NbtUtil.readString(NbtTags.DEEPSLATE_BLOCK, compound, ModernBeta.CHUNK_CONFIG.deepslateBlock);
        
            this.coordinateScale = NbtUtil.readFloat(NbtTags.COORDINATE_SCALE, compound, ModernBeta.CHUNK_CONFIG.coordinateScale);
            this.heightScale = NbtUtil.readFloat(NbtTags.HEIGHT_SCALE, compound, ModernBeta.CHUNK_CONFIG.heightScale);
            this.upperLimitScale = NbtUtil.readFloat(NbtTags.UPPER_LIMIT_SCALE, compound, ModernBeta.CHUNK_CONFIG.upperLimitScale);
            this.lowerLimitScale = NbtUtil.readFloat(NbtTags.LOWER_LIMIT_SCALE, compound, ModernBeta.CHUNK_CONFIG.lowerLimitScale);
            this.depthNoiseScaleX = NbtUtil.readFloat(NbtTags.DEPTH_NOISE_SCALE_X, compound, ModernBeta.CHUNK_CONFIG.depthNoiseScaleX);
            this.depthNoiseScaleZ = NbtUtil.readFloat(NbtTags.DEPTH_NOISE_SCALE_Z, compound, ModernBeta.CHUNK_CONFIG.depthNoiseScaleZ);
            this.mainNoiseScaleX = NbtUtil.readFloat(NbtTags.MAIN_NOISE_SCALE_X, compound, ModernBeta.CHUNK_CONFIG.mainNoiseScaleX);
            this.mainNoiseScaleY = NbtUtil.readFloat(NbtTags.MAIN_NOISE_SCALE_Y, compound, ModernBeta.CHUNK_CONFIG.mainNoiseScaleY);
            this.mainNoiseScaleZ = NbtUtil.readFloat(NbtTags.MAIN_NOISE_SCALE_Z, compound, ModernBeta.CHUNK_CONFIG.mainNoiseScaleZ);
            this.baseSize = NbtUtil.readFloat(NbtTags.BASE_SIZE, compound, ModernBeta.CHUNK_CONFIG.baseSize);
            this.stretchY = NbtUtil.readFloat(NbtTags.STRETCH_Y, compound, ModernBeta.CHUNK_CONFIG.stretchY);
            
            this.topSlideTarget = NbtUtil.readInt(NbtTags.TOP_SLIDE_TARGET, compound, ModernBeta.CHUNK_CONFIG.topSlideTarget);
            this.topSlideSize = NbtUtil.readInt(NbtTags.TOP_SLIDE_SIZE, compound, ModernBeta.CHUNK_CONFIG.topSlideSize);
            this.topSlideOffset = NbtUtil.readInt(NbtTags.TOP_SLIDE_OFFSET, compound, ModernBeta.CHUNK_CONFIG.topSlideOffset);
            
            this.bottomSlideTarget = NbtUtil.readInt(NbtTags.BOTTOM_SLIDE_TARGET, compound, ModernBeta.CHUNK_CONFIG.bottomSlideTarget);
            this.bottomSlideSize = NbtUtil.readInt(NbtTags.BOTTOM_SLIDE_TARGET, compound, ModernBeta.CHUNK_CONFIG.bottomSlideSize);
            this.bottomSlideOffset = NbtUtil.readInt(NbtTags.BOTTOM_SLIDE_OFFSET, compound, ModernBeta.CHUNK_CONFIG.bottomSlideOffset);
            
            this.infdevUsePyramid = NbtUtil.readBoolean(NbtTags.INFDEV_USE_PYRAMID, compound, ModernBeta.CHUNK_CONFIG.infdevUsePyramid);
            this.infdevUseWall = NbtUtil.readBoolean(NbtTags.INFDEV_USE_WALL, compound, ModernBeta.CHUNK_CONFIG.infdevUseWall);
            
            this.indevLevelType = NbtUtil.readString(NbtTags.INDEV_LEVEL_TYPE, compound, ModernBeta.CHUNK_CONFIG.indevLevelType);
            this.indevLevelTheme = NbtUtil.readString(NbtTags.INDEV_LEVEL_THEME, compound, ModernBeta.CHUNK_CONFIG.indevLevelTheme);
            this.indevLevelWidth = NbtUtil.readInt(NbtTags.INDEV_LEVEL_WIDTH, compound, ModernBeta.CHUNK_CONFIG.indevLevelWidth);
            this.indevLevelLength = NbtUtil.readInt(NbtTags.INDEV_LEVEL_LENGTH, compound, ModernBeta.CHUNK_CONFIG.indevLevelLength);
            this.indevLevelHeight = NbtUtil.readInt(NbtTags.INDEV_LEVEL_HEIGHT, compound, ModernBeta.CHUNK_CONFIG.indevLevelHeight);
            this.indevCaveRadius = NbtUtil.readFloat(NbtTags.INDEV_CAVE_RADIUS, compound, ModernBeta.CHUNK_CONFIG.indevCaveRadius);

            this.islesUseIslands = NbtUtil.readBoolean(NbtTags.ISLES_USE_ISLANDS, compound, ModernBeta.CHUNK_CONFIG.islesUseIslands);
            this.islesUseOuterIslands = NbtUtil.readBoolean(NbtTags.ISLES_USE_OUTER_ISLANDS, compound, ModernBeta.CHUNK_CONFIG.islesUseOuterIslands);
            this.islesMaxOceanDepth = NbtUtil.readFloat(NbtTags.ISLES_MAX_OCEAN_DEPTH, compound, ModernBeta.CHUNK_CONFIG.islesMaxOceanDepth);
            this.islesCenterIslandFalloff = NbtUtil.readFloat(NbtTags.ISLES_CENTER_ISLAND_FALLOFF, compound, ModernBeta.CHUNK_CONFIG.islesCenterIslandFalloff);
            this.islesCenterIslandRadius = NbtUtil.readInt(NbtTags.ISLES_CENTER_ISLAND_RADIUS, compound, ModernBeta.CHUNK_CONFIG.islesCenterIslandRadius);
            this.islesCenterOceanFalloffDistance = NbtUtil.readInt(NbtTags.ISLES_CENTER_OCEAN_FALLOFF_DIST, compound, ModernBeta.CHUNK_CONFIG.islesCenterOceanFalloffDistance);
            this.islesCenterOceanRadius = NbtUtil.readInt(NbtTags.ISLES_CENTER_OCEAN_RADIUS, compound, ModernBeta.CHUNK_CONFIG.islesCenterOceanRadius);
            this.islesOuterIslandNoiseScale = NbtUtil.readFloat(NbtTags.ISLES_OUTER_ISLAND_NOISE_SCALE, compound, ModernBeta.CHUNK_CONFIG.islesOuterIslandNoiseScale);
            this.islesOuterIslandNoiseOffset = NbtUtil.readFloat(NbtTags.ISLES_OUTER_ISLAND_NOISE_OFFSET, compound, ModernBeta.CHUNK_CONFIG.islesOuterIslandNoiseOffset);
        }
        
        public ModernBetaChunkSettings build() {
            return new ModernBetaChunkSettings(this);
        }
    }
}