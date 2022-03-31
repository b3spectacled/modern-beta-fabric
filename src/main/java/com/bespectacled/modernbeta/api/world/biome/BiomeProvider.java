package com.bespectacled.modernbeta.api.world.biome;

import java.util.List;

import com.bespectacled.modernbeta.util.settings.Settings;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;

public abstract class BiomeProvider implements BiomeAccess.Storage {
    protected final long seed;
    protected final Settings settings;
    protected final Registry<Biome> biomeRegistry;
    
    /**
     * Constructs a Modern Beta biome provider initialized with seed.
     * Additional settings are supplied in NbtCompound parameter.
     * 
     * @param seed World seed.
     * @param settings Biome settings.
     * @param biomeRegistry TODO
     */
    public BiomeProvider(long seed, Settings settings, Registry<Biome> biomeRegistry) {
        this.seed = seed;
        this.settings = settings;
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
    public abstract RegistryEntry<Biome> getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ);
    
    /**
     * Gets a list of biomes for biome source, for the purpose of locating structures, etc.
     * 
     * @return A list of biomes.
     */
    public List<RegistryEntry<Biome>> getBiomesForRegistry() {
        return List.of();
    }
}
