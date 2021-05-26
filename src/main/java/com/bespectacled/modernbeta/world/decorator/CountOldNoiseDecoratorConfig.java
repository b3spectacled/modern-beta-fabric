package com.bespectacled.modernbeta.world.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.gen.decorator.DecoratorConfig;

public class CountOldNoiseDecoratorConfig implements DecoratorConfig {
    public static final Codec<CountOldNoiseDecoratorConfig> CODEC;
    public final int count;
    public final float extraChance;
    public final int extraCount;

    public CountOldNoiseDecoratorConfig(int count, float extraChance, int extraCount) {
        this.count = count;
        this.extraChance = extraChance;
        this.extraCount = extraCount;
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("count")
                    .forGetter(countNoiseDecoratorConfig -> countNoiseDecoratorConfig.count),
            Codec.FLOAT.fieldOf("extra_chance")
                    .forGetter(countNoiseDecoratorConfig -> countNoiseDecoratorConfig.extraChance),
            Codec.INT.fieldOf("extra_count")
                    .forGetter(countNoiseDecoratorConfig -> countNoiseDecoratorConfig.extraCount)

        ).apply(instance, CountOldNoiseDecoratorConfig::new));
    }
}
