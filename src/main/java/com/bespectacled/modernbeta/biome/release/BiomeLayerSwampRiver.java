package com.bespectacled.modernbeta.biome.release;

import com.bespectacled.modernbeta.biome.release.ReleaseBiomes.ReleaseBiome;

public class BiomeLayerSwampRiver extends BiomeLayer {
    public BiomeLayerSwampRiver(long seed, BiomeLayer parent) {
        super(seed);
        this.parent = parent;
    }

    public int[] sample(int x, int z, int sizeX, int sizeZ) {
        int parentLayer[] = parent.sample(x - 1, z - 1, sizeX + 2, sizeZ + 2);
        int cachedLayer[] = IntCache.getIntCache(sizeX * sizeZ);
        
        for (int sZ = 0; sZ < sizeZ; sZ++) {
            for (int sX = 0; sX < sizeX; sX++) {
                initChunkSeed(sX + x, sZ + z);
                int center = parentLayer[sX + 1 + (sZ + 1) * (sizeX + 2)];
                
                if (center == ReleaseBiome.SWAMP.getId() && nextInt(6) == 0) {
                    cachedLayer[sX + sZ * sizeX] = ReleaseBiome.RIVER.getId();
                } else {
                    cachedLayer[sX + sZ * sizeX] = center;
                }
            }
        }

        return cachedLayer;
    }

}
