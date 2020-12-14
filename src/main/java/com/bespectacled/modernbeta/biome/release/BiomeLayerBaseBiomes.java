package com.bespectacled.modernbeta.biome.release;

import com.bespectacled.modernbeta.biome.release.ReleaseBiomes.ReleaseBiome;

public class BiomeLayerBaseBiomes extends BiomeLayer {
    private ReleaseBiome baseBiomes[];

    public BiomeLayerBaseBiomes(long l, BiomeLayer parent) {
        super(l);
        this.parent = parent;
        
        baseBiomes = (new ReleaseBiome[] {
            ReleaseBiome.DESERT,
            ReleaseBiome.FOREST,
            ReleaseBiome.MOUNTAINS,
            ReleaseBiome.SWAMP,
            ReleaseBiome.PLAINS,
            ReleaseBiome.TAIGA
        });
        
        /*
        baseBiomes = (new BiomeGenBase[] {
            BiomeGenBase.desert, 
            BiomeGenBase.forest, 
            BiomeGenBase.hills, 
            BiomeGenBase.swampland, 
            BiomeGenBase.plains, 
            BiomeGenBase.taiga
        });
        */
    }

    public int[] sample(int x, int z, int sizeX, int sizeZ) {
        int parentLayer[] = parent.sample(x, z, sizeX, sizeZ);
        int cachedLayer[] = IntCache.getIntCache(sizeX * sizeZ);
        
        for(int sZ = 0; sZ < sizeZ; sZ++) {
            for(int sX = 0; sX < sizeX; sX++) {
                initChunkSeed(sX + x, sZ + z);
                int parentBiomeId = parentLayer[sX + sZ * sizeX];
                
                if(parentBiomeId == 0) {
                    cachedLayer[sX + sZ * sizeX] = 0;
                    continue;
                }
                
                if(parentBiomeId == ReleaseBiome.MUSHROOM_FIELDS.getId()) {
                    cachedLayer[sX + sZ * sizeX] = parentBiomeId;
                    continue;
                }
                
                if(parentBiomeId == 1) { // Replace Plains (should be only land biome at this point) with base biome
                    cachedLayer[sX + sZ * sizeX] = baseBiomes[nextInt(baseBiomes.length)].getId();
                } else {
                    cachedLayer[sX + sZ * sizeX] = ReleaseBiome.SNOWY_TUNDRA.getId();
                }
            }

        }

        return cachedLayer;
    }
}
