package com.bespectacled.modernbeta.biome.release;

public class BiomeLayerInit extends BiomeLayer {
    public BiomeLayerInit(long seed) {
        super(seed);
    }

    public int[] sample(int x, int z, int sizeX, int sizeZ) {
        int cachedLayer[] = IntCache.getIntCache(sizeX * sizeZ);
        
        for(int sX = 0; sX < sizeZ; sX++) {
            for(int sZ = 0; sZ < sizeX; sZ++) {
                initChunkSeed(x + sZ, z + sX);
                cachedLayer[sZ + sX * sizeX] = nextInt(10) != 0 ? 0 : 1; // Ocean or plains
            }
        }

        if(x > -sizeX && x <= 0 && z > -sizeZ && z <= 0) {
            cachedLayer[-x + -z * sizeX] = 1;
        }
        
        return cachedLayer;
    }
}
