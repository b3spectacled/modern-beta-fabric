package mod.bespectacled.modernbeta.api.world.cavebiome;

import java.util.List;

import mod.bespectacled.modernbeta.settings.ModernBetaSettingsCaveBiome;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public abstract class CaveBiomeProvider {
    protected final ModernBetaSettingsCaveBiome settings;
    protected final RegistryEntryLookup<Biome> biomeRegistry;
    protected final long seed;
    
    /**
     * Constructs a Modern Beta cave biome provider initialized with seed.
     * Additional settings are supplied in NbtCompound parameter.
     * 
     * @param seed World seed.
     * @param settings Biome settings.
     */
    public CaveBiomeProvider(NbtCompound settings, RegistryEntryLookup<Biome> biomeRegistry, long seed) {
        this.settings = ModernBetaSettingsCaveBiome.fromCompound(settings);
        this.biomeRegistry = biomeRegistry;
        this.seed = seed;
    }
    
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
    public List<RegistryEntry<Biome>> getBiomes() {
        return List.of();
    }
}
