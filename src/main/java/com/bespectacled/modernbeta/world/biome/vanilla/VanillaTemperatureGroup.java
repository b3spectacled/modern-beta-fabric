package com.bespectacled.modernbeta.world.biome.vanilla;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;

// "Re-implementation" of TemperatureGroup, removed for some reason post-1.15(?)
public class VanillaTemperatureGroup {
    /* Referenced from 1.15
     * Temp < 0.2 -> Cold
     * Temp < 1.0 -> Medium/Normal
     * Temp >= 1.0 -> Warm
     */
    
    public static Set<Biome> NORMAL_BIOMES = new HashSet<Biome>();
    public static Set<Biome> WARM_BIOMES = new HashSet<Biome>();
    public static Set<Biome> COLD_BIOMES = new HashSet<Biome>();
    
    private static boolean isInvalidCategory(Category category) {
        boolean isInvalid = 
            category == Category.NONE ||
            category == Category.BEACH ||
            category == Category.OCEAN ||
            category == Category.NETHER ||
            category == Category.THEEND ||
            category == Category.UNDERGROUND;
        
        return isInvalid;
    }
    
    private static void addToTemperatureGroup(Biome biome) {
        if (isInvalidCategory(biome.getCategory())) 
            return; 
        
        double temp = biome.getTemperature();
        
        if (temp < 0.2) {
            COLD_BIOMES.add(biome);
        } else if (temp < 1.0) {
            NORMAL_BIOMES.add(biome);
        } else {
            WARM_BIOMES.add(biome);
        }
    }
    
    static {
        BuiltinRegistries.BIOME.forEach(b -> addToTemperatureGroup(b));
        
        System.out.println("Normal biomes:");
        NORMAL_BIOMES.forEach(b -> System.out.println(b));
        
        System.out.println("\nCold biomes:");
        COLD_BIOMES.forEach(b -> System.out.println(b));
        
        System.out.println("\nWarm biomes:");
        WARM_BIOMES.forEach(b -> System.out.println(b));
    }
}
