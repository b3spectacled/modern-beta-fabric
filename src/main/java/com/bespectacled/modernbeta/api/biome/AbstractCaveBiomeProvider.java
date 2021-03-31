package com.bespectacled.modernbeta.api.biome;

import net.minecraft.nbt.NbtCompound;

public class AbstractCaveBiomeProvider {
    protected final long seed;
    protected final NbtCompound settings;
    
    /**
     * Constructs a Modern Beta cave biome provider initialized with seed.
     * Additional settings are supplied in NbtCompound parameter.
     * 
     * @param seed Seed to initialize biome provider.
     * @param settings NbtCompound for additional settings.
     */
    public AbstractCaveBiomeProvider(long seed, NbtCompound settings) {
        this.seed = seed;
        this.settings = settings;
    }
}
