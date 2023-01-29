package mod.bespectacled.modernbeta.settings;

import java.util.ArrayList;
import java.util.List;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.config.ModernBetaConfigCaveBiome.CaveBiomeVoronoiPoint;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import net.minecraft.nbt.NbtCompound;

public class ModernBetaSettings {
    public final ModernBetaChunkSettings chunkSettings;
    public final ModernBetaBiomeSettings biomeSettings;
    public final ModernBetaCaveBiomeSettings caveBiomeSettings;
    
    public ModernBetaSettings() {
        this.chunkSettings = new ModernBetaChunkSettings();
        this.biomeSettings = new ModernBetaBiomeSettings();
        this.caveBiomeSettings = new ModernBetaCaveBiomeSettings();
    }
    
    public ModernBetaSettings(NbtCompound chunkSettings, NbtCompound biomeSettings, NbtCompound caveBiomeSettings) {
        this.chunkSettings = new ModernBetaChunkSettings.Builder(chunkSettings).build();
        this.biomeSettings = new ModernBetaBiomeSettings.Builder(biomeSettings).build();
        this.caveBiomeSettings = null;
    }
    
    public static class ModernBetaChunkSettings {
        public final String chunkProvider;
        public final boolean useIslands;
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
        
        public final boolean islandsUseOuterIslands;
        public final float islandsCenterIslandFalloff;
        public final int islandsCenterIslandRadius;
        public final int islandsCenterOceanFalloffDistance;
        public final int islandsCenterOceanRadius;
        public final float islandsOuterIslandNoiseScale;
        public final float islandsOuterIslandNoiseOffset;
        
        private ModernBetaChunkSettings() {
            this(new Builder());
        }
        
        private ModernBetaChunkSettings(Builder builder) {
            this.chunkProvider = builder.chunkProvider;
            this.useIslands = builder.useIslands;
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
            
            this.islandsUseOuterIslands = builder.islandsUseOuterIslands;
            this.islandsCenterIslandFalloff = builder.islandsCenterIslandFalloff;
            this.islandsCenterIslandRadius = builder.islandsCenterIslandRadius;
            this.islandsCenterOceanFalloffDistance = builder.islandsCenterOceanFalloffDistance;
            this.islandsCenterOceanRadius = builder.islandsCenterOceanRadius;
            this.islandsOuterIslandNoiseScale = builder.islandsOuterIslandNoiseScale;
            this.islandsOuterIslandNoiseOffset = builder.islandsOuterIslandNoiseOffset;
        }

        private static class Builder {
            public String chunkProvider;
            public boolean useIslands;
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
            
            public boolean islandsUseOuterIslands;
            public float islandsCenterIslandFalloff;
            public int islandsCenterIslandRadius;
            public int islandsCenterOceanFalloffDistance;
            public int islandsCenterOceanRadius;
            public float islandsOuterIslandNoiseScale;
            public float islandsOuterIslandNoiseOffset;
            
            private Builder() {
                this(new NbtCompound());
            }
            
            private Builder(NbtCompound compound) {
                this.chunkProvider = NbtUtil.readString(NbtTags.CHUNK_PROVIDER, compound, ModernBeta.CHUNK_CONFIG.chunkProvider);
                this.useIslands = NbtUtil.readBoolean(NbtTags.USE_ISLANDS, compound, ModernBeta.CHUNK_CONFIG.useIslands);
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
                
                this.islandsUseOuterIslands = NbtUtil.readBoolean(NbtTags.ISLANDS_USE_OUTER_ISLANDS, compound, ModernBeta.CHUNK_CONFIG.islandsUseOuterIslands);
                this.islandsCenterIslandFalloff = NbtUtil.readFloat(NbtTags.ISLANDS_CENTER_ISLAND_FALLOFF, compound, ModernBeta.CHUNK_CONFIG.islandsCenterIslandFalloff);
                this.islandsCenterIslandRadius = NbtUtil.readInt(NbtTags.ISLANDS_CENTER_ISLAND_RADIUS, compound, ModernBeta.CHUNK_CONFIG.islandsCenterIslandRadius);
                this.islandsCenterOceanFalloffDistance = NbtUtil.readInt(NbtTags.ISLANDS_CENTER_OCEAN_FALLOFF_DIST, compound, ModernBeta.CHUNK_CONFIG.islandsCenterOceanFalloffDistance);
                this.islandsCenterOceanRadius = NbtUtil.readInt(NbtTags.ISLANDS_CENTER_OCEAN_RADIUS, compound, ModernBeta.CHUNK_CONFIG.islandsCenterOceanRadius);
                this.islandsOuterIslandNoiseScale = NbtUtil.readFloat(NbtTags.ISLANDS_OUTER_ISLAND_NOISE_SCALE, compound, ModernBeta.CHUNK_CONFIG.islandsOuterIslandNoiseScale);
                this.islandsOuterIslandNoiseOffset = NbtUtil.readFloat(NbtTags.ISLANDS_OUTER_ISLAND_NOISE_OFFSET, compound, ModernBeta.CHUNK_CONFIG.islandsOuterIslandNoiseOffset);
            }
            
            private ModernBetaChunkSettings build() {
                return new ModernBetaChunkSettings(this);
            }
        }
    }
    
    public static class ModernBetaBiomeSettings extends ModernBetaSettings {
        public final String biomeProvider;
        public final String singleBiome;
        public final boolean replaceOceanBiomes;
        
        public final float tempNoiseScale;
        public final float rainNoiseScale;
        public final float detailNoiseScale;
        
        private ModernBetaBiomeSettings() {
            this(new Builder());
        }
        
        private ModernBetaBiomeSettings(Builder builder) {
            this.biomeProvider = builder.biomeProvider;
            this.singleBiome = builder.singleBiome;
            this.replaceOceanBiomes = builder.replaceOceanBiomes;
            
            this.tempNoiseScale = builder.tempNoiseScale;
            this.rainNoiseScale = builder.rainNoiseScale;
            this.detailNoiseScale = builder.detailNoiseScale;
        }
        
        private static class Builder {
            public String biomeProvider;
            public String singleBiome;
            public boolean replaceOceanBiomes;
            
            public float tempNoiseScale;
            public float rainNoiseScale;
            public float detailNoiseScale;
            
            private Builder() {
                this(new NbtCompound());
            }
            
            private Builder(NbtCompound compound) {
                this.biomeProvider = NbtUtil.readString(NbtTags.BIOME_PROVIDER, compound, ModernBeta.BIOME_CONFIG.biomeProvider);
                this.singleBiome = NbtUtil.readString(NbtTags.SINGLE_BIOME, compound, ModernBeta.BIOME_CONFIG.singleBiome);
                this.replaceOceanBiomes = NbtUtil.readBoolean(NbtTags.REPLACE_OCEAN_BIOMES, compound, ModernBeta.BIOME_CONFIG.replaceOceanBiomes);
                
                this.tempNoiseScale = NbtUtil.readFloat(NbtTags.TEMP_NOISE_SCALE, compound, ModernBeta.BIOME_CONFIG.tempNoiseScale);
                this.rainNoiseScale = NbtUtil.readFloat(NbtTags.RAIN_NOISE_SCALE, compound, ModernBeta.BIOME_CONFIG.rainNoiseScale);
                this.detailNoiseScale = NbtUtil.readFloat(NbtTags.DETAIL_NOISE_SCALE, compound, ModernBeta.BIOME_CONFIG.detailNoiseScale);
            }
            
            private ModernBetaBiomeSettings build() {
                return new ModernBetaBiomeSettings(this);
            }
        }
    }
    
    public static class ModernBetaCaveBiomeSettings extends ModernBetaSettings {
        public final String biomeProvider;
        public final String singleBiome;
        
        public final float voronoiHorizontalNoiseScale;
        public final float voronoiVerticalNoiseScale;
        public final List<CaveBiomeVoronoiPoint> voronoiPoints;
        
        private ModernBetaCaveBiomeSettings() {
            this(new Builder());
        }
        
        private ModernBetaCaveBiomeSettings(Builder builder) {
            this.biomeProvider = builder.biomeProvider;
            this.singleBiome = builder.singleBiome;
            
            this.voronoiHorizontalNoiseScale = builder.voronoiHorizontalNoiseScale;
            this.voronoiVerticalNoiseScale = builder.voronoiVerticalNoiseScale;
            this.voronoiPoints = builder.voronoiPoints;
        }
        
        private static class Builder {
            public String biomeProvider;
            public String singleBiome;
            
            public float voronoiHorizontalNoiseScale;
            public float voronoiVerticalNoiseScale;
            public List<CaveBiomeVoronoiPoint> voronoiPoints;
            
            private Builder() {
                this(new NbtCompound());
            }
            
            private Builder(NbtCompound compound) {
                this.biomeProvider = NbtUtil.readString(NbtTags.BIOME_PROVIDER, compound, ModernBeta.CAVE_BIOME_CONFIG.biomeProvider);
                this.singleBiome = NbtUtil.readString(NbtTags.SINGLE_BIOME, compound, ModernBeta.CAVE_BIOME_CONFIG.singleBiome);
                
                this.voronoiHorizontalNoiseScale = NbtUtil.readFloat(NbtTags.VORONOI_HORIZONTAL_NOISE_SCALE, compound, ModernBeta.CAVE_BIOME_CONFIG.voronoiHorizontalNoiseScale);
                this.voronoiVerticalNoiseScale = NbtUtil.readFloat(NbtTags.VORONOI_VERTICAL_NOISE_SCALE, compound, ModernBeta.CAVE_BIOME_CONFIG.voronoiVerticalNoiseScale);
                
                this.voronoiPoints = new ArrayList<>();
                if (compound.contains(NbtTags.BIOMES)) {
                    NbtUtil.toListOrThrow(compound.get(NbtTags.BIOMES)).stream().forEach(e -> {
                        NbtCompound point = NbtUtil.toCompoundOrThrow(e);
                        
                        String biome = NbtUtil.readStringOrThrow(NbtTags.BIOME, point);
                        double temp = NbtUtil.readDoubleOrThrow(NbtTags.TEMP, point);
                        double rain = NbtUtil.readDoubleOrThrow(NbtTags.RAIN, point);
                        boolean nullBiome = NbtUtil.readBooleanOrThrow(NbtTags.NULL_BIOME, point);
                        
                        this.voronoiPoints.add(new CaveBiomeVoronoiPoint(biome, temp, rain, nullBiome));
                    });
                } else {
                    this.voronoiPoints.addAll(ModernBeta.CAVE_BIOME_CONFIG.voronoiPoints);
                }
            }
        }
    }
}
