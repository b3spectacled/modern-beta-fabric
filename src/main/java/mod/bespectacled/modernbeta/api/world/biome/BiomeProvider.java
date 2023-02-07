package mod.bespectacled.modernbeta.api.world.biome;

import java.util.List;

import mod.bespectacled.modernbeta.settings.ModernBetaSettingsBiome;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public abstract class BiomeProvider {
    protected final ModernBetaSettingsBiome settings;
    protected RegistryEntryLookup<Biome> biomeRegistry;
    
    /**
     * Constructs a Modern Beta biome provider initialized with seed.
     * Additional settings are supplied in NbtCompound parameter.
     * 
     * @param settings Biome settings.
     * @param biomeRegistry Minecraft biome registry.
     */
    public BiomeProvider(NbtCompound settings, RegistryEntryLookup<Biome> biomeRegistry) {
        this.settings = new ModernBetaSettingsBiome.Builder(settings).build();
        this.biomeRegistry = biomeRegistry;
    }
    
    /**
     * Inits biome provider fields.
     * 
     * @param seed
     */
    public abstract void initProvider(long seed);
    
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
}
