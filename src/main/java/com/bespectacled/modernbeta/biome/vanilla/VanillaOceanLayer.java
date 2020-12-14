package com.bespectacled.modernbeta.biome.vanilla;

import java.util.function.LongFunction;

import net.minecraft.world.biome.layer.OceanTemperatureLayer;
import net.minecraft.world.biome.layer.ScaleLayer;
import net.minecraft.world.biome.layer.type.ParentedLayer;
import net.minecraft.world.biome.layer.util.CachingLayerContext;
import net.minecraft.world.biome.layer.util.CachingLayerSampler;
import net.minecraft.world.biome.layer.util.LayerFactory;
import net.minecraft.world.biome.layer.util.LayerSampleContext;
import net.minecraft.world.biome.layer.util.LayerSampler;
import net.minecraft.world.biome.source.BiomeLayerSampler;

/**
 * @author PaulEvs
 */
public class VanillaOceanLayer {
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
        LayerFactory<T> oceanFactory = OceanTemperatureLayer.INSTANCE.create((LayerSampleContext) contextProvider.apply(2L));
        
        oceanFactory = stack(2001L, ScaleLayer.NORMAL, oceanFactory, biomeSize, contextProvider);
    
        return oceanFactory;
    }
}
