package com.bespectacled.modernbeta.biome;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;

public class VanillaBiomes {
    public static final List<RegistryKey<Biome>> VANILLA_BIOME_KEYS;
    
    private static boolean isValidCategory(Category category) {
        return  category != Category.NONE &&
                //category != Category.BEACH &&
                //category != Category.OCEAN &&
                category != Category.NETHER &&
                category != Category.THEEND;
    }
    
    static {
        VANILLA_BIOME_KEYS = new ArrayList<RegistryKey<Biome>>();
        Iterator<Entry<RegistryKey<Biome>, Biome>> biomeIter = BuiltinRegistries.BIOME.getEntries().iterator();
        
        while (biomeIter.hasNext()) {
            Entry<RegistryKey<Biome>, Biome> entry = (Entry<RegistryKey<Biome>, Biome>)biomeIter.next();
            
            if (isValidCategory(entry.getValue().getCategory())) {
                VANILLA_BIOME_KEYS.add(entry.getKey());
            }
        }
    }
}
