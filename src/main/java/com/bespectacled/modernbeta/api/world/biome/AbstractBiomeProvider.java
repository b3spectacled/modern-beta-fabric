package com.bespectacled.modernbeta.api.world.biome;

import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public abstract class AbstractBiomeProvider {
    protected final long seed;
    protected final CompoundTag settings;
    
    /**
     * Constructs a Modern Beta biome provider initialized with seed.
     * Additional settings are supplied in CompoundTag parameter.
     * 
     * @param seed Seed to initialize biome provider.
     * @param settings CompoundTag for additional settings.
     */
    public AbstractBiomeProvider(long seed, CompoundTag settings) {
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
    public abstract Biome getBiomeForNoiseGen(Registry<Biome> biomeRegistry, int biomeX, int biomeY, int biomeZ);
    
    /**
     * Gets a biome to overwrite the original biome at given biome coordinates and sufficient depth.
     * 
     * @param biomeRegistry
     * @param biomeX x-coordinate in biome coordinates.
     * @param biomeY y-coordinate in biome coordinates.
     * @param biomeZ z-coordinate in biome coordinates.
     * 
     * @return A biome at given biome coordinates.
     */
    public Biome getOceanBiomeForNoiseGen(Registry<Biome> biomeRegistry, int biomeX, int biomeY, int biomeZ) {
        return this.getBiomeForNoiseGen(biomeRegistry, biomeX, biomeY, biomeZ);
    }
    
    /**
     * Gets a list of biome registry keys for biome source, for the purpose of locating structures, etc.
     * 
     * @return A list of biome registry keys.
     */
    public abstract List<RegistryKey<Biome>> getBiomesForRegistry();
}
