package com.bespectacled.modernbeta.api.world.biome;

import com.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import com.bespectacled.modernbeta.api.world.biome.climate.SkyClimateSampler;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public abstract class ClimateBiomeProvider extends BiomeProvider {
    private final ClimateSampler climateSampler;
    private final SkyClimateSampler skyClimateSampler;
    
    public ClimateBiomeProvider(long seed, NbtCompound settings, Registry<Biome> biomeRegistry, ClimateSampler climateSampler) {
        this(seed, settings, biomeRegistry, climateSampler, null);
    }

    public ClimateBiomeProvider(long seed, NbtCompound settings, Registry<Biome> biomeRegistry, ClimateSampler climateSampler, SkyClimateSampler skyClimateSampler) {
        super(seed, settings, biomeRegistry);
        
        this.climateSampler = climateSampler;
        this.skyClimateSampler = skyClimateSampler;
    }
    
    public ClimateSampler getClimateSampler() {
        return this.climateSampler;
    }
    
    public SkyClimateSampler getSkyClimateSampler() {
        return this.skyClimateSampler;
    }
    
    public boolean sampleBiomeColor() {
        return false;
    }
    
    public boolean sampleSkyColor() {
        return false;
    }
}
