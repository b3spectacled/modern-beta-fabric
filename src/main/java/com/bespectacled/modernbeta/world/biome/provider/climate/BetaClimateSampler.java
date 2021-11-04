package com.bespectacled.modernbeta.world.biome.provider.climate;

import java.util.Random;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import com.bespectacled.modernbeta.api.world.biome.climate.Clime;
import com.bespectacled.modernbeta.api.world.biome.climate.SkyClimateSampler;
import com.bespectacled.modernbeta.noise.SimplexOctaveNoise;
import com.bespectacled.modernbeta.util.chunk.ChunkCache;
import com.bespectacled.modernbeta.util.chunk.ClimateChunk;
import com.bespectacled.modernbeta.util.chunk.SkyClimateChunk;

import net.minecraft.util.math.MathHelper;

public class BetaClimateSampler implements ClimateSampler {
    private final SimplexOctaveNoise tempNoiseOctaves;
    private final SimplexOctaveNoise rainNoiseOctaves;
    private final SimplexOctaveNoise detailNoiseOctaves;
    
    private final ChunkCache<ClimateChunk> climateCache;
    
    public BetaClimateSampler(long seed) {
        this.tempNoiseOctaves = new SimplexOctaveNoise(new Random(seed * 9871L), 4);
        this.rainNoiseOctaves = new SimplexOctaveNoise(new Random(seed * 39811L), 4);
        this.detailNoiseOctaves = new SimplexOctaveNoise(new Random(seed * 543321L), 2);
        
        this.climateCache = new ChunkCache<>(
            "climate", 
            1536, 
            true, 
            (chunkX, chunkZ) -> new ClimateChunk(chunkX, chunkZ, this::sampleClimateNoise)
        );
    }
    
    @Override
    public Clime sampleClime(int x, int z) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        
        return this.climateCache.get(chunkX, chunkZ).sampleClime(x, z);
    }
    
    @Override
    public boolean sampleBiomeColor() {
        return ModernBeta.RENDER_CONFIG.biomeColorConfig.renderBetaBiomeColor;
    }
    
    private Clime sampleClimateNoise(int x, int z) {
        double temp = this.tempNoiseOctaves.sample(x, z, 0.02500000037252903D, 0.02500000037252903D, 0.25D);
        double rain = this.rainNoiseOctaves.sample(x, z, 0.05000000074505806D, 0.05000000074505806D, 0.33333333333333331D);
        double detail = this.detailNoiseOctaves.sample(x, z, 0.25D, 0.25D, 0.58823529411764708D);

        detail = detail * 1.1D + 0.5D;

        temp = (temp * 0.15D + 0.7D) * 0.99D + detail * 0.01D;
        rain = (rain * 0.15D + 0.5D) * 0.998D + detail * 0.002D;

        temp = 1.0D - (1.0D - temp) * (1.0D - temp);
        
        return new Clime(MathHelper.clamp(temp, 0.0, 1.0), MathHelper.clamp(rain, 0.0, 1.0));
    }
    
    public static class BetaSkyClimateSampler implements SkyClimateSampler {
        private final SimplexOctaveNoise tempNoiseOctaves;

        private final ChunkCache<SkyClimateChunk> skyClimateCache;
        
        public BetaSkyClimateSampler(long seed) {
            this.tempNoiseOctaves = new SimplexOctaveNoise(new Random(seed * 9871L), 4);
            
            this.skyClimateCache = new ChunkCache<>(
                "sky", 
                256, 
                true, 
                (chunkX, chunkZ) -> new SkyClimateChunk(chunkX, chunkZ, this::sampleSkyTempNoise)
            );
        }
        
        @Override
        public double sampleSkyTemp(int x, int z) {
            int chunkX = x >> 4;
            int chunkZ = z >> 4;
            
            return this.skyClimateCache.get(chunkX, chunkZ).sampleTemp(x, z);
        }
        
        @Override
        public boolean sampleSkyColor() {
            return ModernBeta.RENDER_CONFIG.biomeColorConfig.renderBetaSkyColor;
        }
        
        private double sampleSkyTempNoise(int x, int z) {
            return this.tempNoiseOctaves.sample(x, z, 0.02500000037252903D, 0.02500000037252903D, 0.5D);
        }
    }
}
