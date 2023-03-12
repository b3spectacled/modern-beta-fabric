package mod.bespectacled.modernbeta.world.biome.provider.climate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import mod.bespectacled.modernbeta.api.world.biome.climate.Clime;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class BiomeClimateRules {
    private final List<BiomeClimateRule> rules;

    private BiomeClimateRules(List<BiomeClimateRule> rules) {
        this.rules = rules;
    }
    
    public Clime apply(RegistryEntry<Biome> biome) {
        for (BiomeClimateRule rule : this.rules) {
            Clime clime = rule.apply(biome);
            
            if (clime != null)
                return clime;
        }
        
        double temp = MathHelper.clamp(biome.value().getTemperature(), 0.0, 1.0);
        double rain = MathHelper.clamp(biome.value().getDownfall(), 0.0, 1.0);
        
        return new Clime(temp, rain);
    }
    
    public static class Builder {
        private final List<BiomeClimateRule> rules;
        
        public Builder() {
            this.rules = new ArrayList<>();
        }
        
        public Builder add(Predicate<RegistryEntry<Biome>> rule, Supplier<Clime> supplier) {
            this.rules.add(new BiomeClimateRule(rule, supplier));
            
            return this;
        }
        
        public BiomeClimateRules build() {
            return new BiomeClimateRules(this.rules);
        }
    }
    
    private static class BiomeClimateRule {
        private final Predicate<RegistryEntry<Biome>> rule;
        private final Supplier<Clime> supplier;
        
        public BiomeClimateRule(Predicate<RegistryEntry<Biome>> rule, Supplier<Clime> supplier) {
            this.rule = rule;
            this.supplier = supplier;
        }
        
        public Clime apply(RegistryEntry<Biome> biome) {
            if (this.rule.test(biome))
                return this.supplier.get();
            
            return null;
        }
    }
}
