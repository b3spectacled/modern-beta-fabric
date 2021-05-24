package com.bespectacled.modernbeta.world.decorator;

import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.world.decorator.noise.InfdevNoiseDecorator;
import com.mojang.serialization.Codec;

public class CountInfdevNoiseDecorator extends CountOldNoiseDecorator {
    public CountInfdevNoiseDecorator(Codec<CountOldNoiseDecoratorConfig> codec) {
        super(codec);
    }

    @Override
    public void setOctaves(PerlinOctaveNoise octaves) {
        this.noiseDecorator = new InfdevNoiseDecorator(octaves);
    }
}
