package com.bespectacled.modernbeta.feature;

import com.bespectacled.modernbeta.ModernBeta;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class OldFeatures {
    
    public static final BetaFreezeTopLayerFeature BETA_FREEZE_TOP_LAYER = (BetaFreezeTopLayerFeature) register("beta_freeze_top_layer", new BetaFreezeTopLayerFeature(DefaultFeatureConfig.CODEC));
    public static final OldFancyOakFeature OLD_FANCY_OAK = (OldFancyOakFeature) register("oak_fancy_oak", new OldFancyOakFeature(DefaultFeatureConfig.CODEC));
    public static final IndevHouseFeature INDEV_HOUSE = (IndevHouseFeature) register("indev_house", new IndevHouseFeature(DefaultFeatureConfig.CODEC));
    
    public static Feature<?> register(String id, Feature<?> feature) {
        return Registry.register(Registry.FEATURE, ModernBeta.createId(id), feature);
    }
}
