package com.bespectacled.modernbeta.world.decorator;

import java.util.Random;

import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.world.decorator.noise.BetaNoiseDecorator;
import com.bespectacled.modernbeta.world.decorator.noise.OldNoiseDecorator;
import com.mojang.serialization.Codec;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.decorator.AbstractCountDecorator;

public abstract class CountOldNoiseDecorator extends AbstractCountDecorator<CountOldNoiseDecoratorConfig> {
    protected OldNoiseDecorator noiseDecorator = new BetaNoiseDecorator(new Random(0L));
    
    public CountOldNoiseDecorator(Codec<CountOldNoiseDecoratorConfig> codec) {
        super(codec);
    }
    
    @Override
    protected int getCount(Random random, CountOldNoiseDecoratorConfig config, BlockPos pos) {
        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;
        
        return this.noiseDecorator.sample(chunkX, chunkZ, random) + config.count + ((random.nextFloat() < config.extraChance) ? config.extraCount : 0);
    }
    
    public abstract void setOctaves(PerlinOctaveNoise octaves);
}
