package mod.bespectacled.modernbeta.config;

import java.util.Map;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import mod.bespectacled.modernbeta.util.NbtCompoundBuilder;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomes;
import mod.bespectacled.modernbeta.world.biome.provider.climate.ClimateMapping.ClimateType;
import net.minecraft.nbt.NbtCompound;

@Config(name = "config_biome")
public class ModernBetaConfigBiome implements ConfigData {
    public String biomeProvider = ModernBetaBuiltInTypes.Biome.BETA.name;
    public String singleBiome = ModernBetaBiomes.ALPHA_ID.toString();
    public boolean replaceOceanBiomes = true;
    
    public float tempNoiseScale = 1.0f;
    public float rainNoiseScale = 1.0f;
    public float detailNoiseScale = 1.0f;
    
    public Map<String, ClimateMapping> climates = createClimateMapping(
        new ClimateMapping(ModernBetaBiomes.BETA_DESERT_ID.toString(), ModernBetaBiomes.BETA_OCEAN_ID.toString()),
        new ClimateMapping(ModernBetaBiomes.BETA_FOREST_ID.toString(), ModernBetaBiomes.BETA_OCEAN_ID.toString()),
        new ClimateMapping(ModernBetaBiomes.BETA_TUNDRA_ID.toString(), ModernBetaBiomes.BETA_FROZEN_OCEAN_ID.toString()),
        new ClimateMapping(ModernBetaBiomes.BETA_PLAINS_ID.toString(), ModernBetaBiomes.BETA_OCEAN_ID.toString()),
        new ClimateMapping(ModernBetaBiomes.BETA_RAINFOREST_ID.toString(), ModernBetaBiomes.BETA_WARM_OCEAN_ID.toString()),
        new ClimateMapping(ModernBetaBiomes.BETA_SAVANNA_ID.toString(), ModernBetaBiomes.BETA_OCEAN_ID.toString()),
        new ClimateMapping(ModernBetaBiomes.BETA_SHRUBLAND_ID.toString(), ModernBetaBiomes.BETA_OCEAN_ID.toString()),
        new ClimateMapping(ModernBetaBiomes.BETA_SEASONAL_FOREST_ID.toString(), ModernBetaBiomes.BETA_LUKEWARM_OCEAN_ID.toString()),
        new ClimateMapping(ModernBetaBiomes.BETA_SWAMPLAND_ID.toString(), ModernBetaBiomes.BETA_COLD_OCEAN_ID.toString()),
        new ClimateMapping(ModernBetaBiomes.BETA_TAIGA_ID.toString(), ModernBetaBiomes.BETA_FROZEN_OCEAN_ID.toString()),
        new ClimateMapping(ModernBetaBiomes.BETA_TUNDRA_ID.toString(), ModernBetaBiomes.BETA_FROZEN_OCEAN_ID.toString())
    );
    
    public Map<String, ClimateMapping> peClimates = createClimateMapping(
        new ClimateMapping(ModernBetaBiomes.PE_DESERT_ID.toString(), ModernBetaBiomes.PE_OCEAN_ID.toString()),
        new ClimateMapping(ModernBetaBiomes.PE_FOREST_ID.toString(), ModernBetaBiomes.PE_OCEAN_ID.toString()),
        new ClimateMapping(ModernBetaBiomes.PE_TUNDRA_ID.toString(), ModernBetaBiomes.PE_FROZEN_OCEAN_ID.toString()),
        new ClimateMapping(ModernBetaBiomes.PE_PLAINS_ID.toString(), ModernBetaBiomes.PE_OCEAN_ID.toString()),
        new ClimateMapping(ModernBetaBiomes.PE_RAINFOREST_ID.toString(), ModernBetaBiomes.PE_WARM_OCEAN_ID.toString()),
        new ClimateMapping(ModernBetaBiomes.PE_SAVANNA_ID.toString(), ModernBetaBiomes.PE_OCEAN_ID.toString()),
        new ClimateMapping(ModernBetaBiomes.PE_SHRUBLAND_ID.toString(), ModernBetaBiomes.PE_OCEAN_ID.toString()),
        new ClimateMapping(ModernBetaBiomes.PE_SEASONAL_FOREST_ID.toString(), ModernBetaBiomes.PE_LUKEWARM_OCEAN_ID.toString()),
        new ClimateMapping(ModernBetaBiomes.PE_SWAMPLAND_ID.toString(), ModernBetaBiomes.PE_COLD_OCEAN_ID.toString()),
        new ClimateMapping(ModernBetaBiomes.PE_TAIGA_ID.toString(), ModernBetaBiomes.PE_FROZEN_OCEAN_ID.toString()),
        new ClimateMapping(ModernBetaBiomes.PE_TUNDRA_ID.toString(), ModernBetaBiomes.PE_FROZEN_OCEAN_ID.toString())
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
        
        public static ClimateMapping fromCompound(NbtCompound compound) {
            return new ClimateMapping(
                NbtUtil.readStringOrThrow(NbtTags.BIOME, compound),
                NbtUtil.readStringOrThrow(NbtTags.OCEAN_BIOME, compound),
                NbtUtil.readStringOrThrow(NbtTags.DEEP_OCEAN_BIOME, compound)
            );
        }
    }
}
