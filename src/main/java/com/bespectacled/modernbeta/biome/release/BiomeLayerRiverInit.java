package com.bespectacled.modernbeta.biome.release;

public class BiomeLayerRiverInit extends BiomeLayer {
    public BiomeLayerRiverInit(long seed, BiomeLayer parent) {
        super(seed);
        this.parent = parent;
    }

    public int[] sample(int x, int z, int sizeX, int sizeZ) {
        int parentLayer[] = parent.sample(x, z, sizeX, sizeZ);
        int cachedLayer[] = IntCache.getIntCache(sizeX * sizeZ);
        
        for(int sZ = 0; sZ < sizeZ; sZ++) {
            for(int sX = 0; sX < sizeX; sX++) {
                initChunkSeed(sX + x, sZ + z);
                cachedLayer[sX + sZ * sizeX] = parentLayer[sX + sZ * sizeX] <= 0 ? 0 : nextInt(2) + 2;
            }
        }

        return cachedLayer;
    }
}
