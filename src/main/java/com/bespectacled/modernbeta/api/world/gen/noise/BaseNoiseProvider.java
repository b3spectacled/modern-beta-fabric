package com.bespectacled.modernbeta.api.world.gen.noise;

public class BaseNoiseProvider extends NoiseProvider {
    private final BaseColumnSampler bufferSampler;
    
    public BaseNoiseProvider(
        int noiseSizeX, 
        int noiseSizeY, 
        int noiseSizeZ, 
        BaseColumnSampler bufferSampler
    ) {
        super(noiseSizeX, noiseSizeY, noiseSizeZ);
        
        this.bufferSampler = bufferSampler;
    }

    @Override
    public double[] sampleNoise(int startNoiseX, int startNoiseZ) {
        double[] buffer = new double[this.noiseResY];
        double[] noise = new double[(this.noiseSizeX + 1) * (this.noiseSizeZ + 1) * (this.noiseSizeY + 1)];
        
        int ndx = 0;
        for (int localNoiseX = 0; localNoiseX < this.noiseResX; ++localNoiseX) {
            for (int localNoiseZ = 0; localNoiseZ < this.noiseResZ; ++localNoiseZ) {
                this.bufferSampler.sampleColumn(buffer, startNoiseX, startNoiseZ, localNoiseX, localNoiseZ);
                
                for (int nY = 0; nY < this.noiseResY; ++nY) {
                    noise[ndx++] = buffer[nY];
                }
            }
        }
        
        return noise;
    }
    
    @FunctionalInterface
    public static interface BaseColumnSampler {
        public void sampleColumn(double[] buffer, int startNoiseX, int startNoiseZ, int localNoiseX, int localNoiseZ);
    }
}
