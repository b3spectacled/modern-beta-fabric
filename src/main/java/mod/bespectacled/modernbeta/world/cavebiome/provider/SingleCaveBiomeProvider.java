package mod.bespectacled.modernbeta.world.cavebiome.provider;

import java.util.List;

import mod.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class SingleCaveBiomeProvider extends CaveBiomeProvider {
    private final RegistryKey<Biome> biomeKey;
    
    public SingleCaveBiomeProvider(long seed, NbtCompound settings, Registry<Biome> biomeRegistry) {
        super(seed, settings, biomeRegistry);

        this.biomeKey = RegistryKey.of(Registry.BIOME_KEY, new Identifier(this.settings.singleBiome));
    }

    @Override
    public RegistryEntry<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
        return this.biomeRegistry.getOrCreateEntry(this.biomeKey);
    }
    
    @Override
    public List<RegistryEntry<Biome>> getBiomesForRegistry() {
        return List.of(this.biomeRegistry.getOrCreateEntry(this.biomeKey));
    }
}
