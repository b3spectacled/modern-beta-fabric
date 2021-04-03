package com.bespectacled.modernbeta.world.biome.vanilla;

import net.minecraft.world.biome.layer.type.DiagonalCrossSamplingLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;

public enum VanillaAddOceanlessMushroomLayer implements DiagonalCrossSamplingLayer {
    INSTANCE;

    public int sample(LayerRandomnessSource context, int sw, int se, int ne, int nw, int center) {
        // Replace jungles with mushroom fields
        return center == 21 && sw == 21 && se == 21 && ne == 21 && nw == 21 && context.nextInt(2) == 0 ? 14 : center;
    }
}
