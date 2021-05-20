package com.bespectacled.modernbeta.world.decorator;

import java.util.Random;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.world.decorator.noise.AlphaNoiseDecorator;
import com.mojang.serialization.Codec;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.decorator.AbstractCountDecorator;

public class CountAlphaNoiseDecorator extends AbstractCountDecorator<CountNoiseDecoratorConfig> {
    private AlphaNoiseDecorator noiseDecorator;

    public CountAlphaNoiseDecorator(Codec<CountNoiseDecoratorConfig> codec) {
        super(codec);
    }

    public void setOctaves(PerlinOctaveNoise octaves) {
        this.noiseDecorator = new AlphaNoiseDecorator(octaves);
    }

    @Override
    protected int getCount(Random random, CountNoiseDecoratorConfig config, BlockPos pos) {
        if (noiseDecorator == null) {
            noiseDecorator = new AlphaNoiseDecorator(random);
        }
        
        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;
        
        int noiseCount = this.noiseDecorator.sample(chunkX, chunkZ, random);

        int finalCount = noiseCount + config.density + ((random.nextFloat() < config.extraChance) ? config.extraCount : 0);

        // Returns just the count, actual block pos placement handled by Square
        // decorator.
        // Unfortunately does not return the same tree density values,
        // would require simulating part of the b1.7.3 generation algorithm using the
        // rand var.
        return finalCount;
    }

}
