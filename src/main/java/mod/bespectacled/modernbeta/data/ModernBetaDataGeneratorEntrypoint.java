package mod.bespectacled.modernbeta.data;

import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class ModernBetaDataGeneratorEntrypoint implements net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
    }
    
    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.BIOME, ModernBetaBiomes::bootstrap);
    }
}
