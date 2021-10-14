package com.bespectacled.modernbeta.world.biome.provider.climate;

import com.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;

public class SingleClimateSampler implements ClimateSampler {
    private final double temp;
    private final double rain;
    
    public SingleClimateSampler(Biome biome) {
        this.temp = MathHelper.clamp(biome.getTemperature(), 0.0, 1.0);
        this.rain = MathHelper.clamp(biome.getDownfall(), 0.0, 1.0);
    }

    @Override
    public double sampleTemp(int x, int z) {
        return this.temp;
    }

    @Override
    public double sampleRain(int x, int z) {
        return this.rain;
    }

}
