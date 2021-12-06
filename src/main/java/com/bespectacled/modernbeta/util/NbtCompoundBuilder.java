package com.bespectacled.modernbeta.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public class NbtCompoundBuilder {
    private final NbtCompound compound;
    private final Map<String, NbtListBuilder> lists;
    
    public NbtCompoundBuilder() {
        this.compound = new NbtCompound();
        this.lists = new HashMap<>();
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
    
    public NbtCompoundBuilder addListItem(String key, NbtElement element) {
        NbtListBuilder builder = this.lists.get(key);
        
        if (builder == null) {
            builder = new NbtListBuilder();
            this.lists.put(key, builder);
        }
        
        builder.add(element);
        
        return this;
    }
    
    public NbtCompound build() {
        for (Entry<String, NbtListBuilder> entry : this.lists.entrySet()) {
            this.compound.put(entry.getKey(), entry.getValue().build());
        }
        
        return this.compound;
    }
}