package mod.bespectacled.modernbeta.world.feature.placed;

import mod.bespectacled.modernbeta.world.feature.configured.ModernBetaTreeConfiguredFeatures;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;

public class ModernBetaTreePlacedFeatures {
    public static final RegistryKey<PlacedFeature> FANCY_OAK = ModernBetaPlacedFeatures.of("fancy_oak");

    public static void bootstrap(Registerable<PlacedFeature> featureRegisterable) {
        RegistryEntryLookup<ConfiguredFeature<?, ?>> registryConfigured = featureRegisterable.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
        
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> fancyOak = registryConfigured.getOrThrow(ModernBetaTreeConfiguredFeatures.FANCY_OAK);

        PlacedFeatures.register(featureRegisterable, FANCY_OAK, fancyOak, PlacedFeatures.wouldSurvive(Blocks.OAK_SAPLING));
    }
}
