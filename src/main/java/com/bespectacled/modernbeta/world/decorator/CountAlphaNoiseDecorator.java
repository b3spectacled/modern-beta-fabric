package com.bespectacled.modernbeta.world.decorator;

import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.world.decorator.noise.AlphaNoiseDecorator;
import com.mojang.serialization.Codec;

public class CountAlphaNoiseDecorator extends CountOldNoiseDecorator {
    public CountAlphaNoiseDecorator(Codec<CountOldNoiseDecoratorConfig> codec) {
        super(codec);
    }

    @Override
    public void setOctaves(PerlinOctaveNoise octaves) {
        this.noiseDecorator = new AlphaNoiseDecorator(octaves);
    }
}
