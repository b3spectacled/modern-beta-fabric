package com.bespectacled.modernbeta.world.feature.placement;

import java.util.Random;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.decorator.DecoratorContext;
import net.minecraft.world.gen.decorator.PlacementModifier;
import net.minecraft.world.gen.decorator.PlacementModifierType;

public class Spread32AbovePlacementModifier extends PlacementModifier {
    private static final Spread32AbovePlacementModifier INSTANCE = new Spread32AbovePlacementModifier();
    public static final Codec<Spread32AbovePlacementModifier> MODIFIER_CODEC = Codec.unit(() -> INSTANCE);
    
    public static Spread32AbovePlacementModifier of() {
        return INSTANCE;
    }
    
    @Override
    public Stream<BlockPos> getPositions(DecoratorContext context, Random random, BlockPos pos) {
        return Stream.of(pos.withY(random.nextInt(Math.max(pos.getY(), 0) + 32)));
    }

    @Override
    public PlacementModifierType<?> getType() {
        return OldPlacementTypes.SPREAD_32_ABOVE;
    }

}
