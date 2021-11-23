package com.bespectacled.modernbeta.world.carver;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.ConfiguredCarver;

public class OldCarvers {
    public static final Carver<ProbabilityConfig> OLD_BETA_CAVE_CARVER = register("old_beta_cave", new OldCaveCarver(ProbabilityConfig.CODEC, 128));
    public static final ConfiguredCarver<?> CONF_OLD_BETA_CAVE_CARVER = register("old_beta_cave", new ConfiguredCarver<ProbabilityConfig>(OLD_BETA_CAVE_CARVER, new ProbabilityConfig(1f)));
    
    public static Carver<ProbabilityConfig> register(String id, Carver<ProbabilityConfig> carver) {
        return Registry.register(Registry.CARVER, ModernBeta.createId(id), carver);
    }
    
    public static ConfiguredCarver<?> register(String id, ConfiguredCarver<?> carver) {
        return Registry.register(BuiltinRegistries.CONFIGURED_CARVER, ModernBeta.createId(id), carver);
    }
}