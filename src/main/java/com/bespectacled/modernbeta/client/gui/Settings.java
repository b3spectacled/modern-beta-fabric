package com.bespectacled.modernbeta.client.gui;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public class Settings {
    private static final boolean DEBUG = false;
    
    private final Map<String, NbtElement> settings;
    
    public Settings() {
        this.settings = new LinkedHashMap<>();
    }
    
    public Settings(NbtCompound initial) {
        this();
        
        this.putCompound(initial);
    }

    public Settings putElement(String key, NbtElement element) {
        this.settings.put(key, element);
        
        if (DEBUG) {
            ModernBeta.log(Level.INFO, "Queueing setting for key '" + key + "'");
            ModernBeta.log(Level.INFO, "Current queue:");
            
            for (Entry<String, NbtElement> change : this.settings.entrySet()) {
                ModernBeta.log(Level.INFO, "* '" + change.getKey() + "'");
            }
            
        }
        
        return this;
    }
    
    public Settings putCompound(NbtCompound compound) {
        for (String key : compound.getKeys()) {
            this.putElement(key, compound.get(key));
        }
        
        return this;
    }
    
    public void clearAll() {
        this.settings.clear();
    }
    
    public NbtElement getElement(String key) {
        if (this.settings.containsKey(key))
            return this.settings.get(key);
        
        return null;
    }
    
    public boolean hasElement(String key) {
        if (this.settings.containsKey(key))
            return true;
        
        return false;
    }
    
    public boolean removeElement(String key) {
        if (this.settings.containsKey(key)) {
            this.settings.remove(key);
            return true;
        }
        
        return false;
    }
    
    public NbtCompound getNbt() {
        NbtCompound compound = new NbtCompound();
        
        for (Entry<String, NbtElement> change : this.settings.entrySet()) {
            compound.put(change.getKey(), change.getValue());
        }
        
        return compound;
    }
}
