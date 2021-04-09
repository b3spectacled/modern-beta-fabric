package com.bespectacled.modernbeta.api.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import com.bespectacled.modernbeta.api.AbstractBiomeProvider;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;

import net.minecraft.nbt.CompoundTag;

public class BiomeProviderRegistry {
    private static final Set<String> LEGACY_TYPES;
    
    public enum BuiltInBiomeType {
        BETA("beta"),
        SINGLE("single"),
        VANILLA("vanilla"),
        
        // Legacy biome types
        SKY("sky"),
        CLASSIC("classic"),
        WINTER("winter"),
        PLUS("plus");
        
        public final String name;
        
        private BuiltInBiomeType(String name) { this.name = name; }
    }
    
    private static final Map<String, BiFunction<Long, CompoundTag, AbstractBiomeProvider>> REGISTRY = new HashMap<>(); 
    
    public static void register(String name, BiFunction<Long, CompoundTag, AbstractBiomeProvider> biomeProvider) {
        if (REGISTRY.containsKey(name)) 
            throw new IllegalArgumentException("[Modern Beta] Registry already contains biome provider named " + name);
        
        REGISTRY.put(name, biomeProvider);
    }
    
    public static BiFunction<Long, CompoundTag, AbstractBiomeProvider> get(String name) {
        if (!REGISTRY.containsKey(name))
            throw new NoSuchElementException("[Modern Beta] Registry does not contain biome provider named " + name);
        
        return REGISTRY.get(name);
    }
    
    public static List<String> getBiomeProviderKeys() {
        List<Entry<String, BiFunction<Long, CompoundTag, AbstractBiomeProvider>>> entryList = new ArrayList<>(REGISTRY.entrySet());
        entryList.sort(Entry.comparingByKey());
        
        return entryList.stream().map(e -> e.getKey()).collect(Collectors.toList());
    }
    
    public static Set<String> getLegacyTypes() {
        return LEGACY_TYPES;
    }
    
    public static String getBiomeProviderType(CompoundTag settings) {
        if (settings.contains("biomeType")) 
            return settings.getString("biomeType");
        
        throw new NoSuchElementException("[Modern Beta] Settings does not contain biomeType field!");
    }
    
    static {
        LEGACY_TYPES = new HashSet<String>();
        
        LEGACY_TYPES.add(BuiltInBiomeType.SKY.name);
        LEGACY_TYPES.add(BuiltInBiomeType.CLASSIC.name);
        LEGACY_TYPES.add(BuiltInBiomeType.WINTER.name);
        LEGACY_TYPES.add(BuiltInBiomeType.PLUS.name);
    }
}
