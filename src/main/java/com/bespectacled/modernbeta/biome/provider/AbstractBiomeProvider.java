package com.bespectacled.modernbeta.biome.provider;

import java.util.List;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public abstract class AbstractBiomeProvider {
    protected final long seed;
    protected final NbtCompound settings;
    
    public AbstractBiomeProvider(long seed, NbtCompound settings) {
        this.seed = seed;
        this.settings = settings;
    }
    
    public abstract Biome getBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ);
    
    public Biome getOceanBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        return this.getBiomeForNoiseGen(registry, biomeX, biomeY, biomeZ);
    }
    
    public Biome getBiomeForSurfaceGen(Registry<Biome> registry, int x, int y, int z) {
        return this.getBiomeForNoiseGen(registry, x >> 2, y >> 2, z >> 2);
    }
    
    public abstract List<RegistryKey<Biome>> getBiomesForRegistry();
    
    
}
