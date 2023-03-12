package mod.bespectacled.modernbeta.world.feature.placed;

import java.util.List;

import mod.bespectacled.modernbeta.world.feature.configured.OldOreConfiguredFeatures;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

public class OldOrePlacedFeatures {
    private static List<PlacementModifier> modifiers(PlacementModifier first, PlacementModifier second) {
        return List.of(first, SquarePlacementModifier.of(), second, BiomePlacementModifier.of());
    }
    
    private static List<PlacementModifier> modifiersWithCount(int count, PlacementModifier modifier) {
        return modifiers(CountPlacementModifier.of(count), modifier);
    }
    
    public static final RegistryEntry<PlacedFeature> ORE_CLAY = OldPlacedFeatures.register(
        "ore_clay",
        OldOreConfiguredFeatures.ORE_CLAY,
        modifiersWithCount(33, HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(127)))
    );
    
    public static final RegistryEntry<PlacedFeature> ORE_EMERALD_Y95 = OldPlacedFeatures.register(
        "ore_emerald_y95",
        OldOreConfiguredFeatures.ORE_EMERALD_Y95,
        modifiersWithCount(11, HeightRangePlacementModifier.uniform(YOffset.fixed(95), YOffset.fixed(256)))
    );
}
