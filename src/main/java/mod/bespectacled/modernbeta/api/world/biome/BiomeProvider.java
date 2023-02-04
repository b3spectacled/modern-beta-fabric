package mod.bespectacled.modernbeta.api.world.biome;

import java.util.List;

import mod.bespectacled.modernbeta.settings.ModernBetaBiomeSettings;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public abstract class BiomeProvider {
    protected final ModernBetaBiomeSettings settings;
    protected RegistryEntryLookup<Biome> biomeRegistry;
    
    /**
     * Constructs a Modern Beta biome provider initialized with seed.
     * Additional settings are supplied in NbtCompound parameter.
     * 
     * @param settings Biome settings.
     * @param biomeRegistry TODO
     */
    public BiomeProvider(NbtCompound settings, RegistryEntryLookup<Biome> biomeRegistry) {
        this.settings = new ModernBetaBiomeSettings.Builder(settings).build();
        this.biomeRegistry = biomeRegistry;
    }
    
    /**
     * Gets a biome for biome source at given biome coordinates.
     * Note that a single biome coordinate unit equals 4 blocks.
     * 
     * @param biomeX x-coordinate in biome coordinates.
     * @param biomeY y-coordinate in biome coordinates.
     * @param biomeZ z-coordinate in biome coordinates.
     * 
     * @return A biome at given biome coordinates.
     */
    public abstract RegistryEntry<Biome> getBiome(int biomeX, int biomeY, int biomeZ);
    
    /**
     * Gets a list of biomes for biome source, for the purpose of locating structures, etc.
     * 
     * @return A list of biomes.
     */
    public List<RegistryEntry<Biome>> getBiomesForRegistry() {
        return List.of();
    }
    
    public boolean initProvider(long seed) {
        return true;
    }
}
