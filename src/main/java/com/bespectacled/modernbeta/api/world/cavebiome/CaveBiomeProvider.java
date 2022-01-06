package com.bespectacled.modernbeta.api.world.cavebiome;

import java.util.List;

import com.bespectacled.modernbeta.util.settings.Settings;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public abstract class CaveBiomeProvider {
    protected final long seed;
    protected final Settings settings;
    protected final Registry<Biome> biomeRegistry;
    
    /**
     * Constructs a Modern Beta cave biome provider initialized with seed.
     * Additional settings are supplied in NbtCompound parameter.
     * 
     * @param seed World seed.
     * @param settings Biome settings.
     */
    public CaveBiomeProvider(long seed, Settings settings, Registry<Biome> biomeRegistry) {
        this.seed = seed;
        this.settings = settings;
        this.biomeRegistry = biomeRegistry;
    }
    
    /**
     * Gets a cave biome to overwrite the original biome at given biome coordinates and sufficient depth.
     * 
     * @param biomeX x-coordinate in biome coordinates.
     * @param biomeY y-coordinate in biome coordinates.
     * @param biomeZ z-coordinate in biome coordinates.
     * 
     * @return A biome at given biome coordinates. May return null, in which case original biome is not replaced.
     */
    public abstract Biome getBiome(int biomeX, int biomeY, int biomeZ);
    
    /**
     * Gets a list of biomes for biome source, for the purpose of locating structures, etc.
     * 
     * @return A list of biomes.
     */
    public List<Biome> getBiomesForRegistry() {
        return List.of();
    }
}
