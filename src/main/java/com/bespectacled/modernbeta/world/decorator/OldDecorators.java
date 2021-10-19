package com.bespectacled.modernbeta.world.decorator;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.Decorator;

public class OldDecorators {
    public static final CountBetaNoiseDecorator COUNT_BETA_NOISE = (CountBetaNoiseDecorator) register("count_beta_noise", new CountBetaNoiseDecorator(CountOldNoiseDecoratorConfig.CODEC));
    public static final CountAlphaNoiseDecorator COUNT_ALPHA_NOISE = (CountAlphaNoiseDecorator) register("count_alpha_noise", new CountAlphaNoiseDecorator(CountOldNoiseDecoratorConfig.CODEC));
    
    public static final CountInfdev611NoiseDecorator COUNT_INFDEV_611_NOISE = (CountInfdev611NoiseDecorator) register("count_infdev_611_noise", new CountInfdev611NoiseDecorator(CountOldNoiseDecoratorConfig.CODEC));
    public static final CountInfdev415NoiseDecorator COUNT_INFDEV_415_NOISE = (CountInfdev415NoiseDecorator) register("count_infdev_415_noise", new CountInfdev415NoiseDecorator(CountOldNoiseDecoratorConfig.CODEC));
    public static final CountInfdev420NoiseDecorator COUNT_INFDEV_420_NOISE = (CountInfdev420NoiseDecorator) register("count_infdev_420_noise", new CountInfdev420NoiseDecorator(CountOldNoiseDecoratorConfig.CODEC));

    private static Decorator<?> register(String id, Decorator<?> decorator) {
        return Registry.register(Registry.DECORATOR, ModernBeta.createId(id), decorator);
    }
}
