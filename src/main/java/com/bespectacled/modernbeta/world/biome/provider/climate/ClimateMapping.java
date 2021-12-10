package com.bespectacled.modernbeta.world.biome.provider.climate;

import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class ClimateMapping {
    public enum ClimateType {
        LAND,
        OCEAN,
        DEEP_OCEAN
    }
    
    private final Identifier biome;
    private final Identifier oceanBiome;
    private final Identifier deepOceanBiome;
    
    public ClimateMapping(Identifier biome, Identifier oceanBiome, Identifier deepOceanBiome) {
        this.biome = biome;
        this.oceanBiome = oceanBiome;
        this.deepOceanBiome = deepOceanBiome;
    }
    
    public Identifier biome() {
        return this.biome;
    }
    
    public Identifier oceanBiome() {
        return this.oceanBiome;
    }
    
    public Identifier deepOceanBiome() {
        return this.deepOceanBiome;
    }
    
    public Identifier biomeByClimateType(ClimateType type) {
        return switch(type) {
            case LAND -> this.biome;
            case OCEAN -> this.oceanBiome;
            case DEEP_OCEAN -> this.deepOceanBiome;
        };
    }
    
    public static ClimateMapping fromCompound(NbtCompound compound) {
        return new ClimateMapping(
            new Identifier(NbtUtil.readStringOrThrow(NbtTags.BIOME, compound)),
            new Identifier(NbtUtil.readStringOrThrow(NbtTags.OCEAN_BIOME, compound)),
            new Identifier(NbtUtil.readStringOrThrow(NbtTags.DEEP_OCEAN_BIOME, compound))
        );
    }
}
