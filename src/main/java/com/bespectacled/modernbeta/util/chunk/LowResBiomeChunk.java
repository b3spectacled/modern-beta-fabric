package com.bespectacled.modernbeta.util.chunk;

import java.util.function.BiFunction;

import net.minecraft.world.biome.Biome;

public class LowResBiomeChunk {
    private final Biome[] biomes = new Biome[16];
    
    public LowResBiomeChunk(int chunkX, int chunkZ, BiFunction<Integer, Integer, Biome> chunkFunc) {
        int startBiomeX = chunkX << 2;
        int startBiomeZ = chunkZ << 2;
        
        int ndx = 0;
        for (int biomeX = startBiomeX; biomeX < startBiomeX + 4; ++biomeX) {
            for (int biomeZ = startBiomeZ; biomeZ < startBiomeZ + 4; ++biomeZ) {
                this.biomes[ndx++] = chunkFunc.apply(biomeX, biomeZ);
            }
        }
    }
    
    public Biome sampleBiome(int biomeX, int biomeZ) {
        return this.biomes[(biomeZ & 0x3) + (biomeX & 0x3) * 4];
    }
}
