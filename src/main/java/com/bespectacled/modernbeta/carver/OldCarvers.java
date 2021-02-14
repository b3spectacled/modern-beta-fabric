package com.bespectacled.modernbeta.carver;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.ConfiguredCarver;

public class OldCarvers {
    public static final BetaCaveCarver BETA_CAVE_CARVER = (BetaCaveCarver) register("beta_cave", new BetaCaveCarver(ProbabilityConfig.CODEC, 128));
    public static final ConfiguredCarver<?> CONF_BETA_CAVE_CARVER = register("beta_cave", new ConfiguredCarver<ProbabilityConfig>(BETA_CAVE_CARVER, new ProbabilityConfig(1f)));
   
    public static final DeepBetaCaveCarver DEEP_BETA_CAVE_CARVER = (DeepBetaCaveCarver) register("deep_beta_cave", new DeepBetaCaveCarver(ProbabilityConfig.CODEC, 0));
    public static final ConfiguredCarver<?> CONF_DEEP_BETA_CAVE_CARVER = register("deep_beta_cave", new ConfiguredCarver<ProbabilityConfig>(DEEP_BETA_CAVE_CARVER, new ProbabilityConfig(1f)));
    
    public static Carver<ProbabilityConfig> register(String id, Carver<ProbabilityConfig> carver) {
        return Registry.register(Registry.CARVER, id, carver);
    }
    
    public static ConfiguredCarver<?> register(String id, ConfiguredCarver<?> carver) {
        return Registry.register(BuiltinRegistries.CONFIGURED_CARVER, ModernBeta.createId(id), carver);
    }
}
