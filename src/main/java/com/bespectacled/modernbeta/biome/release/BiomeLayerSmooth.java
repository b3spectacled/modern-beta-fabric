package com.bespectacled.modernbeta.biome.release;

public class BiomeLayerSmooth extends BiomeLayer {
    public BiomeLayerSmooth(long seed, BiomeLayer parent)
    {
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
                
                int n = parentLayer[sX + 0 + (sZ + 1) * parentSizeX];
                int e = parentLayer[sX + 2 + (sZ + 1) * parentSizeX];
                int w = parentLayer[sX + 1 + (sZ + 0) * parentSizeX];
                int s = parentLayer[sX + 1 + (sZ + 2) * parentSizeX];
                int center = parentLayer[sX + 1 + (sZ + 1) * parentSizeX];
                
                if(n == e && w == s) {
                    initChunkSeed(sX + x, sZ + z);
                    
                    if(nextInt(2) == 0) {
                        center = n;
                    } else {
                        center = w;
                    }
                    
                } else {
                    if(n == e) {
                        center = n;
                    }
                    if(w == s) {
                        center = w;
                    }
                }
                cachedLayer[sX + sZ * sizeX] = center;
            }

        }

        return cachedLayer;
    }
}
