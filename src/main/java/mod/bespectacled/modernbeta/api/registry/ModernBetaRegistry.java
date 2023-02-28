package mod.bespectacled.modernbeta.api.registry;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.event.Level;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;

public final class ModernBetaRegistry<T> {
    private final String name;
    private final Map<String, T> map; // Use LinkedHashMap so entries are displayed in order if retrieved as list.
    
    protected ModernBetaRegistry(String name) {
        this.name = name;
        this.map = new LinkedHashMap<String, T>();
    }
    
    protected ModernBetaRegistry() {
        this("");
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
    
    public Optional<T> getOrEmpty(String key) {
        if (!this.contains(key)) {
            ModernBeta.log(Level.WARN, "Registry " + this.name + " does not contain entry named " + key);
        }
            
        return Optional.ofNullable(this.map.get(key));
    }

    public T getOrDefault(String key) {
        if (!this.contains(key)) {
            ModernBeta.log(Level.WARN, "Registry " + this.name + " does not contain entry named " + key + ", getting default entry.");
            return this.get(ModernBetaBuiltInTypes.DEFAULT_ID);
        }
        
        return this.map.get(key);
    }
    
    public T getOrElse(String key, String alternate) {
        if (!this.contains(key)) {
            return this.get(alternate);
        }
        
        return this.map.get(key);
    }
    
    public boolean contains(String key) {
        return this.map.containsKey(key);
    }
    
    public Set<String> getKeySet() {
        return this.map.keySet()
            .stream()
            .filter(k -> !k.equals(ModernBetaBuiltInTypes.DEFAULT_ID))
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }
    
    public List<T> getEntries() {
        return this.map.entrySet()
            .stream()
            .filter(e -> !e.getKey().equals(ModernBetaBuiltInTypes.DEFAULT_ID))
            .map(e -> e.getValue())
            .collect(Collectors.toList());
    }
    
    public Set<Entry<String, T>> getEntrySet() {
        return this.map.entrySet()
            .stream()
            .filter(e -> !e.getKey().equals(ModernBetaBuiltInTypes.DEFAULT_ID))
            .collect(Collectors.toSet());
    }
}
  