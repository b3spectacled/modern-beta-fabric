package mod.bespectacled.modernbeta.util.settings;

import java.util.Set;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public interface Settings {
    void put(String key, NbtElement element);
    
    void putCompound(NbtCompound compound);
    
    void clear();
    
    boolean remove(String key);

    boolean containsKey(String key);

    NbtElement get(String key);
    
    Set<String> keySet();
}
