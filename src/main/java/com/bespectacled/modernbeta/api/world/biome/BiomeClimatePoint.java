package com.bespectacled.modernbeta.api.world.biome;

import com.bespectacled.modernbeta.api.world.biome.climate.ClimateType;
import com.bespectacled.modernbeta.util.NbtCompoundBuilder;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;

import net.minecraft.nbt.NbtCompound;

public class BiomeClimatePoint {
    private String landBiome;
    private String oceanBiome;
    private String deepOceanBiome;
    
    public BiomeClimatePoint(String landBiome, String oceanBiome, String deepOceanBiome) {
        this.landBiome = landBiome;
        this.oceanBiome = oceanBiome;
        this.deepOceanBiome = deepOceanBiome;
    }
    
    public BiomeClimatePoint(String landBiome, String oceanBiome) {
        this(landBiome, oceanBiome, oceanBiome);
    }
    
    public String landBiome() {
        return this.landBiome;
    }
    
    public String oceanBiome() {
        return this.oceanBiome;
    }
    
    public String deepOceanBiome() {
        return this.deepOceanBiome;
    }
    
    public String getByClimate(ClimateType type) {
        return switch (type) {
            case LAND -> this.landBiome;
            case OCEAN -> this.oceanBiome;
            case DEEP_OCEAN -> this.oceanBiome;
        };
    }
    
    public NbtCompound toCompound() {
        return new NbtCompoundBuilder()
            .putString(NbtTags.LAND_BIOME, this.landBiome)
            .putString(NbtTags.OCEAN_BIOME, this.oceanBiome)
            .putString(NbtTags.DEEP_OCEAN_BIOME, this.deepOceanBiome)
            .build();
    }
    
    public static BiomeClimatePoint fromCompound(NbtCompound compound) {
        return new BiomeClimatePoint(
            NbtUtil.readStringOrThrow(NbtTags.LAND_BIOME, compound),
            NbtUtil.readStringOrThrow(NbtTags.OCEAN_BIOME, compound),
            NbtUtil.readStringOrThrow(NbtTags.DEEP_OCEAN_BIOME, compound)
        );
    }
}
