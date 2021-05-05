package com.bespectacled.modernbeta.api.registry;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;

public final class ProviderRegistry<T> {
    private final String name;
    private final Map<String, T> map; // Use LinkedHashMap so entries are displayed in order if retrieved as list.
    
    protected ProviderRegistry() {
        this.name = "";
        this.map = new LinkedHashMap<String, T>();
    }
    
    protected ProviderRegistry(String name) {
        this.name = name;
        this.map = new LinkedHashMap<String, T>();
    }
    
    public void register(String key, T entry) {
        if (this.contains(key)) 
            throw new IllegalArgumentException("[Modern Beta] Registry " + this.name + " already contains entry named " + key);
        
        this.map.put(key, entry);
    }
    
    public T get(String key) {
        if (!this.contains(key))
            throw new NoSuchElementException("[Modern Beta] Registry " + this.name + " does not contain entry named " + key);
        
        return this.map.get(key);
    }

    public T getOrDefault(String key) {
        if (!this.contains(key)) {
            ModernBeta.LOGGER.log(Level.WARN, "[Modern Beta] Registry " + this.name + " does not contain entry named " + key + ", getting default entry.");
            return this.map.get(BuiltInTypes.DEFAULT_ID);
        }
        
        return this.map.get(key);
    }
    
    public T get(String key, String alternate) {
        if (!this.contains(key)) {
            ModernBeta.LOGGER.log(Level.WARN, "[Modern Beta] Registry " + this.name + " does not contain entry named " + key + ", defaulting to " + alternate);
            return this.map.get(alternate);
        }
        
        return this.map.get(key);
    }
    
    public boolean contains(String key) {
        return this.map.containsKey(key);
    }
    
    public Set<String> getKeys() {
        return this.map.keySet()
            .stream()
            .filter(k -> !k.equals(BuiltInTypes.DEFAULT_ID))
            .collect(Collectors.toSet());
    }
    
    public List<T> getEntries() {
        return this.map.entrySet()
            .stream()
            .filter(e -> !e.getKey().equals(BuiltInTypes.DEFAULT_ID))
            .map(e -> e.getValue())
            .collect(Collectors.toList());
    }
}
  