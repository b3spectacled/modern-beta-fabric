package com.bespectacled.modernbeta.util;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiFunction;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.util.math.ChunkPos;

/*
 * Generic threadsafe(???) cache for anything that outputs T given pair of integer chunk coordinates.
 * 
 */
public class ChunkCache<T> {
    private final String name;
    private final int capacity;
    private final boolean evictOldChunks;
    private final BiFunction<Integer, Integer, T> chunkFunc;
    private final Long2ObjectLinkedOpenHashMap<T> cache;
    private final ReentrantReadWriteLock lock;

    private int hits;
    private int misses;
    private boolean debug;
    
    public ChunkCache(String name, int capacity, boolean evictOldChunks, BiFunction<Integer, Integer, T> chunkFunc, boolean debug) {
        this.name = name;
        this.capacity = capacity;
        this.evictOldChunks = evictOldChunks;
        this.chunkFunc = chunkFunc;
        this.cache = new Long2ObjectLinkedOpenHashMap<>(capacity);
        this.lock = new ReentrantReadWriteLock();
        
        this.hits = 0;
        this.misses = 0;
        this.debug = debug;
    }
    
    public ChunkCache(String name, int capacity, boolean evictOldChunks, BiFunction<Integer, Integer, T> chunkFunc) {
        this(name, capacity, evictOldChunks, chunkFunc, false);
    }
    
    public ChunkCache(String name, int capacity, BiFunction<Integer, Integer, T> chunkFunc) {
        this(name, capacity, true, chunkFunc, false);
    }
    
    public void clear() {
        this.lock.writeLock().lock();
        try {
            this.cache.clear();
            this.cache.trim();
        } finally {
            this.lock.writeLock().unlock();
        }
    }
    
    public T get(int chunkX, int chunkZ) {
        long hashedCoord = ChunkPos.toLong(chunkX, chunkZ);
        T item;
        
        this.lock.readLock().lock();
        try {
            item = this.cache.get(hashedCoord);
        } finally {
            this.lock.readLock().unlock();
        }
        
        if (item == null) { 
            this.lock.writeLock().lock();
            try {
                item = this.chunkFunc.apply(chunkX, chunkZ);
                
                // Ensure cache size remains below capacity
                if (this.evictOldChunks && this.cache.size() >= this.capacity) {
                    this.cache.removeFirst();
                }
                
                this.cache.put(hashedCoord, item);
            } finally {
                this.lock.writeLock().unlock();
            }
            
            misses++;
        } else {
            hits++;
        }
        
        if (this.debug == true && hits != 0 && hits % 512 == 0) {
            float hitMissRate = hits / (float)(hits + misses) * 100F;
            ModernBeta.log(Level.INFO, String.format("Cache '%s' hit/miss rate: %.2f", this.name, hitMissRate));
        }
        
        return item;
    }
}
