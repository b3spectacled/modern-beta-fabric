package com.bespectacled.modernbeta.world.biome.beta;

import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.bespectacled.modernbeta.noise.SimplexOctaveNoise;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.util.math.MathHelper;

public enum BetaClimateSampler {
    INSTANCE;
    
    private final BiomeCache biomeCache;
    
    private SimplexOctaveNoise tempNoiseOctaves = new SimplexOctaveNoise(new Random(1 * 9871L), 4);
    private SimplexOctaveNoise humidNoiseOctaves = new SimplexOctaveNoise(new Random(1 * 39811L), 4);
    private SimplexOctaveNoise noiseOctaves = new SimplexOctaveNoise(new Random(1 * 543321L), 2);
    
    private long seed;
    
    private BetaClimateSampler() {
        this.biomeCache = new BiomeCache(this);
    }
    
    public void setSeed(long seed) {
        if (this.seed == seed) return;
        
        this.seed = seed;
        this.initNoise(seed);
        this.biomeCache.clear();
    }
    
    public double sampleTemp(int x, int z) {
        return this.biomeCache.getCachedChunk(x, z).sampleTempAtPoint(x, z);
    }
    
    public double sampleHumid(int x, int z) {
        return this.biomeCache.getCachedChunk(x, z).sampleHumidAtPoint(x, z);
    }
    
    public void sampleTempHumid(double[] arr, int x, int z) {
        this.biomeCache.getCachedChunk(x, z).sampleTempHumidAtPoint(arr, x, z);
    }
    
    public int getSkyColor(int x, int z) {
        float temp = (float)sampleSkyTemp(x, z);
        
        temp /= 3F;
        temp = MathHelper.clamp(temp, -1F, 1F);
        
        return MathHelper.hsvToRgb(0.6222222F - temp * 0.05F, 0.5F + temp * 0.1F, 1.0F);
    }
    
    private double sampleSkyTemp(int x, int z) {
        return this.biomeCache.getCachedChunk(x, z).sampleSkyTempAtPoint(x, z);
    }
    
    private void sampleTempHumidAtPoint(double arr[], int x, int z) {
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

        arr[0] = MathHelper.clamp(temp, 0.0, 1.0);
        arr[1] = MathHelper.clamp(humid, 0.0, 1.0);
    }
    
    private double sampleSkyTempAtPoint(int x, int z) {
        return this.tempNoiseOctaves.sample(x, z, 0.02500000037252903D, 0.02500000037252903D, 0.5D);
    }
    
    private void initNoise(long seed) {
        this.tempNoiseOctaves = new SimplexOctaveNoise(new Random(seed * 9871L), 4);
        this.humidNoiseOctaves = new SimplexOctaveNoise(new Random(seed * 39811L), 4);
        this.noiseOctaves = new SimplexOctaveNoise(new Random(seed * 543321L), 2);
    }
    
    private class BiomeCache {
        private final Long2ObjectLinkedOpenHashMap<BiomeCacheChunk> biomeCache;
        private final BetaClimateSampler climateSampler;
        private final ReentrantReadWriteLock lock;
        
        public BiomeCache(BetaClimateSampler climateSampler) {
            this.climateSampler = climateSampler;
            this.biomeCache = new Long2ObjectLinkedOpenHashMap<BiomeCacheChunk>(384);
            this.lock = new ReentrantReadWriteLock();
        }
        
        public void clear() {
            this.lock.writeLock().lock();
            try {
                this.biomeCache.clear();
            } finally {
                this.lock.writeLock().unlock();
            }
        }
        
        public BiomeCacheChunk getCachedChunk(int x, int z) {
            int chunkX = x >> 4;
            int chunkZ = z >> 4;
            
            long hashedCoord = (long)chunkX & 0xffffffffL | ((long)chunkZ & 0xffffffffL) << 32;
            BiomeCacheChunk cachedChunk;
            
            this.lock.readLock().lock();
            try {
                cachedChunk = this.biomeCache.get(hashedCoord);
            } finally {
                this.lock.readLock().unlock();
            }
            
            if (cachedChunk == null) { 
                this.lock.writeLock().lock();
                try {
                    cachedChunk = new BiomeCacheChunk(chunkX, chunkZ, this.climateSampler);
                    this.biomeCache.put(hashedCoord, cachedChunk);
                } finally {
                    this.lock.writeLock().unlock();
                }
                
            }
            
            return cachedChunk;
        }
    }
    
    private class BiomeCacheChunk {
        private final double temps[];
        private final double humids[];
        private final float skyTemps[]; // Lower precision, change back to double if there are issues.
        
        public BiomeCacheChunk(int chunkX, int chunkZ, BetaClimateSampler climateSampler) {
            this.temps = new double[256];
            this.humids = new double[256];
            this.skyTemps = new float[256];
            
            int startX = chunkX << 4;
            int startZ = chunkZ << 4;
            double[] tempHumid = new double[2];
            
            int ndx = 0;
            for (int z = startZ; z < startZ + 16; ++z) {
                for (int x = startX; x < startX + 16; ++x) {
                    climateSampler.sampleTempHumidAtPoint(tempHumid, x, z);
                    
                    temps[ndx] = tempHumid[0];
                    humids[ndx] = tempHumid[1];
                    skyTemps[ndx] = (float)climateSampler.sampleSkyTempAtPoint(x, z);

                    ndx++;
                }
            }
        }
        
        public double sampleTempAtPoint(int x, int z) {
            return temps[x & 0xF | (z & 0xF) << 4];
        }
        
        public double sampleHumidAtPoint(int x, int z) {
            return humids[x & 0xF | (z & 0xF) << 4];
        }
        
        public void sampleTempHumidAtPoint(double[] tempHumid, int x, int z) {
            tempHumid[0] = temps[x & 0xF | (z & 0xF) << 4];
            tempHumid[1] = humids[x & 0xF | (z & 0xF) << 4];
        }
        
        public double sampleSkyTempAtPoint(int x, int z) {
            return skyTemps[x & 0xF | (z & 0xF) << 4];
        }
        
    }
    
}
