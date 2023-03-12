package mod.bespectacled.modernbeta.util;

import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

public class NbtListBuilder {
    private final NbtList list;
    private int index;
    
    public NbtListBuilder() {
        this.list = new NbtList();
        this.index = 0;
    }
    
    public NbtListBuilder add(NbtElement element) {
        this.list.add(this.index, element);
        index++;
        
        return this;
    }
    
    public NbtList build() {
        return this.list;
    }
}
