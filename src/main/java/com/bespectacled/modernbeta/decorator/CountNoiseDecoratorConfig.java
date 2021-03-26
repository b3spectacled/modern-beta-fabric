package com.bespectacled.modernbeta.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.gen.decorator.DecoratorConfig;

public class CountNoiseDecoratorConfig implements DecoratorConfig {
    public static final Codec<CountNoiseDecoratorConfig> CODEC;
    public final int density;
    public final float extraChance;
    public final int extraCount;

    public CountNoiseDecoratorConfig(int density, float extraChance, int extraCount) {
        this.density = density;
        this.extraChance = extraChance;
        this.extraCount = extraCount;
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("density")
                        .forGetter(countNoiseDecoratorConfig -> countNoiseDecoratorConfig.density),
                Codec.FLOAT.fieldOf("extra_chance")
                        .forGetter(countNoiseDecoratorConfig -> countNoiseDecoratorConfig.extraChance),
                Codec.INT.fieldOf("extra_count")
                        .forGetter(countNoiseDecoratorConfig -> countNoiseDecoratorConfig.extraCount)

        ).apply(instance, CountNoiseDecoratorConfig::new));
    }
}
