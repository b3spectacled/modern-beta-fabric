package com.bespectacled.modernbeta.api.world.gen.noise;

public class OreVeinNoiseProvider extends NoiseProvider {
    private final int noiseMinY;
    private final OreVeinColumnSampler bufferSampler;
    
    public OreVeinNoiseProvider(
        int noiseSizeX, 
        int noiseSizeY, 
        int noiseSizeZ, 
        int noiseX, 
        int noiseZ,
        int noiseMinY,
        OreVeinColumnSampler bufferSampler
    ) {
        super(noiseSizeX, noiseSizeY, noiseSizeZ, noiseX, noiseZ);
        
        this.noiseMinY = noiseMinY;
        this.bufferSampler = bufferSampler;
    }
    
    public double[] sampleNoise(int startNoiseX, int startNoiseZ) {
        double[] buffer = new double[this.noiseResY];
        double[] noise = new double[this.noiseSize];
        
        int ndx = 0;
        for (int localNoiseX = 0; localNoiseX < this.noiseResX; ++localNoiseX) {
            for (int localNoiseZ = 0; localNoiseZ < this.noiseResZ; ++localNoiseZ) {
                int noiseX = startNoiseX + localNoiseX;
                int noiseZ = startNoiseZ + localNoiseZ;
                
                this.bufferSampler.sampleColumn(buffer, noiseX, noiseZ, this.noiseMinY, this.noiseResY);
                
                for (int nY = 0; nY < this.noiseResY; ++nY) {
                    noise[ndx++] = buffer[nY];
                }
            }
        }
        
        return noise;
    }
    
    @FunctionalInterface
    public static interface OreVeinColumnSampler {
        public void sampleColumn(double[] buffer, int x, int z, int minY, int noiseSizeY);
    }
}
