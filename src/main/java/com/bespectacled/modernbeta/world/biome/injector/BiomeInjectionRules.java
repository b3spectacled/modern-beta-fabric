package com.bespectacled.modernbeta.world.biome.injector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;

public class BiomeInjectionRules {
    private final List<BiomeInjectionRule> rules;
    
    private BiomeInjectionRules(List<BiomeInjectionRule> rules) {
        this.rules = rules;
    }
    
    public Biome test(BiomeInjectionContext context, int biomeX, int biomeY, int biomeZ) {
        for (BiomeInjectionRule rule : this.rules) {
            Biome biome = rule.test(context).apply(biomeX, biomeY, biomeZ);
            
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
        
        public Builder add(Predicate<BiomeInjectionContext> rule, BiomeInjectionResolver resolver) {
            this.rules.add(new BiomeInjectionRule(rule, resolver));
            
            return this;
        }
        
        public BiomeInjectionRules build() {
            return new BiomeInjectionRules(this.rules);
        }
    }
    
    private static class BiomeInjectionRule {
        private final Predicate<BiomeInjectionContext> rule;
        private final BiomeInjectionResolver biomeResolver;
        
        public BiomeInjectionRule(Predicate<BiomeInjectionContext> rule, BiomeInjectionResolver biomeFunc) {
            this.rule = rule;
            this.biomeResolver = biomeFunc;
        }
        
        public BiomeInjectionResolver test(BiomeInjectionContext context) {
            if (this.rule.test(context))
                return this.biomeResolver;
            
            return BiomeInjectionResolver.DEFAULT;
        }
    }
    
    
    public static class BiomeInjectionContext {
        protected final int topHeight;
        protected final int minHeight;
        protected final BlockState blockState;
        
        private int y;
        
        public BiomeInjectionContext(int topHeight, int minHeight, BlockState blockState) {
            this.topHeight = topHeight;
            this.minHeight = minHeight;
            this.blockState = blockState;
            this.y = topHeight;
        }
        
        public BiomeInjectionContext setY(int y) {
            this.y = y;
            
            return this;
        }
        
        public int getY() {
            return this.y;
        }
    }
}
