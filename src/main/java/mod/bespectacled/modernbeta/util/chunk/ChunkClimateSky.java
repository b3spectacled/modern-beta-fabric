package mod.bespectacled.modernbeta.util.chunk;

import java.util.function.BiFunction;

public class ChunkClimateSky {
    private final double temp[] = new double[256];
    
    public ChunkClimateSky(int chunkX, int chunkZ, BiFunction<Integer, Integer, Double> chunkFunc) {
        int startX = chunkX << 4;
        int startZ = chunkZ << 4;
        
        int ndx = 0;
        for (int x = startX; x < startX + 16; ++x) {
            for (int z = startZ; z < startZ + 16; ++z) {    
                this.temp[ndx] = chunkFunc.apply(x, z);
                
                ndx++;
            }
        }
    }
    
    public double sampleTemp(int x, int z) {
        return temp[(z & 0xF) + (x & 0xF) * 16];
    }
}
