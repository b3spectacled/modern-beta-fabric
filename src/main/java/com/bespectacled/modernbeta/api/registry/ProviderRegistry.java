package com.bespectacled.modernbeta.api.registry;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

public final class ProviderRegistry<T> {
    private final Map<String, T> map;
    
    protected ProviderRegistry() {
        this.map = new LinkedHashMap<String, T>();
    }
    
    public void register(String key, T entry) {
        if (this.contains(key)) 
            throw new IllegalArgumentException("[Modern Beta] Registry already contains entry named " + key);
        
        this.map.put(key, entry);
    }
    
    public T get(String key) {
        if (!this.contains(key))
            throw new NoSuchElementException("[Modern Beta] Registry does not contain entry named " + key);
        
        return this.map.get(key);
    }
    
    public T get(String key, T alternate) {
        if (!this.contains(key))
            return alternate;
        
        return this.map.get(key);
    }
    
    public boolean contains(String key) {
        return this.map.containsKey(key);
    }
    
    public Set<String> getKeys() {
        return this.map.keySet();
    }
    
    public List<T> getEntries() {
        return this.map.values().stream().collect(Collectors.toUnmodifiableList());
    }
}
  