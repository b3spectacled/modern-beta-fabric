package com.bespectacled.modernbeta.biome.release;

public class BiomeLayerZoom extends BiomeLayer {

    public BiomeLayerZoom(long seed, BiomeLayer parent) {
        super(seed);
        this.parent = parent;
    }

    @Override
    public int[] sample(int x, int z, int sizeX, int sizeZ) {
        int parentX = x >> 1;
        int parentZ = z >> 1;
        int parentSizeX = (sizeX >> 1) + 3;
        int parentSizeZ = (sizeZ >> 1) + 3;
        
        int parentLayer[] = parent.sample(parentX, parentZ, parentSizeX, parentSizeZ);
        int cachedLayer[] = IntCache.getIntCache(parentSizeX * 2 * (parentSizeZ * 2));
        
        int sizeXDoubled = parentSizeX << 1;
        for(int sZ = 0; sZ < parentSizeZ - 1; sZ++) {
            int sizeZDoubled = sZ << 1;
            int sizeDoubled = sizeZDoubled * sizeXDoubled;
            int p0 = parentLayer[0 + (sZ + 0) * parentSizeX];
            int p1 = parentLayer[0 + (sZ + 1) * parentSizeX];
            
            for(int sX = 0; sX < parentSizeX - 1; sX++) {
                initChunkSeed(sX + parentX << 1, sZ + parentZ << 1);
                int p2 = parentLayer[sX + 1 + (sZ + 0) * parentSizeX];
                int p3 = parentLayer[sX + 1 + (sZ + 1) * parentSizeX];
                
                cachedLayer[sizeDoubled] = p0;
                cachedLayer[sizeDoubled++ + sizeXDoubled] = selectRandom(p0, p1);
                cachedLayer[sizeDoubled] = selectRandom(p0, p2);
                cachedLayer[sizeDoubled++ + sizeXDoubled] = selectRandomOrMode(p0, p2, p1, p3);
                
                p0 = p2;
                p1 = p3;
            }

        }

        int cachedLayer2[] = IntCache.getIntCache(sizeX * sizeZ);
        for(int sZ = 0; sZ < sizeZ; sZ++) {
            System.arraycopy(cachedLayer, (sZ + (z & 1)) * (parentSizeX << 1) + (x & 1), cachedLayer2, sZ * sizeX, sizeX);
        }

        return cachedLayer2;
    }

    protected int selectRandom(int i0, int i1) {
        return nextInt(2) != 0 ? i1 : i0;
    }

    protected int selectRandomOrMode(int i0, int i1, int i2, int i3) {
        if (i1 == i2 && i2 == i3) return i1;
        if (i0 == i1 && i0 == i2) return i0;
        if (i0 == i1 && i0 == i3) return i0;
        if (i0 == i2 && i0 == i3) return i0;
        if (i0 == i1 && i2 != i3) return i0;
        if (i0 == i2 && i1 != i3) return i0;
        if (i0 == i3 && i1 != i2) return i0;
        if (i1 == i0 && i2 != i3) return i1;
        if (i1 == i2 && i0 != i3) return i1;
        if (i1 == i3 && i0 != i2) return i1;
        if (i2 == i0 && i1 != i3) return i2;
        if (i2 == i1 && i0 != i3) return i2;
        if (i2 == i3 && i0 != i1) return i2;
        if (i3 == i0 && i1 != i2) return i2;
        if (i3 == i1 && i0 != i2) return i2;
        if (i3 == i2 && i0 != i1) return i2;
        
        int randInt = nextInt(4);
        if(randInt == 0) return i0;
        if(randInt == 1) return i1;
        if(randInt == 2) return i2;
        
        return i3;
    }

    public static BiomeLayer stack(long seed, BiomeLayer layer, int iterations) {
        BiomeLayer biomeLayer = layer;
        for(int i = 0; i < iterations; i++) {
            biomeLayer = new BiomeLayerZoom(seed + (long)i, biomeLayer);
        }

        return biomeLayer;
    }

}
