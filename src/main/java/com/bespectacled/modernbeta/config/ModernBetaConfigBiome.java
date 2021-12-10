package com.bespectacled.modernbeta.config;

import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.util.NbtCompoundBuilder;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.world.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.world.biome.inf.InfBiomes;
import com.bespectacled.modernbeta.world.biome.pe.PEBiomes;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.minecraft.nbt.NbtCompound;

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
    public ClimateMapping betaDesertBiome = new ClimateMapping(BetaBiomes.DESERT_ID.toString(), BetaBiomes.OCEAN_ID.toString());
    public ClimateMapping betaForestBiome = new ClimateMapping(BetaBiomes.FOREST_ID.toString(), BetaBiomes.OCEAN_ID.toString());
    public ClimateMapping betaIceDesertBiome = new ClimateMapping(BetaBiomes.TUNDRA_ID.toString(), BetaBiomes.FROZEN_OCEAN_ID.toString());
    public ClimateMapping betaPlainsBiome = new ClimateMapping(BetaBiomes.PLAINS_ID.toString(), BetaBiomes.OCEAN_ID.toString());
    public ClimateMapping betaRainforestBiome = new ClimateMapping(BetaBiomes.RAINFOREST_ID.toString(), BetaBiomes.WARM_OCEAN_ID.toString());
    public ClimateMapping betaSavannaBiome = new ClimateMapping(BetaBiomes.SAVANNA_ID.toString(), BetaBiomes.OCEAN_ID.toString());
    public ClimateMapping betaShrublandBiome = new ClimateMapping(BetaBiomes.SHRUBLAND_ID.toString(), BetaBiomes.OCEAN_ID.toString());
    public ClimateMapping betaSeasonalForestBiome = new ClimateMapping(BetaBiomes.SEASONAL_FOREST_ID.toString(), BetaBiomes.LUKEWARM_OCEAN_ID.toString());
    public ClimateMapping betaSwamplandBiome = new ClimateMapping(BetaBiomes.SWAMPLAND_ID.toString(), BetaBiomes.COLD_OCEAN_ID.toString());
    public ClimateMapping betaTaigaBiome = new ClimateMapping(BetaBiomes.TAIGA_ID.toString(), BetaBiomes.FROZEN_OCEAN_ID.toString());
    public ClimateMapping betaTundraBiome = new ClimateMapping(BetaBiomes.TUNDRA_ID.toString(), BetaBiomes.FROZEN_OCEAN_ID.toString());
    
    // PE
    public ClimateMapping peDesertBiome = new ClimateMapping(PEBiomes.PE_DESERT_ID.toString(), PEBiomes.PE_OCEAN_ID.toString());
    public ClimateMapping peForestBiome = new ClimateMapping(PEBiomes.PE_FOREST_ID.toString(), PEBiomes.PE_OCEAN_ID.toString());
    public ClimateMapping peIceDesertBiome = new ClimateMapping(PEBiomes.PE_TUNDRA_ID.toString(), PEBiomes.PE_FROZEN_OCEAN_ID.toString());
    public ClimateMapping pePlainsBiome = new ClimateMapping(PEBiomes.PE_PLAINS_ID.toString(), PEBiomes.PE_OCEAN_ID.toString());
    public ClimateMapping peRainforestBiome = new ClimateMapping(PEBiomes.PE_RAINFOREST_ID.toString(), PEBiomes.PE_WARM_OCEAN_ID.toString());
    public ClimateMapping peSavannaBiome = new ClimateMapping(PEBiomes.PE_SAVANNA_ID.toString(), PEBiomes.PE_OCEAN_ID.toString());
    public ClimateMapping peShrublandBiome = new ClimateMapping(PEBiomes.PE_SHRUBLAND_ID.toString(), PEBiomes.PE_OCEAN_ID.toString());
    public ClimateMapping peSeasonalForestBiome = new ClimateMapping(PEBiomes.PE_SEASONAL_FOREST_ID.toString(), PEBiomes.PE_LUKEWARM_OCEAN_ID.toString());
    public ClimateMapping peSwamplandBiome = new ClimateMapping(PEBiomes.PE_SWAMPLAND_ID.toString(), PEBiomes.PE_COLD_OCEAN_ID.toString());
    public ClimateMapping peTaigaBiome = new ClimateMapping(PEBiomes.PE_TAIGA_ID.toString(), PEBiomes.PE_FROZEN_OCEAN_ID.toString());
    public ClimateMapping peTundraBiome = new ClimateMapping(PEBiomes.PE_TUNDRA_ID.toString(), PEBiomes.PE_FROZEN_OCEAN_ID.toString());
    
    public static class ClimateMapping {
        public String biome;
        public String oceanBiome;
        public String deepOceanBiome;
        
        public ClimateMapping(String biome, String oceanBiome) {
            this(biome, oceanBiome, oceanBiome);
        }
        
        public ClimateMapping(String biome, String oceanBiome, String deepOceanBiome) {
            this.biome = biome;
            this.oceanBiome = oceanBiome;
            this.deepOceanBiome = deepOceanBiome;
        }
        
        public NbtCompound toCompound() {
            return new NbtCompoundBuilder()
                .putString(NbtTags.BIOME, this.biome)
                .putString(NbtTags.OCEAN_BIOME, this.oceanBiome)
                .putString(NbtTags.DEEP_OCEAN_BIOME, this.deepOceanBiome)
                .build();
        }
    }
}
