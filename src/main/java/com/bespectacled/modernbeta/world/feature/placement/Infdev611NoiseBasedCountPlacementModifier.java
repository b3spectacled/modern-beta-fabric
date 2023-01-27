package com.bespectacled.modernbeta.world.feature.placement;

import com.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.world.feature.placement.noise.Infdev611NoiseBasedCount;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.gen.placementmodifier.PlacementModifierType;

public class Infdev611NoiseBasedCountPlacementModifier extends ModernBetaNoiseBasedCountPlacementModifier {
    public static final Codec<Infdev611NoiseBasedCountPlacementModifier> MODIFIER_CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.INT.fieldOf("count").forGetter(arg -> arg.count),
            Codec.DOUBLE.fieldOf("extra_chance").forGetter(arg -> arg.extraChance),
            Codec.INT.fieldOf("extra_count").forGetter(arg -> arg.extraCount)
        ).apply(instance, Infdev611NoiseBasedCountPlacementModifier::of));
    
    protected Infdev611NoiseBasedCountPlacementModifier(int count, double extraChance, int extraCount) {
        super(count, extraChance, extraCount);
    }
    
    public static Infdev611NoiseBasedCountPlacementModifier of(int count, double extraChance, int extraCount) {
        return new Infdev611NoiseBasedCountPlacementModifier(count, extraChance, extraCount);
    }
    
    @Override
    public void setOctaves(PerlinOctaveNoise octaves) {
        this.noiseDecorator = new Infdev611NoiseBasedCount(octaves);
    }
    
    @Override
    public PlacementModifierType<?> getType() {
        return ModernBetaPlacementTypes.INFDEV_611_NOISE_BASED_COUNT;
    }

}
