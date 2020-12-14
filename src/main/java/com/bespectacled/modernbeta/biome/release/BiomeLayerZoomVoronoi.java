package com.bespectacled.modernbeta.biome.release;

public class BiomeLayerZoomVoronoi extends BiomeLayer {
    public BiomeLayerZoomVoronoi(long seed, BiomeLayer parent) {
        super(seed);
        this.parent = parent;
    }

    public int[] sample(int x, int z, int sizeX, int sizeZ) {
        x -= 2;
        z -= 2;
        byte two = 2;
        
        int bI = 1 << two;
        int bX = x >> two;
        int bZ = z >> two;
        
        int bX1 = (sizeX >> two) + 3;
        int bZ1 = (sizeZ >> two) + 3;
        
        int parentLayer[] = parent.sample(bX, bZ, bX1, bZ1);
        int bX2 = bX1 << two;
        int bZ2 = bZ1 << two;
        int cachedLayer[] = IntCache.getIntCache(bX2 * bZ2);
        
        for (int i = 0; i < bZ1 - 1; i++) {
            int p0 = parentLayer[0 + (i + 0) * bX1];
            int p1 = parentLayer[0 + (i + 1) * bX1];
            
            for (int j = 0; j < bX1 - 1; j++) {
                
                double double0 = (double)bI * 0.90000000000000002D;
                
                initChunkSeed(j + bX << two, i + bZ << two);
                double double1 = ((double)nextInt(1024) / 1024D - 0.5D) * double0;
                double double2 = ((double)nextInt(1024) / 1024D - 0.5D) * double0;
                
                initChunkSeed(j + bX + 1 << two, i + bZ << two);
                double double3 = ((double)nextInt(1024) / 1024D - 0.5D) * double0 + (double)bI;
                double double4 = ((double)nextInt(1024) / 1024D - 0.5D) * double0;
                
                initChunkSeed(j + bX << two, i + bZ + 1 << two);
                double double5 = ((double)nextInt(1024) / 1024D - 0.5D) * double0;
                double double6 = ((double)nextInt(1024) / 1024D - 0.5D) * double0 + (double)bI;
                
                initChunkSeed(j + bX + 1 << two, i + bZ + 1 << two);
                double double7 = ((double)nextInt(1024) / 1024D - 0.5D) * double0 + (double)bI;
                double double8 = ((double)nextInt(1024) / 1024D - 0.5D) * double0 + (double)bI;
                
                int p3 = parentLayer[j + 1 + (i + 0) * bX1];
                int p4 = parentLayer[j + 1 + (i + 1) * bX1];
                
                for (int k = 0; k < bI; k++) {
                    int ndx = ((i << two) + k) * bX2 + (j << two);
                    
                    for (int i5 = 0; i5 < bI; i5++) {
                        double double9 = ((double)k - double2) * ((double)k - double2) + ((double)i5 - double1) * ((double)i5 - double1);
                        double double10 = ((double)k - double4) * ((double)k - double4) + ((double)i5 - double3) * ((double)i5 - double3);
                        double double11 = ((double)k - double6) * ((double)k - double6) + ((double)i5 - double5) * ((double)i5 - double5);
                        double double12 = ((double)k - double8) * ((double)k - double8) + ((double)i5 - double7) * ((double)i5 - double7);
                        
                        if (double9 < double10 && double9 < double11 && double9 < double12) {
                            cachedLayer[ndx++] = p0;
                            continue;
                        }
                        
                        if (double10 < double9 && double10 < double11 && double10 < double12) {
                            cachedLayer[ndx++] = p3;
                            continue;
                        }
                        
                        if (double11 < double9 && double11 < double10 && double11 < double12) {
                            cachedLayer[ndx++] = p1;
                        } else {
                            cachedLayer[ndx++] = p4;
                        }
                    }
                }

                p0 = p3;
                p1 = p4;
            }
        }

        int cachedLayer2[] = IntCache.getIntCache(sizeX * sizeZ);
        for (int sZ = 0; sZ < sizeZ; sZ++) {
            System.arraycopy(cachedLayer, (sZ + (z & bI - 1)) * (bX1 << two) + (x & bI - 1), cachedLayer2, sZ * sizeX, sizeX);
        }

        return cachedLayer2;
    }
}
