package com.bespectacled.modernbeta.decorator;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.mojang.serialization.Codec;

import net.minecraft.client.render.chunk.ChunkBuilder.ChunkData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.gen.decorator.SimpleDecorator;

public class CountBetaNoiseDecorator extends SimpleDecorator<CountNoiseDecoratorConfig> {
    public PerlinOctaveNoise forestNoise;

    public CountBetaNoiseDecorator(Codec<CountNoiseDecoratorConfig> codec) {
        super(codec);
    }

    public void setOctaves(PerlinOctaveNoise octaves) {
        forestNoise = octaves;
    }

    @Override
    protected Stream<BlockPos> getPositions(Random random, CountNoiseDecoratorConfig config, BlockPos pos) {
        if (forestNoise == null) {
            forestNoise = new PerlinOctaveNoise(random, 8, false);
        }
        
        int chunkX = (int) pos.getX() / 16;
        int chunkZ = (int) pos.getZ() / 16;
       

        int noiseX = chunkX * 16;
        int noiseZ = chunkZ * 16;

        double d = 0.5D;

        int noiseCount = (int) ((forestNoise.sampleBetaOctaves((double) noiseX * d, (double) noiseZ * d) / 8D
                + random.nextDouble() * 4D + 4D) / 3D);

        int finalCount = noiseCount + config.density
                + ((random.nextFloat() < config.extraChance) ? config.extraCount : 0);

        // Returns just the count, actual block pos placement handled by Square
        // decorator.
        // Unfortunately does not return the same tree density values,
        // would require simulating part of the b1.7.3 generation algorithm using the
        // rand var.
        return IntStream.range(0, finalCount).<BlockPos>mapToObj(integer -> pos);
    }

}
