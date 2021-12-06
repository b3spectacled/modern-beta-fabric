package com.bespectacled.modernbeta.config;

import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.api.world.biome.BiomeClimatePoint;
import com.bespectacled.modernbeta.world.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.world.biome.inf.InfBiomes;
import com.bespectacled.modernbeta.world.biome.pe.PEBiomes;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "config_biome")
public class ModernBetaConfigBiome implements ConfigData {

    // General
    public String biomeType = BuiltInTypes.Biome.BETA.name;
    
    // Oceans
    public boolean generateOceans = true;

    // Single
    public String singleBiome = InfBiomes.ALPHA_ID.toString();
    
    // Vanilla
    public boolean vanillaLargeBiomes = false;
    
    // Beta
    public BiomeClimatePoint betaDesertBiome = new BiomeClimatePoint(BetaBiomes.DESERT_ID.toString(), BetaBiomes.OCEAN_ID.toString());
    public BiomeClimatePoint betaForestBiome = new BiomeClimatePoint(BetaBiomes.FOREST_ID.toString(), BetaBiomes.OCEAN_ID.toString());
    public BiomeClimatePoint betaIceDesertBiome = new BiomeClimatePoint(BetaBiomes.TUNDRA_ID.toString(), BetaBiomes.FROZEN_OCEAN_ID.toString());
    public BiomeClimatePoint betaPlainsBiome = new BiomeClimatePoint(BetaBiomes.PLAINS_ID.toString(), BetaBiomes.OCEAN_ID.toString());
    public BiomeClimatePoint betaRainforestBiome = new BiomeClimatePoint(BetaBiomes.RAINFOREST_ID.toString(), BetaBiomes.WARM_OCEAN_ID.toString());
    public BiomeClimatePoint betaSavannaBiome = new BiomeClimatePoint(BetaBiomes.SAVANNA_ID.toString(), BetaBiomes.OCEAN_ID.toString());
    public BiomeClimatePoint betaShrublandBiome = new BiomeClimatePoint(BetaBiomes.SHRUBLAND_ID.toString(), BetaBiomes.OCEAN_ID.toString());
    public BiomeClimatePoint betaSeasonalForestBiome = new BiomeClimatePoint(BetaBiomes.SEASONAL_FOREST_ID.toString(), BetaBiomes.LUKEWARM_OCEAN_ID.toString());
    public BiomeClimatePoint betaSwamplandBiome = new BiomeClimatePoint(BetaBiomes.SWAMPLAND_ID.toString(), BetaBiomes.COLD_OCEAN_ID.toString());
    public BiomeClimatePoint betaTaigaBiome = new BiomeClimatePoint(BetaBiomes.TAIGA_ID.toString(), BetaBiomes.FROZEN_OCEAN_ID.toString());
    public BiomeClimatePoint betaTundraBiome = new BiomeClimatePoint(BetaBiomes.TUNDRA_ID.toString(), BetaBiomes.FROZEN_OCEAN_ID.toString());
    
    // PE
    public BiomeClimatePoint peDesertBiome = new BiomeClimatePoint(PEBiomes.PE_DESERT_ID.toString(), PEBiomes.PE_OCEAN_ID.toString());
    public BiomeClimatePoint peForestBiome = new BiomeClimatePoint(PEBiomes.PE_FOREST_ID.toString(), PEBiomes.PE_OCEAN_ID.toString());
    public BiomeClimatePoint peIceDesertBiome = new BiomeClimatePoint(PEBiomes.PE_TUNDRA_ID.toString(), PEBiomes.PE_FROZEN_OCEAN_ID.toString());
    public BiomeClimatePoint pePlainsBiome = new BiomeClimatePoint(PEBiomes.PE_PLAINS_ID.toString(), PEBiomes.PE_OCEAN_ID.toString());
    public BiomeClimatePoint peRainforestBiome = new BiomeClimatePoint(PEBiomes.PE_RAINFOREST_ID.toString(), PEBiomes.PE_WARM_OCEAN_ID.toString());
    public BiomeClimatePoint peSavannaBiome = new BiomeClimatePoint(PEBiomes.PE_SAVANNA_ID.toString(), PEBiomes.PE_OCEAN_ID.toString());
    public BiomeClimatePoint peShrublandBiome = new BiomeClimatePoint(PEBiomes.PE_SHRUBLAND_ID.toString(), PEBiomes.PE_OCEAN_ID.toString());
    public BiomeClimatePoint peSeasonalForestBiome = new BiomeClimatePoint(PEBiomes.PE_SEASONAL_FOREST_ID.toString(), PEBiomes.PE_LUKEWARM_OCEAN_ID.toString());
    public BiomeClimatePoint peSwamplandBiome = new BiomeClimatePoint(PEBiomes.PE_SWAMPLAND_ID.toString(), PEBiomes.PE_COLD_OCEAN_ID.toString());
    public BiomeClimatePoint peTaigaBiome = new BiomeClimatePoint(PEBiomes.PE_TAIGA_ID.toString(), PEBiomes.PE_FROZEN_OCEAN_ID.toString());
    public BiomeClimatePoint peTundraBiome = new BiomeClimatePoint(PEBiomes.PE_TUNDRA_ID.toString(), PEBiomes.PE_FROZEN_OCEAN_ID.toString());
}
