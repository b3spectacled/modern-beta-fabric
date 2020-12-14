package com.bespectacled.modernbeta.biome.release;

import com.bespectacled.modernbeta.biome.release.ReleaseBiomes.ReleaseBiome;

public class BiomeLayerSnow extends BiomeLayer {
    public BiomeLayerSnow(long seed, BiomeLayer parent) {
        super(seed);
        this.parent = parent;
    }

    public int[] sample(int x, int z, int sizeX, int sizeZ) {
        int parentX = x - 1;
        int parentZ = z - 1;
        int parentSizeX = sizeX + 2;
        int parentSizeZ = sizeZ + 2;
        
        int parentLayer[] = parent.sample(parentX, parentZ, parentSizeX, parentSizeZ);
        int cachedLayer[] = IntCache.getIntCache(sizeX * sizeZ);
        
        for(int sZ = 0; sZ < sizeZ; sZ++) {
            for(int sX = 0; sX < sizeX; sX++) {
                int center = parentLayer[sX + 1 + (sZ + 1) * parentSizeX];
                initChunkSeed(sX + x, sZ + z);
                
                if(center == 0) {
                    cachedLayer[sX + sZ * sizeX] = 0;
                    continue;
                }
                
                int biomeId = nextInt(5);
                if(biomeId == 0) {
                    biomeId = ReleaseBiome.SNOWY_TUNDRA.getId();
                } else {
                    biomeId = 1;
                }
                
                cachedLayer[sX + sZ * sizeX] = biomeId;
            }

        }

        return cachedLayer;
    }

}
