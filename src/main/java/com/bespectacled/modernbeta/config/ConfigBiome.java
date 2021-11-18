package com.bespectacled.modernbeta.config;

import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.world.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.world.biome.inf.InfBiomes;
import com.bespectacled.modernbeta.world.biome.pe.PEBiomes;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "biome_config")
public class ConfigBiome implements ConfigData {
    
    public GeneralBiomeConfig generalBiomeConfig = new GeneralBiomeConfig();
    public BetaBiomeConfig betaBiomeConfig = new BetaBiomeConfig();
    public PEBiomeConfig peBiomeConfig = new PEBiomeConfig();
    public SingleBiomeConfig singleBiomeConfig = new SingleBiomeConfig();
    public VanillaBiomeConfig vanillaBiomeConfig = new VanillaBiomeConfig();
    
    public static class GeneralBiomeConfig {
        public String biomeType = BuiltInTypes.Biome.BETA.name;
    }
    
    public static class BetaBiomeConfig {
        public String betaDesertBiome = BetaBiomes.DESERT_ID.toString();
        public String betaForestBiome = BetaBiomes.FOREST_ID.toString();
        public String betaIceDesertBiome = BetaBiomes.TUNDRA_ID.toString();
        public String betaPlainsBiome = BetaBiomes.PLAINS_ID.toString();
        public String betaRainforestBiome = BetaBiomes.RAINFOREST_ID.toString();
        public String betaSavannaBiome = BetaBiomes.SAVANNA_ID.toString();
        public String betaShrublandBiome = BetaBiomes.SHRUBLAND_ID.toString();
        public String betaSeasonalForestBiome = BetaBiomes.SEASONAL_FOREST_ID.toString();
        public String betaSwamplandBiome = BetaBiomes.SWAMPLAND_ID.toString();
        public String betaTaigaBiome = BetaBiomes.TAIGA_ID.toString();
        public String betaTundraBiome = BetaBiomes.TUNDRA_ID.toString();
        public String betaOceanBiome = BetaBiomes.OCEAN_ID.toString();
        public String betaColdOceanBiome = BetaBiomes.COLD_OCEAN_ID.toString();
        public String betaFrozenOceanBiome = BetaBiomes.FROZEN_OCEAN_ID.toString();
        public String betaLukewarmOceanBiome = BetaBiomes.LUKEWARM_OCEAN_ID.toString();
        public String betaWarmOceanBiome = BetaBiomes.WARM_OCEAN_ID.toString();
    }
    
    public static class PEBiomeConfig {
        public String peDesertBiome = PEBiomes.PE_DESERT_ID.toString();
        public String peForestBiome = PEBiomes.PE_FOREST_ID.toString();
        public String peIceDesertBiome = PEBiomes.PE_TUNDRA_ID.toString();
        public String pePlainsBiome = PEBiomes.PE_PLAINS_ID.toString();
        public String peRainforestBiome = PEBiomes.PE_RAINFOREST_ID.toString();
        public String peSavannaBiome = PEBiomes.PE_SAVANNA_ID.toString();
        public String peShrublandBiome = PEBiomes.PE_SHRUBLAND_ID.toString();
        public String peSeasonalForestBiome = PEBiomes.PE_SEASONAL_FOREST_ID.toString();
        public String peSwamplandBiome = PEBiomes.PE_SWAMPLAND_ID.toString();
        public String peTaigaBiome = PEBiomes.PE_TAIGA_ID.toString();
        public String peTundraBiome = PEBiomes.PE_TUNDRA_ID.toString();
        public String peOceanBiome = PEBiomes.PE_OCEAN_ID.toString();
        public String peColdOceanBiome = PEBiomes.PE_COLD_OCEAN_ID.toString();
        public String peFrozenOceanBiome = PEBiomes.PE_FROZEN_OCEAN_ID.toString();
        public String peLukewarmOceanBiome = PEBiomes.PE_LUKEWARM_OCEAN_ID.toString();
        public String peWarmOceanBiome = PEBiomes.PE_WARM_OCEAN_ID.toString();
    }
    
    public static class SingleBiomeConfig {
        public String singleBiome = InfBiomes.ALPHA_ID.toString();
    }
    
    public static class VanillaBiomeConfig {
        public boolean largeBiomes = false;
    }
}
