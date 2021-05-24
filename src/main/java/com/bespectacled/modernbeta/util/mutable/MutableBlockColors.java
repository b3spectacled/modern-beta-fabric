package com.bespectacled.modernbeta.util.mutable;

import net.minecraft.client.color.block.BlockColors;

public interface MutableBlockColors {
    static MutableBlockColors inject(BlockColors blockColors) {
        return (MutableBlockColors) blockColors;
    }

    void setSeed(long seed, boolean defaultColors);
}
