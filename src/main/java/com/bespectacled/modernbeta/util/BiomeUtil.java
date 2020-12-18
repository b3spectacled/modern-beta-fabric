package com.bespectacled.modernbeta.util;

import java.util.Map;
import java.util.Random;
import com.bespectacled.modernbeta.noise.SimplexOctaveNoise;

import net.minecraft.util.math.MathHelper;

/**
 * Author: WorldEdit
 */
public class BiomeUtil {
    private static final int HORIZONTAL_SECTION_COUNT = (int) Math.round(Math.log(16.0D) / Math.log(2.0D)) - 2;
    private static final int VERTICAL_SECTION_COUNT = (int) Math.round(Math.log(256.0D) / Math.log(2.0D)) - 2;
    private static final int HORIZONTAL_BIT_MASK = (1 << HORIZONTAL_SECTION_COUNT) - 1;
    private static final int VERTICAL_BIT_MASK = (1 << VERTICAL_SECTION_COUNT) - 1;

    private static SimplexOctaveNoise tempNoiseOctaves = new SimplexOctaveNoise(new Random(1 * 9871L), 4);
    private static SimplexOctaveNoise humidNoiseOctaves = new SimplexOctaveNoise(new Random(1 * 39811L), 4);
    private static SimplexOctaveNoise noiseOctaves = new SimplexOctaveNoise(new Random(1 * 543321L), 2);

    private static final Map<Long, BiomeCacheChunk> BIOME_CACHE = new BoundedHashMap<Long, BiomeCacheChunk>(1024); // 32 x 32 chunks

    // Convert absolute coordinates to BiomeArray index
    public static int computeBiomeIndex(int x, int y, int z) {
        int l = (x >> 2) & HORIZONTAL_BIT_MASK;
        int m = MathHelper.clamp(y >> 2, 0, VERTICAL_BIT_MASK);
        int n = (z >> 2) & HORIZONTAL_BIT_MASK;

        return m << HORIZONTAL_SECTION_COUNT + HORIZONTAL_SECTION_COUNT | n << HORIZONTAL_SECTION_COUNT | l;
    }

    public static void setSeed(long seed) {
        initOctaves(seed);
        BIOME_CACHE.clear();
    }

    private static void initOctaves(long seed) {
        tempNoiseOctaves = new SimplexOctaveNoise(new Random(seed * 9871L), 4);
        humidNoiseOctaves = new SimplexOctaveNoise(new Random(seed * 39811L), 4);
        noiseOctaves = new SimplexOctaveNoise(new Random(seed * 543321L), 2);
    }
    
    public static void sampleTempHumid(double[] arr, int x, int z) {
        BiomeCacheChunk cachedChunk = getCachedChunk(x, z);
        
        cachedChunk.sampleTempHumidAtPoint(arr, x, z);
    }
    
    public static void sampleTempHumid(int x, int z, double[] temps, double[] humids) {
        int startX = (x >> 4) << 4;
        int startZ = (z >> 4) << 4;
        
        double tempHumid[] = new double[2];
        
        BiomeCacheChunk cachedChunk = getCachedChunk(x, z);
        
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
    
    public static void sampleTempHumidAtPoint(double arr[], int x, int z) {
        double temp = tempNoiseOctaves.sample(x, z, 0.02500000037252903D, 0.02500000037252903D, 0.25D);
        double humid = humidNoiseOctaves.sample(x, z, 0.05000000074505806D, 0.05000000074505806D, 0.33333333333333331D);
        double noise = noiseOctaves.sample(x, z, 0.25D, 0.25D, 0.58823529411764708D);

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

    public static double sampleSkyTemp(int x, int z) {
        BiomeCacheChunk cachedChunk = getCachedChunk(x, z);
        
        return cachedChunk.sampleSkyTempAtPoint(x, z);
    }
    
    public static double sampleSkyTempAtPoint(int x, int z) {
        return tempNoiseOctaves.sample(x, z, 0.02500000037252903D, 0.02500000037252903D, 0.5D);
    }
    
    // Synchronized needed to prevent crash when more than one thread attempts to modify map, I think
    private static synchronized BiomeCacheChunk getCachedChunk(int x, int z) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        
        long hashedCoord = (long)chunkX & 0xffffffffL | ((long)chunkZ & 0xffffffffL) << 32;
        BiomeCacheChunk cachedChunk = BIOME_CACHE.get(hashedCoord);
        
        if (cachedChunk == null) { 
            cachedChunk = new BiomeCacheChunk(chunkX, chunkZ);
            BIOME_CACHE.put(hashedCoord, cachedChunk);
        }
        
        return cachedChunk;
    }
}