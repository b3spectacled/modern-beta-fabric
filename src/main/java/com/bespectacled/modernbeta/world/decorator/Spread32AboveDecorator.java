package com.bespectacled.modernbeta.world.decorator;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.world.gen.decorator.AbstractRangeDecorator;
import net.minecraft.world.gen.decorator.DecoratorContext;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;

public class Spread32AboveDecorator extends AbstractRangeDecorator<NopeDecoratorConfig> {
    public Spread32AboveDecorator(Codec<NopeDecoratorConfig> configCodec) {
        super(configCodec);
    }

    @Override
    protected int getY(DecoratorContext context, Random random, NopeDecoratorConfig config, int y) {
        return random.nextInt(Math.max(y, 0) + 32);
    }
}
