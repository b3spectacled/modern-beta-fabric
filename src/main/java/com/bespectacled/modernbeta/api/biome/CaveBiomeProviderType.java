package com.bespectacled.modernbeta.api.biome;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

import net.minecraft.nbt.NbtCompound;

public class CaveBiomeProviderType {
    public enum BuiltInCaveBiomeType {
        NONE("none"),
        VANILLA("vanilla");
        
        public final String id;
        
        private BuiltInCaveBiomeType(String id) { this.id = id; }
    }
    
    private static final Map<String, BiFunction<Long, NbtCompound, AbstractCaveBiomeProvider>> REGISTRY = new HashMap<>(); 
    
    public static void registerProvider(String name, BiFunction<Long, NbtCompound, AbstractCaveBiomeProvider> biomeProvider) {
        if (REGISTRY.containsKey(name)) 
            throw new IllegalArgumentException("[Modern Beta] Registry already contains cave biome provider named " + name);
        
        REGISTRY.put(name, biomeProvider);
    }
    
    public static BiFunction<Long, NbtCompound, AbstractCaveBiomeProvider> getProvider(String name) {
        if (!REGISTRY.containsKey(name))
            throw new NoSuchElementException("[Modern Beta] Registry does not contain cave biome provider named " + name);
        
        return REGISTRY.get(name);
    }
    
    public static String getCaveBiomeProviderType(NbtCompound settings) {
        if (settings.contains("caveBiomeType")) 
            return settings.getString("caveBiomeType");
        
        throw new NoSuchElementException("[Modern Beta] Settings does not contain caveBiomeType field!");
    }
}
