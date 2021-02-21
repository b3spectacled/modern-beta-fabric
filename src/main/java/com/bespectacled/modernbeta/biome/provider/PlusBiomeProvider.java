package com.bespectacled.modernbeta.biome.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bespectacled.modernbeta.biome.BiomeType;
import com.bespectacled.modernbeta.biome.beta.BetaClimateSampler;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class PlusBiomeProvider extends AbstractBiomeProvider {
    
    private final Map<BiomeType, Identifier> biomeMapping;
    
    public PlusBiomeProvider(long seed, Map<BiomeType, Identifier> biomeMapping) {
        this.biomeMapping = biomeMapping;
        BetaClimateSampler.INSTANCE.setSeed(seed);
    }

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        return BetaClimateSampler.INSTANCE.sampleTemp(absX, absZ) < 0.5f ? 
            registry.get(biomeMapping.get(BiomeType.WINTER)) : 
            registry.get(biomeMapping.get(BiomeType.CLASSIC));
    } 

    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        List<RegistryKey<Biome>> biomeList = new ArrayList<RegistryKey<Biome>>();

        for (Identifier i : this.biomeMapping.values()) {
            biomeList.add(RegistryKey.of(Registry.BIOME_KEY, i));
        }
        
        return biomeList;
    }

}