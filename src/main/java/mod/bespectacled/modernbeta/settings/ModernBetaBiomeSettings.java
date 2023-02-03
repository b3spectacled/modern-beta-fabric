package mod.bespectacled.modernbeta.settings;

import java.util.LinkedHashMap;
import java.util.Map;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.config.ModernBetaConfigBiome.ConfigClimateMapping;
import mod.bespectacled.modernbeta.util.NbtCompoundBuilder;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import net.minecraft.nbt.NbtCompound;

public class ModernBetaBiomeSettings {
    public final String biomeProvider;
    public final String singleBiome;
    public final boolean replaceOceanBiomes;
    
    public final float tempNoiseScale;
    public final float rainNoiseScale;
    public final float detailNoiseScale;
    
    public final Map<String, ConfigClimateMapping> climates;
    
    public ModernBetaBiomeSettings() {
        this(new Builder());
    }
    
    public ModernBetaBiomeSettings(ModernBetaBiomeSettings.Builder builder) {
        this.biomeProvider = builder.biomeProvider;
        this.singleBiome = builder.singleBiome;
        this.replaceOceanBiomes = builder.replaceOceanBiomes;
        
        this.tempNoiseScale = builder.tempNoiseScale;
        this.rainNoiseScale = builder.rainNoiseScale;
        this.detailNoiseScale = builder.detailNoiseScale;
        
        this.climates = builder.climates;
    }
    
    public NbtCompound toCompound() {
        NbtCompound compound = new NbtCompound();
        
        compound.putString(NbtTags.BIOME_PROVIDER, this.biomeProvider);
        compound.putString(NbtTags.SINGLE_BIOME, this.singleBiome);
        compound.putBoolean(NbtTags.REPLACE_OCEAN_BIOMES, this.replaceOceanBiomes);
        
        compound.putFloat(NbtTags.TEMP_NOISE_SCALE, this.tempNoiseScale);
        compound.putFloat(NbtTags.RAIN_NOISE_SCALE, this.rainNoiseScale);
        compound.putFloat(NbtTags.DETAIL_NOISE_SCALE, this.detailNoiseScale);
        
        NbtCompoundBuilder builder = new NbtCompoundBuilder();
        ModernBeta.BIOME_CONFIG.climates.keySet().forEach(key -> {
            builder.putCompound(key, this.climates.get(key).toCompound());
        });
        compound.put(NbtTags.BIOMES, builder.build());
        
        return compound;
    }
    
    public static class Builder {
        public String biomeProvider;
        public String singleBiome;
        public boolean replaceOceanBiomes;
        
        public float tempNoiseScale;
        public float rainNoiseScale;
        public float detailNoiseScale;
        
        public Map<String, ConfigClimateMapping> climates;
        
        public Builder() {
            this(new NbtCompound());
        }
        
        public Builder(NbtCompound compound) {
            this.biomeProvider = NbtUtil.readString(NbtTags.BIOME_PROVIDER, compound, ModernBeta.BIOME_CONFIG.biomeProvider);
            this.singleBiome = NbtUtil.readString(NbtTags.SINGLE_BIOME, compound, ModernBeta.BIOME_CONFIG.singleBiome);
            this.replaceOceanBiomes = NbtUtil.readBoolean(NbtTags.REPLACE_OCEAN_BIOMES, compound, ModernBeta.BIOME_CONFIG.replaceOceanBiomes);
            
            this.tempNoiseScale = NbtUtil.readFloat(NbtTags.TEMP_NOISE_SCALE, compound, ModernBeta.BIOME_CONFIG.tempNoiseScale);
            this.rainNoiseScale = NbtUtil.readFloat(NbtTags.RAIN_NOISE_SCALE, compound, ModernBeta.BIOME_CONFIG.rainNoiseScale);
            this.detailNoiseScale = NbtUtil.readFloat(NbtTags.DETAIL_NOISE_SCALE, compound, ModernBeta.BIOME_CONFIG.detailNoiseScale);
            
            this.climates = new LinkedHashMap<>();
            if (compound.contains(NbtTags.BIOMES)) {
                NbtCompound biomes = NbtUtil.readCompoundOrThrow(NbtTags.BIOMES, compound);
                
                biomes.getKeys().forEach(key -> {
                    this.climates.put(key, ConfigClimateMapping.fromCompound(NbtUtil.readCompoundOrThrow(key, biomes)));
                });
                
            } else {
                this.climates.putAll(ModernBeta.BIOME_CONFIG.climates);
            }
        }
        
        public ModernBetaBiomeSettings build() {
            return new ModernBetaBiomeSettings(this);
        }
    }
}