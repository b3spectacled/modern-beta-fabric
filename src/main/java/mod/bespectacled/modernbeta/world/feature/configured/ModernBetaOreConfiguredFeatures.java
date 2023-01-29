package mod.bespectacled.modernbeta.world.feature.configured;

import java.util.List;

import mod.bespectacled.modernbeta.world.feature.ModernBetaFeatures;
import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreConfiguredFeatures;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class ModernBetaOreConfiguredFeatures {
    public static final List<OreFeatureConfig.Target> EMERALD_ORES = List.of(
        OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, Blocks.EMERALD_ORE.getDefaultState()), 
        OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, Blocks.DEEPSLATE_EMERALD_ORE.getDefaultState())
    );
    
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> ORE_CLAY = ModernBetaConfiguredFeatures.register(
        "ore_clay",
        ModernBetaFeatures.ORE_CLAY,
        new OreFeatureConfig(new BlockMatchRuleTest(Blocks.SAND), Blocks.CLAY.getDefaultState(), 33)
    );
    
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> ORE_EMERALD_Y95 = ModernBetaConfiguredFeatures.register(
        "ore_emerald_y95",
        Feature.ORE,
        new OreFeatureConfig(EMERALD_ORES, 8)
    );
}
