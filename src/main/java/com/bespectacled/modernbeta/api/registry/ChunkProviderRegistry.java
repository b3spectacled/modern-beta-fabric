package com.bespectacled.modernbeta.api.registry;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.AbstractBiomeProvider;
import com.bespectacled.modernbeta.api.AbstractChunkProvider;
import com.bespectacled.modernbeta.util.QuadFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class ChunkProviderRegistry {
    public enum BuiltInChunkType {
        BETA("beta"),
        SKYLANDS("skylands"),
        ALPHA("alpha"),
        INFDEV_415("infdev"),
        INFDEV_227("infdev_old"),
        INDEV("indev"),
        BETA_ISLANDS("beta_islands")
        ;
        
        public final String name;
        
        private BuiltInChunkType(String name) { this.name = name; }
    }
    
    private static final Map<String, QuadFunction<Long, AbstractBiomeProvider, Supplier<ChunkGeneratorSettings>, CompoundTag, AbstractChunkProvider>> REGISTRY = new HashMap<>(); 
    
    public static void register(String name, QuadFunction<Long, AbstractBiomeProvider, Supplier<ChunkGeneratorSettings>, CompoundTag, AbstractChunkProvider> chunkProvider) {
        if (REGISTRY.containsKey(name)) 
            throw new IllegalArgumentException("[Modern Beta] Registry already contains chunk provider named " + name);
        
        REGISTRY.put(name, chunkProvider);
    }
    
    public static QuadFunction<Long, AbstractBiomeProvider, Supplier<ChunkGeneratorSettings>, CompoundTag, AbstractChunkProvider> get(String name) {
        if (!REGISTRY.containsKey(name))
            throw new NoSuchElementException("[Modern Beta] Registry does not contain chunk provider named " + name);
        
        return REGISTRY.get(name);
    }
    
    public static String getChunkProviderType(CompoundTag settings) {
        if (settings.contains("worldType")) 
            return settings.getString("worldType");
        
        throw new NoSuchElementException("[Modern Beta] Settings does not contain worldType field!");
    }
    
    public static Set<String> getChunkProviders() {
        return Collections.unmodifiableSet(new HashSet<String>(REGISTRY.keySet()));
    }
}
