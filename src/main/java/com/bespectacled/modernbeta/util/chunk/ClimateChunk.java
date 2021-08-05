package com.bespectacled.modernbeta.util.chunk;

import java.util.function.BiFunction;

import com.bespectacled.modernbeta.api.world.biome.climate.Clime;

public class ClimateChunk {
    private final double temp[] = new double[256];
    private final double rain[] = new double[256];
    
    public ClimateChunk(int chunkX, int chunkZ, BiFunction<Integer, Integer, Clime> chunkFunc) {
        int startX = chunkX << 4;
        int startZ = chunkZ << 4;
        
        int ndx = 0;
        for (int x = startX; x < startX + 16; ++x) {
            for (int z = startZ; z < startZ + 16; ++z) {
                Clime clime = chunkFunc.apply(x, z);
                
                this.temp[ndx] = clime.temp();
                this.rain[ndx] = clime.rain();

                ndx++;
            }
        }
    }
    
    public double sampleTemp(int x, int z) {
        return temp[(z & 0xF) + (x & 0xF) * 16];
    }
    
    public double sampleRain(int x, int z) {
        return rain[(z & 0xF) + (x & 0xF) * 16];
    }
}
