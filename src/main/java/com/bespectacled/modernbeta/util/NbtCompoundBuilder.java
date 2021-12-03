package com.bespectacled.modernbeta.util;

import net.minecraft.nbt.NbtCompound;

public class NbtCompoundBuilder {
    private final NbtCompound compound;
    
    public NbtCompoundBuilder() {
        this.compound = new NbtCompound();
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
    
    public NbtCompound build() {
        return this.compound;
    }
}
