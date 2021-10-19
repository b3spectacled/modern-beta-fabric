package com.bespectacled.modernbeta.world.decorator;

import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.world.decorator.noise.Infdev611NoiseDecorator;
import com.mojang.serialization.Codec;

public class CountInfdev611NoiseDecorator extends CountOldNoiseDecorator {
    public CountInfdev611NoiseDecorator(Codec<CountOldNoiseDecoratorConfig> codec) {
        super(codec);
    }

    @Override
    public void setOctaves(PerlinOctaveNoise octaves) {
        this.noiseDecorator = new Infdev611NoiseDecorator(octaves);
    }
}
