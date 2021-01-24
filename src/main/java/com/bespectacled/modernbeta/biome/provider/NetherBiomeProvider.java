package com.bespectacled.modernbeta.biome.provider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

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
        List<RegistryKey<Biome>> biomeList = new ArrayList<RegistryKey<Biome>>();
        
        Iterator<Entry<RegistryKey<Biome>, Biome>> biomeIter = BuiltinRegistries.BIOME.getEntries().iterator();
        
        while (biomeIter.hasNext()) {
            Entry<RegistryKey<Biome>, Biome> entry = (Entry<RegistryKey<Biome>, Biome>)biomeIter.next();
            
            if (entry.getValue().getCategory() == Category.NETHER) {
                biomeList.add(entry.getKey());
            }
        }
        
        return biomeList;
    }

}
