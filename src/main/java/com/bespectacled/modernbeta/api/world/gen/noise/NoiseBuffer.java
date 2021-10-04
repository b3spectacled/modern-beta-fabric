package com.bespectacled.modernbeta.api.world.gen.noise;

public class NoiseBuffer {
    private double[] buffer;
    
    public NoiseBuffer(int noiseSize) {
        this.buffer = new double[noiseSize];
    }
    
    public double getNoise(int noiseX, int noiseY, int noiseZ) {
        return 0;
    }
}
