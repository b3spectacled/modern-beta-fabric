package mod.bespectacled.modernbeta.settings;

import net.minecraft.nbt.NbtCompound;

public interface Settings {
    NbtCompound toCompound();
    
    void fromCompound();
}
