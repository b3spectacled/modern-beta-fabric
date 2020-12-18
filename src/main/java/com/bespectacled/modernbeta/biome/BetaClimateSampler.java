package com.bespectacled.modernbeta.biome;

import java.util.Random;

import com.bespectacled.modernbeta.noise.SimplexOctaveNoise;

public class BetaClimateSampler {
    private static final BetaClimateSampler INSTANCE = new BetaClimateSampler();
    private static final BiomeCache BIOME_CACHE = new BiomeCache(INSTANCE);
    
    private SimplexOctaveNoise tempNoiseOctaves = new SimplexOctaveNoise(new Random(1 * 9871L), 4);
    private SimplexOctaveNoise humidNoiseOctaves = new SimplexOctaveNoise(new Random(1 * 39811L), 4);
    private SimplexOctaveNoise noiseOctaves = new SimplexOctaveNoise(new Random(1 * 543321L), 2);
    
    private long seed;
    
    private BetaClimateSampler() {}
    
    public static BetaClimateSampler getInstance() {
        return INSTANCE;
    }
    
    public void setSeed(long seed) {
        if (this.seed == seed) return;
        
        this.seed = seed;
        initOctaves(seed);
        BIOME_CACHE.clear();
    }
    
    private void initOctaves(long seed) {
        this.tempNoiseOctaves = new SimplexOctaveNoise(new Random(seed * 9871L), 4);
        this.humidNoiseOctaves = new SimplexOctaveNoise(new Random(seed * 39811L), 4);
        this.noiseOctaves = new SimplexOctaveNoise(new Random(seed * 543321L), 2);
    }
    
    public void sampleTempHumid(double[] arr, int x, int z) {
        BiomeCacheChunk cachedChunk = BIOME_CACHE.getCachedChunk(x, z);
        
        cachedChunk.sampleTempHumidAtPoint(arr, x, z);
    }
    
    public void sampleTempHumid(int x, int z, double[] temps, double[] humids) {
        int startX = (x >> 4) << 4;
        int startZ = (z >> 4) << 4;
        
        double tempHumid[] = new double[2];
        
        BiomeCacheChunk cachedChunk = BIOME_CACHE.getCachedChunk(x, z);
        
        int ndx = 0;
        for (int relX = 0; relX < 16; relX++) {
            for (int relZ = 0; relZ < 16; relZ++) {
                cachedChunk.sampleTempHumidAtPoint(tempHumid, startX + relX, startZ + relZ);
                
                temps[ndx] = tempHumid[0];
                humids[ndx] = tempHumid[1];
                
                ndx++;
            }
        }
    }
    
    public double sampleSkyTemp(int x, int z) {
        BiomeCacheChunk cachedChunk = BIOME_CACHE.getCachedChunk(x, z);
        
        return cachedChunk.sampleSkyTempAtPoint(x, z);
    }
    
    public void sampleTempHumidAtPoint(double arr[], int x, int z) {
        double temp  = this.tempNoiseOctaves.sample(x, z, 0.02500000037252903D, 0.02500000037252903D, 0.25D);
        double humid = this.humidNoiseOctaves.sample(x, z, 0.05000000074505806D, 0.05000000074505806D, 0.33333333333333331D);
        double noise = this.noiseOctaves.sample(x, z, 0.25D, 0.25D, 0.58823529411764708D);

        double d = noise * 1.1000000000000001D + 0.5D;
        double d1 = 0.01D;
        double d2 = 1.0D - d1;

        temp = (temp * 0.14999999999999999D + 0.69999999999999996D) * d2 + d * d1;

        d1 = 0.002D;
        d2 = 1.0D - d1;

        humid = (humid * 0.14999999999999999D + 0.5D) * d2 + d * d1;

        temp = 1.0D - (1.0D - temp) * (1.0D - temp);
        
        if (temp < 0.0D) {
            temp = 0.0D;
        }
        if (humid < 0.0D) {
            humid = 0.0D;
        }
        if (temp > 1.0D) {
            temp = 1.0D;
        }
        if (humid > 1.0D) {
            humid = 1.0D;
        }

        arr[0] = temp;
        arr[1] = humid;
    }
    
    public double sampleSkyTempAtPoint(int x, int z) {
        return this.tempNoiseOctaves.sample(x, z, 0.02500000037252903D, 0.02500000037252903D, 0.5D);
    }
    
}
