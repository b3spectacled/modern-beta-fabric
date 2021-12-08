package com.bespectacled.modernbeta.world.biome.provider.climate;

import com.bespectacled.modernbeta.api.world.biome.climate.Clime;
import com.bespectacled.modernbeta.util.NbtCompoundBuilder;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class VoronoiClimateMapping extends BetaClimateMapping {
    public static final VoronoiClimateMapping DEFAULT = new VoronoiClimateMapping(
        new Identifier("plains"),
        new Identifier("plains"),
        new Identifier("plains"),
        new Clime(0.5, 0.5)
    );
    
    private final Clime clime;

    public VoronoiClimateMapping(Identifier landBiome, Identifier oceanBiome, Identifier deepOceanBiome, Clime clime) {
        super(landBiome, oceanBiome, deepOceanBiome);
        
        this.clime = clime;
    }
    
    public Clime clime() {
        return this.clime;
    }
    
    public double calculateDistanceTo(Clime other) {
        return
            (this.clime.temp() - other.temp()) * (this.clime.temp() - other.temp()) +
            (this.clime.rain() - other.rain()) * (this.clime.rain() - other.rain());
    }
    
    public static VoronoiClimateMapping fromCompound(NbtCompound compound) {
        return new VoronoiClimateMapping(
            new Identifier(NbtUtil.readStringOrThrow(NbtTags.LAND_BIOME, compound)),
            new Identifier(NbtUtil.readStringOrThrow(NbtTags.OCEAN_BIOME, compound)),
            new Identifier(NbtUtil.readStringOrThrow(NbtTags.DEEP_OCEAN_BIOME, compound)),
            new Clime(NbtUtil.readDoubleOrThrow(NbtTags.TEMP, compound), NbtUtil.readDoubleOrThrow(NbtTags.RAIN, compound))
        );
    }
    
    public static class Config extends BetaClimateMapping.Config {
        private double temp;
        private double rain;
        
        public Config(String landBiome, String oceanBiome, String deepOceanBiome, double temp, double rain) {
            super(landBiome, oceanBiome, deepOceanBiome);
            
            this.temp = temp;
            this.rain = rain;
        }
        
        public Config(String landBiome, String oceanBiome, double temp, double rain) {
            this(landBiome, oceanBiome, oceanBiome, temp, rain);
        }
        
        @Override
        public NbtCompound toCompound() {
            return new NbtCompoundBuilder(super.toCompound())
                .putDouble(NbtTags.TEMP, this.temp)
                .putDouble(NbtTags.RAIN, this.rain)
                .build();
        }
    }
}
