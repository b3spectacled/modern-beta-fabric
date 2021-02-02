package com.bespectacled.modernbeta.util;

import net.minecraft.client.world.ClientWorld;

public interface MutableClientWorld {
    static MutableClientWorld inject(ClientWorld world) {
        return (MutableClientWorld)world;
    }
    
    boolean usesBetaColors();
}
