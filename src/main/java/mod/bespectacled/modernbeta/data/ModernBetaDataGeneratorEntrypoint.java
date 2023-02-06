package mod.bespectacled.modernbeta.data;

import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomes;
import mod.bespectacled.modernbeta.world.carver.configured.ModernBetaConfiguredCarvers;
import mod.bespectacled.modernbeta.world.feature.configured.ModernBetaConfiguredFeatures;
import mod.bespectacled.modernbeta.world.feature.placed.ModernBetaPlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class ModernBetaDataGeneratorEntrypoint implements net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        
        pack.addProvider(ModernBetaWorldGenProvider::new);
        pack.addProvider(ModernBetaBiomeTagProvider::new);
    }
    
    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, ModernBetaPlacedFeatures::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ModernBetaConfiguredFeatures::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_CARVER, ModernBetaConfiguredCarvers::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.BIOME, ModernBetaBiomes::bootstrap);
    }
}
