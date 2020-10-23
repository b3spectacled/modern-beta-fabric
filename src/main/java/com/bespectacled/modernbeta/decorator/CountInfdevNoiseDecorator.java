package com.bespectacled.modernbeta.decorator;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.bespectacled.modernbeta.noise.OldNoiseGeneratorOctaves;
import com.mojang.serialization.Codec;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.decorator.SimpleDecorator;

public class CountInfdevNoiseDecorator extends SimpleDecorator<CountNoiseDecoratorConfig> {
    public OldNoiseGeneratorOctaves forestNoise;

    public CountInfdevNoiseDecorator(Codec<CountNoiseDecoratorConfig> codec) {
        super(codec);
    }

    public void setOctaves(OldNoiseGeneratorOctaves octaves) {
        forestNoise = octaves;
    }

    @Override
    protected Stream<BlockPos> getPositions(Random random, CountNoiseDecoratorConfig config, BlockPos pos) {
        if (forestNoise == null) {
            forestNoise = new OldNoiseGeneratorOctaves(random, 8, false);
        }

        int chunkX = (int) pos.getX() / 16;
        int chunkZ = (int) pos.getZ() / 16;
        
        chunkX <<= 4;
        chunkZ <<= 4;

        int noiseCount = (int) forestNoise.generateInfdevOctaves(
            (double) chunkX * 0.25D, 
            (double) chunkZ * 0.25D) << 3;
        
        // Returns just the count, actual block pos placement handled by Square
        // decorator.
        // Unfortunately does not return the same tree density values,
        // would require simulating part of the b1.7.3 generation algorithm using the
        // rand var.
        return IntStream.range(0, noiseCount).<BlockPos>mapToObj(integer -> pos);
    }

}
