package com.bespectacled.modernbeta.world.biome.provider.climate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.world.biome.climate.Clime;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;

public class VanillaClimateRules {
    private final List<VanillaClimateRule> rules;

    private VanillaClimateRules(List<VanillaClimateRule> rules) {
        this.rules = rules;
    }
    
    public Clime apply(Biome biome) {
        for (VanillaClimateRule rule : this.rules) {
            Clime clime = rule.apply(biome);
            
            if (clime != null)
                return clime;
        }
        
        double temp = MathHelper.clamp(biome.getTemperature(), 0.0, 1.0);
        double rain = MathHelper.clamp(biome.getDownfall(), 0.0, 1.0);
        
        return new Clime(temp, rain);
    }
    
    public static class Builder {
        private final List<VanillaClimateRule> rules;
        
        public Builder() {
            this.rules = new ArrayList<>();
        }
        
        public Builder add(Predicate<Biome> rule, Supplier<Clime> supplier) {
            this.rules.add(new VanillaClimateRule(rule, supplier));
            
            return this;
        }
        
        public VanillaClimateRules build() {
            return new VanillaClimateRules(this.rules);
        }
    }
}
