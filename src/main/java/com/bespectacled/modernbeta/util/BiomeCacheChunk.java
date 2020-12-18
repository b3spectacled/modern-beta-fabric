package com.bespectacled.modernbeta.util;

public class BiomeCacheChunk {
    private final double temps[];
    private final double humids[];
    private final double skyTemps[];
    
    public BiomeCacheChunk(int chunkX, int chunkZ) {
        this.temps = new double[256];
        this.humids = new double[256];
        this.skyTemps = new double[256];
        
        int startX = chunkX << 4;
        int startZ = chunkZ << 4;
        double[] tempHumid = new double[2];
        
        int ndx = 0;
        for (int i = startZ; i < startZ + 16; ++i) {
            for (int j = startX; j < startX + 16; ++j) {
                BiomeUtil.sampleTempHumidAtPoint(tempHumid, j, i);
                
                temps[ndx] = tempHumid[0];
                humids[ndx] = tempHumid[1];
                skyTemps[ndx] = BiomeUtil.sampleSkyTempAtPoint(j, i);

                ndx++;
            }
        }
    }
    
    public void sampleTempHumidAtPoint(double[] tempHumid, int x, int z) {
        tempHumid[0] = temps[x & 0xF | (z & 0xF) << 4];
        tempHumid[1] = humids[x & 0xF | (z & 0xF) << 4];
    }
    
    public double sampleSkyTempAtPoint(int x, int z) {
        return skyTemps[x & 0xF | (z & 0xF) << 4];
    }
}