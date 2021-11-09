package com.bespectacled.modernbeta.api.world.biome;

import com.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import com.bespectacled.modernbeta.api.world.biome.climate.SkyClimateSampler;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public abstract class ClimateBiomeProvider extends BiomeProvider {
    private final ClimateSampler climateSampler;
    private final SkyClimateSampler skyClimateSampler;
    
    public ClimateBiomeProvider(long seed, NbtCompound settings, Registry<Biome> biomeRegistry, ClimateSampler climateSampler) {
        this(seed, settings, biomeRegistry, climateSampler, climateSampler instanceof SkyClimateSampler skyClimateSampler ? skyClimateSampler : null);
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
    
    public static double getClimateScale(NbtCompound settings, double defaultScale) {
        return NbtUtil.readDouble(NbtTags.CLIMATE_SCALE, settings, defaultScale);
    }
}
