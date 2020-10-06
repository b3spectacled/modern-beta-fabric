package com.bespectacled.modernbeta.util;

import net.minecraft.world.World;

public interface MutableClientWorld {
    static MutableClientWorld inject(World world) {
        return (MutableClientWorld) world;
    }
    
    void setSeed(long seed);
}
