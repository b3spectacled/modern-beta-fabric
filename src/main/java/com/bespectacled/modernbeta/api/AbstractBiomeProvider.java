package com.bespectacled.modernbeta.api;

import java.util.List;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public abstract class AbstractBiomeProvider {
    protected final long seed;
    protected final NbtCompound settings;
    
    /**
     * Constructs a Modern Beta biome provider initialized with seed.
     * Additional settings are supplied in NbtCompound parameter.
     * 
     * @param seed Seed to initialize biome provider.
     * @param settings NbtCompound for additional settings.
     */
    public AbstractBiomeProvider(long seed, NbtCompound settings) {
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
     * Gets a biome at given block coordinates, for purpose of surface generation.
     * 
     * @param biomeRegistry
     * @param x x-coordinate in block coordinates.
     * @param y y-coordinate in block coordinates.
     * @param z z-coordinate in block coordinates.
     * 
     * @return A biome at given block coordinates.
     */
    public Biome getBiomeForSurfaceGen(Registry<Biome> biomeRegistry, int x, int y, int z) {
        return this.getBiomeForNoiseGen(biomeRegistry, x >> 2, y >> 2, z >> 2);
    }
    
    
    /**
     * Gets a list of biome registry keys for biome source, for the purpose of locating structures, etc.
     * 
     * @return A list of biome registry keys.
     */
    public abstract List<RegistryKey<Biome>> getBiomesForRegistry();
}
