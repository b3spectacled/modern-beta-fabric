package mod.bespectacled.modernbeta.world.cavebiome.provider;

import java.util.List;

import mod.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class CaveBiomeProviderSingle extends CaveBiomeProvider {
    private final RegistryKey<Biome> biome;
    
    public CaveBiomeProviderSingle(NbtCompound settings, RegistryEntryLookup<Biome> biomeRegistry) {
        super(settings, biomeRegistry);

        this.biome = RegistryKey.of(RegistryKeys.BIOME, new Identifier(this.settings.singleBiome));
    }

    @Override
    public void initProvider(long seed) {}

    @Override
    public RegistryEntry<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
        return this.biomeRegistry.getOrThrow(this.biome);
    }
    
    @Override
    public List<RegistryEntry<Biome>> getBiomesForRegistry() {
        return List.of(this.biomeRegistry.getOrThrow(this.biome));
    }
}
