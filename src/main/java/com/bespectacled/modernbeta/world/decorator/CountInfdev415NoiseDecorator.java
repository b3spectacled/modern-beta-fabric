package com.bespectacled.modernbeta.world.decorator;

import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.world.decorator.noise.Infdev415NoiseDecorator;
import com.mojang.serialization.Codec;

public class CountInfdev415NoiseDecorator extends CountOldNoiseDecorator {
    public CountInfdev415NoiseDecorator(Codec<CountOldNoiseDecoratorConfig> codec) {
        super(codec);
    }

    @Override
    public void setOctaves(PerlinOctaveNoise octaves) {
        this.noiseDecorator = new Infdev415NoiseDecorator(octaves);
    }
}
