package com.bespectacled.modernbeta.world.biome.beta.climate;

import java.util.Random;

import com.bespectacled.modernbeta.api.world.biome.ClimateSampler;
import com.bespectacled.modernbeta.noise.SimplexOctaveNoise;
import com.bespectacled.modernbeta.util.chunk.ChunkCache;

import net.minecraft.util.math.MathHelper;

public class BetaClimateSampler implements ClimateSampler {
    private final SimplexOctaveNoise tempNoiseOctaves;
    private final SimplexOctaveNoise rainNoiseOctaves;
    private final SimplexOctaveNoise detailNoiseOctaves;
    
    private final ChunkCache<ClimateChunk> climateCache;
    private final ChunkCache<SkyChunk> skyCache;
    
    public BetaClimateSampler(long seed) {
        this.tempNoiseOctaves = new SimplexOctaveNoise(new Random(seed * 9871L), 4);
        this.rainNoiseOctaves = new SimplexOctaveNoise(new Random(seed * 39811L), 4);
        this.detailNoiseOctaves = new SimplexOctaveNoise(new Random(seed * 543321L), 2);
        
        this.climateCache = new ChunkCache<>(
            "climate", 
            1536, 
            true, 
            (chunkX, chunkZ) -> new ClimateChunk(chunkX, chunkZ, this)
        );
        
        this.skyCache = new ChunkCache<>(
            "sky", 
            256, 
            true, 
            (chunkX, chunkZ) -> new SkyChunk(chunkX, chunkZ, this)
        );
    }

    @Override
    public double sampleTemp(int x, int z) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        
        return this.climateCache.get(chunkX, chunkZ).sampleTemp(x, z);
    }

    @Override
    public double sampleRain(int x, int z) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        
        return this.climateCache.get(chunkX, chunkZ).sampleRain(x, z);
    }

    @Override
    public double sampleSkyTemp(int x, int z) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        
        return this.skyCache.get(chunkX, chunkZ).sampleTemp(x, z);
    }
    
    private void sampleClimateNoise(double arr[], int x, int z) {
        double temp = this.tempNoiseOctaves.sample(x, z, 0.02500000037252903D, 0.02500000037252903D, 0.25D);
        double rain = this.rainNoiseOctaves.sample(x, z, 0.05000000074505806D, 0.05000000074505806D, 0.33333333333333331D);
        double detail = this.detailNoiseOctaves.sample(x, z, 0.25D, 0.25D, 0.58823529411764708D);

        double d = detail * 1.1000000000000001D + 0.5D;
        double d1 = 0.01D;
        double d2 = 1.0D - d1;

        temp = (temp * 0.14999999999999999D + 0.69999999999999996D) * d2 + d * d1;

        d1 = 0.002D;
        d2 = 1.0D - d1;

        rain = (rain * 0.14999999999999999D + 0.5D) * d2 + d * d1;

        temp = 1.0D - (1.0D - temp) * (1.0D - temp);

        arr[0] = MathHelper.clamp(temp, 0.0, 1.0);
        arr[1] = MathHelper.clamp(rain, 0.0, 1.0);
    }
    
    private double sampleSkyTempNoise(int x, int z) {
        return this.tempNoiseOctaves.sample(x, z, 0.02500000037252903D, 0.02500000037252903D, 0.5D);
    }

    private class ClimateChunk {
        private final double temp[] = new double[256];
        private final double rain[] = new double[256];
        
        private ClimateChunk(int chunkX, int chunkZ, BetaClimateSampler climateSampler) {
            int startX = chunkX << 4;
            int startZ = chunkZ << 4;
            double[] tempRain = new double[2];
            
            int ndx = 0;
            for (int x = startX; x < startX + 16; ++x) {
                for (int z = startZ; z < startZ + 16; ++z) {
                    climateSampler.sampleClimateNoise(tempRain, x, z);
                    
                    this.temp[ndx] = tempRain[0];
                    this.rain[ndx] = tempRain[1];

                    ndx++;
                }
            }
        }
        
        private double sampleTemp(int x, int z) {
            return temp[(z & 0xF) + (x & 0xF) * 16];
        }
        
        private double sampleRain(int x, int z) {
            return rain[(z & 0xF) + (x & 0xF) * 16];
        }
    }
    
    private class SkyChunk {
        private final double temp[] = new double[256];
        
        private SkyChunk(int chunkX, int chunkZ, BetaClimateSampler climateSampler) {
            int startX = chunkX << 4;
            int startZ = chunkZ << 4;
            
            int ndx = 0;
            for (int x = startX; x < startX + 16; ++x) {
                for (int z = startZ; z < startZ + 16; ++z) {    
                    this.temp[ndx] = climateSampler.sampleSkyTempNoise(x, z);
                    
                    ndx++;
                }
            }
        }
        
        private double sampleTemp(int x, int z) {
            return temp[(z & 0xF) + (x & 0xF) * 16];
        }
    }
}
