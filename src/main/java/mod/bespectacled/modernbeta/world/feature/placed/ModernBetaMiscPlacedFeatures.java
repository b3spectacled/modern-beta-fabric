package mod.bespectacled.modernbeta.world.feature.placed;

import mod.bespectacled.modernbeta.world.feature.configured.ModernBetaMiscConfiguredFeatures;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;

public class ModernBetaMiscPlacedFeatures {
    public static final RegistryKey<PlacedFeature> FREEZE_TOP_LAYER = ModernBetaPlacedFeatures.of("freeze_top_layer");
    
    public static void bootstrap(Registerable<PlacedFeature> featureRegisterable) {
        RegistryEntryLookup<ConfiguredFeature<?, ?>> registryConfigured = featureRegisterable.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
        
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> freezeTopLayer = registryConfigured.getOrThrow(ModernBetaMiscConfiguredFeatures.FREEZE_TOP_LAYER);
        
        PlacedFeatures.register(featureRegisterable, FREEZE_TOP_LAYER, freezeTopLayer, BiomePlacementModifier.of());
    }
}
