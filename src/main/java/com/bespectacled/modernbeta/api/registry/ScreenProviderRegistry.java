package com.bespectacled.modernbeta.api.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;

import com.bespectacled.modernbeta.api.AbstractLevelScreenProvider;
import com.bespectacled.modernbeta.util.PentaFunction;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.DynamicRegistryManager;

public class ScreenProviderRegistry {
    public enum BuiltInScreenType {
        INF("inf"),
        SKYLANDS("skylands"),
        INFDEV_OLD("infdev_old"),
        INDEV("indev"),
        ISLAND("island")
        ;
        
        public final String id;
        
        private BuiltInScreenType(String id) { this.id = id; }
    }
    
    private static final Map<String, PentaFunction<CreateWorldScreen, DynamicRegistryManager, NbtCompound, NbtCompound, BiConsumer<NbtCompound, NbtCompound>, AbstractLevelScreenProvider>> REGISTRY = new HashMap<>();
        
    public static void register(String name, PentaFunction<CreateWorldScreen, DynamicRegistryManager, NbtCompound, NbtCompound, BiConsumer<NbtCompound, NbtCompound>, AbstractLevelScreenProvider> screenProvider) {
        if (REGISTRY.containsKey(name)) 
            throw new IllegalArgumentException("[Modern Beta] Registry already contains screen provider named " + name);
        
        REGISTRY.put(name, screenProvider);
    }
    
    public static PentaFunction<CreateWorldScreen, DynamicRegistryManager, NbtCompound, NbtCompound, BiConsumer<NbtCompound, NbtCompound>, AbstractLevelScreenProvider> get(String name) {
        if (!REGISTRY.containsKey(name))
            throw new NoSuchElementException("[Modern Beta] Registry does not contain screen provider named " + name);
        
        return REGISTRY.get(name);
    }
}
