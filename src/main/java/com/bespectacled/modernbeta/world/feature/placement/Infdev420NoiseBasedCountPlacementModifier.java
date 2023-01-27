package com.bespectacled.modernbeta.world.feature.placement;

import com.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.world.feature.placement.noise.Infdev420NoiseBasedCount;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.gen.placementmodifier.PlacementModifierType;

public class Infdev420NoiseBasedCountPlacementModifier extends ModernBetaNoiseBasedCountPlacementModifier {
    public static final Codec<Infdev420NoiseBasedCountPlacementModifier> MODIFIER_CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.INT.fieldOf("count").forGetter(arg -> arg.count),
            Codec.DOUBLE.fieldOf("extra_chance").forGetter(arg -> arg.extraChance),
            Codec.INT.fieldOf("extra_count").forGetter(arg -> arg.extraCount)
        ).apply(instance, Infdev420NoiseBasedCountPlacementModifier::of));
    
    protected Infdev420NoiseBasedCountPlacementModifier(int count, double extraChance, int extraCount) {
        super(count, extraChance, extraCount);
    }
    
    public static Infdev420NoiseBasedCountPlacementModifier of(int count, double extraChance, int extraCount) {
        return new Infdev420NoiseBasedCountPlacementModifier(count, extraChance, extraCount);
    }
    
    @Override
    public void setOctaves(PerlinOctaveNoise octaves) {
        this.noiseDecorator = new Infdev420NoiseBasedCount(octaves);
    }
    
    @Override
    public PlacementModifierType<?> getType() {
        return ModernBetaPlacementTypes.INFDEV_420_NOISE_BASED_COUNT;
    }

}
