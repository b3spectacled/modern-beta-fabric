package com.bespectacled.modernbeta.world.biome.provider.climate;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import com.bespectacled.modernbeta.api.world.biome.climate.Clime;
import com.bespectacled.modernbeta.api.world.biome.climate.SkyClimateSampler;
import com.bespectacled.modernbeta.util.chunk.ChunkCache;
import com.bespectacled.modernbeta.util.chunk.ClimateChunk;
import com.bespectacled.modernbeta.util.mersenne.MTRandom;
import com.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;

import net.minecraft.util.math.MathHelper;

public class PEClimateSampler implements ClimateSampler, SkyClimateSampler {
    private final PerlinOctaveNoise tempNoiseOctaves;
    private final PerlinOctaveNoise rainNoiseOctaves;
    private final PerlinOctaveNoise detailNoiseOctaves;
    
    private final ChunkCache<ClimateChunk> climateCache;
    
    private final double tempScale;
    private final double rainScale;
    private final double detailScale;
    
    public PEClimateSampler(long seed) {
        this(seed, 1D);
    }
    
    public PEClimateSampler(long seed, double climateScale) {
        this.tempNoiseOctaves = new PerlinOctaveNoise(new MTRandom(seed * 9871L), 4, true);
        this.rainNoiseOctaves = new PerlinOctaveNoise(new MTRandom(seed * 39811L), 4, true);
        this.detailNoiseOctaves = new PerlinOctaveNoise(new MTRandom(seed * 543321L), 2, true);
        
        this.climateCache = new ChunkCache<>(
            "climate", 
            1536,
            true,
            (chunkX, chunkZ) -> new ClimateChunk(chunkX, chunkZ, this::sampleClimateNoise)
        );
        
        this.tempScale = 0.02500000037252903D / climateScale;
        this.rainScale = 0.05000000074505806D / climateScale;
        this.detailScale = 0.25D / climateScale;
    }
    
    @Override
    public Clime sampleClime(int x, int z) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        
        return this.climateCache.get(chunkX, chunkZ).sampleClime(x, z);
    }
    
    @Override
    public double sampleSkyTemp(int x, int z) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        
        return this.climateCache.get(chunkX, chunkZ).sampleClime(x, z).temp();
    }
    
    @Override
    public boolean sampleBiomeColor() {
        return ModernBeta.RENDER_CONFIG.configBiomeColor.renderPEBetaBiomeColor;
    }
    
    @Override
    public boolean sampleSkyColor() {
        return ModernBeta.RENDER_CONFIG.configBiomeColor.renderPEBetaSkyColor;
    }
    
    private Clime sampleClimateNoise(int x, int z) {
        double temp = this.tempNoiseOctaves.sample(x, z, this.tempScale, this.tempScale);
        double rain = this.rainNoiseOctaves.sample(x, z, this.rainScale, this.rainScale);
        double detail = this.detailNoiseOctaves.sample(x, z, this.detailScale, this.detailScale);

        detail = detail * 1.1D + 0.5D;

        temp = (temp * 0.15D + 0.7D) * 0.99D + detail * 0.01D;
        rain = (rain * 0.15D + 0.5D) * 0.998D + detail * 0.002D;

        temp = 1.0D - (1.0D - temp) * (1.0D - temp);
        
        return new Clime(MathHelper.clamp(temp, 0.0, 1.0), MathHelper.clamp(rain, 0.0, 1.0));
    }
}
