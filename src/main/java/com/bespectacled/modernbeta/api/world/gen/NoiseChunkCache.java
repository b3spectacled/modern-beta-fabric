package com.bespectacled.modernbeta.api.world.gen;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.bespectacled.modernbeta.util.pool.DoubleArrayPool;
import com.bespectacled.modernbeta.util.function.TriConsumer;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.util.math.ChunkPos;

public class NoiseChunkCache {
    private final int capacity;
    private final DoubleArrayPool noisePool;
    private final Long2ObjectLinkedOpenHashMap<double[]> chunkCache;
    private final ReentrantReadWriteLock lock;

    public NoiseChunkCache(int capacity, int arraySize) {
        this.capacity = capacity;
        this.noisePool = new DoubleArrayPool((int)(capacity * 1.5), arraySize);
        this.chunkCache = new Long2ObjectLinkedOpenHashMap<>(capacity);
        this.lock = new ReentrantReadWriteLock();
    }
    
    public void clear() {
        this.lock.writeLock().lock();
        try {
            this.chunkCache.clear();
        } finally {
            this.lock.writeLock().unlock();
        }
    }
    
    public double[] get(int chunkX, int chunkZ, TriConsumer<Integer, Integer, double[]> noisePopulator) {
        long hashedCoord = ChunkPos.toLong(chunkX, chunkZ);
        
        double[] noise;
        
        this.lock.readLock().lock();
        try {
            noise = this.chunkCache.get(hashedCoord);
        } finally {
            this.lock.readLock().unlock();
        }
        
        if (noise == null) { 
            this.lock.writeLock().lock();
            try {
                // Grab noise array from pool and generate.
                noise = this.noisePool.borrowArr();
                noisePopulator.accept(chunkX, chunkZ, noise);
                
                // Ensure cache size remains below capacity,
                // return oldest noise array to pool if over.
                if (this.chunkCache.size() >= this.capacity) {
                    this.noisePool.returnArr(this.chunkCache.removeFirst());
                }
                
                this.chunkCache.put(hashedCoord, noise);
            } finally {
                this.lock.writeLock().unlock();
            }
        }
        
        return noise;
    }
}
