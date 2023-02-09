package mod.bespectacled.modernbeta.config;

import java.util.List;
import java.util.Map;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import mod.bespectacled.modernbeta.util.NbtCompoundBuilder;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomes;
import mod.bespectacled.modernbeta.world.biome.provider.climate.ClimateType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

@Config(name = "config_biome")
public class ModernBetaConfigBiome implements ConfigData {
    public String biomeProvider = ModernBetaBuiltInTypes.Biome.BETA.id;
    public String singleBiome = ModernBetaBiomes.ALPHA.getValue().toString();
    public boolean useOceanBiomes = true;
    
    public float climateTempNoiseScale = 0.025f;
    public float climateRainNoiseScale = 0.05f;
    public float climateDetailNoiseScale = 0.25f;
    public Map<String, ConfigClimateMapping> climateMappings = createClimateMapping(
        new ConfigClimateMapping(
            ModernBetaBiomes.BETA_DESERT.getValue().toString(),
            ModernBetaBiomes.BETA_OCEAN.getValue().toString()
        ),
        new ConfigClimateMapping(
            ModernBetaBiomes.BETA_FOREST.getValue().toString(),
            ModernBetaBiomes.BETA_OCEAN.getValue().toString()
        ),
        new ConfigClimateMapping(
            ModernBetaBiomes.BETA_TUNDRA.getValue().toString(),
            ModernBetaBiomes.BETA_FROZEN_OCEAN.getValue().toString()
        ),
        new ConfigClimateMapping(
            ModernBetaBiomes.BETA_PLAINS.getValue().toString(),
            ModernBetaBiomes.BETA_OCEAN.getValue().toString()
        ),
        new ConfigClimateMapping(
            ModernBetaBiomes.BETA_RAINFOREST.getValue().toString(),
            ModernBetaBiomes.BETA_WARM_OCEAN.getValue().toString()
        ),
        new ConfigClimateMapping(
            ModernBetaBiomes.BETA_SAVANNA.getValue().toString(),
            ModernBetaBiomes.BETA_OCEAN.getValue().toString()
        ),
        new ConfigClimateMapping(
            ModernBetaBiomes.BETA_SHRUBLAND.getValue().toString(),
            ModernBetaBiomes.BETA_OCEAN.getValue().toString()
        ),
        new ConfigClimateMapping(
            ModernBetaBiomes.BETA_SEASONAL_FOREST.getValue().toString(),
            ModernBetaBiomes.BETA_LUKEWARM_OCEAN.getValue().toString()
        ),
        new ConfigClimateMapping(
            ModernBetaBiomes.BETA_SWAMPLAND.getValue().toString(),
            ModernBetaBiomes.BETA_COLD_OCEAN.getValue().toString()
        ),
        new ConfigClimateMapping(
            ModernBetaBiomes.BETA_TAIGA.getValue().toString(),
            ModernBetaBiomes.BETA_FROZEN_OCEAN.getValue().toString()
        ),
        new ConfigClimateMapping(
            ModernBetaBiomes.BETA_TUNDRA.getValue().toString(),
            ModernBetaBiomes.BETA_FROZEN_OCEAN.getValue().toString()
        )
    );
    public List<ConfigVoronoiPoint> voronoiPoints = List.of(
        new ConfigVoronoiPoint(
            BiomeKeys.SNOWY_PLAINS.getValue().toString(),
            BiomeKeys.FROZEN_OCEAN.getValue().toString(),
            0.0, 0.0
        ),
        new ConfigVoronoiPoint(
            BiomeKeys.SNOWY_TAIGA.getValue().toString(),
            BiomeKeys.FROZEN_OCEAN.getValue().toString(),
            0.0, 0.5
        ),
        new ConfigVoronoiPoint(
            BiomeKeys.SWAMP.getValue().toString(),
            BiomeKeys.COLD_OCEAN.getValue().toString(),
            0.0, 1.0
        ),
        new ConfigVoronoiPoint(
            BiomeKeys.SAVANNA.getValue().toString(),
            BiomeKeys.OCEAN.getValue().toString(),
            0.5, 0.0
        ),
        new ConfigVoronoiPoint(
            BiomeKeys.FOREST.getValue().toString(),
            BiomeKeys.OCEAN.getValue().toString(),
            0.5, 0.5
        ),
        new ConfigVoronoiPoint(
            BiomeKeys.PLAINS.getValue().toString(),
            BiomeKeys.OCEAN.getValue().toString(),
            0.5, 1.0
        ),
        new ConfigVoronoiPoint(
            BiomeKeys.DESERT.getValue().toString(),
            BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
            1.0, 0.0
        ),
        new ConfigVoronoiPoint(
            BiomeKeys.DARK_FOREST.getValue().toString(),
            BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
            1.0, 0.5
        ),
        new ConfigVoronoiPoint(
            BiomeKeys.JUNGLE.getValue().toString(),
            BiomeKeys.WARM_OCEAN.getValue().toString(),
            1.0, 1.0
        )
    );
    
    private static Map<String, ConfigClimateMapping> createClimateMapping(
        ConfigClimateMapping desert,
        ConfigClimateMapping forest,
        ConfigClimateMapping iceDesert,
        ConfigClimateMapping plains,
        ConfigClimateMapping rainforest,
        ConfigClimateMapping savanna,
        ConfigClimateMapping shrubland,
        ConfigClimateMapping seasonal_forest,
        ConfigClimateMapping swampland,
        ConfigClimateMapping taiga,
        ConfigClimateMapping tundra
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
    
    public static class ConfigClimateMapping {
        public String biome;
        public String oceanBiome;
        public String deepOceanBiome;
        
        public ConfigClimateMapping(String biome, String oceanBiome) {
            this(biome, oceanBiome, oceanBiome);
        }
        
        public ConfigClimateMapping(String biome, String oceanBiome, String deepOceanBiome) {
            this.biome = biome;
            this.oceanBiome = oceanBiome;
            this.deepOceanBiome = deepOceanBiome;
        }
        
        public RegistryKey<Biome> biomeByClimateType(ClimateType type) {
            return switch(type) {
                case LAND -> keyOf(this.biome);
                case OCEAN -> keyOf(this.oceanBiome);
                case DEEP_OCEAN -> keyOf(this.deepOceanBiome);
            };
        }
        
        public RegistryKey<Biome> biome() {
            return keyOf(this.biome);
        }
        
        public RegistryKey<Biome> oceanBiome() {
            return keyOf(this.oceanBiome);
        }
        
        public RegistryKey<Biome> deepOceanBiome() {
            return keyOf(this.deepOceanBiome);
        }
        
        public NbtCompound toCompound() {
            return new NbtCompoundBuilder()
                .putString(NbtTags.BIOME, this.biome)
                .putString(NbtTags.OCEAN_BIOME, this.oceanBiome)
                .putString(NbtTags.DEEP_OCEAN_BIOME, this.deepOceanBiome)
                .build();
        }
        
        public static ConfigClimateMapping fromCompound(NbtCompound compound) {
            return new ConfigClimateMapping(
                NbtUtil.readStringOrThrow(NbtTags.BIOME, compound),
                NbtUtil.readStringOrThrow(NbtTags.OCEAN_BIOME, compound),
                NbtUtil.readStringOrThrow(NbtTags.DEEP_OCEAN_BIOME, compound)
            );
        }
        
        private static RegistryKey<Biome> keyOf(String id) {
            return RegistryKey.of(RegistryKeys.BIOME, new Identifier(id));
        }
    }
    
    public static class ConfigVoronoiPoint {
        public static final ConfigVoronoiPoint DEFAULT = new ConfigVoronoiPoint("modern_beta:beta_forest", "modern_beta:beta_ocean", 0.5, 0.5);
        
        public String biome;
        public String oceanBiome;
        public String deepOceanBiome;
        public double temp;
        public double rain;
        
        public ConfigVoronoiPoint(String biome, String oceanBiome, double temp, double rain) {
            this(biome, oceanBiome, oceanBiome, temp, rain);
        }
        
        public ConfigVoronoiPoint(String biome, String oceanBiome, String deepOceanBiome,  double temp, double rain) {
            this.biome = biome;
            this.oceanBiome = oceanBiome;
            this.deepOceanBiome = deepOceanBiome;
            this.temp = temp;
            this.rain = rain;
        }
        
        public NbtCompound toCompound() {
            return new NbtCompoundBuilder()
                .putString(NbtTags.BIOME, this.biome)
                .putString(NbtTags.OCEAN_BIOME, this.oceanBiome)
                .putString(NbtTags.DEEP_OCEAN_BIOME, this.deepOceanBiome)
                .putDouble(NbtTags.TEMP, this.temp)
                .putDouble(NbtTags.RAIN, this.rain)
                .build();
        }
    }
}
