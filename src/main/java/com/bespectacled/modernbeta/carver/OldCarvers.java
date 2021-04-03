package com.bespectacled.modernbeta.carver;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.class_6108;
import net.minecraft.class_6120;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverDebugConfig;
import net.minecraft.world.gen.carver.ConfiguredCarver;

public class OldCarvers {
    public static final Carver<class_6108> OLD_BETA_CAVE_CARVER = register("old_beta_cave", new OldBetaCaveCarver(class_6108.field_31491));
    public static final Carver<class_6108> DEEP_BETA_CAVE_CARVER = register("deep_beta_cave", new DeepBetaCaveCarver(class_6108.field_31491));
    
    public static final ConfiguredCarver<?> CONF_OLD_BETA_CAVE_CARVER = register("old_beta_cave", new ConfiguredCarver<class_6108>(OLD_BETA_CAVE_CARVER, new class_6108(0.14285715f, class_6120.method_35377(YOffset.fixed(0), YOffset.fixed(127), 8), ConstantFloatProvider.create(0.5f), YOffset.aboveBottom(11), CarverDebugConfig.create(false, Blocks.CRIMSON_BUTTON.getDefaultState()), ConstantFloatProvider.create(1.0f), ConstantFloatProvider.create(1.0f), ConstantFloatProvider.create(-0.7f))));
    public static final ConfiguredCarver<?> CONF_DEEP_BETA_CAVE_CARVER = register("deep_beta_cave", new ConfiguredCarver<class_6108>(DEEP_BETA_CAVE_CARVER, new class_6108(0.14285715f, class_6120.method_35377(YOffset.fixed(0), YOffset.fixed(127), 8), ConstantFloatProvider.create(0.5f), YOffset.aboveBottom(11), CarverDebugConfig.create(false, Blocks.CRIMSON_BUTTON.getDefaultState()), ConstantFloatProvider.create(1.0f), ConstantFloatProvider.create(1.0f), ConstantFloatProvider.create(-0.7f))));
    
    public static Carver<class_6108> register(String id, Carver<class_6108> carver) {
        return Registry.register(Registry.CARVER, ModernBeta.createId(id), carver);
    }
    
    public static ConfiguredCarver<?> register(String id, ConfiguredCarver<?> carver) {
        return Registry.register(BuiltinRegistries.CONFIGURED_CARVER, ModernBeta.createId(id), carver);
    }
}
