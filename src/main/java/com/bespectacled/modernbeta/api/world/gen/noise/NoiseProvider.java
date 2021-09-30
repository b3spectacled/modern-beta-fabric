package com.bespectacled.modernbeta.api.world.gen.noise;

import net.minecraft.util.math.MathHelper;

/*
 * A shoddy imitation of NoiseColumnSampler that is hopefully flexible enough
 * to accommodate Beta terrain generation and 1.18 gen changes, argh!
 */
public abstract class NoiseProvider {
    protected final int noiseSizeX;
    protected final int noiseSizeY;
    protected final int noiseSizeZ;
    
    protected final int noiseResX;
    protected final int noiseResY;
    protected final int noiseResZ;
    
    protected final int noiseSize;
    
    protected double[] noise;

    private double lowerNW;
    private double lowerSW;
    private double lowerNE;
    private double lowerSE;
    
    private double upperNW; 
    private double upperSW;
    private double upperNE;
    private double upperSE;
    
    private double nw;
    private double ne;
    private double sw;
    private double se;
    
    private double n;
    private double s;
    
    private double sample;
    
    public NoiseProvider(
        int noiseSizeX, 
        int noiseSizeY, 
        int noiseSizeZ, 
        int noiseX, 
        int noiseZ
    ) {
        this.noiseSizeX = noiseSizeX;
        this.noiseSizeY = noiseSizeY;
        this.noiseSizeZ = noiseSizeZ;
        
        this.noiseResX = noiseSizeX + 1;
        this.noiseResY = noiseSizeY + 1;
        this.noiseResZ = noiseSizeZ + 1;
        
        this.noiseSize = this.noiseResX * this.noiseResY * this.noiseResZ;
    }
    
    public abstract double[] generateNoise(int startNoiseX, int startNoiseZ);
    
    public void sampleNoiseCorners(int subChunkX, int subChunkY, int subChunkZ) {
        this.lowerNW = noise[((subChunkX + 0) * this.noiseResX + (subChunkZ + 0)) * this.noiseResY + (subChunkY + 0)];
        this.lowerSW = noise[((subChunkX + 0) * this.noiseResX + (subChunkZ + 1)) * this.noiseResY + (subChunkY + 0)];
        this.lowerNE = noise[((subChunkX + 1) * this.noiseResX + (subChunkZ + 0)) * this.noiseResY + (subChunkY + 0)];
        this.lowerSE = noise[((subChunkX + 1) * this.noiseResX + (subChunkZ + 1)) * this.noiseResY + (subChunkY + 0)];
        
        this.upperNW = noise[((subChunkX + 0) * this.noiseResX + (subChunkZ + 0)) * this.noiseResY + (subChunkY + 1)]; 
        this.upperSW = noise[((subChunkX + 0) * this.noiseResX + (subChunkZ + 1)) * this.noiseResY + (subChunkY + 1)];
        this.upperNE = noise[((subChunkX + 1) * this.noiseResX + (subChunkZ + 0)) * this.noiseResY + (subChunkY + 1)];
        this.upperSE = noise[((subChunkX + 1) * this.noiseResX + (subChunkZ + 1)) * this.noiseResY + (subChunkY + 1)];
    }
    
    public void sampleNoiseY(double deltaY) {
        this.nw = MathHelper.lerp(deltaY, this.lowerNW, this.upperNW);
        this.sw = MathHelper.lerp(deltaY, this.lowerSW, this.upperSW);
        this.ne = MathHelper.lerp(deltaY, this.lowerNE, this.upperNE);
        this.se = MathHelper.lerp(deltaY, this.lowerSE, this.upperSE);
    }
    
    public void sampleNoiseX(double deltaX) {
        this.n = MathHelper.lerp(deltaX, this.nw, this.ne);
        this.s = MathHelper.lerp(deltaX, this.sw, this.se);
    }
    
    public void sampleDensity(double deltaZ) {
        this.sample = MathHelper.lerp(deltaZ, this.n, this.s);
    }
    
    public double sample() {
        return this.sample;
    }
}
