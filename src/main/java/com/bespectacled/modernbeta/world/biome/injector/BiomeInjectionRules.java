package com.bespectacled.modernbeta.world.biome.injector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.bespectacled.modernbeta.util.function.TriPredicate;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;

public class BiomeInjectionRules {
    private final List<BiomeInjectionRule> rules;
    
    private BiomeInjectionRules(List<BiomeInjectionRule> rules) {
        this.rules = rules;
    }
    
    public Biome test(int y, int topHeight, int minHeight, BlockState blockState, int biomeX, int biomeY, int biomeZ) {
        for (BiomeInjectionRule rule : this.rules) {
            Biome biome = rule.test(y, topHeight, minHeight, blockState).apply(biomeX, biomeY, biomeZ);
            
            if (biome != null)
                return biome;
        }
        
        return null;
    }

    public static class Builder {
        private final List<BiomeInjectionRule> rules;
        
        public Builder() {
            this.rules = new ArrayList<>();
        }
        
        public Builder add(
            TriPredicate<Integer, Integer, Integer> heightRule,
            Predicate<BlockState> stateRule,
            BiomeInjectionResolver biomeFunc
        ) {
            this.rules.add(new BiomeInjectionRule(heightRule, stateRule, biomeFunc));
            
            return this;
        }
        
        public BiomeInjectionRules build() {
            return new BiomeInjectionRules(this.rules);
        }
    }
    
    private static class BiomeInjectionRule {
        private final TriPredicate<Integer, Integer, Integer> heightRule;
        private final Predicate<BlockState> stateRule;
        private final BiomeInjectionResolver biomeResolver;
        
        public BiomeInjectionRule(
            TriPredicate<Integer, Integer, Integer> heightRule,
            Predicate<BlockState> stateRule, 
            BiomeInjectionResolver biomeFunc
        ) {
            this.heightRule = heightRule;
            this.stateRule = stateRule;
            this.biomeResolver = biomeFunc;
        }
        
        public BiomeInjectionResolver test(int y, int topHeight, int minHeight, BlockState blockState) {
            if (this.heightRule.test(y, topHeight, minHeight) && this.stateRule.test(blockState))
                return this.biomeResolver;
            
            return BiomeInjectionResolver.DEFAULT;
        }
    }
}
