package com.bespectacled.modernbeta.world.decorator;

import java.util.Random;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorContext;
import net.minecraft.world.gen.decorator.HeightmapDecoratorConfig;

public class SpreadDoubleHeightmapDecorator extends Decorator<HeightmapDecoratorConfig> {

    public SpreadDoubleHeightmapDecorator(Codec<HeightmapDecoratorConfig> codec) {
        super(codec);
    }

    @Override
    public Stream<BlockPos> getPositions(DecoratorContext context, Random random, HeightmapDecoratorConfig config, BlockPos pos) {
        int x = pos.getX();
        int z = pos.getZ();
        
        int y = context.getTopY(config.heightmap, x, z);
        if (y == context.getBottomY()) {
            return Stream.of(new BlockPos[0]);
        }
        
        return Stream.of(new BlockPos(x, context.getBottomY() + random.nextInt((y - context.getBottomY()) * 2), z));
    }
}
