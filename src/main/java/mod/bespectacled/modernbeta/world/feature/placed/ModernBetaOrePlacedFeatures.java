package mod.bespectacled.modernbeta.world.feature.placed;

import java.util.List;

import mod.bespectacled.modernbeta.world.feature.configured.ModernBetaOreConfiguredFeatures;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

public class ModernBetaOrePlacedFeatures {
    private static List<PlacementModifier> modifiers(PlacementModifier first, PlacementModifier second) {
        return List.of(first, SquarePlacementModifier.of(), second, BiomePlacementModifier.of());
    }
    
    private static List<PlacementModifier> modifiersWithCount(int count, PlacementModifier modifier) {
        return modifiers(CountPlacementModifier.of(count), modifier);
    }
    
    public static final RegistryEntry<PlacedFeature> ORE_CLAY = ModernBetaPlacedFeatures.register(
        "ore_clay",
        ModernBetaOreConfiguredFeatures.ORE_CLAY,
        modifiersWithCount(33, HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(127)))
    );
    
    public static final RegistryEntry<PlacedFeature> ORE_EMERALD_Y95 = ModernBetaPlacedFeatures.register(
        "ore_emerald_y95",
        ModernBetaOreConfiguredFeatures.ORE_EMERALD_Y95,
        modifiersWithCount(11, HeightRangePlacementModifier.uniform(YOffset.fixed(95), YOffset.fixed(256)))
    );
}
