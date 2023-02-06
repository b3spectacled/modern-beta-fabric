package mod.bespectacled.modernbeta.data;

import java.util.concurrent.CompletableFuture;

import mod.bespectacled.modernbeta.ModernBeta;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;

public class ModernBetaWorldGenProvider extends FabricDynamicRegistryProvider {
    public ModernBetaWorldGenProvider(FabricDataOutput output, CompletableFuture<WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        final RegistryWrapper.Impl<Biome> registryBiome = registries.getWrapperOrThrow(RegistryKeys.BIOME);
        final RegistryWrapper.Impl<ConfiguredFeature<?, ?>> registryConfiguredFeature = registries.getWrapperOrThrow(RegistryKeys.CONFIGURED_FEATURE);
        final RegistryWrapper.Impl<PlacedFeature> registryPlacedFeature = registries.getWrapperOrThrow(RegistryKeys.PLACED_FEATURE);
        final RegistryWrapper.Impl<ConfiguredCarver<?>> registryConfiguredCarver = registries.getWrapperOrThrow(RegistryKeys.CONFIGURED_CARVER);

        entries.addAll(registryBiome);
        entries.addAll(registryConfiguredFeature);
        entries.addAll(registryPlacedFeature);
        entries.addAll(registryConfiguredCarver);
    }
    
    @Override
    public String getName() {
        return ModernBeta.MOD_NAME;
    }
}
