package com.bespectacled.modernbeta.api.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;

import com.bespectacled.modernbeta.api.AbstractWorldScreenProvider;

import net.minecraft.client.gui.screen.Screen;

public class BiomeScreenProviderRegistry {
    private static final Map<String, Function<AbstractWorldScreenProvider, Screen>> REGISTRY = new HashMap<>(); 
    
    public static void register(String name, Function<AbstractWorldScreenProvider, Screen> biomeScreen) {
        if (REGISTRY.containsKey(name)) 
            throw new IllegalArgumentException("[Modern Beta] Registry already contains biome screen named " + name);
        
        REGISTRY.put(name, biomeScreen);
    }
    
    public static Function<AbstractWorldScreenProvider, Screen> get(String name) {
        if (!REGISTRY.containsKey(name))
            throw new NoSuchElementException("[Modern Beta] Registry does not contain biome screen named " + name);
        
        return REGISTRY.get(name);
    }
}
