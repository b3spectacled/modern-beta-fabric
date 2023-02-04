package mod.bespectacled.modernbeta.settings;

import java.util.LinkedHashMap;
import java.util.Map;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.config.ModernBetaConfigBiome;
import mod.bespectacled.modernbeta.config.ModernBetaConfigBiome.ConfigClimateMapping;
import mod.bespectacled.modernbeta.util.NbtCompoundBuilder;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import net.minecraft.nbt.NbtCompound;

public class ModernBetaBiomeSettings {
    private static final ModernBetaConfigBiome CONFIG = ModernBeta.BIOME_CONFIG;
    
    public final String biomeProvider;
    public final String singleBiome;
    public final boolean replaceOceanBiomes;
    
    public final float betaTempNoiseScale;
    public final float betaRainNoiseScale;
    public final float betaDetailNoiseScale;
    
    public final Map<String, ConfigClimateMapping> betaClimates;
    
    public ModernBetaBiomeSettings() {
        this(new Builder());
    }
    
    public ModernBetaBiomeSettings(ModernBetaBiomeSettings.Builder builder) {
        this.biomeProvider = builder.biomeProvider;
        this.singleBiome = builder.singleBiome;
        this.replaceOceanBiomes = builder.replaceOceanBiomes;
        
        this.betaTempNoiseScale = builder.betaTempNoiseScale;
        this.betaRainNoiseScale = builder.betaRainNoiseScale;
        this.betaDetailNoiseScale = builder.betaDetailNoiseScale;
        
        this.betaClimates = builder.betaClimates;
    }
    
    public NbtCompound toCompound() {
        NbtCompound compound = new NbtCompound();
        
        compound.putString(NbtTags.BIOME_PROVIDER, this.biomeProvider);
        compound.putString(NbtTags.SINGLE_BIOME, this.singleBiome);
        compound.putBoolean(NbtTags.REPLACE_OCEAN_BIOMES, this.replaceOceanBiomes);
        
        compound.putFloat(NbtTags.BETA_TEMP_NOISE_SCALE, this.betaTempNoiseScale);
        compound.putFloat(NbtTags.BETA_RAIN_NOISE_SCALE, this.betaRainNoiseScale);
        compound.putFloat(NbtTags.BETA_DETAIL_NOISE_SCALE, this.betaDetailNoiseScale);
        
        NbtCompoundBuilder builder = new NbtCompoundBuilder();
        CONFIG.betaClimates.keySet().forEach(key -> {
            builder.putCompound(key, this.betaClimates.get(key).toCompound());
        });
        compound.put(NbtTags.BIOMES, builder.build());
        
        return compound;
    }
    
    public static class Builder {
        public String biomeProvider;
        public String singleBiome;
        public boolean replaceOceanBiomes;
        
        public float betaTempNoiseScale;
        public float betaRainNoiseScale;
        public float betaDetailNoiseScale;
        
        public Map<String, ConfigClimateMapping> betaClimates;
        
        public Builder() {
            this(new NbtCompound());
        }
        
        public Builder(NbtCompound compound) {
            this.biomeProvider = NbtUtil.readString(NbtTags.BIOME_PROVIDER, compound, CONFIG.biomeProvider);
            this.singleBiome = NbtUtil.readString(NbtTags.SINGLE_BIOME, compound, CONFIG.singleBiome);
            this.replaceOceanBiomes = NbtUtil.readBoolean(NbtTags.REPLACE_OCEAN_BIOMES, compound, CONFIG.replaceOceanBiomes);
            
            this.betaTempNoiseScale = NbtUtil.readFloat(NbtTags.BETA_TEMP_NOISE_SCALE, compound, CONFIG.betaTempNoiseScale);
            this.betaRainNoiseScale = NbtUtil.readFloat(NbtTags.BETA_RAIN_NOISE_SCALE, compound, CONFIG.betaRainNoiseScale);
            this.betaDetailNoiseScale = NbtUtil.readFloat(NbtTags.BETA_DETAIL_NOISE_SCALE, compound, CONFIG.betaDetailNoiseScale);
            
            this.betaClimates = new LinkedHashMap<>();
            if (compound.contains(NbtTags.BIOMES)) {
                NbtCompound biomes = NbtUtil.readCompoundOrThrow(NbtTags.BIOMES, compound);
                
                biomes.getKeys().forEach(key -> {
                    this.betaClimates.put(key, ConfigClimateMapping.fromCompound(NbtUtil.readCompoundOrThrow(key, biomes)));
                });
                
            } else {
                this.betaClimates.putAll(CONFIG.betaClimates);
            }
        }
        
        public ModernBetaBiomeSettings build() {
            return new ModernBetaBiomeSettings(this);
        }
    }
}