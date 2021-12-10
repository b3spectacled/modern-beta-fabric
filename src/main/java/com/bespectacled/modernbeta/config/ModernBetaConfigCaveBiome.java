package com.bespectacled.modernbeta.config;

import java.util.List;

import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.util.NbtCompoundBuilder;
import com.bespectacled.modernbeta.util.NbtTags;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.biome.BiomeKeys;

@Config(name = "config_cave_biome")
public class ModernBetaConfigCaveBiome implements ConfigData {
    // General
    public String caveBiomeType = BuiltInTypes.CaveBiome.SINGLE.name;
    
    // Single
    public String singleBiome = BiomeKeys.LUSH_CAVES.getValue().toString();
    
    // Voronoi
    public int horizontalNoiseScale = 32;
    public int verticalNoiseScale = 16;
    public List<CaveBiomeVoronoiPoint> voronoiPoints = List.of(
        new CaveBiomeVoronoiPoint("null", 0.0, 0.0),
        new CaveBiomeVoronoiPoint("minecraft:lush_caves", 0.1, 0.0),
        new CaveBiomeVoronoiPoint("null", 0.5, 0.0),
        new CaveBiomeVoronoiPoint("minecraft:dripstone_caves", 0.9, 0.0),
        new CaveBiomeVoronoiPoint("null", 0.9, 0.0)
    );
    
    public static class CaveBiomeVoronoiPoint {
        public String biome;
        public double temp;
        public double rain;
        
        public CaveBiomeVoronoiPoint(String biome, double temp, double rain) {
            this.biome = biome;
            this.temp = temp;
            this.rain = rain;
        }
        
        public NbtCompound toCompound() {
            return new NbtCompoundBuilder()
                .putString(NbtTags.BIOME, this.biome)
                .putDouble(NbtTags.TEMP, this.temp)
                .putDouble(NbtTags.RAIN, this.rain)
                .build();
        }
    }
}
