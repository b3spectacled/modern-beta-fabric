package com.bespectacled.modernbeta.world.biome.provider.climate;

import java.util.function.Predicate;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.world.biome.climate.Clime;

import net.minecraft.world.biome.Biome;

public class VanillaClimateRule {
    private final Predicate<Biome> rule;
    private final Supplier<Clime> supplier;
    
    public VanillaClimateRule(Predicate<Biome> rule, Supplier<Clime> supplier) {
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
