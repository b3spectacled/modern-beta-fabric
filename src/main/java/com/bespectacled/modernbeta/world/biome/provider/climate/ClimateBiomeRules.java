package com.bespectacled.modernbeta.world.biome.provider.climate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.world.biome.climate.Clime;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;

public class ClimateBiomeRules {
    private final List<ClimateBiomeRule> rules;

    private ClimateBiomeRules(List<ClimateBiomeRule> rules) {
        this.rules = rules;
    }
    
    public Clime apply(Biome biome) {
        for (ClimateBiomeRule rule : this.rules) {
            Clime clime = rule.apply(biome);
            
            if (clime != null)
                return clime;
        }
        
        double temp = MathHelper.clamp(biome.getTemperature(), 0.0, 1.0);
        double rain = MathHelper.clamp(biome.getDownfall(), 0.0, 1.0);
        
        return new Clime(temp, rain);
    }
    
    public static class Builder {
        private final List<ClimateBiomeRule> rules;
        
        public Builder() {
            this.rules = new ArrayList<>();
        }
        
        public Builder add(Predicate<Biome> rule, Supplier<Clime> supplier) {
            this.rules.add(new ClimateBiomeRule(rule, supplier));
            
            return this;
        }
        
        public ClimateBiomeRules build() {
            return new ClimateBiomeRules(this.rules);
        }
    }
    
    private static class ClimateBiomeRule {
        private final Predicate<Biome> rule;
        private final Supplier<Clime> supplier;
        
        public ClimateBiomeRule(Predicate<Biome> rule, Supplier<Clime> supplier) {
            this.rule = rule;
            this.supplier = supplier;
        }
        
        public Clime apply(Biome biome) {
            if (this.test(biome))
                return this.supplier.get();
            
            return null;
        }
        
        private boolean test(Biome biome) {
            return this.rule.test(biome);
        }
    }
}
