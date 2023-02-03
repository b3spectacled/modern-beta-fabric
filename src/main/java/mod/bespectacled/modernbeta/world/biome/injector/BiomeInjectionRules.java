package mod.bespectacled.modernbeta.world.biome.injector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.block.BlockState;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class BiomeInjectionRules {
    private final List<BiomeInjectionRule> rules;
    
    private BiomeInjectionRules(List<BiomeInjectionRule> rules) {
        this.rules = rules;
    }
    
    public RegistryEntry<Biome> test(BiomeInjectionContext context, int biomeX, int biomeY, int biomeZ) {
        for (BiomeInjectionRule rule : this.rules) {
            RegistryEntry<Biome> biome = rule.test(context).apply(biomeX, biomeY, biomeZ);
            
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
        private final BiomeInjectionResolver resolver;
        
        public BiomeInjectionRule(Predicate<BiomeInjectionContext> rule, BiomeInjectionResolver resolver) {
            this.rule = rule;
            this.resolver = resolver;
        }
        
        public BiomeInjectionResolver test(BiomeInjectionContext context) {
            if (this.rule.test(context))
                return this.resolver;
            
            return BiomeInjectionResolver.DEFAULT;
        }
    }
    
    public static class BiomeInjectionContext {
        protected final int worldMinY;
        protected final int topHeight;
        protected final int minHeight;
        protected final BlockState topState;
        protected final BlockState minState;
        
        private int y;
        
        public BiomeInjectionContext(int worldMinY, int topHeight, int minHeight, BlockState topState, BlockState minState) {
            this.worldMinY = worldMinY;
            this.topHeight = topHeight;
            this.minHeight = minHeight;
            this.topState = topState;
            this.minState = minState;
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
