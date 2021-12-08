package com.bespectacled.modernbeta.config;

import java.util.List;

import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.world.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.world.biome.inf.InfBiomes;
import com.bespectacled.modernbeta.world.biome.pe.PEBiomes;
import com.bespectacled.modernbeta.world.biome.provider.climate.BetaClimateMapping;
import com.bespectacled.modernbeta.world.biome.provider.climate.VoronoiClimateMapping;

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
    public BetaClimateMapping.Config betaDesertBiome = new BetaClimateMapping.Config(BetaBiomes.DESERT_ID.toString(), BetaBiomes.OCEAN_ID.toString());
    public BetaClimateMapping.Config betaForestBiome = new BetaClimateMapping.Config(BetaBiomes.FOREST_ID.toString(), BetaBiomes.OCEAN_ID.toString());
    public BetaClimateMapping.Config betaIceDesertBiome = new BetaClimateMapping.Config(BetaBiomes.TUNDRA_ID.toString(), BetaBiomes.FROZEN_OCEAN_ID.toString());
    public BetaClimateMapping.Config betaPlainsBiome = new BetaClimateMapping.Config(BetaBiomes.PLAINS_ID.toString(), BetaBiomes.OCEAN_ID.toString());
    public BetaClimateMapping.Config betaRainforestBiome = new BetaClimateMapping.Config(BetaBiomes.RAINFOREST_ID.toString(), BetaBiomes.WARM_OCEAN_ID.toString());
    public BetaClimateMapping.Config betaSavannaBiome = new BetaClimateMapping.Config(BetaBiomes.SAVANNA_ID.toString(), BetaBiomes.OCEAN_ID.toString());
    public BetaClimateMapping.Config betaShrublandBiome = new BetaClimateMapping.Config(BetaBiomes.SHRUBLAND_ID.toString(), BetaBiomes.OCEAN_ID.toString());
    public BetaClimateMapping.Config betaSeasonalForestBiome = new BetaClimateMapping.Config(BetaBiomes.SEASONAL_FOREST_ID.toString(), BetaBiomes.LUKEWARM_OCEAN_ID.toString());
    public BetaClimateMapping.Config betaSwamplandBiome = new BetaClimateMapping.Config(BetaBiomes.SWAMPLAND_ID.toString(), BetaBiomes.COLD_OCEAN_ID.toString());
    public BetaClimateMapping.Config betaTaigaBiome = new BetaClimateMapping.Config(BetaBiomes.TAIGA_ID.toString(), BetaBiomes.FROZEN_OCEAN_ID.toString());
    public BetaClimateMapping.Config betaTundraBiome = new BetaClimateMapping.Config(BetaBiomes.TUNDRA_ID.toString(), BetaBiomes.FROZEN_OCEAN_ID.toString());
    
    // PE
    public BetaClimateMapping.Config peDesertBiome = new BetaClimateMapping.Config(PEBiomes.PE_DESERT_ID.toString(), PEBiomes.PE_OCEAN_ID.toString());
    public BetaClimateMapping.Config peForestBiome = new BetaClimateMapping.Config(PEBiomes.PE_FOREST_ID.toString(), PEBiomes.PE_OCEAN_ID.toString());
    public BetaClimateMapping.Config peIceDesertBiome = new BetaClimateMapping.Config(PEBiomes.PE_TUNDRA_ID.toString(), PEBiomes.PE_FROZEN_OCEAN_ID.toString());
    public BetaClimateMapping.Config pePlainsBiome = new BetaClimateMapping.Config(PEBiomes.PE_PLAINS_ID.toString(), PEBiomes.PE_OCEAN_ID.toString());
    public BetaClimateMapping.Config peRainforestBiome = new BetaClimateMapping.Config(PEBiomes.PE_RAINFOREST_ID.toString(), PEBiomes.PE_WARM_OCEAN_ID.toString());
    public BetaClimateMapping.Config peSavannaBiome = new BetaClimateMapping.Config(PEBiomes.PE_SAVANNA_ID.toString(), PEBiomes.PE_OCEAN_ID.toString());
    public BetaClimateMapping.Config peShrublandBiome = new BetaClimateMapping.Config(PEBiomes.PE_SHRUBLAND_ID.toString(), PEBiomes.PE_OCEAN_ID.toString());
    public BetaClimateMapping.Config peSeasonalForestBiome = new BetaClimateMapping.Config(PEBiomes.PE_SEASONAL_FOREST_ID.toString(), PEBiomes.PE_LUKEWARM_OCEAN_ID.toString());
    public BetaClimateMapping.Config peSwamplandBiome = new BetaClimateMapping.Config(PEBiomes.PE_SWAMPLAND_ID.toString(), PEBiomes.PE_COLD_OCEAN_ID.toString());
    public BetaClimateMapping.Config peTaigaBiome = new BetaClimateMapping.Config(PEBiomes.PE_TAIGA_ID.toString(), PEBiomes.PE_FROZEN_OCEAN_ID.toString());
    public BetaClimateMapping.Config peTundraBiome = new BetaClimateMapping.Config(PEBiomes.PE_TUNDRA_ID.toString(), PEBiomes.PE_FROZEN_OCEAN_ID.toString());

    // Voronoi
    public List<VoronoiClimateMapping.Config> voronoiBiomes = List.of(
        new VoronoiClimateMapping.Config(BetaBiomes.PLAINS_ID.toString(), BetaBiomes.OCEAN_ID.toString(), BetaBiomes.OCEAN_ID.toString(), 0.5, 0.5),
        new VoronoiClimateMapping.Config(BetaBiomes.SHRUBLAND_ID.toString(), BetaBiomes.OCEAN_ID.toString(), BetaBiomes.OCEAN_ID.toString(), 0.25, 0.5),
        new VoronoiClimateMapping.Config(BetaBiomes.SAVANNA_ID.toString(), BetaBiomes.OCEAN_ID.toString(), BetaBiomes.OCEAN_ID.toString(), 0.75, 0.5),
        new VoronoiClimateMapping.Config(BetaBiomes.FOREST_ID.toString(), BetaBiomes.OCEAN_ID.toString(), BetaBiomes.OCEAN_ID.toString(), 0.75, 0.75),
        new VoronoiClimateMapping.Config(BetaBiomes.DESERT_ID.toString(), BetaBiomes.OCEAN_ID.toString(), BetaBiomes.OCEAN_ID.toString(), 0.75, 0.25),
        new VoronoiClimateMapping.Config(BetaBiomes.TUNDRA_ID.toString(), BetaBiomes.FROZEN_OCEAN_ID.toString(), BetaBiomes.FROZEN_OCEAN_ID.toString(), 0.25, 0.25),
        new VoronoiClimateMapping.Config(BetaBiomes.TAIGA_ID.toString(), BetaBiomes.FROZEN_OCEAN_ID.toString(), BetaBiomes.FROZEN_OCEAN_ID.toString(), 0.25, 0.75)
    );
}
