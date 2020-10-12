package com.bespectacled.modernbeta.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.gen.decorator.DecoratorConfig;

public class CountNoiseDecoratorConfig implements DecoratorConfig {
    public static final Codec<CountNoiseDecoratorConfig> CODEC;
    public final int density;
    public final long seed;
    public final float extraChance;
    public final int extraCount;

    public CountNoiseDecoratorConfig(int density, long seed, float extraChance, int extraCount) {
        this.density = density;
        this.seed = seed;
        this.extraChance = extraChance;
        this.extraCount = extraCount;
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("density")
                        .forGetter(countBetaNoiseDecoratorConfig -> countBetaNoiseDecoratorConfig.density),
                Codec.LONG.fieldOf("seed")
                        .forGetter(countBetaNoiseDecoratorConfig -> countBetaNoiseDecoratorConfig.seed),
                Codec.FLOAT.fieldOf("extra_chance")
                        .forGetter(countBetaNoiseDecoratorConfig -> countBetaNoiseDecoratorConfig.extraChance),
                Codec.INT.fieldOf("extra_count")
                        .forGetter(countBetaNoiseDecoratorConfig -> countBetaNoiseDecoratorConfig.extraCount)

        ).apply(instance, CountNoiseDecoratorConfig::new));
    }
}
