package com.bespectacled.modernbeta.world.biome.vanilla;

import net.minecraft.world.biome.layer.type.IdentitySamplingLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;

public enum VanillaAddDeepOceanLayer implements IdentitySamplingLayer {
    INSTANCE;
    
    @Override
    public int sample(LayerRandomnessSource context, int value) {
        
        if (value == 44) {
            return 47;
        }
        if (value == 45) {
            return 48;
        }
        if (value == 0) {
            return 24;
        }
        if (value == 46) {
            return 49;
        }
        if (value == 10) {
            return 50;
        }
        
        return 24;
    }
}
