package com.bespectacled.modernbeta.gui;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public class Settings {
    private static final boolean DEBUG = false;
    
    private final NbtCompound compound;
    private final Map<String, NbtElement> changes;
    
    public Settings() {
        this.compound = new NbtCompound();
        this.changes = new LinkedHashMap<>();
    }
    
    public Settings(NbtCompound initial) {
        this.compound = new NbtCompound().copyFrom(initial);
        this.changes = new LinkedHashMap<>();
    }

    public void putChange(String key, NbtElement element) {
        this.changes.put(key, element);
        
        if (DEBUG) {
            ModernBeta.log(Level.INFO, "Queueing change for key '" + key + "'");
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
    
    public void applyChanges() {
        for (Entry<String, NbtElement> change : this.changes.entrySet()) {
            this.compound.put(change.getKey(), change.getValue());
            
            if (DEBUG)
                ModernBeta.log(Level.INFO, "Applying change for key '" + change.getKey() + "'");
        }
        
        this.clearChanges();
    }
    
    public NbtElement getSetting(String key) {
        // If change to a setting is queued,
        // then get that as it is newer.
        if (this.changes.containsKey(key))
            return this.changes.get(key);
        
        return this.compound.get(key);
    }
    
    public NbtCompound getStored() {
        return new NbtCompound().copyFrom(this.compound);
    }
    
    public NbtCompound getStoredAndQueued() {
        NbtCompound queued = this.getStored();
        
        for (Entry<String, NbtElement> change : this.changes.entrySet()) {
            queued.put(change.getKey(), change.getValue());
        }
        
        return queued;
    }
}
