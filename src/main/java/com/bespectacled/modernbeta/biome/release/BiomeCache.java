package com.bespectacled.modernbeta.biome.release;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bespectacled.modernbeta.biome.provider.AbstractBiomeProvider;
import com.bespectacled.modernbeta.biome.provider.ReleaseBiomeProvider;
import com.bespectacled.modernbeta.biome.release.ReleaseBiomes.ReleaseBiome;
import com.bespectacled.modernbeta.util.BoundedHashMap;

import net.minecraft.util.math.BlockPos;

public class BiomeCache {
    private final ReleaseBiomeProvider biomeProvider;
    private final Map<Long, BiomeCacheBlock> cache;
    
    public BiomeCache(ReleaseBiomeProvider biomeProvider) {
        this.biomeProvider = biomeProvider;
        this.cache = new BoundedHashMap<Long, BiomeCacheBlock>(256);
    }
    
    public BiomeCacheBlock getBiomeCacheBlock(int x, int z) {
        x >>= 4; // Convert to chunk coordinates
        z >>= 4;
        
        long hashedCoord = (long)x & 0xffffffffL | ((long)z & 0xffffffffL) << 32;
        BiomeCacheBlock cachedBlock = this.cache.get(hashedCoord);
        
        if (cachedBlock == null) {
            cachedBlock = new BiomeCacheBlock(this, x, z);
            
            cache.put(hashedCoord, cachedBlock);
        }
        
        return cachedBlock;
    }

    public ReleaseBiome getBiomeGenAt(int x, int z) {
        return getBiomeCacheBlock(x, z).getBiomeGenAt(x, z);
    }
    
    public ReleaseBiome[] getCachedBiomes(int x, int z) {
        return getBiomeCacheBlock(x, z).biomes;
    }
    
    private class BiomeCacheBlock {
        private ReleaseBiome biomes[];
        private final BiomeCache biomeCache;
        private final int chunkX;
        private final int chunkZ;
        
        public BiomeCacheBlock(BiomeCache biomeCache, int chunkX, int chunkZ) {
            this.biomeCache = biomeCache;
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
            this.biomes = new ReleaseBiome[256];
            
            this.biomeCache.biomeProvider.getBiomeGenAt(biomes, this.chunkX << 4, this.chunkZ << 4, 16, 16, false);
        }
        
        public ReleaseBiome getBiomeGenAt(int x, int z) {
            return biomes[x & 0xF | (z & 0xF) << 4];
        }
    }
}
