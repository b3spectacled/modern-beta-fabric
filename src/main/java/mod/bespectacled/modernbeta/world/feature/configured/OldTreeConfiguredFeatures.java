package mod.bespectacled.modernbeta.world.feature.configured;

import mod.bespectacled.modernbeta.world.feature.OldFeatures;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;

public class OldTreeConfiguredFeatures {
    public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> OLD_FANCY_OAK = OldConfiguredFeatures.register(
        "old_fancy_oak",
        OldFeatures.OLD_FANCY_OAK,
        FeatureConfig.DEFAULT
    );   
}   
