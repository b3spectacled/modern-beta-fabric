package com.bespectacled.modernbeta.api.world.cavebiome.climate;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.Identifier;

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
        
        public Builder add(ClimateNoiseRule noiseRange) {
            this.noiseRules.add(noiseRange);
            
            return this;
        }
        
        public ClimateNoiseRules build() {
            return new ClimateNoiseRules(this.noiseRules);
        }
    }

}
