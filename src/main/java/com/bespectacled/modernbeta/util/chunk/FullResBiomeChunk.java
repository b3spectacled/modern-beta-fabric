package com.bespectacled.modernbeta.util.chunk;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class FullResBiomeChunk {
    private final List<RegistryEntry<Biome>> biomes = new ArrayList<>(256);
    
    public FullResBiomeChunk(int chunkX, int chunkZ, BiFunction<Integer, Integer, RegistryEntry<Biome>> chunkFunc) {
        int startX = chunkX << 4;
        int startZ = chunkZ << 4;
        
        int ndx = 0;
        for (int x = startX; x < startX + 16; ++x) {
            for (int z = startZ; z < startZ + 16; ++z) {
                this.biomes.add(ndx, chunkFunc.apply(x, z));
                ndx++;
            }
        }
    }
    
    public RegistryEntry<Biome> sampleBiome(int x, int z) {
        return this.biomes.get((z & 0xF) + (x & 0xF) * 16);
    }
}
