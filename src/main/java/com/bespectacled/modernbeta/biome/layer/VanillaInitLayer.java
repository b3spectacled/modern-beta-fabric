package com.bespectacled.modernbeta.biome.layer;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.biome.layer.type.InitLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;

/**
 * @author Paulevs 
 */
public enum VanillaInitLayer implements InitLayer {
    INSTANCE;
    
    @Override
    public int sample(LayerRandomnessSource context, int x, int y) {
        return BuiltinRegistries.BIOME.getRawId(BuiltinRegistries.BIOME.get(new Identifier("beach")));
    }
}