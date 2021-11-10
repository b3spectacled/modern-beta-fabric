package com.bespectacled.modernbeta.util.chunk;

public class Noise2dChunk {
    private final double[] noise;
    private final int noiseSizeX;
    
    public Noise2dChunk(double[] noise, int noiseSizeX) {
        this.noise = noise;
        this.noiseSizeX = noiseSizeX;
    }
    
    public double sample(int localX, int localZ) {
        return noise[localX * this.noiseSizeX + localZ];
    }
}
