package com.bespectacled.modernbeta.world.biome.vanilla;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.bespectacled.modernbeta.mixin.MixinVanillaLayeredBiomeSourceAccessor;

import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.Biome.Category;

public class VanillaSurfaceBiomes {
    public static List<Biome> BIOMES;
    
    private static boolean isValidCategory(Category category) {
        return  category != Category.NONE &&
                category != Category.BEACH &&
                category != Category.OCEAN &&
                category != Category.NETHER &&
                category != Category.THEEND;
    }
    
    static {
        BIOMES = MixinVanillaLayeredBiomeSourceAccessor.getBIOMES()
            .stream()
            .map(k -> BuiltinRegistries.BIOME.get(k))
            .filter(b -> isValidCategory(b.getCategory()))
            .collect(Collectors.toList());
        
        BIOMES.add(BuiltinRegistries.BIOME.get(BiomeKeys.BAMBOO_JUNGLE_HILLS));
        BIOMES.add(BuiltinRegistries.BIOME.get(BiomeKeys.BAMBOO_JUNGLE));
        
        BIOMES.sort(
            new Comparator<Biome>() {
                @Override
                public int compare(Biome b0, Biome b1) {
                    String name0 = BuiltinRegistries.BIOME.getId(b0).getPath();
                    String name1 = BuiltinRegistries.BIOME.getId(b1).getPath();
                    
                    return name0.compareTo(name1);
                }
            }
        );
    }
}
