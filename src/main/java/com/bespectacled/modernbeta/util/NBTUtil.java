package com.bespectacled.modernbeta.util;

import net.minecraft.nbt.NbtCompound;

public abstract class NBTUtil {
    public static String readString(String key, NbtCompound tag) {
        if (tag.contains(key))
            return tag.getString(key);
        
        throw new IllegalArgumentException("NBT compound does not contain field " + key);
    }
}
