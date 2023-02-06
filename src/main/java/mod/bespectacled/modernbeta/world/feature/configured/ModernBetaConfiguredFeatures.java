package mod.bespectacled.modernbeta.world.feature.configured;

import mod.bespectacled.modernbeta.ModernBeta;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class ModernBetaConfiguredFeatures {
    public static void bootstrap(Registerable<?> featureRegisterable) {
        ModernBetaMiscConfiguredFeatures.bootstrap(featureRegisterable);
        ModernBetaOreConfiguredFeatures.bootstrap(featureRegisterable);
        ModernBetaTreeConfiguredFeatures.bootstrap(featureRegisterable);
        ModernBetaVegetationConfiguredFeatures.bootstrap(featureRegisterable);
    }
    
    public static RegistryKey<ConfiguredFeature<?, ?>> of(String id) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, ModernBeta.createId(id));
    }
}
