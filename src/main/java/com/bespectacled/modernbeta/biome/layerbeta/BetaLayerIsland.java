package com.bespectacled.modernbeta.biome.layerbeta;

import com.bespectacled.modernbeta.util.IntCache;

public class BetaLayerIsland extends BetaBiomeLayer {

    public BetaLayerIsland(long seed, BetaBiomeLayer layer) {
        super(seed);
        parent = layer;
    }

    @Override
    public int[] sample(int i, int j, int sizeX, int sizeZ) {
        /*
        int i1 = i - 1;
        int j1 = j - 1;
        int k1 = sizeX + 2;
        int l1 = sizeZ + 2;
        int parentBiomes[] = parent.sample(i1, j1, k1, l1);
        int ai1[] = IntCache.getIntCache(sizeX * sizeZ);
        for(int z = 0; z < sizeZ; z++)
        {
            for(int x = 0; x < sizeX; x++)
            {
                int k2 = parentBiomes[x + 0 + (z + 0) * k1];
                int l2 = parentBiomes[x + 2 + (z + 0) * k1];
                int i3 = parentBiomes[x + 0 + (z + 2) * k1];
                int j3 = parentBiomes[x + 2 + (z + 2) * k1];
                int k3 = parentBiomes[x + 1 + (z + 1) * k1];
                initChunkSeed(x + i, z + j);
                if(k3 == 0 && (k2 != 0 || l2 != 0 || i3 != 0 || j3 != 0))
                {
                    int l3 = 1;
                    int biomeId = 1;
                    if(k2 != 0 && nextInt(l3++) == 0)
                    {
                        biomeId = k2;
                    }
                    if(l2 != 0 && nextInt(l3++) == 0)
                    {
                        biomeId = l2;
                    }
                    if(i3 != 0 && nextInt(l3++) == 0)
                    {
                        biomeId = i3;
                    }
                    if(j3 != 0 && nextInt(l3++) == 0)
                    {
                        biomeId = j3;
                    }
                    if(nextInt(3) == 0)
                    {
                        ai1[x + z * sizeX] = biomeId;
                        continue;
                    }
                    if(biomeId == BiomeGenBase.icePlains.biomeID)
                    {
                        ai1[x + z * sizeX] = BiomeGenBase.frozenOcean.biomeID;
                    } else
                    {
                        ai1[x + z * sizeX] = 0;
                    }
                    continue;
                }
                if(k3 > 0 && (k2 == 0 || l2 == 0 || i3 == 0 || j3 == 0))
                {
                    if(nextInt(5) == 0) {
                        if(k3 == BiomeGenBase.icePlains.biomeID)
                        {
                            ai1[x + z * sizeX] = BiomeGenBase.frozenOcean.biomeID;
                        } else
                        {
                            ai1[x + z * sizeX] = 0;
                        }
                    } else {
                        ai1[x + z * sizeX] = k3;
                    }
                } else {
                    ai1[x + z * sizeX] = k3;
                }
            }

        }*/
        
        

        //return ai1;
        return null;
    }

}
