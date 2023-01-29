package mod.bespectacled.modernbeta.world.feature.configured;

import mod.bespectacled.modernbeta.world.feature.ModernBetaFeatures;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;

public class ModernBetaMiscConfiguredFeatures {
    public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> FREEZE_TOP_LAYER = ModernBetaConfiguredFeatures.register(
        "freeze_top_layer",
        ModernBetaFeatures.FREEZE_TOP_LAYER,
        FeatureConfig.DEFAULT
    );
}
