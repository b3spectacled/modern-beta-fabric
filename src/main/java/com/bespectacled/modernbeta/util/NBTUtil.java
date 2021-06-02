package com.bespectacled.modernbeta.util;

import net.minecraft.nbt.CompoundTag;

public class NBTUtil {
    public static String readStringOrThrow(String key, CompoundTag tag) {
        if (tag.contains(key))
            return tag.getString(key);
        
        throw new IllegalArgumentException("[Modern Beta] NBT compound does not contain field " + key);
    }
    
    public static String readString(String key, CompoundTag tag, String alternate) {
        if (tag.contains(key))
            return tag.getString(key);
        
        return alternate;
    }
    
    public static int readIntOrThrow(String key, CompoundTag tag) {
        if (tag.contains(key))
            return tag.getInt(key);
        
        throw new IllegalArgumentException("[Modern Beta] NBT compound does not contain field " + key);
    }
    
    public static int readInt(String key, CompoundTag tag, int alternate) {
        if (tag.contains(key))
            return tag.getInt(key);
        
        return alternate;
    }
    
    public static float readFloatOrThrow(String key, CompoundTag tag) {
        if (tag.contains(key))
            return tag.getFloat(key);
        
        throw new IllegalArgumentException("[Modern Beta] NBT compound does not contain field " + key);
    }
    
    public static float readFloat(String key, CompoundTag tag, float alternate) {
        if (tag.contains(key))
            return tag.getFloat(key);
        
        return alternate;
    }
    
    public static boolean readBooleanOrThrow(String key, CompoundTag tag) {
        if (tag.contains(key))
            return tag.getBoolean(key);
        
        throw new IllegalArgumentException("[Modern Beta] NBT compound does not contain field " + key);
    }
    
    public static boolean readBoolean(String key, CompoundTag tag, boolean alternate) {
        if (tag.contains(key))
            return tag.getBoolean(key);
        
        return alternate;
    }
}
