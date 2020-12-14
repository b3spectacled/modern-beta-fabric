package com.bespectacled.modernbeta.biome.release;

import com.bespectacled.modernbeta.biome.release.ReleaseBiomes.ReleaseBiome;

public class BiomeLayerRiverMix extends BiomeLayer {
    private BiomeLayer mainBiomeLayer;
    private BiomeLayer riverBiomeLayer;

    public BiomeLayerRiverMix(long l, BiomeLayer mainLayer, BiomeLayer riverLayer) {
        super(l);
        this.mainBiomeLayer = mainLayer;
        this.riverBiomeLayer = riverLayer;
    }

    @Override
    public void initWorldSeed(long seed) {
        mainBiomeLayer.initWorldSeed(seed);
        riverBiomeLayer.initWorldSeed(seed);
        super.initWorldSeed(seed);
    }

    @Override
    public int[] sample(int x, int z, int sizeX, int sizeZ) {
        int mainLayer[] = mainBiomeLayer.sample(x, z, sizeX, sizeZ);
        int riverLayer[] = riverBiomeLayer.sample(x, z, sizeX, sizeZ);
        int cachedLayer[] = IntCache.getIntCache(sizeX * sizeZ);
        
        for (int i = 0; i < sizeX * sizeZ; i++) {
            
            // Skip river replacement if current biome is ocean.
            if (mainLayer[i] == ReleaseBiome.OCEAN.getId()) {
                cachedLayer[i] = mainLayer[i];
                continue;
            }
            
            // If biome on river layer not ocean, then do replacement.
            if (riverLayer[i] >= 0) {
                if (mainLayer[i] == ReleaseBiome.SNOWY_TUNDRA.getId())
                {
                    cachedLayer[i] = ReleaseBiome.FROZEN_RIVER.getId();
                    continue;
                }
                
                if (mainLayer[i] == ReleaseBiome.MUSHROOM_FIELDS.getId() || mainLayer[i] == ReleaseBiome.MUSHROOM_SHORE.getId()) {
                    cachedLayer[i] = ReleaseBiome.MUSHROOM_SHORE.getId();
                } else {
                    cachedLayer[i] = riverLayer[i];
                }
            } else {
                cachedLayer[i] = mainLayer[i];
            }
        }

        return cachedLayer;
    }
}
