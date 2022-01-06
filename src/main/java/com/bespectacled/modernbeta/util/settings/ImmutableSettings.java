package com.bespectacled.modernbeta.util.settings;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public class ImmutableSettings implements Settings {
    private final Map<String, NbtElement> entries;
    
    public ImmutableSettings() {
        this.entries = new LinkedHashMap<>();
    }
    
    public ImmutableSettings(NbtCompound initial) {
        this();

        for (String key : initial.getKeys()) {
            this.entries.put(key, initial.get(key));
        }
    }
    
    @Override
    public void put(String key, NbtElement element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putCompound(NbtCompound compound) {
        throw new UnsupportedOperationException();
        
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsKey(String key) {
        return this.entries.containsKey(key);
    }

    @Override
    public NbtElement get(String key) {
        if (this.entries.containsKey(key))
            return this.entries.get(key);
        
        return null;
    }
    
    @Override
    public Set<String> keySet() {
        return this.entries.keySet();
    }
    
    @Override
    public NbtCompound getNbt() {
        NbtCompound compound = new NbtCompound();
        
        for (Entry<String, NbtElement> change : this.entries.entrySet()) {
            compound.put(change.getKey(), change.getValue().copy());
        }
        
        return compound;
    }
    
    public static ImmutableSettings copyOf(Settings settings) {
        NbtCompound compound = new NbtCompound();
        
        for (String key : settings.keySet()) {
            compound.put(key, settings.get(key));
        }
        
        return new ImmutableSettings(compound);
    }
}
