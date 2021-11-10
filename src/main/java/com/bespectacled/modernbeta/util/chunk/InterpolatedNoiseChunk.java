package com.bespectacled.modernbeta.util.chunk;

public class InterpolatedNoiseChunk {
    private final double[][] noise;
    private final int noiseSizeX;
    
    public InterpolatedNoiseChunk(double[][] noise, int noiseSizeX) {
        this.noise = noise;
        this.noiseSizeX = noiseSizeX;
    }
    
    public double sample(int localX, int localY, int localZ) {
        return noise[localX * this.noiseSizeX + localZ][localY];
    }
}
