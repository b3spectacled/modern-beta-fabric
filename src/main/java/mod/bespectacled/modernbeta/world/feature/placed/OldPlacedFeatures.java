package mod.bespectacled.modernbeta.world.feature.placed;

import java.util.List;

import mod.bespectacled.modernbeta.ModernBeta;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;

public class OldPlacedFeatures {
    public static PlacedFeature register(String id, PlacedFeature feature) {
        return Registry.register(BuiltinRegistries.PLACED_FEATURE, ModernBeta.createId(id), feature);
    }
    
    public static RegistryEntry<PlacedFeature> register(String id, RegistryEntry<? extends ConfiguredFeature<?, ?>> registryEntry, PlacementModifier ... modifiers) {
        return PlacedFeatures.register(
            ModernBeta.createId(id).toString(),
            registryEntry,
            List.of(modifiers)
        );
    }
    
    public static RegistryEntry<PlacedFeature> register(String id, RegistryEntry<? extends ConfiguredFeature<?, ?>> registryEntry, List<PlacementModifier> modifiers) {
        return BuiltinRegistries.add(
            BuiltinRegistries.PLACED_FEATURE,
            ModernBeta.createId(id),
            new PlacedFeature(RegistryEntry.upcast(registryEntry), List.copyOf(modifiers))
        );
    }
}
