package mod.bespectacled.modernbeta.util.settings;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.Level;

import mod.bespectacled.modernbeta.ModernBeta;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public class MutableSettings implements Settings {
    private static final boolean DEBUG = false;
    
    private final Map<String, NbtElement> entries = new LinkedHashMap<>();
    
    public MutableSettings(NbtCompound initial) {
        this.putCompound(initial);
    }

    @Override
    public void put(String key, NbtElement element) {
        this.entries.put(key, element);
        
        if (DEBUG) {
            ModernBeta.log(Level.INFO, "Queueing setting for key '" + key + "'");
            ModernBeta.log(Level.INFO, "Current queue:");
            
            for (Entry<String, NbtElement> change : this.entries.entrySet()) {
                ModernBeta.log(Level.INFO, "* '" + change.getKey() + "'");
            }
        }
    }
    
    @Override
    public void putCompound(NbtCompound compound) {
        for (String key : compound.getKeys()) {
            this.put(key, compound.get(key));
        }
    }
    
    @Override
    public void clear() {
        this.entries.clear();
    }
    
    @Override
    public boolean containsKey(String key) {
        return this.entries.containsKey(key);
    }

    @Override
    public NbtElement get(String key) {
        return this.entries.get(key);
    }
    
    @Override
    public boolean remove(String key) {
        if (this.entries.containsKey(key)) {
            this.entries.remove(key);
            return true;
        }
        
        return false;
    }
    
    @Override
    public Set<String> keySet() {
        return this.entries.keySet();
    }

    public static MutableSettings copyOf(Settings settings) {
        NbtCompound compound = new NbtCompound();
        
        for (String key : settings.keySet()) {
            compound.put(key, settings.get(key));
        }
        
        return new MutableSettings(compound);
    }
}
