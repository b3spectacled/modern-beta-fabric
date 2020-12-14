package com.bespectacled.modernbeta.biome.release;

public class BiomeLayerZoomFuzzy extends BiomeLayer {

    public BiomeLayerZoomFuzzy(long seed, BiomeLayer parent) {
        super(seed);
        this.parent = parent;
    }

    public int[] sample(int x, int z, int sizeX, int sizeZ) {
        int x0 = x >> 1;
        int z0 = z >> 1;
        int sizeX0 = (sizeX >> 1) + 3;
        int sizeZ0 = (sizeZ >> 1) + 3;
        
        int parentLayer[] = parent.sample(x0, z0, sizeX0, sizeZ0);
        int cachedLayer[] = IntCache.getIntCache(sizeX0 * 2 * (sizeZ0 * 2));
        int sizeXDoubled = sizeX0 << 1;
        
        for(int sZ = 0; sZ < sizeZ0 - 1; sZ++) {
            int sizeZDoubled = sZ << 1;
            int sizeDoubled = sizeZDoubled * sizeXDoubled;
            int p0 = parentLayer[0 + (sZ + 0) * sizeX0];
            int p1 = parentLayer[0 + (sZ + 1) * sizeX0];
            
            for(int sX = 0; sX < sizeX0 - 1; sX++) {
                initChunkSeed(sX + x0 << 1, sZ + z0 << 1);
                int p2 = parentLayer[sX + 1 + (sZ + 0) * sizeX0];
                int p3 = parentLayer[sX + 1 + (sZ + 1) * sizeX0];
                
                cachedLayer[sizeDoubled] = p0;
                cachedLayer[sizeDoubled++ + sizeXDoubled] = selectRandom(p0, p1);
                cachedLayer[sizeDoubled] = selectRandom(p0, p2);
                cachedLayer[sizeDoubled++ + sizeXDoubled] = selectRandom(p0, p2, p1, p3);
                
                p0 = p2;
                p1 = p3;
            }

        }

        int cachedLayer2[] = IntCache.getIntCache(sizeX * sizeZ);
        for(int sZ = 0; sZ < sizeZ; sZ++) {
            System.arraycopy(cachedLayer, (sZ + (z & 1)) * (sizeX0 << 1) + (x & 1), cachedLayer2, sZ * sizeX, sizeX);
        }

        return cachedLayer2;
    }

    protected int selectRandom(int i0, int i1) {
        return nextInt(2) != 0 ? i1 : i0;
    }

    protected int selectRandom(int i0, int i1, int i2, int i3) {
        int randInt = nextInt(4);
        
        if(randInt == 0) return i0;
        if(randInt == 1) return i1;
        if(randInt == 2) return i2;

        return i3;
    }
}
