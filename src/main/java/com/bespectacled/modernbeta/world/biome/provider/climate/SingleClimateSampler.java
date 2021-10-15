package com.bespectacled.modernbeta.world.biome.provider.climate;

import com.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import com.bespectacled.modernbeta.api.world.biome.climate.Clime;

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
    public Clime sampleClime(int x, int z) {
        return new Clime(temp, rain);
    }

}
