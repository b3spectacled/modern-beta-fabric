package mod.bespectacled.modernbeta.api.world.cavebiome;

import java.util.List;

import mod.bespectacled.modernbeta.settings.ModernBetaSettingsCaveBiome;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public abstract class CaveBiomeProvider {
    protected final ModernBetaSettingsCaveBiome settings;
    protected RegistryEntryLookup<Biome> biomeRegistry;
    
    /**
     * Constructs a Modern Beta cave biome provider initialized with seed.
     * Additional settings are supplied in NbtCompound parameter.
     * 
     * @param seed World seed.
     * @param settings Biome settings.
     */
    public CaveBiomeProvider(NbtCompound settings, RegistryEntryLookup<Biome> biomeRegistry) {
        this.settings = new ModernBetaSettingsCaveBiome.Builder(settings).build();
        this.biomeRegistry = biomeRegistry;
    }
    
    /**
     * Inits biome provider fields.
     * 
     * @param seed
     */
    public abstract void initProvider(long seed);
    
    /**
     * Gets a cave biome to overwrite the original biome at given biome coordinates.
     * 
     * @param biomeX x-coordinate in biome coordinates.
     * @param biomeY y-coordinate in biome coordinates.
     * @param biomeZ z-coordinate in biome coordinates.
     * 
     * @return A biome at given biome coordinates. May return null, in which case original biome is not replaced.
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
}
