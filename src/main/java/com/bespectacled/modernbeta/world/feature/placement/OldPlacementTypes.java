package com.bespectacled.modernbeta.world.feature.placement;

import com.bespectacled.modernbeta.ModernBeta;
import com.mojang.serialization.Codec;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.PlacementModifier;
import net.minecraft.world.gen.decorator.PlacementModifierType;

public class OldPlacementTypes<P extends PlacementModifier> {
    public static final PlacementModifierType<BetaNoiseBasedCountPlacementModifier> BETA_NOISE_BASED_COUNT;
    public static final PlacementModifierType<AlphaNoiseBasedCountPlacementModifier> ALPHA_NOISE_BASED_COUNT;
    public static final PlacementModifierType<Infdev415NoiseBasedCountPlacementModifier> INFDEV_415_NOISE_BASED_COUNT;
    public static final PlacementModifierType<Infdev420NoiseBasedCountPlacementModifier> INFDEV_420_NOISE_BASED_COUNT;
    public static final PlacementModifierType<Infdev611NoiseBasedCountPlacementModifier> INFDEV_611_NOISE_BASED_COUNT;
    
    public static final PlacementModifierType<HeightmapSpreadDoublePlacementModifier> HEIGHTMAP_SPREAD_DOUBLE;
    public static final PlacementModifierType<Spread32AbovePlacementModifier> SPREAD_32_ABOVE;
    
    private static <P extends PlacementModifier> PlacementModifierType<P> register(String id, Codec<P> codec) {
        return Registry.register(Registry.PLACEMENT_MODIFIER_TYPE, ModernBeta.createId(id), () -> codec);
    }
    
    static {
        BETA_NOISE_BASED_COUNT = register("beta_noise_based_count", BetaNoiseBasedCountPlacementModifier.MODIFIER_CODEC);
        ALPHA_NOISE_BASED_COUNT = register("alpha_noise_based_count", AlphaNoiseBasedCountPlacementModifier.MODIFIER_CODEC);
        INFDEV_415_NOISE_BASED_COUNT = register("infdev_415_noise_based_count", Infdev415NoiseBasedCountPlacementModifier.MODIFIER_CODEC);
        INFDEV_420_NOISE_BASED_COUNT = register("infdev_420_noise_based_count", Infdev420NoiseBasedCountPlacementModifier.MODIFIER_CODEC);
        INFDEV_611_NOISE_BASED_COUNT = register("infdev_611_noise_based_count", Infdev611NoiseBasedCountPlacementModifier.MODIFIER_CODEC);
    
        HEIGHTMAP_SPREAD_DOUBLE = register("heightmap_spread_double", HeightmapSpreadDoublePlacementModifier.MODIFIER_CODEC);
        SPREAD_32_ABOVE = register("spread_32_above", Spread32AbovePlacementModifier.MODIFIER_CODEC);
    }
}
