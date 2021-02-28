package com.bespectacled.modernbeta.biome.provider;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;

public class NetherBiomeProvider extends AbstractBiomeProvider {
    
    private final long seed;
    private MultiNoiseBiomeSource biomeSource;
    
    public NetherBiomeProvider(long seed) {
        this.seed = seed;
        this.biomeSource = null;
    }

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        if (this.biomeSource == null) this.biomeSource = MultiNoiseBiomeSource.Preset.NETHER.getBiomeSource(registry, this.seed);
        
        return this.biomeSource.getBiomeForNoiseGen(biomeX, biomeY, biomeZ);
    }

    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        return BuiltinRegistries.BIOME.getEntries()
            .stream()
            .filter(e -> e.getValue().getCategory() == Category.NETHER)
            .map(e -> e.getKey())
            .collect(Collectors.toList());
    }

}
