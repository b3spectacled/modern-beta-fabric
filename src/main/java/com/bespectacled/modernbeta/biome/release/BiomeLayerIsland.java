package com.bespectacled.modernbeta.biome.release;

import com.bespectacled.modernbeta.biome.release.ReleaseBiomes.ReleaseBiome;

public class BiomeLayerIsland extends BiomeLayer {

    public BiomeLayerIsland(long seed, BiomeLayer parent) {
        super(seed);
        this.parent = parent;
    }

    @Override
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
                
                if(center == 0 && (n != 0 || e != 0 || w != 0 || s != 0)) {
                    int intBound = 1;
                    int biomeId = 1;
                    
                    if(n != 0 && nextInt(intBound++) == 0) {
                        biomeId = n;
                    }
                    if(e != 0 && nextInt(intBound++) == 0) {
                        biomeId = e;
                    }
                    if(w != 0 && nextInt(intBound++) == 0) {
                        biomeId = w;
                    }
                    if(s != 0 && nextInt(intBound++) == 0) {
                        biomeId = s;
                    }
                    
                    if(nextInt(3) == 0) {
                        cachedLayer[sX + sZ * sizeX] = biomeId;
                        continue;
                    }
                    
                    if(biomeId == ReleaseBiome.SNOWY_TUNDRA.getId()) {
                        cachedLayer[sX + sZ * sizeX] = ReleaseBiome.FROZEN_OCEAN.getId();
                    } else {
                        cachedLayer[sX + sZ * sizeX] = 0;
                    }
                    continue;
                }
                
                if(center > 0 && (n == 0 || e == 0 || w == 0 || s == 0)) {
                    if(nextInt(5) == 0) {
                        if(center == ReleaseBiome.SNOWY_TUNDRA.getId()) {
                            cachedLayer[sX + sZ * sizeX] = ReleaseBiome.FROZEN_OCEAN.getId();
                        } else {
                            cachedLayer[sX + sZ * sizeX] = 0;
                        }
                    } else {
                        cachedLayer[sX + sZ * sizeX] = center;
                    }
                } else {
                    cachedLayer[sX + sZ * sizeX] = center;
                }
            }

        }

        return cachedLayer;
    }
}
