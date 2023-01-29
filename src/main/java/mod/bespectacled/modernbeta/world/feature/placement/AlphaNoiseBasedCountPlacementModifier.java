package mod.bespectacled.modernbeta.world.feature.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import mod.bespectacled.modernbeta.world.feature.placement.noise.AlphaNoiseBasedCount;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;

public class AlphaNoiseBasedCountPlacementModifier extends ModernBetaNoiseBasedCountPlacementModifier {
    public static final Codec<AlphaNoiseBasedCountPlacementModifier> MODIFIER_CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.INT.fieldOf("count").forGetter(arg -> arg.count),
            Codec.DOUBLE.fieldOf("extra_chance").forGetter(arg -> arg.extraChance),
            Codec.INT.fieldOf("extra_count").forGetter(arg -> arg.extraCount)
        ).apply(instance, AlphaNoiseBasedCountPlacementModifier::of));
    
    protected AlphaNoiseBasedCountPlacementModifier(int count, double extraChance, int extraCount) {
        super(count, extraChance, extraCount);
    }
    
    public static AlphaNoiseBasedCountPlacementModifier of(int count, double extraChance, int extraCount) {
        return new AlphaNoiseBasedCountPlacementModifier(count, extraChance, extraCount);
    }
    
    @Override
    public void setOctaves(PerlinOctaveNoise octaves) {
        this.noiseDecorator = new AlphaNoiseBasedCount(octaves);
    }
    
    @Override
    public PlacementModifierType<?> getType() {
        return ModernBetaPlacementTypes.ALPHA_NOISE_BASED_COUNT;
    }

}
