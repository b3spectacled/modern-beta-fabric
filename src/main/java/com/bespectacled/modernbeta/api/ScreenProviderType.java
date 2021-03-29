package com.bespectacled.modernbeta.api;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;
import com.bespectacled.modernbeta.util.PentaFunction;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.DynamicRegistryManager;

public class ScreenProviderType {
    public static final String INF = "inf";
    
    private static final Map<String, PentaFunction<CreateWorldScreen, DynamicRegistryManager, NbtCompound, NbtCompound, BiConsumer<NbtCompound, NbtCompound>, AbstractScreenProvider>> REGISTRY = 
        new HashMap<String, PentaFunction<CreateWorldScreen, DynamicRegistryManager, NbtCompound, NbtCompound, BiConsumer<NbtCompound, NbtCompound>, AbstractScreenProvider>>(); 
        
    public static void registerScreenProvider(String name, PentaFunction<CreateWorldScreen, DynamicRegistryManager, NbtCompound, NbtCompound, BiConsumer<NbtCompound, NbtCompound>, AbstractScreenProvider> screenProvider) {
        if (REGISTRY.containsKey(name)) 
            throw new IllegalArgumentException("[Modern Beta] Registry already contains screen provider named " + name);
        
        REGISTRY.put(name, screenProvider);
    }
    
    public static PentaFunction<CreateWorldScreen, DynamicRegistryManager, NbtCompound, NbtCompound, BiConsumer<NbtCompound, NbtCompound>, AbstractScreenProvider> getScreenProvider(String name) {
        if (!REGISTRY.containsKey(name))
            throw new NoSuchElementException("[Modern Beta] Registry does not contain screen provider named " + name);
        
        return REGISTRY.get(name);
    }
}
