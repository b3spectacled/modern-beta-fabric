package com.bespectacled.modernbeta.world.cavebiome.provider;

import java.util.List;

import com.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class VanillaCaveBiomeProvider extends CaveBiomeProvider {
    public VanillaCaveBiomeProvider(OldBiomeSource biomeSource) {
        super(biomeSource);
    }

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> biomeRegistry, int biomeX, int biomeY, int biomeZ) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        // TODO Auto-generated method stub
        return null;
    }
}
