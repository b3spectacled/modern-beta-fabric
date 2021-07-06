package com.bespectacled.modernbeta.util;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
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
    
    private final ReentrantReadWriteLock lock;
    private final WriteLock writeLock;
    private final ReadLock readLock;

    private int hits;
    private int misses;
    private boolean debug;
    
    public ChunkCache(String name, int capacity, boolean evictOldChunks, BiFunction<Integer, Integer, T> chunkFunc, boolean debug) {
        this.name = name;
        this.capacity = capacity;
        this.evictOldChunks = evictOldChunks;
        this.chunkFunc = chunkFunc;
        this.chunkMap = new Long2ObjectLinkedOpenHashMap<>(capacity);
        
        this.lock = new ReentrantReadWriteLock();
        this.writeLock = this.lock.writeLock();
        this.readLock = this.lock.readLock();
        
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
        this.writeLock.lock();
        try {
            this.chunkMap.clear();
            this.chunkMap.trim();
        } finally {
            this.writeLock.unlock();
        }
    }
    
    public T get(int chunkX, int chunkZ) {
        long hashedCoord = ChunkPos.toLong(chunkX, chunkZ);
        T item;
        
        this.readLock.lock();
        try {
            item = this.chunkMap.get(hashedCoord);
        } finally {
            this.readLock.unlock();
        }
        
        if (item == null) { 
            this.writeLock.lock();
            try {
                item = this.chunkFunc.apply(chunkX, chunkZ);
                
                // Ensure cache size remains below capacity
                if (this.evictOldChunks && this.chunkMap.size() >= this.capacity) {
                    this.chunkMap.removeFirst();
                }
                
                this.chunkMap.put(hashedCoord, item);
            } finally {
                this.writeLock.unlock();
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
