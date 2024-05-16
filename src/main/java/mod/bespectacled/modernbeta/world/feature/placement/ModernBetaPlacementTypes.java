package mod.bespectacled.modernbeta.world.feature.placement;

import com.mojang.serialization.Codec;

import mod.bespectacled.modernbeta.ModernBeta;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;

public class ModernBetaPlacementTypes {
    public static final PlacementModifierType<NoiseBasedCountPlacementModifierBeta> BETA_NOISE_BASED_COUNT;
    public static final PlacementModifierType<NoiseBasedCountPlacementModifierAlpha> ALPHA_NOISE_BASED_COUNT;
    public static final PlacementModifierType<NoiseBasedCountPlacementModifierInfdev325> INFDEV_325_NOISE_BASED_COUNT;
    public static final PlacementModifierType<NoiseBasedCountPlacementModifierInfdev415> INFDEV_415_NOISE_BASED_COUNT;
    public static final PlacementModifierType<NoiseBasedCountPlacementModifierInfdev420> INFDEV_420_NOISE_BASED_COUNT;
    public static final PlacementModifierType<NoiseBasedCountPlacementModifierInfdev611> INFDEV_611_NOISE_BASED_COUNT;
    
    public static final PlacementModifierType<HeightmapSpreadDoublePlacementModifier> HEIGHTMAP_SPREAD_DOUBLE;
    
    private static <P extends PlacementModifier> PlacementModifierType<P> register(String id, Codec<P> codec) {
        return Registry.register(Registries.PLACEMENT_MODIFIER_TYPE, ModernBeta.createId(id), () -> codec);
    }
    
    public static void register() {}
    
    static {
        BETA_NOISE_BASED_COUNT = register("beta_noise_based_count", NoiseBasedCountPlacementModifierBeta.MODIFIER_CODEC);
        ALPHA_NOISE_BASED_COUNT = register("alpha_noise_based_count", NoiseBasedCountPlacementModifierAlpha.MODIFIER_CODEC);
        INFDEV_325_NOISE_BASED_COUNT = register("infdev_325_noise_based_count", NoiseBasedCountPlacementModifierInfdev325.MODIFIER_CODEC);
        INFDEV_415_NOISE_BASED_COUNT = register("infdev_415_noise_based_count", NoiseBasedCountPlacementModifierInfdev415.MODIFIER_CODEC);
        INFDEV_420_NOISE_BASED_COUNT = register("infdev_420_noise_based_count", NoiseBasedCountPlacementModifierInfdev420.MODIFIER_CODEC);
        INFDEV_611_NOISE_BASED_COUNT = register("infdev_611_noise_based_count", NoiseBasedCountPlacementModifierInfdev611.MODIFIER_CODEC);
    
        HEIGHTMAP_SPREAD_DOUBLE = register("heightmap_spread_double", HeightmapSpreadDoublePlacementModifier.MODIFIER_CODEC);
    }
}