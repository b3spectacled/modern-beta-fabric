package com.bespectacled.modernbeta.world.biome.vanilla;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.mixin.MixinVanillaBiomeParametersAccessor;

import com.mojang.datafixers.util.Pair;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.VanillaBiomeParameters;

public class ExtendedVanillaBiomeParameters extends VanillaBiomeParameters {
    public void writeDeepOceanBiomes(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters) {
        MixinVanillaBiomeParametersAccessor accessor = ((MixinVanillaBiomeParametersAccessor)this);
        
        MultiNoiseUtil.ParameterRange[] temperatureParams = accessor.getTEMPERATURE_PARAMETERS();
        MultiNoiseUtil.ParameterRange defaultParam = accessor.getDEFAULT_PARAMETER();
        RegistryKey<Biome>[] deepOceanBiomes = accessor.getOCEAN_BIOMES()[0];
        
        for (int i = 0; i < temperatureParams.length; ++i) {
            MultiNoiseUtil.ParameterRange temperatureRange = temperatureParams[i];
            accessor.invokeWriteBiomeParameters(parameters, temperatureRange, defaultParam, defaultParam, defaultParam, defaultParam, 0.0f, deepOceanBiomes[i]);
        }
    }
    
    /*
     * Remove mushroom islands (for now) and deep oceans from biome pool
     */
    @Override
    protected void writeOceanBiomes(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters) {
        MixinVanillaBiomeParametersAccessor accessor = ((MixinVanillaBiomeParametersAccessor)this);
        
        MultiNoiseUtil.ParameterRange[] temperatureParams = accessor.getTEMPERATURE_PARAMETERS();
        MultiNoiseUtil.ParameterRange defaultParam = accessor.getDEFAULT_PARAMETER();
        RegistryKey<Biome>[] oceanBiomes = accessor.getOCEAN_BIOMES()[1];
        
        for (int i = 0; i < temperatureParams.length; ++i) {
            MultiNoiseUtil.ParameterRange temperatureRange = temperatureParams[i];
            accessor.invokeWriteBiomeParameters(parameters, temperatureRange, defaultParam, defaultParam, defaultParam, defaultParam, 0.0f, oceanBiomes[i]);
        }
    }
    
    /*
     * Replace beaches with plains equivalents.
     */
    @Override
    protected RegistryKey<Biome> getShoreBiome(int temperature, int humidity) {
        if (temperature == 0) {
            return BiomeKeys.SNOWY_PLAINS;
        }
        
        if (temperature == 4 && humidity < 3) {
            return BiomeKeys.DESERT;
        }
        
        return BiomeKeys.PLAINS;
    }
}
