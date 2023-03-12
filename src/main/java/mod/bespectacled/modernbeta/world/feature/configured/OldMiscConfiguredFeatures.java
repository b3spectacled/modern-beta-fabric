package mod.bespectacled.modernbeta.world.feature.configured;

import mod.bespectacled.modernbeta.world.feature.OldFeatures;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;

public class OldMiscConfiguredFeatures {
    public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> BETA_FREEZE_TOP_LAYER = OldConfiguredFeatures.register(
        "beta_freeze_top_layer",
        OldFeatures.BETA_FREEZE_TOP_LAYER,
        FeatureConfig.DEFAULT
    );
}
