package com.bespectacled.modernbeta.decorator;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.bespectacled.modernbeta.noise.AlphaNoiseGeneratorOctaves;
import com.mojang.serialization.Codec;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.decorator.SimpleDecorator;

public class CountAlphaNoiseDecorator extends SimpleDecorator<CountNoiseDecoratorConfig> {
	public AlphaNoiseGeneratorOctaves forestNoise;
	
	public CountAlphaNoiseDecorator(Codec<CountNoiseDecoratorConfig> codec) {
		super(codec);
	}
	
	public void setSeed(long seed) {
		forestNoise = new AlphaNoiseGeneratorOctaves(new Random(seed), 8);
	}

	@Override
	protected Stream<BlockPos> getPositions(Random random, CountNoiseDecoratorConfig config, BlockPos pos) {
		if (forestNoise == null) {
			forestNoise = new AlphaNoiseGeneratorOctaves(new Random(0), 8);
		}
		
		int chunkX = (int)pos.getX() / 16;
		int chunkZ = (int)pos.getZ() / 16;
		
		int noiseX = chunkX * 16;
		int noiseY = chunkZ * 16;
		
		double d = 0.5D;
		
		int noiseCount = (int)((forestNoise.func_806_a((double)noiseX * d, (double)noiseY * d) / 8D + random.nextDouble() * 4D + 4D) / 3D);
		if (noiseCount < 0) noiseCount = 0;
		
		int finalCount = noiseCount + config.density + ((random.nextFloat() < config.extraChance) ? config.extraCount : 0);
		
		// Returns just the count, actual block pos placement handled by Square decorator.
		// Unfortunately does not return the same tree density values, 
		// would require simulating part of the b1.7.3 generation algorithm using the rand var.
		return IntStream.range(0, finalCount).<BlockPos>mapToObj(integer -> pos);
	}


}
