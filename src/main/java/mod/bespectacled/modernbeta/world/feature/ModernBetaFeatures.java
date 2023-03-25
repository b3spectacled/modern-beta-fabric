package mod.bespectacled.modernbeta.world.feature;

import mod.bespectacled.modernbeta.ModernBeta;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.LakeFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

@SuppressWarnings("deprecation")
public class ModernBetaFeatures {
    public static final BetaFreezeTopLayerFeature FREEZE_TOP_LAYER = (BetaFreezeTopLayerFeature) register(
        ModernBetaFeatureTags.FREEZE_TOP_LAYER, new BetaFreezeTopLayerFeature(DefaultFeatureConfig.CODEC)
    );

    public static final BetaFancyOakFeature OLD_FANCY_OAK = (BetaFancyOakFeature) register(
        ModernBetaFeatureTags.FANCY_OAK, new BetaFancyOakFeature(DefaultFeatureConfig.CODEC)
    );
    
    public static final BetaOreClayFeature ORE_CLAY = (BetaOreClayFeature) register(
        ModernBetaFeatureTags.ORE_CLAY, new BetaOreClayFeature(OreFeatureConfig.CODEC)
    );
    
    public static final Feature<LakeFeature.Config> LAKE_WATER = Feature.LAKE;
    
    private static Feature<?> register(String id, Feature<?> feature) {
        return Registry.register(Registries.FEATURE, ModernBeta.createId(id), feature);
    }
    
    public static void register() {}
}