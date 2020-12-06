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
import net.minecraft.world.biome.source.BiomeLayerSampler;

public class BetaVoronoiBiomeProvider extends AbstractBiomeProvider {

    private final BiomeLayerSampler biomeSampler;
    
    public BetaVoronoiBiomeProvider(long seed) {
        //this.biomeSampler = BetaBiomeLayer.build(seed, false, 4, 4);
        this.biomeSampler = null;
    }
    
    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        return this.biomeSampler.sample(registry, biomeX, biomeZ);
    }

    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        List<RegistryKey<Biome>> biomeList = new ArrayList<RegistryKey<Biome>>();
        
        Iterator<Entry<RegistryKey<Biome>, Biome>> biomeIter = BuiltinRegistries.BIOME.getEntries().iterator();
        
        while (biomeIter.hasNext()) {
            Entry<RegistryKey<Biome>, Biome> entry = (Entry<RegistryKey<Biome>, Biome>)biomeIter.next();
            
            if (isValidCategory(entry.getValue().getCategory())) {
                biomeList.add(entry.getKey());
            }
        }
        
        return biomeList;
    }
    
    private static boolean isValidCategory(Category category) {
        return  category != Category.NONE &&
                category != Category.BEACH &&
                category != Category.OCEAN &&
                category != Category.NETHER &&
                category != Category.THEEND;
    }
}
