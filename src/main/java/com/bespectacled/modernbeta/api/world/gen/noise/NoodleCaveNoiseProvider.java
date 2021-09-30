package com.bespectacled.modernbeta.api.world.gen.noise;

public class NoodleCaveNoiseProvider extends NoiseProvider {
    private final int noiseMinY;
    private final NoodleCaveColumnSampler bufferSampler;
    
    public NoodleCaveNoiseProvider(
        int noiseSizeX, 
        int noiseSizeY, 
        int noiseSizeZ, 
        int noiseX, 
        int noiseZ,
        int noiseMinY,
        NoodleCaveColumnSampler bufferSampler
        
    ) {
        super(noiseSizeX, noiseSizeY, noiseSizeZ, noiseX, noiseZ);
        
        this.noiseMinY = noiseMinY;
        this.bufferSampler = bufferSampler;
        
        this.noise = this.generateNoise(noiseX, noiseZ);
    }
    
    public double[] generateNoise(int startNoiseX, int startNoiseZ) {
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
    public static interface NoodleCaveColumnSampler {
        public void sampleColumn(double[] buffer, int x, int z, int minY, int noiseSizeY);
    }
}
