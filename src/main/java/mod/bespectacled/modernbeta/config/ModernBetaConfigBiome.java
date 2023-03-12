package mod.bespectacled.modernbeta.config;

import java.util.Map;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import mod.bespectacled.modernbeta.util.NbtCompoundBuilder;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.world.biome.beta.BetaBiomes;
import mod.bespectacled.modernbeta.world.biome.inf.InfBiomes;
import mod.bespectacled.modernbeta.world.biome.pe.PEBiomes;
import mod.bespectacled.modernbeta.world.biome.provider.climate.ClimateMapping.ClimateType;
import net.minecraft.nbt.NbtCompound;

@Config(name = "config_biome")
public class ModernBetaConfigBiome implements ConfigData {

    // General
    public String biomeType = ModernBetaBuiltInTypes.Biome.BETA.name;
    
    // Oceans
    public boolean generateOceans = true;

    // Single
    public String singleBiome = InfBiomes.ALPHA_KEY.getValue().toString();
    
    // Vanilla
    public boolean vanillaLargeBiomes = false;
    
    public Map<String, ClimateMapping> betaClimates = createClimateMapping(
        new ClimateMapping(BetaBiomes.DESERT_KEY.getValue().toString(), BetaBiomes.OCEAN_KEY.getValue().toString()),
        new ClimateMapping(BetaBiomes.FOREST_KEY.getValue().toString(), BetaBiomes.OCEAN_KEY.getValue().toString()),
        new ClimateMapping(BetaBiomes.TUNDRA_KEY.getValue().toString(), BetaBiomes.FROZEN_OCEAN_KEY.getValue().toString()),
        new ClimateMapping(BetaBiomes.PLAINS_KEY.getValue().toString(), BetaBiomes.OCEAN_KEY.getValue().toString()),
        new ClimateMapping(BetaBiomes.RAINFOREST_KEY.getValue().toString(), BetaBiomes.WARM_OCEAN_KEY.getValue().toString()),
        new ClimateMapping(BetaBiomes.SAVANNA_KEY.getValue().toString(), BetaBiomes.OCEAN_KEY.getValue().toString()),
        new ClimateMapping(BetaBiomes.SHRUBLAND_KEY.getValue().toString(), BetaBiomes.OCEAN_KEY.getValue().toString()),
        new ClimateMapping(BetaBiomes.SEASONAL_FOREST_KEY.getValue().toString(), BetaBiomes.LUKEWARM_OCEAN_KEY.getValue().toString()),
        new ClimateMapping(BetaBiomes.SWAMPLAND_KEY.getValue().toString(), BetaBiomes.COLD_OCEAN_KEY.getValue().toString()),
        new ClimateMapping(BetaBiomes.TAIGA_KEY.getValue().toString(), BetaBiomes.FROZEN_OCEAN_KEY.getValue().toString()),
        new ClimateMapping(BetaBiomes.TUNDRA_KEY.getValue().toString(), BetaBiomes.FROZEN_OCEAN_KEY.getValue().toString())
    );
    
    public Map<String, ClimateMapping> peClimates = createClimateMapping(
        new ClimateMapping(PEBiomes.PE_DESERT_KEY.getValue().toString(), PEBiomes.PE_OCEAN_KEY.getValue().toString()),
        new ClimateMapping(PEBiomes.PE_FOREST_KEY.getValue().toString(), PEBiomes.PE_OCEAN_KEY.getValue().toString()),
        new ClimateMapping(PEBiomes.PE_TUNDRA_KEY.getValue().toString(), PEBiomes.PE_FROZEN_OCEAN_KEY.getValue().toString()),
        new ClimateMapping(PEBiomes.PE_PLAINS_KEY.getValue().toString(), PEBiomes.PE_OCEAN_KEY.getValue().toString()),
        new ClimateMapping(PEBiomes.PE_RAINFOREST_KEY.getValue().toString(), PEBiomes.PE_WARM_OCEAN_KEY.getValue().toString()),
        new ClimateMapping(PEBiomes.PE_SAVANNA_KEY.getValue().toString(), PEBiomes.PE_OCEAN_KEY.getValue().toString()),
        new ClimateMapping(PEBiomes.PE_SHRUBLAND_KEY.getValue().toString(), PEBiomes.PE_OCEAN_KEY.getValue().toString()),
        new ClimateMapping(PEBiomes.PE_SEASONAL_FOREST_KEY.getValue().toString(), PEBiomes.PE_LUKEWARM_OCEAN_KEY.getValue().toString()),
        new ClimateMapping(PEBiomes.PE_SWAMPLAND_KEY.getValue().toString(), PEBiomes.PE_COLD_OCEAN_KEY.getValue().toString()),
        new ClimateMapping(PEBiomes.PE_TAIGA_KEY.getValue().toString(), PEBiomes.PE_FROZEN_OCEAN_KEY.getValue().toString()),
        new ClimateMapping(PEBiomes.PE_TUNDRA_KEY.getValue().toString(), PEBiomes.PE_FROZEN_OCEAN_KEY.getValue().toString())
    );
    
    private static Map<String, ClimateMapping> createClimateMapping(
        ClimateMapping desert,
        ClimateMapping forest,
        ClimateMapping iceDesert,
        ClimateMapping plains,
        ClimateMapping rainforest,
        ClimateMapping savanna,
        ClimateMapping shrubland,
        ClimateMapping seasonal_forest,
        ClimateMapping swampland,
        ClimateMapping taiga,
        ClimateMapping tundra
    ) {
        return Map.ofEntries(
            Map.entry("desert", desert),
            Map.entry("forest", forest),
            Map.entry("ice_desert", iceDesert),
            Map.entry("plains", plains),
            Map.entry("rainforest", rainforest),
            Map.entry("savanna", savanna),
            Map.entry("shrubland", shrubland),
            Map.entry("seasonal_forest", seasonal_forest),
            Map.entry("swampland", swampland),
            Map.entry("taiga", taiga),
            Map.entry("tundra", tundra)
        );
    }
    
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
        
        public String biomeByClimateType(ClimateType type) {
            return switch(type) {
                case LAND -> this.biome;
                case OCEAN -> this.oceanBiome;
                case DEEP_OCEAN -> this.deepOceanBiome;
            };
        }
        
        public void setBiomeByClimateType(String biome, ClimateType type) {
            switch(type) {
                case LAND -> this.biome = biome;
                case OCEAN -> this.oceanBiome = biome;
                case DEEP_OCEAN -> this.deepOceanBiome = biome;
            }
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
