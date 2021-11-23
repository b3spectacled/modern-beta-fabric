package com.bespectacled.modernbeta.world.decorator;

import com.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.world.decorator.noise.Infdev420NoiseDecorator;
import com.mojang.serialization.Codec;

public class CountInfdev420NoiseDecorator extends CountOldNoiseDecorator {
    public CountInfdev420NoiseDecorator(Codec<CountOldNoiseDecoratorConfig> codec) {
        super(codec);
    }

    @Override
    public void setOctaves(PerlinOctaveNoise octaves) {
        this.noiseDecorator = new Infdev420NoiseDecorator(octaves);
    }
}
