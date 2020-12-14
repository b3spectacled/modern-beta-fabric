package com.bespectacled.modernbeta.biome.release;

public abstract class BiomeLayer {
    protected BiomeLayer parent;
    
    private long worldSeed;
    private long chunkSeed;
    private long baseSeed;
    
    public static BiomeLayer[] build(long seed, int biomeSize, int riverSize) {
        BiomeLayer layer0 = new BiomeLayerInit(1L);
        layer0 = new BiomeLayerZoomFuzzy(2000L, layer0);
        layer0 = new BiomeLayerIsland(1L, layer0);
        layer0 = new BiomeLayerZoom(2001L, layer0);
        layer0 = new BiomeLayerIsland(2L, layer0);
        
        layer0 = new BiomeLayerSnow(2L, layer0);
        layer0 = new BiomeLayerZoom(2002L, layer0);
        layer0 = new BiomeLayerIsland(3L, layer0);
        layer0 = new BiomeLayerZoom(2003L, layer0);
        layer0 = new BiomeLayerIsland(4L, layer0);
        layer0 = new BiomeLayerMushroomIsland(5L, layer0);
        
        BiomeLayer layer1 = layer0;
        layer1 = BiomeLayerZoom.stack(1000L, layer1, 0);
        layer1 = new BiomeLayerRiverInit(100L, layer1);
        layer1 = BiomeLayerZoom.stack(1000L, layer1, riverSize);
        layer1 = new BiomeLayerRiver(1L, layer1);
        layer1 = new BiomeLayerSmooth(1000L, layer1);
        
        
        BiomeLayer layer2 = layer0;
        layer2 = BiomeLayerZoom.stack(1000L, layer2, 0);
        layer2 = new BiomeLayerBaseBiomes(200L, layer2);
        layer2 = BiomeLayerZoom.stack(1000L, layer2, 2);

        layer2 = new BiomeLayerHills(1000L, layer2);
        
        for (int i = 0; i < biomeSize; i++) {
            layer2 = new BiomeLayerZoom(1000L + i, layer2);
            
            if (i == 0) {
                layer2 = new BiomeLayerIsland(3L, layer2);
            }
            
            if (i == 1) {
                layer2 = new BiomeLayerEdge(1000L, layer2);
                layer2 = new BiomeLayerSwampRiver(1000L, layer2);
            }
        }
        
        layer2 = new BiomeLayerSmooth(1000L, layer2);
        layer2 = new BiomeLayerRiverMix(100L, layer2, layer1);
        
        // Used for fetching biomes at an absolute coordinate level.
        BiomeLayerZoomVoronoi zoomVoronoi = new BiomeLayerZoomVoronoi(10L, layer2);
        
        layer2.initWorldSeed(seed);
        zoomVoronoi.initWorldSeed(seed);
        
        return new BiomeLayer[] {layer2, zoomVoronoi};
    }
    
    public BiomeLayer(long seed) {
        baseSeed = seed;
        baseSeed *= baseSeed * 0x5851f42d4c957f2dL + 0x14057b7ef767814fL;
        baseSeed += seed;
        baseSeed *= baseSeed * 0x5851f42d4c957f2dL + 0x14057b7ef767814fL;
        baseSeed += seed;
        baseSeed *= baseSeed * 0x5851f42d4c957f2dL + 0x14057b7ef767814fL;
        baseSeed += seed;
    }
    
    public void initWorldSeed(long seed)
    {
        worldSeed = seed;
        if(parent != null) {
            parent.initWorldSeed(seed);
        }
        worldSeed *= worldSeed * 0x5851f42d4c957f2dL + 0x14057b7ef767814fL;
        worldSeed += baseSeed;
        worldSeed *= worldSeed * 0x5851f42d4c957f2dL + 0x14057b7ef767814fL;
        worldSeed += baseSeed;
        worldSeed *= worldSeed * 0x5851f42d4c957f2dL + 0x14057b7ef767814fL;
        worldSeed += baseSeed;
    }

    public void initChunkSeed(long chunkX, long chunkZ) {
        chunkSeed = worldSeed;
        chunkSeed *= chunkSeed * 0x5851f42d4c957f2dL + 0x14057b7ef767814fL;
        chunkSeed += chunkX;
        chunkSeed *= chunkSeed * 0x5851f42d4c957f2dL + 0x14057b7ef767814fL;
        chunkSeed += chunkZ;
        chunkSeed *= chunkSeed * 0x5851f42d4c957f2dL + 0x14057b7ef767814fL;
        chunkSeed += chunkX;
        chunkSeed *= chunkSeed * 0x5851f42d4c957f2dL + 0x14057b7ef767814fL;
        chunkSeed += chunkZ;
    }

    protected int nextInt(int bound) {
        int rand = (int)((chunkSeed >> 24) % (long)bound);
        if(rand < 0) {
            rand += bound;
        }
        
        chunkSeed *= chunkSeed * 0x5851f42d4c957f2dL + 0x14057b7ef767814fL;
        chunkSeed += worldSeed;
        return rand;
    }

    public abstract int[] sample(int x, int z, int sizeX, int sizeZ);
}
