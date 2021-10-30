package com.bespectacled.modernbeta.api.world.biome.climate;

import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.world.biome.Biome;

public class ClimateBiomeRule {
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
