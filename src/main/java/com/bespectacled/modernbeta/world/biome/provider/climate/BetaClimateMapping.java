package com.bespectacled.modernbeta.world.biome.provider.climate;

import com.bespectacled.modernbeta.api.world.biome.climate.ClimateType;
import com.bespectacled.modernbeta.util.NbtCompoundBuilder;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class BetaClimateMapping {
    private final Identifier landBiome;
    private final Identifier oceanBiome;
    private final Identifier deepOceanBiome;
    
    public BetaClimateMapping(Identifier landBiome, Identifier oceanBiome, Identifier deepOceanBiome) {
        this.landBiome = landBiome;
        this.oceanBiome = oceanBiome;
        this.deepOceanBiome = deepOceanBiome;
    }
    
    public Identifier landBiome() {
        return this.landBiome;
    }
    
    public Identifier oceanBiome() {
        return this.oceanBiome;
    }
    
    public Identifier deepOceanBiome() {
        return this.deepOceanBiome;
    }
    
    public Identifier biomeByClimateType(ClimateType type) {
        return switch(type) {
            case LAND -> this.landBiome;
            case OCEAN -> this.oceanBiome;
            case DEEP_OCEAN -> this.deepOceanBiome;
        };
    }
    
    public static BetaClimateMapping fromCompound(NbtCompound compound) {
        return new BetaClimateMapping(
            new Identifier(NbtUtil.readStringOrThrow(NbtTags.LAND_BIOME, compound)),
            new Identifier(NbtUtil.readStringOrThrow(NbtTags.OCEAN_BIOME, compound)),
            new Identifier(NbtUtil.readStringOrThrow(NbtTags.DEEP_OCEAN_BIOME, compound))
        );
    }
    
    /*
     * Helper class solely so biome strings play nicely with Cloth Config
     * 
     */
    public static class Config {
        private String landBiome;
        private String oceanBiome;
        private String deepOceanBiome;
        
        public Config(String landBiome, String oceanBiome, String deepOceanBiome) {
            this.landBiome = landBiome;
            this.oceanBiome = oceanBiome;
            this.deepOceanBiome = deepOceanBiome;
        }
        
        public Config(String landBiome, String oceanBiome) {
            this(landBiome, oceanBiome, oceanBiome);
        }
        
        public NbtCompound toCompound() {
            return new NbtCompoundBuilder()
                .putString(NbtTags.LAND_BIOME, this.landBiome)
                .putString(NbtTags.OCEAN_BIOME, this.oceanBiome)
                .putString(NbtTags.DEEP_OCEAN_BIOME, this.deepOceanBiome)
                .build();
        }
    }
}
