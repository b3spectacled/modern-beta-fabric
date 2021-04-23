package com.bespectacled.modernbeta.api.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import com.bespectacled.modernbeta.api.AbstractBiomeProvider;

import java.util.NoSuchElementException;
import java.util.Map.Entry;

import net.minecraft.nbt.NbtCompound;

public class BiomeProviderRegistry {
    public enum BuiltInBiomeType {
        BETA("beta"),
        SINGLE("single"),
        VANILLA("vanilla");
        
        public final String name;
        
        private BuiltInBiomeType(String name) { this.name = name; }
    }
    
    private static final Map<String, BiFunction<Long, NbtCompound, AbstractBiomeProvider>> REGISTRY = new HashMap<>(); 
    
    public static void register(String name, BiFunction<Long, NbtCompound, AbstractBiomeProvider> biomeProvider) {
        if (REGISTRY.containsKey(name)) 
            throw new IllegalArgumentException("[Modern Beta] Registry already contains biome provider named " + name);
        
        REGISTRY.put(name, biomeProvider);
    }
    
    public static BiFunction<Long, NbtCompound, AbstractBiomeProvider> get(String name) {
        if (!REGISTRY.containsKey(name))
            throw new NoSuchElementException("[Modern Beta] Registry does not contain biome provider named " + name);
        
        return REGISTRY.get(name);
    }
    
    public static List<String> getBiomeProviderKeys() {
        List<Entry<String, BiFunction<Long, NbtCompound, AbstractBiomeProvider>>> entryList = new ArrayList<>(REGISTRY.entrySet());
        entryList.sort(Entry.comparingByKey());
        
        return entryList.stream().map(e -> e.getKey()).collect(Collectors.toList());
    }
    
    public static String getBiomeProviderType(NbtCompound settings) {
        if (settings.contains("biomeType")) 
            return settings.getString("biomeType");
        
        throw new NoSuchElementException("[Modern Beta] Settings does not contain biomeType field!");
    }
}
