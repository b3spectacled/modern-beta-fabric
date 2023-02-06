package mod.bespectacled.modernbeta.world.feature.placed;

import mod.bespectacled.modernbeta.ModernBeta;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.feature.PlacedFeature;

public class ModernBetaPlacedFeatures {
    public static void bootstrap(Registerable<PlacedFeature> featureRegisterable) {
        ModernBetaMiscPlacedFeatures.bootstrap(featureRegisterable);
        ModernBetaOrePlacedFeatures.bootstrap(featureRegisterable);
        ModernBetaTreePlacedFeatures.bootstrap(featureRegisterable);
        ModernBetaVegetationPlacedFeatures.bootstrap(featureRegisterable);
    }
    
    public static RegistryKey<PlacedFeature> of(String id) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, ModernBeta.createId(id));
    }
}
