package com.bespectacled.modernbeta.util.chunk;

import java.util.function.BiFunction;

import net.minecraft.world.biome.Biome;

public class BiomeChunk {
    private final Biome[] biomes = new Biome[256];
    
    public BiomeChunk(int chunkX, int chunkZ, BiFunction<Integer, Integer, Biome> chunkFunc) {
        int startX = chunkX << 4;
        int startZ = chunkZ << 4;
        
        int ndx = 0;
        for (int x = startX; x < startX + 16; ++x) {
            for (int z = startZ; z < startZ + 16; ++z) {
                this.biomes[ndx++] = chunkFunc.apply(x, z);
            }
        }
    }
    
    public Biome sampleBiome(int x, int z) {
        return this.biomes[(z & 0xF) + (x & 0xF) * 16];
    }
}