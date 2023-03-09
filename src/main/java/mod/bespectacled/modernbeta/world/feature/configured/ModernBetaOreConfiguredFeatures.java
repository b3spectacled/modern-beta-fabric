package mod.bespectacled.modernbeta.world.feature.configured;

import java.util.List;

import mod.bespectacled.modernbeta.world.feature.ModernBetaFeatureTags;
import mod.bespectacled.modernbeta.world.feature.ModernBetaFeatures;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class ModernBetaOreConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_CLAY = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.ORE_CLAY);
    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_EMERALD_Y95 = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.ORE_EMERALD_Y95);
    
    @SuppressWarnings("unchecked")
    public static void bootstrap(Registerable<?> registerable) {
        Registerable<ConfiguredFeature<?, ?>> featureRegisterable = (Registerable<ConfiguredFeature<?, ?>>)registerable;
        
        TagMatchRuleTest ruleStone = new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES);
        TagMatchRuleTest ruleDeepslate = new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        
        List<OreFeatureConfig.Target> targets = List.of(
            OreFeatureConfig.createTarget(ruleStone, Blocks.EMERALD_ORE.getDefaultState()),
            OreFeatureConfig.createTarget(ruleDeepslate, Blocks.DEEPSLATE_EMERALD_ORE.getDefaultState())
        );
        
        ConfiguredFeatures.register(featureRegisterable, ORE_CLAY, ModernBetaFeatures.ORE_CLAY, new OreFeatureConfig(new BlockMatchRuleTest(Blocks.SAND), Blocks.CLAY.getDefaultState(), 33));
        ConfiguredFeatures.register(featureRegisterable, ORE_EMERALD_Y95, Feature.ORE, new OreFeatureConfig(targets, 8, 0.9f));
    }
}
