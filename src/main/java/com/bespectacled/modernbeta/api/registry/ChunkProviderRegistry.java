package com.bespectacled.modernbeta.api.registry;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.AbstractChunkProvider;
import com.bespectacled.modernbeta.util.QuadFunction;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class ChunkProviderRegistry {
    public enum BuiltInChunkType {
        BETA("beta"),
        SKYLANDS("skylands"),
        ALPHA("alpha"),
        INFDEV_415("infdev_415"),
        INFDEV_227("infdev_227"),
        INDEV("indev"),
        BETA_ISLANDS("beta_islands")
        ;
        
        public final String name;
        
        private BuiltInChunkType(String name) { this.name = name; }
    }
    
    private static final Map<String, QuadFunction<Long, ChunkGenerator, Supplier<ChunkGeneratorSettings>, NbtCompound, AbstractChunkProvider>> REGISTRY = new HashMap<>(); 
    
    public static void register(String name, QuadFunction<Long, ChunkGenerator, Supplier<ChunkGeneratorSettings>, NbtCompound, AbstractChunkProvider> chunkProvider) {
        if (REGISTRY.containsKey(name)) 
            throw new IllegalArgumentException("[Modern Beta] Registry already contains chunk provider named " + name);
        
        REGISTRY.put(name, chunkProvider);
    }
    
    public static QuadFunction<Long, ChunkGenerator, Supplier<ChunkGeneratorSettings>, NbtCompound, AbstractChunkProvider> get(String name) {
        if (!REGISTRY.containsKey(name))
            throw new NoSuchElementException("[Modern Beta] Registry does not contain chunk provider named " + name);
        
        return REGISTRY.get(name);
    }
    
    public static String getChunkProviderType(NbtCompound settings) {
        if (settings.contains("worldType")) 
            return settings.getString("worldType");
        
        throw new NoSuchElementException("[Modern Beta] Settings does not contain worldType field!");
    }
    
    public static Set<String> getChunkProviders() {
        return Collections.unmodifiableSet(new HashSet<String>(REGISTRY.keySet()));
    }
}
