package com.bespectacled.modernbeta.biome;

import java.util.Map;

import com.bespectacled.modernbeta.util.BoundedHashMap;

public class BiomeCache {
    private final Map<Long, BiomeCacheChunk> biomeCache;
    private final BetaClimateSampler climateSampler;
    
    public BiomeCache(BetaClimateSampler climateSampler) {
        this.climateSampler = climateSampler;
        this.biomeCache = new BoundedHashMap<Long, BiomeCacheChunk>(1024); // 32 x 32 chunks
    }
    
    public void clear() {
        this.biomeCache.clear();
    }
    
    // Synchronized needed to prevent crash when more than one thread attempts to modify map, I think
    public synchronized BiomeCacheChunk getCachedChunk(int x, int z) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        
        long hashedCoord = (long)chunkX & 0xffffffffL | ((long)chunkZ & 0xffffffffL) << 32;
        BiomeCacheChunk cachedChunk = this.biomeCache.get(hashedCoord);
        
        if (cachedChunk == null) { 
            cachedChunk = new BiomeCacheChunk(chunkX, chunkZ, this.climateSampler);
            this.biomeCache.put(hashedCoord, cachedChunk);
        }
        
        return cachedChunk;
    }
}

