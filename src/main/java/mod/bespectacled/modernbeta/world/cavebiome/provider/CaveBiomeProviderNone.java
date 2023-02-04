package mod.bespectacled.modernbeta.world.cavebiome.provider;

import java.util.List;

import mod.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class CaveBiomeProviderNone extends CaveBiomeProvider {
    public CaveBiomeProviderNone(NbtCompound settings, RegistryEntryLookup<Biome> biomeRegistry) {
        super(settings, biomeRegistry);
    }

    @Override
    public RegistryEntry<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
        return null;
    }

    @Override
    public List<RegistryEntry<Biome>> getBiomesForRegistry() {
        return List.of();
    }
}
