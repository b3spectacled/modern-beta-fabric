package com.bespectacled.modernbeta.util.chunk;

import java.util.concurrent.locks.StampedLock;
import java.util.function.BiFunction;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.util.math.ChunkPos;

/*
 * Generic threadsafe(???) cache for anything that outputs T given pair of integer chunk coordinates.
 * 
 */
public class ChunkCache<T> {
    @SuppressWarnings("unused")
    private final String name;
    private final int capacity;
    private final boolean evictOldChunks;
    
    private final BiFunction<Integer, Integer, T> chunkFunc;
    private final Long2ObjectLinkedOpenHashMap<T> chunkMap;
    
    private final StampedLock lock;
    
    public ChunkCache(String name, int capacity, boolean evictOldChunks, BiFunction<Integer, Integer, T> chunkFunc) {
        this.name = name;
        this.capacity = capacity;
        this.evictOldChunks = evictOldChunks;
        
        this.chunkFunc = chunkFunc;
        this.chunkMap = new Long2ObjectLinkedOpenHashMap<>(capacity);
        
        this.lock = new StampedLock();
    }
    
    public ChunkCache(String name, int capacity, BiFunction<Integer, Integer, T> chunkFunc) {
        this(name, capacity, true, chunkFunc);
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
        T chunk;
        long stamp;
        
        long key = ChunkPos.toLong(chunkX, chunkZ);
        
        stamp = this.lock.readLock();
        try {
            chunk = this.chunkMap.get(key);
        } finally {
            this.lock.unlockRead(stamp);
        }
        
        if (chunk == null) { 
            stamp = this.lock.writeLock();
            try {
                chunk = this.chunkFunc.apply(chunkX, chunkZ);
                
                // Ensure cache size remains below capacity
                if (this.evictOldChunks && this.chunkMap.size() >= this.capacity) {
                    this.chunkMap.removeFirst();
                }
                
                this.chunkMap.put(key, chunk);
            } finally {
                this.lock.unlockWrite(stamp);
            }
        }
        
        return chunk;
    }
}
