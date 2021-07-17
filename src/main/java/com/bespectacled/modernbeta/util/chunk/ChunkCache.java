package com.bespectacled.modernbeta.util.chunk;

import java.util.concurrent.locks.StampedLock;
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
    private final Long2ObjectLinkedOpenHashMap<T> chunkMap;
    
    private final StampedLock lock;

    private int hits;
    private int misses;
    private boolean debug;
    
    public ChunkCache(String name, int capacity, boolean evictOldChunks, BiFunction<Integer, Integer, T> chunkFunc, boolean debug) {
        this.name = name;
        this.capacity = capacity;
        this.evictOldChunks = evictOldChunks;
        this.chunkFunc = chunkFunc;
        this.chunkMap = new Long2ObjectLinkedOpenHashMap<>(capacity);
        
        this.lock = new StampedLock();
        
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
        long stamp = this.lock.writeLock();
        try {
            this.chunkMap.clear();
            this.chunkMap.trim();
        } finally {
            this.lock.unlock(stamp);
        }
    }
    
    public T get(int chunkX, int chunkZ) {
        T item;
        long stamp;
        
        long key = ChunkPos.toLong(chunkX, chunkZ);
        
        stamp = this.lock.readLock();
        try {
            item = this.chunkMap.get(key);
        } finally {
            this.lock.unlockRead(stamp);
        }
        
        if (item == null) { 
            stamp = this.lock.writeLock();
            try {
                item = this.chunkFunc.apply(chunkX, chunkZ);
                
                // Ensure cache size remains below capacity
                if (this.evictOldChunks && this.chunkMap.size() >= this.capacity) {
                    this.chunkMap.removeFirst();
                }
                
                this.chunkMap.put(key, item);
            } finally {
                this.lock.unlockWrite(stamp);
            }
            
            misses++;
        } else {
            hits++;
        }
        
        if (this.debug && hits % 512 == 0) {
            float hitMissRate = hits / (float)(hits + misses) * 100F;
            ModernBeta.log(Level.INFO, String.format("Cache '%s' hit/miss rate: %.2f", this.name, hitMissRate));
        }
        
        return item;
    }
}
