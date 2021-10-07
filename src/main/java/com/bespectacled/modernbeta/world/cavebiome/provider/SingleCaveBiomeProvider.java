package com.bespectacled.modernbeta.world.cavebiome.provider;

import java.util.Arrays;
import java.util.List;

import com.bespectacled.modernbeta.api.world.biome.climate.CaveClimateSampler;
import com.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.world.cavebiome.climate.BaseCaveClimateSampler;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public class SingleCaveBiomeProvider extends CaveBiomeProvider implements CaveClimateSampler {
    private static final Identifier DEFAULT_BIOME_ID = BiomeKeys.LUSH_CAVES.getValue();
    
    private final boolean useCaveNoise;
    
    private final Identifier biomeId;
    private final CaveClimateSampler climateSampler;
    
    public SingleCaveBiomeProvider(long seed, NbtCompound settings) {
        super(seed, settings);
        
        //this.useCaveNoise = false;
        this.useCaveNoise = NbtUtil.readBoolean(NbtTags.USE_CAVE_NOISE, settings, false);
        
        this.biomeId = new Identifier(NbtUtil.readString(NbtTags.SINGLE_BIOME, settings, DEFAULT_BIOME_ID.toString()));
        this.climateSampler = new BaseCaveClimateSampler(seed);
    }

    @Override
    public Biome getBiome(Registry<Biome> biomeRegistry, int biomeX, int biomeY, int biomeZ) {
        Biome biome = null;
        
        if (this.climateSampler != null) {
            double noise = this.climateSampler.sample(biomeX, biomeY, biomeZ);

            if (noise > 0.0)
                biome = biomeRegistry.get(biomeId);
        } else {
            biome = biomeRegistry.get(biomeId);
        }
        
        return biome;
    }
    
    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        return Arrays.asList(RegistryKey.of(Registry.BIOME_KEY, this.biomeId));
    }

    @Override
    public double sample(int x, int y, int z) {
        return this.climateSampler.sample(x, y, z);
    }
}
