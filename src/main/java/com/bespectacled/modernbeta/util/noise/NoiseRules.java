package com.bespectacled.modernbeta.util.noise;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.util.math.MathHelper;

public class NoiseRules<T> {
    private final List<NoiseRule<T>> noiseRules;
    
    private NoiseRules(List<NoiseRule<T>> noiseRules) {
        this.noiseRules = noiseRules;
    }
    
    public T sample(double noise) {
        for (NoiseRule<T> noiseRange : this.noiseRules) {
            T item = noiseRange.sample(noise);
            
            if (item != null)
                return item;
        }
        
        return null;
    }
    
    public List<NoiseRule<T>> asList() {
        return ImmutableList.copyOf(this.noiseRules);
    }
    
    public static class Builder<T> {
        private final List<NoiseRule<T>> noiseRules;
        
        public Builder() {
            this.noiseRules = new ArrayList<>();
        }
        
        public Builder<T> add(double min, double max, T id) {
            this.noiseRules.add(new NoiseRule<T>(min, max, id));
            
            return this;
        }
        
        public NoiseRules<T> build() {
            return new NoiseRules<T>(this.noiseRules);
        }
    }

    public static class NoiseRule<T> {
        public final double min;
        public final double max;
        public final T item;
        
        public NoiseRule(double min, double max, T item) {
            this.min = MathHelper.clamp(min, -1.0, 1.0);
            this.max = MathHelper.clamp(max, -1.0, 1.0);
            this.item = item;
            
            if (this.min > this.max)
                throw new IllegalArgumentException("[Modern Beta] Invalid noise ranges for: " + this.item.toString());
        }
        
        public T sample(double noise) {
            return min <= noise && noise <= max ? this.item : null;
        }
    }
}
