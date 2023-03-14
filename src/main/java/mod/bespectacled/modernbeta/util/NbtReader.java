package mod.bespectacled.modernbeta.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class NbtReader {
    private final NbtCompound compound;
    
    public NbtReader(NbtCompound compound) {
        this.compound = compound;
    }
    
    public boolean contains(String key) {
        return this.compound.contains(key);
    }
    
    public int readIntOrThrow(String key) {
        return NbtUtil.readIntOrThrow(key, this.compound);
    }
    
    public int readInt(String key, int alternate) {
        return NbtUtil.readInt(key, this.compound, alternate);
    }
    
    public boolean readBooleanOrThrow(String key) {
        return NbtUtil.readBooleanOrThrow(key, this.compound);
    }
    
    public boolean readBoolean(String key, boolean alternate) {
        return NbtUtil.readBoolean(key, this.compound, alternate);
    }
    
    public float readFloatOrThrow(String key) {
        return NbtUtil.readFloatOrThrow(key, this.compound);
    }
    
    public float readFloat(String key, float alternate) {
        return NbtUtil.readFloat(key, this.compound, alternate);
    }
    
    public double readDoubleOrThrow(String key) {
        return NbtUtil.readDoubleOrThrow(key, this.compound);
    }
    
    public double readDouble(String key, double alternate) {
        return NbtUtil.readDouble(key, this.compound, alternate);
    }
    
    public String readStringOrThrow(String key) {
        return NbtUtil.readStringOrThrow(key, this.compound);
    }
    
    public String readString(String key, String alternate) {
        return NbtUtil.readString(key, this.compound, alternate);
    }
    
    public NbtCompound readCompoundOrThrow(String key) {
        return NbtUtil.readCompoundOrThrow(key, this.compound);
    }
    
    public NbtCompound readCompound(String key, NbtCompound alternate) {
        return NbtUtil.readCompound(key, this.compound, alternate);
    }
    
    public NbtList readListOrThrow(String key) {
        return NbtUtil.readListOrThrow(key, this.compound);
    }
    
    public NbtList readList(String key, NbtList alternate) {
        return NbtUtil.readList(key, this.compound, alternate);
    }
}
