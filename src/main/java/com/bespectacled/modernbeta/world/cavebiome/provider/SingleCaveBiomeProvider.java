package com.bespectacled.modernbeta.world.cavebiome.provider;

import java.util.Arrays;
import java.util.List;

import com.bespectacled.modernbeta.api.world.biome.climate.CaveClimateSampler;
import com.bespectacled.modernbeta.api.world.biome.climate.NoiseRange;
import com.bespectacled.modernbeta.api.world.biome.climate.NoiseRanges;
import com.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.world.cavebiome.provider.climate.BaseCaveClimateSampler;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public class SingleCaveBiomeProvider extends CaveBiomeProvider implements CaveClimateSampler {
    private static final Identifier DEFAULT_BIOME_ID = BiomeKeys.LUSH_CAVES.getValue();
    
    private final boolean useCaveNoise;
    
    private final CaveClimateSampler climateSampler;
    private final NoiseRanges noiseRanges;
    
    public SingleCaveBiomeProvider(long seed, NbtCompound settings, Registry<Biome> biomeRegistry) {
        super(seed, settings, biomeRegistry);
        
        Identifier biomeId = new Identifier(NbtUtil.readString(NbtTags.SINGLE_BIOME, settings, DEFAULT_BIOME_ID.toString()));
        
        this.useCaveNoise = NbtUtil.readBoolean(NbtTags.USE_CAVE_NOISE, settings, false);
        this.climateSampler = new BaseCaveClimateSampler(seed, 2, 8);
        this.noiseRanges = new NoiseRanges.Builder()
            .add(new NoiseRange(0.0, 1.0, biomeId))
            .build();
    }

    @Override
    public Biome getBiome(Registry<Biome> biomeRegistry, int biomeX, int biomeY, int biomeZ) {
        return this.useCaveNoise ?
            biomeRegistry.getOrEmpty(this.noiseRanges.sample(this.climateSampler.sample(biomeX, biomeY, biomeZ))).orElse(null) :
            biomeRegistry.get(this.noiseRanges.sample(1.0));
    }
    
    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        return Arrays.asList(RegistryKey.of(Registry.BIOME_KEY, this.noiseRanges.sample(1.0)));
    }

    @Override
    public double sample(int x, int y, int z) {
        return this.climateSampler.sample(x, y, z);
    }
}
