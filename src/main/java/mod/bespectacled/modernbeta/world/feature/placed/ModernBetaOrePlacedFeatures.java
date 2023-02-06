package mod.bespectacled.modernbeta.world.feature.placed;

import java.util.List;

import mod.bespectacled.modernbeta.world.feature.ModernBetaFeatureTags;
import mod.bespectacled.modernbeta.world.feature.configured.ModernBetaOreConfiguredFeatures;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

public class ModernBetaOrePlacedFeatures {
    public static final RegistryKey<PlacedFeature> ORE_CLAY = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.ORE_CLAY);
    public static final RegistryKey<PlacedFeature> ORE_EMERALD_Y95 = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.ORE_EMERALD_Y95);
    
    public static void bootstrap(Registerable<PlacedFeature> featureRegisterable) {
        RegistryEntryLookup<ConfiguredFeature<?, ?>> registryConfigured = featureRegisterable.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
        
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> oreClay = registryConfigured.getOrThrow(ModernBetaOreConfiguredFeatures.ORE_CLAY);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> oreEmeraldY95 = registryConfigured.getOrThrow(ModernBetaOreConfiguredFeatures.ORE_EMERALD_Y95);
        
        PlacedFeatures.register(featureRegisterable, ORE_CLAY, oreClay, modifiersWithCount(33, HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(127))));
        PlacedFeatures.register(featureRegisterable, ORE_EMERALD_Y95, oreEmeraldY95, modifiersWithCount(11, HeightRangePlacementModifier.uniform(YOffset.fixed(95), YOffset.fixed(256))));
    }

    private static List<PlacementModifier> modifiers(PlacementModifier first, PlacementModifier second) {
        return List.of(first, SquarePlacementModifier.of(), second, BiomePlacementModifier.of());
    }
    
    private static List<PlacementModifier> modifiersWithCount(int count, PlacementModifier modifier) {
        return modifiers(CountPlacementModifier.of(count), modifier);
    }
}
