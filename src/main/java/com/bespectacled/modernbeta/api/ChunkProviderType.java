package com.bespectacled.modernbeta.api;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import com.bespectacled.modernbeta.util.TriFunction;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class ChunkProviderType {
    public static final String BETA = "beta";
    public static final String SKYLANDS = "skylands";
    public static final String ALPHA = "alpha";
    public static final String INFDEV_415 = "infdev_415";
    public static final String INFDEV_227 = "infdev_227";
    public static final String INDEV = "indev";
    
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
