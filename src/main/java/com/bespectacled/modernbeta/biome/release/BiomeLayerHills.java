package com.bespectacled.modernbeta.biome.release;

import com.bespectacled.modernbeta.biome.release.ReleaseBiomes.ReleaseBiome;

public class BiomeLayerHills extends BiomeLayer {
    public BiomeLayerHills(long seed, BiomeLayer parent) {
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
        
        for (int sZ = 0; sZ < sizeZ; sZ++) {
            for (int sX = 0; sX < sizeX; sX++) {
                initChunkSeed(sX + x, sZ + z);
                int center = parentLayer[sX + 1 + (sZ + 1) * (sizeX + 2)];
                
                if (nextInt(3) == 0) {
                    int biomeId = center;
                    
                    if (center == ReleaseBiome.DESERT.getId()) {
                        biomeId = ReleaseBiome.DESERT_HILLS.getId();
                    } else if (center == ReleaseBiome.FOREST.getId()) {
                        biomeId = ReleaseBiome.WOODED_HILLS.getId();
                    } else if (center == ReleaseBiome.TAIGA.getId()) {
                        biomeId = ReleaseBiome.TAIGA_HILLS.getId();
                    } else if (center == ReleaseBiome.PLAINS.getId()) {
                        biomeId = ReleaseBiome.FOREST.getId();
                    } else if (center == ReleaseBiome.SNOWY_TUNDRA.getId()) {
                        biomeId = ReleaseBiome.SNOWY_MOUNTAINS.getId();
                    }
                    
                    if (biomeId != center) {
                        int n = parentLayer[sX + 1 + ((sZ + 1) - 1) * (sizeX + 2)];
                        int e = parentLayer[sX + 1 + 1 + (sZ + 1) * (sizeX + 2)];
                        int w = parentLayer[((sX + 1) - 1) + (sZ + 1) * (sizeX + 2)];
                        int s = parentLayer[sX + 1 + (sZ + 1 + 1) * (sizeX + 2)];
                        
                        // Only add hills biome if current cell completely contained by base biome
                        if (n == center && e == center && w == center && s == center) {
                            cachedLayer[sX + sZ * sizeX] = biomeId;
                        } else {
                            cachedLayer[sX + sZ * sizeX] = center;
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
