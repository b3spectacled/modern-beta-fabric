package com.bespectacled.modernbeta.world.gen.sampler;

import java.util.Random;

import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.chunk.ChunkCache;
import com.bespectacled.modernbeta.util.chunk.InterpolatedNoiseChunk;

public class InterpolatedNoiseSampler {
    protected final PerlinOctaveNoise minLimitNoiseOctaves;
    protected final PerlinOctaveNoise maxLimitNoiseOctaves;
    protected final PerlinOctaveNoise mainNoiseOctaves;
    
    protected final double coordinateScale;
    protected final double heightScale;
    
    protected final double mainNoiseScaleXZ;
    protected final double mainNoiseScaleY;
    
    protected final double limitScale;
    
    protected final int noiseSizeX;
    protected final int noiseSizeY;
    protected final int noiseSizeZ;
    protected final int noiseMinY;
    
    protected final double densityScale;
    
    private ChunkCache<InterpolatedNoiseChunk> chunkCache;

    public InterpolatedNoiseSampler(
        Random random,
        double coordinateScale,
        double heightScale,
        double mainNoiseScaleXZ,
        double mainNoiseScaleY,
        double limitScale,
        double densityScale,
        int noiseSizeX,
        int noiseSizeY,
        int noiseSizeZ,
        int noiseMinY
    ) {
        this.minLimitNoiseOctaves = new PerlinOctaveNoise(random, 16, true);
        this.maxLimitNoiseOctaves = new PerlinOctaveNoise(random, 16, true);
        this.mainNoiseOctaves = new PerlinOctaveNoise(random, 8, true);
        
        this.coordinateScale = coordinateScale;
        this.heightScale = heightScale;
        
        this.mainNoiseScaleXZ = mainNoiseScaleXZ;
        this.mainNoiseScaleY = mainNoiseScaleY;
        
        this.limitScale = limitScale;
        
        this.noiseSizeX = noiseSizeX;
        this.noiseSizeY = noiseSizeY;
        this.noiseSizeZ = noiseSizeZ;
        this.noiseMinY = noiseMinY;
        
        // Density norm (sum of 16 octaves of noise / limitScale => [-128, 128])
        this.densityScale = densityScale;
        
        this.chunkCache = new ChunkCache<>(
            "interpolated_noise",
            512,
            true,
            (chunkX, chunkZ) -> {
                int noiseStartX = chunkX * this.noiseSizeX;
                int noiseStartZ = chunkZ * this.noiseSizeZ;
                int noiseResY = this.noiseSizeY + 1;
                
                double[][] noise = new double[this.noiseSizeX * this.noiseSizeZ][noiseResY];
                
                int ndx = 0;
                for (int x = 0; x < this.noiseSizeX; ++x) {
                    int noiseX = x + noiseStartX;
                    
                    for (int z = 0; z < this.noiseSizeZ; ++z) {
                        int noiseZ = z + noiseStartZ;
                        
                        noise[ndx++] = this.sampleNoiseColumn(noiseX, noiseZ);
                    }
                }
                
                return new InterpolatedNoiseChunk(noise, this.noiseSizeX);
            }
        );
    }
    
    public double getDensityScale() {
        return this.densityScale;
    }
    
    public double sample(int chunkX, int chunkZ, int localNoiseX, int noiseY, int localNoiseZ) {
        int modNoiseX = Math.floorMod(localNoiseX, this.noiseSizeX);
        int modNoiseZ = Math.floorMod(localNoiseZ, this.noiseSizeZ);
        
        // If local noise coordinate is larger than noise cell size,
        // then start in the next chunk.
        if (localNoiseX >= this.noiseSizeX) chunkX++;
        if (localNoiseZ >= this.noiseSizeZ) chunkZ++;
        
        return this.chunkCache.get(chunkX, chunkZ).sample(modNoiseX, noiseY, modNoiseZ);
    }
    
    protected double[] sampleNoiseColumn(int noiseX, int noiseZ) {
        double[] buffer = new double[this.noiseSizeY + 1];
        
        for (int y = 0; y < buffer.length; ++y) {
            int noiseY = y + this.noiseMinY;
            
            buffer[y] = this.sampleNoise(noiseX, noiseY, noiseZ);
        }
        
        return buffer;
    }
    
    protected double sampleNoise(int noiseX, int noiseY, int noiseZ) {
        double density;
        
        double mainNoise = (this.mainNoiseOctaves.sample(
            noiseX, noiseY, noiseZ, 
            this.coordinateScale / this.mainNoiseScaleXZ, 
            this.heightScale / this.mainNoiseScaleY, 
            this.coordinateScale / this.mainNoiseScaleXZ
        ) / 10D + 1.0D) / 2D;
        
        if (mainNoise < 0.0D) {
            density = this.minLimitNoiseOctaves.sample(
                noiseX, noiseY, noiseZ, 
                this.coordinateScale, 
                this.heightScale, 
                this.coordinateScale
            ) / this.limitScale;
            
        } else if (mainNoise > 1.0D) {
            density = this.maxLimitNoiseOctaves.sample(
                noiseX, noiseY, noiseZ, 
                this.coordinateScale, 
                this.heightScale, 
                this.coordinateScale
            ) / this.limitScale;
            
        } else {
            double minLimitNoise = this.minLimitNoiseOctaves.sample(
                noiseX, noiseY, noiseZ, 
                this.coordinateScale, 
                this.heightScale, 
                this.coordinateScale
            ) / this.limitScale;
            
            double maxLimitNoise = this.maxLimitNoiseOctaves.sample(
                noiseX, noiseY, noiseZ, 
                this.coordinateScale, 
                this.heightScale, 
                this.coordinateScale
            ) / this.limitScale;
            
            density = minLimitNoise + (maxLimitNoise - minLimitNoise) * mainNoise;
        }
        
        return density / this.densityScale;
    }
}
