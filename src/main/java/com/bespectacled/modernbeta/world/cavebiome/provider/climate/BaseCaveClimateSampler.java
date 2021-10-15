package com.bespectacled.modernbeta.world.cavebiome.provider.climate;

import java.util.Random;

import com.bespectacled.modernbeta.api.world.biome.climate.CaveClimateSampler;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;

import net.minecraft.util.math.MathHelper;

public class BaseCaveClimateSampler implements CaveClimateSampler {
    private final PerlinOctaveNoise climateNoiseOctaves;
    private final PerlinOctaveNoise detailNoiseOctaves;
    
    private final int verticalScale;
    private final int horizontalScale;
    
    public BaseCaveClimateSampler(long seed) {
        this(seed, 2, 8);
    }
    
    public BaseCaveClimateSampler(long seed, int verticalScale, int horizontalScale) {
        this.climateNoiseOctaves = new PerlinOctaveNoise(new Random(seed * 39811L), 4, true);
        this.detailNoiseOctaves = new PerlinOctaveNoise(new Random(seed * 543321L), 2, true);
        
        this.verticalScale = verticalScale;
        this.horizontalScale = horizontalScale;
    }
    
    @Override
    public double sample(int x, int y, int z) {
        // 2 Octave range: -1.4281536012354779/1.4303502066204832
        // 4 Octave range: -7.6556244276339145/7.410194314594666
        
        double climateNoise = this.climateNoiseOctaves.sample(
            x / (double)this.horizontalScale, 
            y / (double)this.verticalScale, 
            z / (double)this.horizontalScale
        );
        double detailNoise = this.detailNoiseOctaves.sample(
            x / (double)this.horizontalScale, 
            y / (double)this.verticalScale, 
            z / (double)this.horizontalScale
        );
        
        detailNoise /= 1.43D;
        climateNoise = (climateNoise + 0.15D) / 7.5D;
        
        climateNoise = climateNoise * 0.99D + detailNoise * 0.01D;
        
        return MathHelper.clamp(climateNoise, -1.0, 1.0);
    }

}
