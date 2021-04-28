package com.bespectacled.modernbeta.util;

import net.minecraft.nbt.CompoundTag;

public abstract class NBTUtil {
    public static String readString(String key, CompoundTag tag) {
        if (tag.contains(key))
            return tag.getString(key);
        
        throw new IllegalArgumentException("NBT compound does not contain field " + key);
    }
}