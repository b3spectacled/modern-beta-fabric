package mod.bespectacled.modernbeta.world.feature.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import mod.bespectacled.modernbeta.world.feature.placement.noise.NoiseBasedCountInfdev420;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;

public class NoiseBasedCountPlacementModifierInfdev420 extends NoiseBasedCountPlacementModifier {
    public static final Codec<NoiseBasedCountPlacementModifierInfdev420> MODIFIER_CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.INT.fieldOf("count").forGetter(arg -> arg.count),
            Codec.DOUBLE.fieldOf("extra_chance").forGetter(arg -> arg.extraChance),
            Codec.INT.fieldOf("extra_count").forGetter(arg -> arg.extraCount)
        ).apply(instance, NoiseBasedCountPlacementModifierInfdev420::of));
    
    protected NoiseBasedCountPlacementModifierInfdev420(int count, double extraChance, int extraCount) {
        super(count, extraChance, extraCount);
    }
    
    public static NoiseBasedCountPlacementModifierInfdev420 of(int count, double extraChance, int extraCount) {
        return new NoiseBasedCountPlacementModifierInfdev420(count, extraChance, extraCount);
    }
    
    @Override
    public void setOctaves(PerlinOctaveNoise octaves) {
        this.noiseDecorator = new NoiseBasedCountInfdev420(octaves);
    }
    
    @Override
    public PlacementModifierType<?> getType() {
        return ModernBetaPlacementTypes.INFDEV_420_NOISE_BASED_COUNT;
    }

}
