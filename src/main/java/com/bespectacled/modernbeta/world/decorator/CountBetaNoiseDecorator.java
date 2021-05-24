package com.bespectacled.modernbeta.world.decorator;

import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.world.decorator.noise.BetaNoiseDecorator;
import com.mojang.serialization.Codec;

public class CountBetaNoiseDecorator extends CountOldNoiseDecorator {
    public CountBetaNoiseDecorator(Codec<CountOldNoiseDecoratorConfig> codec) {
        super(codec);
    }

    @Override
    public void setOctaves(PerlinOctaveNoise octaves) {
        this.noiseDecorator = new BetaNoiseDecorator(octaves);
    }
}
