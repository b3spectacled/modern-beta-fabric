package com.bespectacled.modernbeta.world.decorator;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.world.decorator.noise.BetaNoiseDecorator;
import com.bespectacled.modernbeta.world.decorator.noise.OldNoiseDecorator;
import com.mojang.serialization.Codec;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.decorator.SimpleDecorator;

public abstract class CountOldNoiseDecorator extends SimpleDecorator<CountOldNoiseDecoratorConfig> {
    protected OldNoiseDecorator noiseDecorator = new BetaNoiseDecorator(new Random(0L));
    
    public CountOldNoiseDecorator(Codec<CountOldNoiseDecoratorConfig> codec) {
        super(codec);
    }
    
    @Override
    protected Stream<BlockPos> getPositions(Random random, CountOldNoiseDecoratorConfig config, BlockPos pos) {
        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;
        
        int count = this.noiseDecorator.sample(chunkX, chunkZ, random) + config.count + ((random.nextFloat() < config.extraChance) ? config.extraCount : 0);
        
        return IntStream.range(0, count).<BlockPos>mapToObj(integer -> pos);
    }
    
    public abstract void setOctaves(PerlinOctaveNoise octaves);
}