package mod.bespectacled.modernbeta.world.feature.configured;

import mod.bespectacled.modernbeta.world.feature.ModernBetaFeatureTags;
import mod.bespectacled.modernbeta.world.feature.ModernBetaFeatures;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.FeatureConfig;

public class ModernBetaTreeConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> FANCY_OAK = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.FANCY_OAK);
    
    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> featureRegisterable) {
        ConfiguredFeatures.register(featureRegisterable, FANCY_OAK, ModernBetaFeatures.OLD_FANCY_OAK, FeatureConfig.DEFAULT);
    }
}   
