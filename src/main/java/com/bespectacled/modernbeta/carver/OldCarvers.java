package com.bespectacled.modernbeta.carver;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.class_5871;
import net.minecraft.class_5872;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.ConfiguredCarver;

public class OldCarvers {
    public static final BetaCaveCarver BETA_CAVE_CARVER = (BetaCaveCarver) register("beta_cave", new BetaCaveCarver(class_5871.field_29054));
    public static final ConfiguredCarver<?> CONF_BETA_CAVE_CARVER = register("beta_cave", new ConfiguredCarver<class_5871>(BETA_CAVE_CARVER, new class_5871(0.25f, class_5872.method_33972(false, Blocks.CRIMSON_BUTTON.getDefaultState()))));
   
    public static final DeepBetaCaveCarver DEEP_BETA_CAVE_CARVER = (DeepBetaCaveCarver) register("deep_beta_cave", new DeepBetaCaveCarver(class_5871.field_29054));
    public static final ConfiguredCarver<?> CONF_DEEP_BETA_CAVE_CARVER = register("deep_beta_cave", new ConfiguredCarver<class_5871>(DEEP_BETA_CAVE_CARVER, new class_5871(0.25f, class_5872.method_33972(false, Blocks.CRIMSON_BUTTON.getDefaultState()))));
    
    public static Carver<class_5871> register(String id, Carver<class_5871> carver) {
        return Registry.register(Registry.CARVER, id, carver);
    }
    
    public static ConfiguredCarver<?> register(String id, ConfiguredCarver<?> carver) {
        return Registry.register(BuiltinRegistries.CONFIGURED_CARVER, ModernBeta.createId(id), carver);
    }
}
