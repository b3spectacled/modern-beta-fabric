package com.bespectacled.modernbeta.api.world.biome.climate;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class NoiseRange {
    private final double min;
    private final double max;
    private final Identifier id;
    
    public NoiseRange(double min, double max, Identifier id) {
        this.min = MathHelper.clamp(min, -1.0, 1.0);
        this.max = MathHelper.clamp(max, -1.0, 1.0);
        this.id = id;
        
        if (this.min > this.max)
            throw new IllegalArgumentException("[Modern Beta] Invalid noise ranges for: " + this.id.toString());
    }
    
    public Identifier getId(double noise) {
        return min <= noise && noise <= max ? this.id : null;
    }
}
