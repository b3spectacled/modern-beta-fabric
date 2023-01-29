package mod.bespectacled.modernbeta.world.feature;

import mod.bespectacled.modernbeta.ModernBeta;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class ModernBetaFeatures {
    public static final BetaFreezeTopLayerFeature FREEZE_TOP_LAYER = (BetaFreezeTopLayerFeature) register(
        "freeze_top_layer", new BetaFreezeTopLayerFeature(DefaultFeatureConfig.CODEC)
    );
    
    public static final BetaFancyOakFeature OLD_FANCY_OAK = (BetaFancyOakFeature) register(
        "fancy_oak", new BetaFancyOakFeature(DefaultFeatureConfig.CODEC)
    );
    
    public static final BetaOreClayFeature ORE_CLAY = (BetaOreClayFeature) register(
        "ore_clay", new BetaOreClayFeature(OreFeatureConfig.CODEC)
    );
    
    private static Feature<?> register(String id, Feature<?> feature) {
        return Registry.register(Registry.FEATURE, ModernBeta.createId(id), feature);
    }
}