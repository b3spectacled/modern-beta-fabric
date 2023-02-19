package mod.bespectacled.modernbeta.world.cavebiome.provider;

import mod.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class CaveBiomeProviderNone extends CaveBiomeProvider {
    public CaveBiomeProviderNone(NbtCompound settings, RegistryEntryLookup<Biome> biomeRegistry, long seed) {
        super(settings, biomeRegistry, seed);
    }

    @Override
    public RegistryEntry<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
        return null;
    }
}
