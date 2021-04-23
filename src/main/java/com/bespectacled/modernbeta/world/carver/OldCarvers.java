package com.bespectacled.modernbeta.world.carver;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverDebugConfig;
import net.minecraft.world.gen.carver.CaveCarverConfig;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.heightprovider.BiasedToBottomHeightProvider;

public class OldCarvers {
    public static final Carver<CaveCarverConfig> OLD_BETA_CAVE_CARVER = register("old_beta_cave", new OldBetaCaveCarver(CaveCarverConfig.CAVE_CODEC));

    public static final ConfiguredCarver<?> CONF_OLD_BETA_CAVE_CARVER = register("old_beta_cave", new ConfiguredCarver<CaveCarverConfig>(OLD_BETA_CAVE_CARVER, new CaveCarverConfig(0.14285715f, BiasedToBottomHeightProvider.create(YOffset.fixed(0), YOffset.fixed(127), 8), ConstantFloatProvider.create(0.5f), YOffset.aboveBottom(11), false, CarverDebugConfig.create(false, Blocks.CRIMSON_BUTTON.getDefaultState()), ConstantFloatProvider.create(1.0f), ConstantFloatProvider.create(1.0f), ConstantFloatProvider.create(-0.69999999999999996f))));
    public static final ConfiguredCarver<?> CONF_DEEP_BETA_CAVE_CARVER = register("deep_beta_cave", new ConfiguredCarver<CaveCarverConfig>(OLD_BETA_CAVE_CARVER, new CaveCarverConfig(0.14285715f, BiasedToBottomHeightProvider.create(YOffset.aboveBottom(8), YOffset.fixed(-1), 8), ConstantFloatProvider.create(0.5f), YOffset.aboveBottom(9), false, CarverDebugConfig.create(false, Blocks.CRIMSON_BUTTON.getDefaultState()), ConstantFloatProvider.create(1.0f), ConstantFloatProvider.create(1.0f), ConstantFloatProvider.create(-0.69999999999999996f))));
    
    public static Carver<CaveCarverConfig> register(String id, Carver<CaveCarverConfig> carver) {
        return Registry.register(Registry.CARVER, ModernBeta.createId(id), carver);
    }
    
    public static ConfiguredCarver<?> register(String id, ConfiguredCarver<?> carver) {
        return Registry.register(BuiltinRegistries.CONFIGURED_CARVER, ModernBeta.createId(id), carver);
    }
}
