package com.bespectacled.modernbeta.api.chunk;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.util.TriFunction;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class ChunkProviderType {
    public enum BuiltInChunkType {
        BETA("beta"),
        SKYLANDS("skylands"),
        ALPHA("alpha"),
        INFDEV_415("infdev_415"),
        INFDEV_227("infdev_227"),
        INDEV("indev");
        
        public final String id;
        
        private BuiltInChunkType(String id) { this.id = id; }
    }
    
    private static final Map<String, TriFunction<Long, Supplier<ChunkGeneratorSettings>, NbtCompound, AbstractChunkProvider>> REGISTRY = 
        new HashMap<String, TriFunction<Long, Supplier<ChunkGeneratorSettings>, NbtCompound, AbstractChunkProvider>>(); 
    
    public static void registerChunkProvider(String name, TriFunction<Long, Supplier<ChunkGeneratorSettings>, NbtCompound, AbstractChunkProvider> chunkProvider) {
        if (REGISTRY.containsKey(name)) 
            throw new IllegalArgumentException("[Modern Beta] Registry already contains chunk provider named " + name);
        
        REGISTRY.put(name, chunkProvider);
    }
    
    public static TriFunction<Long, Supplier<ChunkGeneratorSettings>, NbtCompound, AbstractChunkProvider> getChunkProvider(String name) {
        if (!REGISTRY.containsKey(name))
            throw new NoSuchElementException("[Modern Beta] Registry does not contain chunk provider named " + name);
        
        return REGISTRY.get(name);
    }
    
    public static String getChunkProviderType(NbtCompound settings) {
        if (settings.contains("worldType")) 
            return settings.getString("worldType");
        
        throw new NoSuchElementException("[Modern Beta] Settings does not contain worldType field!");
    }
}
