package com.bespectacled.modernbeta.world.gen;

import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.VanillaSurfaceRules;

public class OldSurfaceRules {
    public static MaterialRules.MaterialRule createVanilla(boolean hasSurface) {
        return VanillaSurfaceRules.createDefaultRule(hasSurface, false, false);
    }
}
