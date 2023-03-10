package mod.bespectacled.modernbeta.settings;

import com.google.gson.Gson;

import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import mod.bespectacled.modernbeta.world.chunk.provider.indev.IndevTheme;
import mod.bespectacled.modernbeta.world.chunk.provider.indev.IndevType;
import mod.bespectacled.modernbeta.world.chunk.provider.island.IslandShape;
import net.minecraft.nbt.NbtCompound;

public class ModernBetaSettingsChunk implements ModernBetaSettings {
    public final String chunkProvider;
    
    public final boolean useDeepslate;
    public final int deepslateMinY;
    public final int deepslateMaxY;
    public final String deepslateBlock;
    
    public final boolean useCaves;
    
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
    public final boolean indevUseCaves;

    public final boolean islesUseIslands;
    public final boolean islesUseOuterIslands;
    public final float islesOceanSlideTarget;
    public final String islesCenterIslandShape;
    public final int islesCenterIslandRadius;
    public final int islesCenterIslandFalloffDistance;
    public final int islesCenterOceanRadius;
    public final int islesCenterOceanFalloffDistance;
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
        
        this.useCaves = builder.useCaves;
        
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
        this.indevUseCaves = builder.indevUseCaves;

        this.islesUseIslands = builder.islesUseIslands;
        this.islesUseOuterIslands = builder.islesUseOuterIslands;
        this.islesOceanSlideTarget = builder.islesOceanSlideTarget;
        this.islesCenterIslandShape = builder.islesCenterIslandShape;
        this.islesCenterIslandRadius = builder.islesCenterIslandRadius;
        this.islesCenterIslandFalloffDistance = builder.islesCenterIslandFalloffDistance;
        this.islesCenterOceanRadius = builder.islesCenterOceanRadius;
        this.islesCenterOceanFalloffDistance = builder.islesCenterOceanFalloffDistance;
        this.islesOuterIslandNoiseScale = builder.islesOuterIslandNoiseScale;
        this.islesOuterIslandNoiseOffset = builder.islesOuterIslandNoiseOffset;
    }
    
    public static ModernBetaSettingsChunk fromString(String string) {
        Gson gson = new Gson();
        
        return gson.fromJson(string, ModernBetaSettingsChunk.class);
    }
    
    public static ModernBetaSettingsChunk fromCompound(NbtCompound compound) {
        return new Builder().fromCompound(compound).build();
    }
    
    public NbtCompound toCompound() {
        NbtCompound compound = new NbtCompound();
        
        compound.putString(NbtTags.CHUNK_PROVIDER, this.chunkProvider);
        
        compound.putBoolean(NbtTags.USE_DEEPSLATE, this.useDeepslate);
        compound.putInt(NbtTags.DEEPSLATE_MIN_Y, this.deepslateMinY);
        compound.putInt(NbtTags.DEEPSLATE_MAX_Y, this.deepslateMaxY);
        compound.putString(NbtTags.DEEPSLATE_BLOCK, this.deepslateBlock);
        
        compound.putBoolean(NbtTags.USE_CAVES, this.useCaves);
        
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
        compound.putBoolean(NbtTags.INDEV_USE_CAVES, this.indevUseCaves);

        compound.putBoolean(NbtTags.ISLES_USE_ISLANDS, this.islesUseIslands);
        compound.putBoolean(NbtTags.ISLES_USE_OUTER_ISLANDS, this.islesUseOuterIslands);
        compound.putFloat(NbtTags.ISLES_OCEAN_SLIDE_TARGET, this.islesOceanSlideTarget);
        compound.putString(NbtTags.ISLES_CENTER_ISLAND_SHAPE, this.islesCenterIslandShape);
        compound.putInt(NbtTags.ISLES_CENTER_ISLAND_RADIUS, this.islesCenterIslandRadius);
        compound.putInt(NbtTags.ISLES_CENTER_ISLAND_FALLOFF_DIST, this.islesCenterIslandFalloffDistance);
        compound.putInt(NbtTags.ISLES_CENTER_OCEAN_RADIUS, this.islesCenterOceanRadius);
        compound.putInt(NbtTags.ISLES_CENTER_OCEAN_FALLOFF_DIST, this.islesCenterOceanFalloffDistance);
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
        
        public boolean useCaves;
        
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
        public boolean indevUseCaves;

        public boolean islesUseIslands;
        public boolean islesUseOuterIslands;
        public float islesOceanSlideTarget;
        public String islesCenterIslandShape;
        public int islesCenterIslandRadius;
        public int islesCenterIslandFalloffDistance;
        public int islesCenterOceanRadius;
        public int islesCenterOceanFalloffDistance;
        public float islesOuterIslandNoiseScale;
        public float islesOuterIslandNoiseOffset;
        
        public Builder() {
            this.chunkProvider = ModernBetaBuiltInTypes.Chunk.BETA.id;
            
            this.useDeepslate = true;
            this.deepslateMinY = 0;
            this.deepslateMaxY = 8;
            this.deepslateBlock = "minecraft:deepslate";
            
            this.useCaves = true;
            
            this.noiseCoordinateScale = 684.412f;
            this.noiseHeightScale = 684.412f;
            this.noiseUpperLimitScale = 512f;
            this.noiseLowerLimitScale = 512f;
            this.noiseDepthNoiseScaleX = 200;
            this.noiseDepthNoiseScaleZ = 200;
            this.noiseMainNoiseScaleX = 80f;
            this.noiseMainNoiseScaleY = 160f;
            this.noiseMainNoiseScaleZ = 80f;
            this.noiseBaseSize = 8.5f;
            this.noiseStretchY = 12.0f;
            
            this.noiseTopSlideTarget = -10;
            this.noiseTopSlideSize = 3;
            this.noiseTopSlideOffset = 0;
            
            this.noiseBottomSlideTarget = 15;
            this.noiseBottomSlideSize = 3;
            this.noiseBottomSlideOffset = 0;
            
            this.infdevUsePyramid = true;
            this.infdevUseWall = true;
            
            this.indevLevelType = IndevType.ISLAND.getId();
            this.indevLevelTheme = IndevTheme.NORMAL.getId();
            this.indevLevelWidth = 256;
            this.indevLevelLength = 256;
            this.indevLevelHeight = 128;
            this.indevCaveRadius = 1.0f;
            this.indevUseCaves = true;
            
            this.islesUseIslands = false;
            this.islesUseOuterIslands = true;
            this.islesOceanSlideTarget = -200.0F;
            this.islesCenterIslandShape = IslandShape.CIRCLE.getId();
            this.islesCenterIslandRadius = 16;
            this.islesCenterIslandFalloffDistance = 8;
            this.islesCenterOceanRadius = 64;
            this.islesCenterOceanFalloffDistance = 16;
            this.islesOuterIslandNoiseScale = 300F;
            this.islesOuterIslandNoiseOffset = 0.25F;
        }
        
        public Builder fromCompound(NbtCompound compound) {
            this.chunkProvider = NbtUtil.readString(NbtTags.CHUNK_PROVIDER, compound, this.chunkProvider);
            
            this.useDeepslate = NbtUtil.readBoolean(NbtTags.USE_DEEPSLATE, compound, this.useDeepslate);
            this.deepslateMinY = NbtUtil.readInt(NbtTags.DEEPSLATE_MIN_Y, compound, this.deepslateMinY);
            this.deepslateMaxY = NbtUtil.readInt(NbtTags.DEEPSLATE_MAX_Y, compound, this.deepslateMaxY);
            this.deepslateBlock = NbtUtil.readString(NbtTags.DEEPSLATE_BLOCK, compound, this.deepslateBlock);
            
            this.useCaves = NbtUtil.readBoolean(NbtTags.USE_CAVES, compound, this.useCaves);
        
            this.noiseCoordinateScale = NbtUtil.readFloat(NbtTags.NOISE_COORDINATE_SCALE, compound, this.noiseCoordinateScale);
            this.noiseHeightScale = NbtUtil.readFloat(NbtTags.NOISE_HEIGHT_SCALE, compound, this.noiseHeightScale);
            this.noiseUpperLimitScale = NbtUtil.readFloat(NbtTags.NOISE_UPPER_LIMIT_SCALE, compound, this.noiseUpperLimitScale);
            this.noiseLowerLimitScale = NbtUtil.readFloat(NbtTags.NOISE_LOWER_LIMIT_SCALE, compound, this.noiseLowerLimitScale);
            this.noiseDepthNoiseScaleX = NbtUtil.readFloat(NbtTags.NOISE_DEPTH_NOISE_SCALE_X, compound, this.noiseDepthNoiseScaleX);
            this.noiseDepthNoiseScaleZ = NbtUtil.readFloat(NbtTags.NOISE_DEPTH_NOISE_SCALE_Z, compound, this.noiseDepthNoiseScaleZ);
            this.noiseMainNoiseScaleX = NbtUtil.readFloat(NbtTags.NOISE_MAIN_NOISE_SCALE_X, compound, this.noiseMainNoiseScaleX);
            this.noiseMainNoiseScaleY = NbtUtil.readFloat(NbtTags.NOISE_MAIN_NOISE_SCALE_Y, compound, this.noiseMainNoiseScaleY);
            this.noiseMainNoiseScaleZ = NbtUtil.readFloat(NbtTags.NOISE_MAIN_NOISE_SCALE_Z, compound, this.noiseMainNoiseScaleZ);
            this.noiseBaseSize = NbtUtil.readFloat(NbtTags.NOISE_BASE_SIZE, compound, this.noiseBaseSize);
            this.noiseStretchY = NbtUtil.readFloat(NbtTags.NOISE_STRETCH_Y, compound, this.noiseStretchY);
            
            this.noiseTopSlideTarget = NbtUtil.readInt(NbtTags.NOISE_TOP_SLIDE_TARGET, compound, this.noiseTopSlideTarget);
            this.noiseTopSlideSize = NbtUtil.readInt(NbtTags.NOISE_TOP_SLIDE_SIZE, compound, this.noiseTopSlideSize);
            this.noiseTopSlideOffset = NbtUtil.readInt(NbtTags.NOISE_TOP_SLIDE_OFFSET, compound, this.noiseTopSlideOffset);
            
            this.noiseBottomSlideTarget = NbtUtil.readInt(NbtTags.NOISE_BOTTOM_SLIDE_TARGET, compound, this.noiseBottomSlideTarget);
            this.noiseBottomSlideSize = NbtUtil.readInt(NbtTags.NOISE_BOTTOM_SLIDE_SIZE, compound, this.noiseBottomSlideSize);
            this.noiseBottomSlideOffset = NbtUtil.readInt(NbtTags.NOISE_BOTTOM_SLIDE_OFFSET, compound, this.noiseBottomSlideOffset);
            
            this.infdevUsePyramid = NbtUtil.readBoolean(NbtTags.INFDEV_USE_PYRAMID, compound, this.infdevUsePyramid);
            this.infdevUseWall = NbtUtil.readBoolean(NbtTags.INFDEV_USE_WALL, compound, this.infdevUseWall);
            
            this.indevLevelType = NbtUtil.readString(NbtTags.INDEV_LEVEL_TYPE, compound, this.indevLevelType);
            this.indevLevelTheme = NbtUtil.readString(NbtTags.INDEV_LEVEL_THEME, compound, this.indevLevelTheme);
            this.indevLevelWidth = NbtUtil.readInt(NbtTags.INDEV_LEVEL_WIDTH, compound, this.indevLevelWidth);
            this.indevLevelLength = NbtUtil.readInt(NbtTags.INDEV_LEVEL_LENGTH, compound, this.indevLevelLength);
            this.indevLevelHeight = NbtUtil.readInt(NbtTags.INDEV_LEVEL_HEIGHT, compound, this.indevLevelHeight);
            this.indevCaveRadius = NbtUtil.readFloat(NbtTags.INDEV_CAVE_RADIUS, compound, this.indevCaveRadius);
            this.indevUseCaves = NbtUtil.readBoolean(NbtTags.INDEV_USE_CAVES, compound, this.indevUseCaves);

            this.islesUseIslands = NbtUtil.readBoolean(NbtTags.ISLES_USE_ISLANDS, compound, this.islesUseIslands);
            this.islesUseOuterIslands = NbtUtil.readBoolean(NbtTags.ISLES_USE_OUTER_ISLANDS, compound, this.islesUseOuterIslands);
            this.islesOceanSlideTarget = NbtUtil.readFloat(NbtTags.ISLES_OCEAN_SLIDE_TARGET, compound, this.islesOceanSlideTarget);
            this.islesCenterIslandShape = NbtUtil.readString(NbtTags.ISLES_CENTER_ISLAND_SHAPE, compound, this.islesCenterIslandShape);
            this.islesCenterIslandRadius = NbtUtil.readInt(NbtTags.ISLES_CENTER_ISLAND_RADIUS, compound, this.islesCenterIslandRadius);
            this.islesCenterIslandFalloffDistance = NbtUtil.readInt(NbtTags.ISLES_CENTER_ISLAND_FALLOFF_DIST, compound, this.islesCenterIslandFalloffDistance);
            this.islesCenterOceanRadius = NbtUtil.readInt(NbtTags.ISLES_CENTER_OCEAN_RADIUS, compound, this.islesCenterOceanRadius);
            this.islesCenterOceanFalloffDistance = NbtUtil.readInt(NbtTags.ISLES_CENTER_OCEAN_FALLOFF_DIST, compound, this.islesCenterOceanFalloffDistance);
            this.islesOuterIslandNoiseScale = NbtUtil.readFloat(NbtTags.ISLES_OUTER_ISLAND_NOISE_SCALE, compound, this.islesOuterIslandNoiseScale);
            this.islesOuterIslandNoiseOffset = NbtUtil.readFloat(NbtTags.ISLES_OUTER_ISLAND_NOISE_OFFSET, compound, this.islesOuterIslandNoiseOffset);
            
            this.loadDatafix(compound);
            
            return this;
        }
        
        public ModernBetaSettingsChunk build() {
            return new ModernBetaSettingsChunk(this);
        }
        
        private void loadDatafix(NbtCompound compound) {
            String tag0 = "islesMaxOceanDepth";
            Runnable fix0 = () -> this.islesOceanSlideTarget = -NbtUtil.readFloatOrThrow(tag0, compound);
            
            ModernBetaSettings.datafix(tag0, compound, fix0);
        }
    }
}