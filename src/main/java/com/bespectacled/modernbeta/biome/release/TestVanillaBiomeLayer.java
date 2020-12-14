package com.bespectacled.modernbeta.biome.release;

import java.util.function.LongFunction;

import com.bespectacled.modernbeta.biome.vanilla.VanillaInitLayer;

import net.minecraft.world.biome.layer.AddBambooJungleLayer;
import net.minecraft.world.biome.layer.AddClimateLayers;
import net.minecraft.world.biome.layer.AddColdClimatesLayer;
import net.minecraft.world.biome.layer.AddEdgeBiomesLayer;
import net.minecraft.world.biome.layer.AddHillsLayer;
import net.minecraft.world.biome.layer.AddIslandLayer;
import net.minecraft.world.biome.layer.AddMushroomIslandLayer;
import net.minecraft.world.biome.layer.AddRiversLayer;
import net.minecraft.world.biome.layer.AddSunflowerPlainsLayer;
import net.minecraft.world.biome.layer.ContinentLayer;
import net.minecraft.world.biome.layer.EaseBiomeEdgeLayer;
import net.minecraft.world.biome.layer.IncreaseEdgeCurvatureLayer;
import net.minecraft.world.biome.layer.NoiseToRiverLayer;
import net.minecraft.world.biome.layer.ScaleLayer;
import net.minecraft.world.biome.layer.SetBaseBiomesLayer;
import net.minecraft.world.biome.layer.SimpleLandNoiseLayer;
import net.minecraft.world.biome.layer.SmoothenShorelineLayer;
import net.minecraft.world.biome.layer.type.ParentedLayer;
import net.minecraft.world.biome.layer.util.CachingLayerContext;
import net.minecraft.world.biome.layer.util.CachingLayerSampler;
import net.minecraft.world.biome.layer.util.LayerFactory;
import net.minecraft.world.biome.layer.util.LayerSampleContext;
import net.minecraft.world.biome.layer.util.LayerSampler;
import net.minecraft.world.biome.source.BiomeLayerSampler;

public class TestVanillaBiomeLayer {
    public static BiomeLayerSampler build(long seed, boolean old, int biomeSize, int riverSize) {
        LayerFactory<CachingLayerSampler> factory = build(
            old, biomeSize, riverSize, salt -> new CachingLayerContext(25, seed, salt)
        );
        
        return new BiomeLayerSampler(factory);
    }
    
    private static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T> stack(
        long seed, 
        ParentedLayer layer, 
        LayerFactory<T> parent, 
        int count, 
        LongFunction<C> contextProvider
    ) {
        LayerFactory<T> factory = parent;
        for (int j = 0; j < count; ++j) {
            factory = layer.<T>create(contextProvider.apply(seed + j), factory);
        }
        return factory;
    }
    
    /*
     * See BiomeLayer for reference
     */
    private static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T> build(boolean old, int biomeSize, int riverSize, LongFunction<C> contextProvider) {
        LayerFactory<T> layerFactory = ContinentLayer.INSTANCE.<T>create((LayerSampleContext<T>)contextProvider.apply(1L));
        
        layerFactory = ScaleLayer.FUZZY.<T>create(contextProvider.apply(2000L), layerFactory);
        layerFactory = IncreaseEdgeCurvatureLayer.INSTANCE.<T>create(contextProvider.apply(1L), layerFactory);
        layerFactory = ScaleLayer.NORMAL.<T>create(contextProvider.apply(2001L), layerFactory);
        layerFactory = IncreaseEdgeCurvatureLayer.INSTANCE.<T>create(contextProvider.apply(2L), layerFactory);
       
        //layerFactory = AddIslandLayer.INSTANCE.<T>create(contextProvider.apply(2L), layerFactory);
        layerFactory = AddColdClimatesLayer.INSTANCE.<T>create(contextProvider.apply(2L), layerFactory);
        layerFactory = ScaleLayer.NORMAL.<T>create(contextProvider.apply(2002L), layerFactory);
        layerFactory = IncreaseEdgeCurvatureLayer.INSTANCE.<T>create(contextProvider.apply(3L), layerFactory);
        layerFactory = ScaleLayer.NORMAL.<T>create(contextProvider.apply(2003L), layerFactory);
        layerFactory = IncreaseEdgeCurvatureLayer.INSTANCE.<T>create(contextProvider.apply(4L), layerFactory);
        layerFactory = AddMushroomIslandLayer.INSTANCE.<T>create(contextProvider.apply(5L), layerFactory);

        LayerFactory<T> layerFactory2 = layerFactory;
        layerFactory2 = stack(1000L, ScaleLayer.NORMAL, layerFactory2, 0, contextProvider);
        layerFactory2 = SimpleLandNoiseLayer.INSTANCE.<T>create(contextProvider.apply(100L), layerFactory2);
        layerFactory2 = stack(1000L, ScaleLayer.NORMAL, layerFactory2, 6, contextProvider);
        layerFactory2 = NoiseToRiverLayer.INSTANCE.<T>create(contextProvider.apply(1L), layerFactory2);
        layerFactory2 = SmoothenShorelineLayer.INSTANCE.<T>create(contextProvider.apply(1000L), layerFactory2);
        
        LayerFactory<T> layerFactory3 = layerFactory2;
        layerFactory3 = stack(1000L, ScaleLayer.NORMAL, layerFactory3, 0, contextProvider);
        layerFactory3 = new SetBaseBiomesLayer(true).<T>create(contextProvider.apply(200L), layerFactory3);
        //layerFactory3 = stack(1000L, ScaleLayer.NORMAL, layerFactory3, 2, contextProvider);
        
        LayerFactory<T> layerFactory4 = layerFactory2;
        layerFactory4 = stack(1000L, ScaleLayer.NORMAL, layerFactory4, 2, contextProvider);
        layerFactory3 = AddHillsLayer.INSTANCE.<T>create(contextProvider.apply(1000L), layerFactory3, layerFactory4);
        
        for (int k = 0; k < biomeSize; ++k) {
            layerFactory3 = ScaleLayer.NORMAL.<T>create(contextProvider.apply(1000 + k), layerFactory3);
            
            if (k == 0) {
                layerFactory3 = IncreaseEdgeCurvatureLayer.INSTANCE.<T>create(contextProvider.apply(3L), layerFactory);
            }
            if (k == 1 || biomeSize == 1) {
                layerFactory3 = AddEdgeBiomesLayer.INSTANCE.<T>create(contextProvider.apply(1000L), layerFactory3);
            }
        }

        layerFactory3 = SmoothenShorelineLayer.INSTANCE.<T>create(contextProvider.apply(1000L), layerFactory3);
        layerFactory3 = AddRiversLayer.INSTANCE.<T>create(contextProvider.apply(100L), layerFactory3, layerFactory2);
        
        return layerFactory3;
    }
}
