package mod.bespectacled.modernbeta.settings;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import net.minecraft.nbt.NbtCompound;

public class ModernBetaChunkSettings {
    public final String chunkProvider;
    public final boolean useDeepslate;
    
    // TODO: Think about putting main noise scale here
    
    public final float upperLimitScale;
    public final float lowerLimitScale;
    public final float depthNoiseScaleX;
    public final float depthNoiseScaleZ;
    public final float baseSize;
    public final float stretchY;
    
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
        
        this.upperLimitScale = builder.upperLimitScale;
        this.lowerLimitScale = builder.lowerLimitScale;
        this.depthNoiseScaleX = builder.depthNoiseScaleX;
        this.depthNoiseScaleZ = builder.depthNoiseScaleZ;
        this.baseSize = builder.baseSize;
        this.stretchY = builder.stretchY;
        
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
        
        compound.putFloat(NbtTags.UPPER_LIMIT_SCALE, this.upperLimitScale);
        compound.putFloat(NbtTags.LOWER_LIMIT_SCALE, this.lowerLimitScale);
        compound.putFloat(NbtTags.DEPTH_NOISE_SCALE_X, this.depthNoiseScaleX);
        compound.putFloat(NbtTags.DEPTH_NOISE_SCALE_Z, this.depthNoiseScaleZ);
        compound.putFloat(NbtTags.BASE_SIZE, this.baseSize);
        compound.putFloat(NbtTags.STRETCH_Y, this.stretchY);
        
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
        
        public float upperLimitScale;
        public float lowerLimitScale;
        public float depthNoiseScaleX;
        public float depthNoiseScaleZ;
        public float baseSize;
        public float stretchY;

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
        
            this.upperLimitScale = NbtUtil.readFloat(NbtTags.UPPER_LIMIT_SCALE, compound, ModernBeta.CHUNK_CONFIG.upperLimitScale);
            this.lowerLimitScale = NbtUtil.readFloat(NbtTags.LOWER_LIMIT_SCALE, compound, ModernBeta.CHUNK_CONFIG.lowerLimitScale);
            this.depthNoiseScaleX = NbtUtil.readFloat(NbtTags.DEPTH_NOISE_SCALE_X, compound, ModernBeta.CHUNK_CONFIG.depthNoiseScaleX);
            this.depthNoiseScaleZ = NbtUtil.readFloat(NbtTags.DEPTH_NOISE_SCALE_Z, compound, ModernBeta.CHUNK_CONFIG.depthNoiseScaleZ);
            this.baseSize = NbtUtil.readFloat(NbtTags.BASE_SIZE, compound, ModernBeta.CHUNK_CONFIG.baseSize);
            this.stretchY = NbtUtil.readFloat(NbtTags.STRETCH_Y, compound, ModernBeta.CHUNK_CONFIG.stretchY);
            
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