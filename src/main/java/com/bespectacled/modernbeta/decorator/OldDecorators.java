package com.bespectacled.modernbeta.decorator;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.Decorator;

public class OldDecorators {
    public static final CountBetaNoiseDecorator COUNT_BETA_NOISE_DECORATOR = (CountBetaNoiseDecorator) register("count_beta_noise", new CountBetaNoiseDecorator(CountNoiseDecoratorConfig.CODEC));
    public static final CountAlphaNoiseDecorator COUNT_ALPHA_NOISE_DECORATOR = (CountAlphaNoiseDecorator) register("count_alpha_noise", new CountAlphaNoiseDecorator(CountNoiseDecoratorConfig.CODEC));
    public static final CountInfdevNoiseDecorator COUNT_INFDEV_NOISE_DECORATOR = (CountInfdevNoiseDecorator) register("count_infdev_noise", new CountInfdevNoiseDecorator(CountNoiseDecoratorConfig.CODEC));
    
    private static Decorator<?> register(String id, Decorator<?> decorator) {
        return Registry.register(Registry.DECORATOR, ModernBeta.createId(id), decorator);
    }
}
