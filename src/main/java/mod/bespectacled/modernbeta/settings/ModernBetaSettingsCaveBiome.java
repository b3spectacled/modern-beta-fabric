package mod.bespectacled.modernbeta.settings;

import java.util.ArrayList;
import java.util.List;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.config.ModernBetaConfigCaveBiome;
import mod.bespectacled.modernbeta.config.ModernBetaConfigCaveBiome.VoronoiPointCaveBiome;
import mod.bespectacled.modernbeta.util.NbtListBuilder;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import net.minecraft.nbt.NbtCompound;

public class ModernBetaSettingsCaveBiome {
    private static final ModernBetaConfigCaveBiome CONFIG = ModernBeta.CAVE_BIOME_CONFIG;
    
    public final String biomeProvider;
    public final String singleBiome;
    
    public final float voronoiHorizontalNoiseScale;
    public final float voronoiVerticalNoiseScale;
    public final int voronoiDepthMinY;
    public final int voronoiDepthMaxY;
    public final List<VoronoiPointCaveBiome> voronoiPoints;
    
    public ModernBetaSettingsCaveBiome() {
        this(new Builder());
    }
    
    public ModernBetaSettingsCaveBiome(ModernBetaSettingsCaveBiome.Builder builder) {
        this.biomeProvider = builder.biomeProvider;
        this.singleBiome = builder.singleBiome;
        
        this.voronoiHorizontalNoiseScale = builder.voronoiHorizontalNoiseScale;
        this.voronoiVerticalNoiseScale = builder.voronoiVerticalNoiseScale;
        this.voronoiDepthMinY = builder.voronoiDepthMinY;
        this.voronoiDepthMaxY = builder.voronoiDepthMaxY;
        this.voronoiPoints = builder.voronoiPoints;
    }
    
    public NbtCompound toCompound() {
        NbtCompound compound = new NbtCompound();
        
        compound.putString(NbtTags.BIOME_PROVIDER, this.biomeProvider);
        compound.putString(NbtTags.SINGLE_BIOME, this.singleBiome);
        
        compound.putFloat(NbtTags.VORONOI_HORIZONTAL_NOISE_SCALE, this.voronoiHorizontalNoiseScale);
        compound.putFloat(NbtTags.VORONOI_VERTICAL_NOISE_SCALE, this.voronoiVerticalNoiseScale);
        
        NbtListBuilder builder = new NbtListBuilder();
        this.voronoiPoints.forEach(p -> builder.add(p.toCompound()));
        compound.put(NbtTags.BIOMES, builder.build());
        
        return compound;
    }
    
    public static class Builder {
        public String biomeProvider;
        public String singleBiome;
        
        public float voronoiHorizontalNoiseScale;
        public float voronoiVerticalNoiseScale;
        public int voronoiDepthMinY;
        public int voronoiDepthMaxY;
        public List<VoronoiPointCaveBiome> voronoiPoints;
        
        public Builder() {
            this(new NbtCompound());
        }
        
        public Builder(NbtCompound compound) {
            this.biomeProvider = NbtUtil.readString(NbtTags.BIOME_PROVIDER, compound, CONFIG.biomeProvider);
            this.singleBiome = NbtUtil.readString(NbtTags.SINGLE_BIOME, compound, CONFIG.singleBiome);
            
            this.voronoiDepthMinY = NbtUtil.readInt(NbtTags.VORONOI_DEPTH_MIN_Y, compound, CONFIG.voronoiDepthMinY);
            this.voronoiDepthMaxY = NbtUtil.readInt(NbtTags.VORONOI_DEPTH_MAX_Y, compound, CONFIG.voronoiDepthMaxY);
            this.voronoiHorizontalNoiseScale = NbtUtil.readFloat(NbtTags.VORONOI_HORIZONTAL_NOISE_SCALE, compound, CONFIG.voronoiHorizontalNoiseScale);
            this.voronoiVerticalNoiseScale = NbtUtil.readFloat(NbtTags.VORONOI_VERTICAL_NOISE_SCALE, compound, CONFIG.voronoiVerticalNoiseScale);
            
            this.voronoiPoints = new ArrayList<>();
            if (compound.contains(NbtTags.BIOMES)) {
                NbtUtil.toListOrThrow(compound.get(NbtTags.BIOMES)).stream().forEach(e -> {
                    NbtCompound point = NbtUtil.toCompoundOrThrow(e);
                    
                    String biome = NbtUtil.readStringOrThrow(NbtTags.BIOME, point);
                    double temp = NbtUtil.readDoubleOrThrow(NbtTags.TEMP, point);
                    double rain = NbtUtil.readDoubleOrThrow(NbtTags.RAIN, point);
                    double weight = NbtUtil.readDoubleOrThrow(NbtTags.DEPTH, point);
                    
                    this.voronoiPoints.add(new VoronoiPointCaveBiome(biome, temp, rain, weight));
                });
            } else {
                this.voronoiPoints.addAll(CONFIG.voronoiPoints);
            }
        }
        
        public ModernBetaSettingsCaveBiome build() {
            return new ModernBetaSettingsCaveBiome(this);
        }
    }
}