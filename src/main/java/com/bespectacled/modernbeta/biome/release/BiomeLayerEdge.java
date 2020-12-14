package com.bespectacled.modernbeta.biome.release;

import com.bespectacled.modernbeta.biome.release.ReleaseBiomes.ReleaseBiome;

public class BiomeLayerEdge extends BiomeLayer {
    public BiomeLayerEdge(long seed, BiomeLayer parent)
    {
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
                
                // Place shore/edge biomes, if parent biome borders non-parent biome (e.g. ocean or non-mountains biome)
                if (center == ReleaseBiome.MUSHROOM_FIELDS.getId()) {
                    int n = parentLayer[sX + 1 + ((sZ + 1) - 1) * (sizeX + 2)];
                    int e = parentLayer[sX + 1 + 1 + (sZ + 1) * (sizeX + 2)];
                    int w = parentLayer[((sX + 1) - 1) + (sZ + 1) * (sizeX + 2)];
                    int s = parentLayer[sX + 1 + (sZ + 1 + 1) * (sizeX + 2)];
                    
                    if (n == ReleaseBiome.OCEAN.getId() || e == ReleaseBiome.OCEAN.getId() || w == ReleaseBiome.OCEAN.getId() || s == ReleaseBiome.OCEAN.getId()) {
                        cachedLayer[sX + sZ * sizeX] = ReleaseBiome.MUSHROOM_SHORE.getId();
                    } else {
                        cachedLayer[sX + sZ * sizeX] = center;
                    }
                    
                    continue;
                }
                
                if (center != ReleaseBiome.OCEAN.getId() && center != ReleaseBiome.RIVER.getId() && center != ReleaseBiome.SWAMP.getId() && center != ReleaseBiome.MOUNTAINS.getId()) {
                    int n = parentLayer[sX + 1 + ((sZ + 1) - 1) * (sizeX + 2)];
                    int e = parentLayer[sX + 1 + 1 + (sZ + 1) * (sizeX + 2)];
                    int w = parentLayer[((sX + 1) - 1) + (sZ + 1) * (sizeX + 2)];
                    int s = parentLayer[sX + 1 + (sZ + 1 + 1) * (sizeX + 2)];
                    
                    if (n == ReleaseBiome.OCEAN.getId() || e == ReleaseBiome.OCEAN.getId() || w == ReleaseBiome.OCEAN.getId() || s == ReleaseBiome.OCEAN.getId()) {
                        cachedLayer[sX + sZ * sizeX] = ReleaseBiome.BEACH.getId();
                    } else {
                        cachedLayer[sX + sZ * sizeX] = center;
                    }
                    
                    continue;
                }
                
                if (center == ReleaseBiome.MOUNTAINS.getId()) {
                    int n = parentLayer[sX + 1 + ((sZ + 1) - 1) * (sizeX + 2)];
                    int e = parentLayer[sX + 1 + 1 + (sZ + 1) * (sizeX + 2)];
                    int w = parentLayer[((sX + 1) - 1) + (sZ + 1) * (sizeX + 2)];
                    int s = parentLayer[sX + 1 + (sZ + 1 + 1) * (sizeX + 2)];
                    
                    if (n != ReleaseBiome.MOUNTAINS.getId() || e != ReleaseBiome.MOUNTAINS.getId() || w != ReleaseBiome.MOUNTAINS.getId() || s != ReleaseBiome.MOUNTAINS.getId()) {
                        cachedLayer[sX + sZ * sizeX] = ReleaseBiome.MOUNTAIN_EDGE.getId();
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
