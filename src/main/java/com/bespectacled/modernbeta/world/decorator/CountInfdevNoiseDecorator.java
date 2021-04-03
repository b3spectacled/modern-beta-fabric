package com.bespectacled.modernbeta.world.decorator;

import java.util.Random;

import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.mojang.serialization.Codec;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.decorator.AbstractCountDecorator;

public class CountInfdevNoiseDecorator extends AbstractCountDecorator<CountNoiseDecoratorConfig> {
    public PerlinOctaveNoise forestNoise;

    public CountInfdevNoiseDecorator(Codec<CountNoiseDecoratorConfig> codec) {
        super(codec);
    }

    public void setOctaves(PerlinOctaveNoise octaves) {
        forestNoise = octaves;
    }

    @Override
    protected int getCount(Random random, CountNoiseDecoratorConfig config, BlockPos pos) {
        if (forestNoise == null) {
            forestNoise = new PerlinOctaveNoise(random, 8, true);
        }

        int chunkX = (int) pos.getX() / 16;
        int chunkZ = (int) pos.getZ() / 16;
        
        chunkX <<= 4;
        chunkZ <<= 4;

        int noiseCount = (int) forestNoise.sample(
            (double) chunkX * 0.25D, 
            (double) chunkZ * 0.25D) << 3;
        
        // Returns just the count, actual block pos placement handled by Square
        // decorator.
        // Unfortunately does not return the same tree density values,
        // would require simulating part of the b1.7.3 generation algorithm using the
        // rand var.
        return noiseCount;
    }

}
