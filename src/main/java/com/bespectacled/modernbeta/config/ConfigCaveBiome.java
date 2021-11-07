package com.bespectacled.modernbeta.config;

import com.bespectacled.modernbeta.api.registry.BuiltInTypes;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.minecraft.world.biome.BiomeKeys;

@Config(name = "cave_biome_config")
public class ConfigCaveBiome implements ConfigData {
    
    public GeneralBiomeConfig generalBiomeConfig = new GeneralBiomeConfig();
    public SingleBiomeConfig singleBiomeConfig = new SingleBiomeConfig();
    public NoiseBiomeConfig noiseBiomeConfig = new NoiseBiomeConfig();
    
    public static class GeneralBiomeConfig {
        public String caveBiomeType = BuiltInTypes.CaveBiome.SINGLE.name;
    }
    
    public static class SingleBiomeConfig {
        public String singleBiome = BiomeKeys.LUSH_CAVES.getValue().toString();
        public boolean useNoise = true;
        public int horizontalNoiseScale = 32;
        public int verticalNoiseScale = 16;
    }
    
    public static class NoiseBiomeConfig {
        public int horizontalNoiseScale = 32;
        public int verticalNoiseScale = 16;
    }
}
