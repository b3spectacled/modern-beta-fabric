package mod.bespectacled.modernbeta.world.feature.configured;

import mod.bespectacled.modernbeta.world.feature.ModernBetaFeatures;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;

public class ModernBetaMiscConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> FREEZE_TOP_LAYER = ModernBetaConfiguredFeatures.of("freeze_top_layer");
    
    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> featureRegisterable) {
        ConfiguredFeatures.register(featureRegisterable, FREEZE_TOP_LAYER, ModernBetaFeatures.FREEZE_TOP_LAYER);
    }
}
