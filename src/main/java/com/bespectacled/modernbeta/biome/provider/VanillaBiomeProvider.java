package com.bespectacled.modernbeta.biome.provider;

import com.bespectacled.modernbeta.biome.layer.VanillaBiomeLayer;
import com.bespectacled.modernbeta.biome.layer.VanillaOceanLayer;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeLayerSampler;

public class VanillaBiomeProvider extends AbstractBiomeProvider {
    
    private final BiomeLayerSampler biomeSampler;
    private final BiomeLayerSampler oceanSampler;
    
    public VanillaBiomeProvider(long seed) {
        this.biomeSampler = VanillaBiomeLayer.build(seed, false, 4, -1);
        this.oceanSampler = VanillaOceanLayer.build(seed, false, 6, -1);
    }

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        return this.biomeSampler.sample(registry, biomeX, biomeZ);
    }

    @Override
    public Biome getOceanBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        return this.oceanSampler.sample(registry, biomeX, biomeZ);
    }

}
