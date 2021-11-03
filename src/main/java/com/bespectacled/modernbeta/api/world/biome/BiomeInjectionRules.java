package com.bespectacled.modernbeta.api.world.biome;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import com.bespectacled.modernbeta.util.function.TriFunction;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;

public class BiomeInjectionRules {
    private final List<BiomeInjectionRule> rules;
    
    private BiomeInjectionRules(List<BiomeInjectionRule> rules) {
        this.rules = rules;
    }
    
    public Biome apply(int y, int height, BlockState blockState, int biomeX, int biomeY, int biomeZ) {
        for (BiomeInjectionRule rule : this.rules) {
            Biome biome = rule.apply(y, height, blockState, biomeX, biomeY, biomeZ);
            
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
            BiPredicate<Integer, Integer> heightRule,
            Predicate<BlockState> stateRule,
            TriFunction<Integer, Integer, Integer, Biome> biomeFunc
        ) {
            this.rules.add(new BiomeInjectionRule(heightRule, stateRule, biomeFunc));
            
            return this;
        }
        
        public BiomeInjectionRules build() {
            return new BiomeInjectionRules(this.rules);
        }
    }
    
    private static class BiomeInjectionRule {
        private final BiPredicate<Integer, Integer> heightRule;
        private final Predicate<BlockState> stateRule;
        private final TriFunction<Integer, Integer, Integer, Biome> biomeFunc;
        
        public BiomeInjectionRule(
            BiPredicate<Integer, Integer> heightRule,
            Predicate<BlockState> stateRule, 
            TriFunction<Integer, Integer, Integer, Biome> biomeFunc
        ) {
            this.heightRule = heightRule;
            this.stateRule = stateRule;
            this.biomeFunc = biomeFunc;
        }
        
        public Biome apply(int y, int height, BlockState blockState, int biomeX, int biomeY, int biomeZ) {
            if (this.heightRule.test(y, height) && this.stateRule.test(blockState))
                return this.biomeFunc.apply(biomeX, biomeY, biomeZ);
            
            return null;
        }
    }
}
