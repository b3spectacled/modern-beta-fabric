package com.bespectacled.modernbeta.util;

import net.minecraft.nbt.NbtCompound;

public abstract class NBTUtil {
    public static String readStringOrThrow(String key, NbtCompound tag) {
        if (tag.contains(key))
            return tag.getString(key);
        
        throw new IllegalArgumentException("NBT compound does not contain field " + key);
    }
    
    public static String readString(String key, NbtCompound tag, String alternate) {
        if (tag.contains(key))
            return tag.getString(key);
        
        return alternate;
    }
    
    public static int readIntOrThrow(String key, NbtCompound tag) {
        if (tag.contains(key))
            return tag.getInt(key);
        
        throw new IllegalArgumentException("NBT compound does not contain field " + key);
    }
    
    public static int readInt(String key, NbtCompound tag, int alternate) {
        if (tag.contains(key))
            return tag.getInt(key);
        
        return alternate;
    }
    
    public static float readFloatOrThrow(String key, NbtCompound tag) {
        if (tag.contains(key))
            return tag.getFloat(key);
        
        throw new IllegalArgumentException("NBT compound does not contain field " + key);
    }
    
    public static float readFloat(String key, NbtCompound tag, float alternate) {
        if (tag.contains(key))
            return tag.getFloat(key);
        
        return alternate;
    }
    
    public static boolean readBooleanOrThrow(String key, NbtCompound tag) {
        if (tag.contains(key))
            return tag.getBoolean(key);
        
        throw new IllegalArgumentException("NBT compound does not contain field " + key);
    }
    
    public static boolean readBoolean(String key, NbtCompound tag, boolean alternate) {
        if (tag.contains(key))
            return tag.getBoolean(key);
        
        return alternate;
    }
}
