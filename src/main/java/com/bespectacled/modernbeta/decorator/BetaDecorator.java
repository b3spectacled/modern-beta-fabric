package com.bespectacled.modernbeta.decorator;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorConfig;

public class BetaDecorator {
    public static final Map<Identifier, Decorator> DECORATORS = new HashMap<>();

    public static final CountBetaNoiseDecorator COUNT_BETA_NOISE_DECORATOR = add("count_beta_noise",
            new CountBetaNoiseDecorator(CountNoiseDecoratorConfig.CODEC));

    public static final CountAlphaNoiseDecorator COUNT_ALPHA_NOISE_DECORATOR = add("count_alpha_noise",
            new CountAlphaNoiseDecorator(CountNoiseDecoratorConfig.CODEC));
    
    public static final CountInfdevNoiseDecorator COUNT_INFDEV_NOISE_DECORATOR = add("count_infdev_noise",
            new CountInfdevNoiseDecorator(CountNoiseDecoratorConfig.CODEC));

    static <D extends Decorator<? extends DecoratorConfig>> D add(String name, D decorator) {
        DECORATORS.put(new Identifier(ModernBeta.ID, name), decorator);
        return decorator;
    }

    public static void register() {
        for (Identifier id : DECORATORS.keySet()) {
            Registry.register(Registry.DECORATOR, id, DECORATORS.get(id));
        }

        ModernBeta.LOGGER.log(Level.INFO, "Registered decorators.");
    }
}
