package com.bespectacled.modernbeta.biome.source;

import java.util.ArrayList;

import com.bespectacled.modernbeta.biome.vanilla.VanillaBiomeLayer;
import com.bespectacled.modernbeta.biome.vanilla.VanillaOceanLayer;
import com.mojang.serialization.Codec;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import net.minecraft.world.biome.source.BiomeSource;

public class VanillaLayeredOceanlessBiomeSource extends BiomeSource implements IOldBiomeSource {

    private final long seed;
    private final Registry<Biome> biomeRegistry;
    
    private final BiomeLayerSampler biomeSampler;
    private final BiomeLayerSampler oceanSampler;
    
    protected VanillaLayeredOceanlessBiomeSource(long seed, Registry<Biome> biomeRegistry) {
        super(new ArrayList<Biome>());
        
        this.seed = seed;
        this.biomeRegistry = biomeRegistry;
        
        this.biomeSampler = VanillaBiomeLayer.build(seed, false, 4, -1);
        this.oceanSampler = VanillaOceanLayer.build(seed, false, 6, -1);
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.biomeSampler.sample(this.biomeRegistry, biomeX, biomeZ);
    }
    
    @Override
    public Biome getOceanBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.oceanSampler.sample(this.biomeRegistry, biomeX, biomeZ);
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return null;
    }

    @Override
    public BiomeSource withSeed(long seed) {
        return new VanillaLayeredOceanlessBiomeSource(seed, this.biomeRegistry);
    }

}
