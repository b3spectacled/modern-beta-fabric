package com.bespectacled.modernbeta.carver;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.block.Blocks;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverConfig;
import net.minecraft.world.gen.carver.CarverDebugConfig;
import net.minecraft.world.gen.carver.ConfiguredCarver;

public class OldCarvers {
    public static final Carver<CarverConfig> OLD_BETA_CAVE_CARVER = register("old_beta_cave", new OldBetaCaveCarver(CarverConfig.CONFIG_CODEC));
    public static final Carver<CarverConfig> DEEP_BETA_CAVE_CARVER = register("deep_beta_cave", new DeepBetaCaveCarver(CarverConfig.CONFIG_CODEC));
    
    public static final ConfiguredCarver<?> CONF_OLD_BETA_CAVE_CARVER = register("old_beta_cave", new ConfiguredCarver<CarverConfig>(OLD_BETA_CAVE_CARVER, new CarverConfig(0.25f, CarverDebugConfig.create(false, Blocks.CRIMSON_BUTTON.getDefaultState())))); 
    public static final ConfiguredCarver<?> CONF_DEEP_BETA_CAVE_CARVER = register("deep_beta_cave", new ConfiguredCarver<CarverConfig>(DEEP_BETA_CAVE_CARVER, new CarverConfig(0.25f, CarverDebugConfig.create(false, Blocks.CRIMSON_BUTTON.getDefaultState()))));
    
    public static Carver<CarverConfig> register(String id, Carver<CarverConfig> carver) {
        return Registry.register(Registry.CARVER, ModernBeta.createId(id), carver);
    }
    
    public static ConfiguredCarver<?> register(String id, ConfiguredCarver<?> carver) {
        return Registry.register(BuiltinRegistries.CONFIGURED_CARVER, ModernBeta.createId(id), carver);
    }
}
