package com.bespectacled.modernbeta.api.world.cavebiome;

import java.util.List;

import com.bespectacled.modernbeta.world.biome.OldBiomeSource;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public abstract class CaveBiomeProvider {
    protected final long seed;
    protected final NbtCompound settings;
    
    /**
     * Constructs a Modern Beta cave biome provider initialized with seed.
     * Additional settings are supplied in NbtCompound parameter.
     * 
     * @param seed World seed.
     * @param settings Biome settings.
     */
    public CaveBiomeProvider(long seed, NbtCompound settings) {
        this.seed = seed;
        this.settings = settings;
    }
    
    /**
     * Gets a biome for biome source at given biome coordinates.
     * Note that a single biome coordinate unit equals 4 blocks.
     * 
     * @param biomeRegistry
     * @param biomeX x-coordinate in biome coordinates.
     * @param biomeY y-coordinate in biome coordinates.
     * @param biomeZ z-coordinate in biome coordinates.
     * 
     * @return A biome at given biome coordinates.
     */
    public abstract Biome getBiome(Registry<Biome> biomeRegistry, int biomeX, int biomeY, int biomeZ);
    
    /**
     * Gets a list of biome registry keys for biome source, for the purpose of locating structures, etc.
     * 
     * @return A list of biome registry keys.
     */
    public abstract List<RegistryKey<Biome>> getBiomesForRegistry();
}
