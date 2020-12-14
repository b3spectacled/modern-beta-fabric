package com.bespectacled.modernbeta.biome.release;

import com.bespectacled.modernbeta.biome.release.ReleaseBiomes.ReleaseBiome;

public class BiomeLayerMushroomIsland extends BiomeLayer {
    public BiomeLayerMushroomIsland(long seed, BiomeLayer parent) {
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
                int n = parentLayer[sX + 0 + (sZ + 0) * parentSizeX];
                int e = parentLayer[sX + 2 + (sZ + 0) * parentSizeX];
                int w = parentLayer[sX + 0 + (sZ + 2) * parentSizeX];
                int s = parentLayer[sX + 2 + (sZ + 2) * parentSizeX];
                int center = parentLayer[sX + 1 + (sZ + 1) * parentSizeX];
                
                initChunkSeed(sX + x, sZ + z);
                
                if(center == 0 && n == 0 && e == 0 && w == 0 && s == 0 && nextInt(100) == 0) {
                    cachedLayer[sX + sZ * sizeX] = ReleaseBiome.MUSHROOM_FIELDS.getId();
                } else {
                    cachedLayer[sX + sZ * sizeX] = center;
                }
            }

        }

        return cachedLayer;
    }
}
