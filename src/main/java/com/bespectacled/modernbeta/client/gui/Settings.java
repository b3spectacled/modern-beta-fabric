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
    
    private final Map<String, NbtElement> changes;
    
    public Settings() {
        this.changes = new LinkedHashMap<>();
    }
    
    public Settings(NbtCompound initial) {
        this();
        
        this.readNbt(initial);
    }

    public void putChange(String key, NbtElement element) {
        this.changes.put(key, element);
        
        if (DEBUG) {
            ModernBeta.log(Level.INFO, "Queueing setting for key '" + key + "'");
            ModernBeta.log(Level.INFO, "Current queue:");
            
            for (Entry<String, NbtElement> change : this.changes.entrySet()) {
                ModernBeta.log(Level.INFO, "* '" + change.getKey() + "'");
            }
            
        }
    }
    
    public void putChanges(NbtCompound compound) {
        for (String key : compound.getKeys()) {
            this.putChange(key, compound.get(key));
        }
    }
    
    public void clearChanges() {
        this.changes.clear();
    }
    
    public NbtElement getSetting(String key) {
        if (this.changes.containsKey(key))
            return this.changes.get(key);
        
        return null;
    }
    
    public NbtCompound getNbt() {
        NbtCompound compound = new NbtCompound();
        
        for (Entry<String, NbtElement> change : this.changes.entrySet()) {
            compound.put(change.getKey(), change.getValue());
        }
        
        return compound;
    }
    
    private void readNbt(NbtCompound compound) {
        for (String key : compound.getKeys()) {
            this.changes.put(key, compound.get(key));
        }
    }
}
