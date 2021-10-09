package com.bespectacled.modernbeta.api.world.biome.climate;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.Identifier;

public class NoiseRanges {
    private final List<NoiseRange> noiseRanges;
    
    private NoiseRanges(List<NoiseRange> noiseRanges) {
        this.noiseRanges = noiseRanges;
    }
    
    public Identifier sample(double noise) {
        for (NoiseRange noiseRange : this.noiseRanges) {
            Identifier id = noiseRange.getId(noise);
            
            if (id != null)
                return id;
        }
        
        return null;
    }
    
    public static class Builder {
        private final List<NoiseRange> noiseRanges;
        
        public Builder() {
            this.noiseRanges = new ArrayList<>();
        }
        
        public Builder add(NoiseRange noiseRange) {
            this.noiseRanges.add(noiseRange);
            
            return this;
        }
        
        public NoiseRanges build() {
            return new NoiseRanges(this.noiseRanges);
        }
    }

}
