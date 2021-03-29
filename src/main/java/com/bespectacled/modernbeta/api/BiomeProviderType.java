package com.bespectacled.modernbeta.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;

import net.minecraft.nbt.NbtCompound;

public class BiomeProviderType {
    public static final String BETA = "beta";
    public static final String SINGLE = "single";
    public static final String VANILLA = "vanilla";
    
    // Legacy biome provider types
    public static final String SKY = "sky";
    public static final String CLASSIC = "classic";
    public static final String WINTER = "winter";
    public static final String PLUS = "plus";
    
    private static final Set<String> LEGACY_TYPES;
    
    private static final Map<String, BiFunction<Long, NbtCompound, AbstractBiomeProvider>> REGISTRY = 
        new HashMap<String, BiFunction<Long, NbtCompound, AbstractBiomeProvider>>(); 
    
    public static void registerBiomeProvider(String name, BiFunction<Long, NbtCompound, AbstractBiomeProvider> biomeProvider) {
        if (REGISTRY.containsKey(name)) 
            throw new IllegalArgumentException("[Modern Beta] Registry already contains biome provider named " + name);
        
        REGISTRY.put(name, biomeProvider);
    }
    
    public static BiFunction<Long, NbtCompound, AbstractBiomeProvider> getBiomeProvider(String name) {
        if (!REGISTRY.containsKey(name))
            throw new NoSuchElementException("[Modern Beta] Registry does not contain biome provider named " + name);
        
        return REGISTRY.get(name);
    }
    
    public static List<String> getBiomeProviderKeys() {
        List<Entry<String, BiFunction<Long, NbtCompound, AbstractBiomeProvider>>> entryList = new ArrayList<>(REGISTRY.entrySet());
        entryList.sort(Entry.comparingByKey());
        
        return entryList.stream().map(e -> e.getKey()).collect(Collectors.toList());
    }
    
    public static Set<String> getLegacyTypes() {
        return LEGACY_TYPES;
    }
    
    public static String getBiomeProviderType(NbtCompound settings) {
        if (settings.contains("biomeType")) 
            return settings.getString("biomeType");
        
        throw new NoSuchElementException("[Modern Beta] Settings does not contain biomeType field!");
    }
    
    static {
        LEGACY_TYPES = new HashSet<String>();
        
        LEGACY_TYPES.add(SKY);
        LEGACY_TYPES.add(CLASSIC);
        LEGACY_TYPES.add(WINTER);
        LEGACY_TYPES.add(PLUS);
    }
}
