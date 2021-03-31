package com.bespectacled.modernbeta.api.gen;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.biome.AbstractBiomeProvider;
import com.bespectacled.modernbeta.util.QuadFunction;
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
    
    private static final Map<String, QuadFunction<Long, AbstractBiomeProvider, Supplier<ChunkGeneratorSettings>, NbtCompound, AbstractChunkProvider>> REGISTRY = new HashMap<>(); 
    
    public static void registerProvider(String name, QuadFunction<Long, AbstractBiomeProvider, Supplier<ChunkGeneratorSettings>, NbtCompound, AbstractChunkProvider> chunkProvider) {
        if (REGISTRY.containsKey(name)) 
            throw new IllegalArgumentException("[Modern Beta] Registry already contains chunk provider named " + name);
        
        REGISTRY.put(name, chunkProvider);
    }
    
    public static QuadFunction<Long, AbstractBiomeProvider, Supplier<ChunkGeneratorSettings>, NbtCompound, AbstractChunkProvider> getProvider(String name) {
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
