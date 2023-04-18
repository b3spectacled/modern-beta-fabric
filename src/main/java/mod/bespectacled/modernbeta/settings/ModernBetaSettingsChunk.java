package mod.bespectacled.modernbeta.settings;

import com.google.gson.Gson;

import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import mod.bespectacled.modernbeta.util.NbtCompoundBuilder;
import mod.bespectacled.modernbeta.util.NbtReader;
import mod.bespectacled.modernbeta.util.NbtTags;
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
        return new NbtCompoundBuilder()
            .putString(NbtTags.CHUNK_PROVIDER, this.chunkProvider)
            .putBoolean(NbtTags.USE_DEEPSLATE, this.useDeepslate)
            .putInt(NbtTags.DEEPSLATE_MIN_Y, this.deepslateMinY)
            .putInt(NbtTags.DEEPSLATE_MAX_Y, this.deepslateMaxY)
            .putString(NbtTags.DEEPSLATE_BLOCK, this.deepslateBlock)
            
            .putBoolean(NbtTags.USE_CAVES, this.useCaves)
            
            .putFloat(NbtTags.NOISE_COORDINATE_SCALE, this.noiseCoordinateScale)
            .putFloat(NbtTags.NOISE_HEIGHT_SCALE, this.noiseHeightScale)
            .putFloat(NbtTags.NOISE_UPPER_LIMIT_SCALE, this.noiseUpperLimitScale)
            .putFloat(NbtTags.NOISE_LOWER_LIMIT_SCALE, this.noiseLowerLimitScale)
            .putFloat(NbtTags.NOISE_DEPTH_NOISE_SCALE_X, this.noiseDepthNoiseScaleX)
            .putFloat(NbtTags.NOISE_DEPTH_NOISE_SCALE_Z, this.noiseDepthNoiseScaleZ)
            .putFloat(NbtTags.NOISE_MAIN_NOISE_SCALE_X, this.noiseMainNoiseScaleX)
            .putFloat(NbtTags.NOISE_MAIN_NOISE_SCALE_Y, this.noiseMainNoiseScaleY)
            .putFloat(NbtTags.NOISE_MAIN_NOISE_SCALE_Z, this.noiseMainNoiseScaleZ)
            .putFloat(NbtTags.NOISE_BASE_SIZE, this.noiseBaseSize)
            .putFloat(NbtTags.NOISE_STRETCH_Y, this.noiseStretchY)
            
            .putInt(NbtTags.NOISE_TOP_SLIDE_TARGET, this.noiseTopSlideTarget)
            .putInt(NbtTags.NOISE_TOP_SLIDE_SIZE, this.noiseTopSlideSize)
            .putInt(NbtTags.NOISE_TOP_SLIDE_OFFSET, this.noiseTopSlideOffset)
            
            .putInt(NbtTags.NOISE_BOTTOM_SLIDE_TARGET, this.noiseBottomSlideTarget)
            .putInt(NbtTags.NOISE_BOTTOM_SLIDE_SIZE, this.noiseBottomSlideSize)
            .putInt(NbtTags.NOISE_BOTTOM_SLIDE_OFFSET, this.noiseBottomSlideOffset)
           
            .putBoolean(NbtTags.INFDEV_USE_PYRAMID, this.infdevUsePyramid)
            .putBoolean(NbtTags.INFDEV_USE_WALL, this.infdevUseWall)
            
            .putString(NbtTags.INDEV_LEVEL_THEME, this.indevLevelTheme)
            .putString(NbtTags.INDEV_LEVEL_TYPE, this.indevLevelType)
            .putInt(NbtTags.INDEV_LEVEL_WIDTH, this.indevLevelWidth)
            .putInt(NbtTags.INDEV_LEVEL_LENGTH, this.indevLevelLength)
            .putInt(NbtTags.INDEV_LEVEL_HEIGHT, this.indevLevelHeight)
            .putFloat(NbtTags.INDEV_CAVE_RADIUS, this.indevCaveRadius)
            .putBoolean(NbtTags.INDEV_USE_CAVES, this.indevUseCaves)
        
            .putBoolean(NbtTags.ISLES_USE_ISLANDS, this.islesUseIslands)
            .putBoolean(NbtTags.ISLES_USE_OUTER_ISLANDS, this.islesUseOuterIslands)
            .putFloat(NbtTags.ISLES_OCEAN_SLIDE_TARGET, this.islesOceanSlideTarget)
            .putString(NbtTags.ISLES_CENTER_ISLAND_SHAPE, this.islesCenterIslandShape)
            .putInt(NbtTags.ISLES_CENTER_ISLAND_RADIUS, this.islesCenterIslandRadius)
            .putInt(NbtTags.ISLES_CENTER_ISLAND_FALLOFF_DIST, this.islesCenterIslandFalloffDistance)
            .putInt(NbtTags.ISLES_CENTER_OCEAN_RADIUS, this.islesCenterOceanRadius)
            .putInt(NbtTags.ISLES_CENTER_OCEAN_FALLOFF_DIST, this.islesCenterOceanFalloffDistance)
            .putFloat(NbtTags.ISLES_OUTER_ISLAND_NOISE_SCALE, this.islesOuterIslandNoiseScale)
            .putFloat(NbtTags.ISLES_OUTER_ISLAND_NOISE_OFFSET, this.islesOuterIslandNoiseOffset)
            
            .build();
    }

    public static class Builder {
        public String chunkProvider;
        
        public boolean useDeepslate;
        public int deepslateMinY;
        public int deepslateMaxY;
        public String deepslateBlock;
        
        public boolean useCaves;
        public boolean useFixedCaves;
        
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
            this.useFixedCaves = false;
            
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
            NbtReader reader = new NbtReader(compound);
            
            this.chunkProvider = reader.readString(NbtTags.CHUNK_PROVIDER, this.chunkProvider);
            
            this.useDeepslate = reader.readBoolean(NbtTags.USE_DEEPSLATE, this.useDeepslate);
            this.deepslateMinY = reader.readInt(NbtTags.DEEPSLATE_MIN_Y, this.deepslateMinY);
            this.deepslateMaxY = reader.readInt(NbtTags.DEEPSLATE_MAX_Y, this.deepslateMaxY);
            this.deepslateBlock = reader.readString(NbtTags.DEEPSLATE_BLOCK, this.deepslateBlock);
            
            this.useCaves = reader.readBoolean(NbtTags.USE_CAVES, this.useCaves);
        
            this.noiseCoordinateScale = reader.readFloat(NbtTags.NOISE_COORDINATE_SCALE, this.noiseCoordinateScale);
            this.noiseHeightScale = reader.readFloat(NbtTags.NOISE_HEIGHT_SCALE, this.noiseHeightScale);
            this.noiseUpperLimitScale = reader.readFloat(NbtTags.NOISE_UPPER_LIMIT_SCALE, this.noiseUpperLimitScale);
            this.noiseLowerLimitScale = reader.readFloat(NbtTags.NOISE_LOWER_LIMIT_SCALE, this.noiseLowerLimitScale);
            this.noiseDepthNoiseScaleX = reader.readFloat(NbtTags.NOISE_DEPTH_NOISE_SCALE_X, this.noiseDepthNoiseScaleX);
            this.noiseDepthNoiseScaleZ = reader.readFloat(NbtTags.NOISE_DEPTH_NOISE_SCALE_Z, this.noiseDepthNoiseScaleZ);
            this.noiseMainNoiseScaleX = reader.readFloat(NbtTags.NOISE_MAIN_NOISE_SCALE_X, this.noiseMainNoiseScaleX);
            this.noiseMainNoiseScaleY = reader.readFloat(NbtTags.NOISE_MAIN_NOISE_SCALE_Y, this.noiseMainNoiseScaleY);
            this.noiseMainNoiseScaleZ = reader.readFloat(NbtTags.NOISE_MAIN_NOISE_SCALE_Z, this.noiseMainNoiseScaleZ);
            this.noiseBaseSize = reader.readFloat(NbtTags.NOISE_BASE_SIZE, this.noiseBaseSize);
            this.noiseStretchY = reader.readFloat(NbtTags.NOISE_STRETCH_Y, this.noiseStretchY);
            
            this.noiseTopSlideTarget = reader.readInt(NbtTags.NOISE_TOP_SLIDE_TARGET, this.noiseTopSlideTarget);
            this.noiseTopSlideSize = reader.readInt(NbtTags.NOISE_TOP_SLIDE_SIZE, this.noiseTopSlideSize);
            this.noiseTopSlideOffset = reader.readInt(NbtTags.NOISE_TOP_SLIDE_OFFSET, this.noiseTopSlideOffset);
            
            this.noiseBottomSlideTarget = reader.readInt(NbtTags.NOISE_BOTTOM_SLIDE_TARGET, this.noiseBottomSlideTarget);
            this.noiseBottomSlideSize = reader.readInt(NbtTags.NOISE_BOTTOM_SLIDE_SIZE, this.noiseBottomSlideSize);
            this.noiseBottomSlideOffset = reader.readInt(NbtTags.NOISE_BOTTOM_SLIDE_OFFSET, this.noiseBottomSlideOffset);
            
            this.infdevUsePyramid = reader.readBoolean(NbtTags.INFDEV_USE_PYRAMID, this.infdevUsePyramid);
            this.infdevUseWall = reader.readBoolean(NbtTags.INFDEV_USE_WALL, this.infdevUseWall);
            
            this.indevLevelType = reader.readString(NbtTags.INDEV_LEVEL_TYPE, this.indevLevelType);
            this.indevLevelTheme = reader.readString(NbtTags.INDEV_LEVEL_THEME, this.indevLevelTheme);
            this.indevLevelWidth = reader.readInt(NbtTags.INDEV_LEVEL_WIDTH, this.indevLevelWidth);
            this.indevLevelLength = reader.readInt(NbtTags.INDEV_LEVEL_LENGTH, this.indevLevelLength);
            this.indevLevelHeight = reader.readInt(NbtTags.INDEV_LEVEL_HEIGHT, this.indevLevelHeight);
            this.indevCaveRadius = reader.readFloat(NbtTags.INDEV_CAVE_RADIUS, this.indevCaveRadius);
            this.indevUseCaves = reader.readBoolean(NbtTags.INDEV_USE_CAVES, this.indevUseCaves);

            this.islesUseIslands = reader.readBoolean(NbtTags.ISLES_USE_ISLANDS, this.islesUseIslands);
            this.islesUseOuterIslands = reader.readBoolean(NbtTags.ISLES_USE_OUTER_ISLANDS, this.islesUseOuterIslands);
            this.islesOceanSlideTarget = reader.readFloat(NbtTags.ISLES_OCEAN_SLIDE_TARGET, this.islesOceanSlideTarget);
            this.islesCenterIslandShape = reader.readString(NbtTags.ISLES_CENTER_ISLAND_SHAPE, this.islesCenterIslandShape);
            this.islesCenterIslandRadius = reader.readInt(NbtTags.ISLES_CENTER_ISLAND_RADIUS, this.islesCenterIslandRadius);
            this.islesCenterIslandFalloffDistance = reader.readInt(NbtTags.ISLES_CENTER_ISLAND_FALLOFF_DIST, this.islesCenterIslandFalloffDistance);
            this.islesCenterOceanRadius = reader.readInt(NbtTags.ISLES_CENTER_OCEAN_RADIUS, this.islesCenterOceanRadius);
            this.islesCenterOceanFalloffDistance = reader.readInt(NbtTags.ISLES_CENTER_OCEAN_FALLOFF_DIST, this.islesCenterOceanFalloffDistance);
            this.islesOuterIslandNoiseScale = reader.readFloat(NbtTags.ISLES_OUTER_ISLAND_NOISE_SCALE, this.islesOuterIslandNoiseScale);
            this.islesOuterIslandNoiseOffset = reader.readFloat(NbtTags.ISLES_OUTER_ISLAND_NOISE_OFFSET, this.islesOuterIslandNoiseOffset);
            
            this.loadDatafix(reader);
            
            return this;
        }
        
        public ModernBetaSettingsChunk build() {
            return new ModernBetaSettingsChunk(this);
        }
        
        private void loadDatafix(NbtReader reader) {
            String tag0 = "islesMaxOceanDepth";
            
            ModernBetaSettings.datafix(tag0, reader, () -> this.islesOceanSlideTarget = -reader.readFloatOrThrow(tag0));
        }
    }
}