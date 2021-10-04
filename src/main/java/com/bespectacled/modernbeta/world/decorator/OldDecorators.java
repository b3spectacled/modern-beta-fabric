package com.bespectacled.modernbeta.world.decorator;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;

public class OldDecorators {
    public static final CountBetaNoiseDecorator COUNT_BETA_NOISE = (CountBetaNoiseDecorator) register("count_beta_noise", new CountBetaNoiseDecorator(CountOldNoiseDecoratorConfig.CODEC));
    public static final CountAlphaNoiseDecorator COUNT_ALPHA_NOISE = (CountAlphaNoiseDecorator) register("count_alpha_noise", new CountAlphaNoiseDecorator(CountOldNoiseDecoratorConfig.CODEC));
    public static final CountInfdevNoiseDecorator COUNT_INFDEV_NOISE = (CountInfdevNoiseDecorator) register("count_infdev_noise", new CountInfdevNoiseDecorator(CountOldNoiseDecoratorConfig.CODEC));
    public static final Decorator<NopeDecoratorConfig> SPREAD_32_ABOVE = (Spread32AboveDecorator) register("spread_32_above", new Spread32AboveDecorator(NopeDecoratorConfig.CODEC));
    
    
    private static Decorator<?> register(String id, Decorator<?> decorator) {
        return Registry.register(Registry.DECORATOR, ModernBeta.createId(id), decorator);
    }
}
