package com.bespectacled.modernbeta.world.cavebiome.provider.climate;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class ClimateNoiseRules {
    private final List<ClimateNoiseRule> noiseRules;
    
    private ClimateNoiseRules(List<ClimateNoiseRule> noiseRules) {
        this.noiseRules = noiseRules;
    }
    
    public Identifier sample(double noise) {
        for (ClimateNoiseRule noiseRange : this.noiseRules) {
            Identifier id = noiseRange.getId(noise);
            
            if (id != null)
                return id;
        }
        
        return null;
    }
    
    public static class Builder {
        private final List<ClimateNoiseRule> noiseRules;
        
        public Builder() {
            this.noiseRules = new ArrayList<>();
        }
        
        public Builder add(double min, double max, Identifier id) {
            this.noiseRules.add(new ClimateNoiseRule(min, max, id));
            
            return this;
        }
        
        public ClimateNoiseRules build() {
            return new ClimateNoiseRules(this.noiseRules);
        }
    }

    private static class ClimateNoiseRule {
        private final double min;
        private final double max;
        private final Identifier id;
        
        public ClimateNoiseRule(double min, double max, Identifier id) {
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
}
