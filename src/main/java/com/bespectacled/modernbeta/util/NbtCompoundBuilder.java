package com.bespectacled.modernbeta.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class NbtCompoundBuilder {
    private final NbtCompound compound;
    
    public NbtCompoundBuilder() {
        this.compound = new NbtCompound();
    }
    
    public NbtCompoundBuilder(NbtCompound initial) {
        this.compound = initial.copy();
    }
    
    public NbtCompoundBuilder putString(String key, String value) {
        this.compound.putString(key, value);
        
        return this;
    }
    
    public NbtCompoundBuilder putInt(String key, int value) {
        this.compound.putInt(key, value);
        
        return this;
    }
    
    public NbtCompoundBuilder putBoolean(String key, boolean value) {
        this.compound.putBoolean(key, value);
        
        return this;
    }
    
    public NbtCompoundBuilder putFloat(String key, float value) {
        this.compound.putFloat(key, value);
        
        return this;
    }
    
    public NbtCompoundBuilder putDouble(String key, double value) {
        this.compound.putDouble(key, value);
        
        return this;
    }
    
    public NbtCompoundBuilder putList(String key, NbtList list) {
        this.compound.put(key, list);
        
        return this;
    }
    
    public NbtCompoundBuilder putCompound(String key, NbtCompound compound) {
        this.compound.put(key, compound);
        
        return this;
    }
    
    public NbtCompoundBuilder putBuilder(NbtCompoundBuilder builder) {
        builder.compound.getKeys().forEach(key -> this.compound.put(key, compound.get(key)));
        
        return this;
    }
    
    public NbtCompound build() {
        return this.compound;
    }
}
