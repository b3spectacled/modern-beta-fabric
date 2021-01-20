package com.bespectacled.modernbeta.biome.provider;

import java.util.ArrayList;
import java.util.List;

import com.bespectacled.modernbeta.biome.beta.BetaClimateSampler;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class SingleBiomeProvider extends AbstractBiomeProvider {
    
    private final Identifier biomeId;
    
    public SingleBiomeProvider(long seed, Identifier biomeId) {
        this.biomeId = biomeId;
        BetaClimateSampler.getInstance().setSeed(seed);
    }

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        return registry.get(biomeId); 
    }

    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        List<RegistryKey<Biome>> biomeList = new ArrayList<RegistryKey<Biome>>();
        biomeList.add(RegistryKey.of(Registry.BIOME_KEY, this.biomeId));
        
        return biomeList;
    }
}
