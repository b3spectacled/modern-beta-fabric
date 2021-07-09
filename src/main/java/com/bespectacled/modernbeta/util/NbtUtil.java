package com.bespectacled.modernbeta.util;

import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtString;

public class NbtUtil {
    /*
     * Helper methods for reading primitive values from NbtCompound objects
     */
    
    public static String readStringOrThrow(String key, NbtCompound tag) {
        if (tag.contains(key))
            return tag.getString(key);
        
        throw new IllegalArgumentException("[Modern Beta] NBT compound does not contain field " + key);
    }
    
    public static String readString(String key, NbtCompound tag, String alternate) {
        if (tag.contains(key))
            return tag.getString(key);
        
        return alternate;
    }
    
    public static int readIntOrThrow(String key, NbtCompound tag) {
        if (tag.contains(key))
            return tag.getInt(key);
        
        throw new IllegalArgumentException("[Modern Beta] NBT compound does not contain field " + key);
    }
    
    public static int readInt(String key, NbtCompound tag, int alternate) {
        if (tag.contains(key))
            return tag.getInt(key);
        
        return alternate;
    }
    
    public static float readFloatOrThrow(String key, NbtCompound tag) {
        if (tag.contains(key))
            return tag.getFloat(key);
        
        throw new IllegalArgumentException("[Modern Beta] NBT compound does not contain field " + key);
    }
    
    public static float readFloat(String key, NbtCompound tag, float alternate) {
        if (tag.contains(key))
            return tag.getFloat(key);
        
        return alternate;
    }
    
    public static boolean readBooleanOrThrow(String key, NbtCompound tag) {
        if (tag.contains(key))
            return tag.getBoolean(key);
        
        throw new IllegalArgumentException("[Modern Beta] NBT compound does not contain field " + key);
    }
    
    public static boolean readBoolean(String key, NbtCompound tag, boolean alternate) {
        if (tag.contains(key))
            return tag.getBoolean(key);
        
        return alternate;
    }
    
    /*
     * Conversion methods for extracting primitive values from NbtElement objects
     */
    
    public static String toStringOrThrow(NbtElement element) {
        if (element instanceof NbtString nbtString)
            return nbtString.asString();
        
        throw new IllegalArgumentException("[Modern Beta] NBT Element is not a string! Type:" + element.getType());
    }
    
    public static String toString(NbtElement element, String alternate) {
        if (element instanceof NbtString nbtString)
            return nbtString.asString();
        
        return alternate;
    }
    
    public static int toIntOrThrow(NbtElement element) {
        if (element instanceof NbtInt nbtInt)
            return nbtInt.intValue();
        
        throw new IllegalArgumentException("[Modern Beta] NBT Element is not an int! Type: " + element.getType()); 
    }
    
    public static int toInt(NbtElement element, int alternate) {
        if (element instanceof NbtInt nbtInt) 
            return nbtInt.intValue();
        
        return alternate;
    }
    
    public static float toFloatOrThrow(NbtElement element) {
        if (element instanceof NbtFloat nbtFloat) 
            return nbtFloat.floatValue();
        
        throw new IllegalArgumentException("[Modern Beta] NBT Element is not an float! Type: " + element.getType()); 
    }
    
    public static float toFloat(NbtElement element, float alternate) {
        if (element instanceof NbtFloat nbtFloat) 
            return nbtFloat.floatValue();
        
        return alternate;
    }
    
    public static boolean toBooleanOrThrow(NbtElement element) {
        if (element instanceof NbtByte nbtByte) 
            return nbtByte.byteValue() == 1;
        
        throw new IllegalArgumentException("[Modern Beta] NBT Element is not an byte/boolean! Type: " + element.getType()); 
    }
    
    public static boolean toBoolean(NbtElement element, boolean alternate) {
        if (element instanceof NbtByte nbtByte) 
            return nbtByte.byteValue() == 1;
        
        return alternate;
    }
}
