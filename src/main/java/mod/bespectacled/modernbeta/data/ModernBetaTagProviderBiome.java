package mod.bespectacled.modernbeta.data;

import java.util.concurrent.CompletableFuture;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public class ModernBetaTagProviderBiome extends FabricTagProvider<Biome> {
    public static final TagKey<Biome> IS_MODERN_BETA = keyOf("is_modern_beta");
    public static final TagKey<Biome> IS_BETA = keyOf("is_beta");
    public static final TagKey<Biome> IS_PE = keyOf("is_pe");
    public static final TagKey<Biome> IS_ALPHA = keyOf("is_alpha");
    public static final TagKey<Biome> IS_INFDEV = keyOf("is_infdev");
    public static final TagKey<Biome> IS_INDEV = keyOf("is_indev");
    
    public static final TagKey<Biome> IS_FOREST = keyOf("is_forest");
    public static final TagKey<Biome> IS_SEASONAL_FOREST = keyOf("is_seasonal_forest");
    public static final TagKey<Biome> IS_RAINFOREST = keyOf("is_rainforest");
    public static final TagKey<Biome> IS_DESERT = keyOf("is_desert");
    public static final TagKey<Biome> IS_PLAINS = keyOf("is_plains");
    public static final TagKey<Biome> IS_SHRUBLAND = keyOf("is_shrubland");
    public static final TagKey<Biome> IS_SAVANNA = keyOf("is_savanna");
    public static final TagKey<Biome> IS_SWAMP = keyOf("is_swamp");
    public static final TagKey<Biome> IS_TAIGA = keyOf("is_taiga");
    public static final TagKey<Biome> IS_TUNDRA = keyOf("is_tundra");
    public static final TagKey<Biome> IS_OCEAN = keyOf("is_ocean");
    
    public static final TagKey<Biome> INDEV_STRONGHOLD_HAS_STRUCTURE = keyOf("has_structure/indev_stronghold");
    
    /*
     * TODO: Deprecated, remove 1.20
     */
    public static final TagKey<Biome> SURFACE_CONFIG_IS_DESERT = keyOf("surface_config/is_desert");
    public static final TagKey<Biome> SURFACE_CONFIG_IS_BADLANDS = keyOf("surface_config/is_badlands");
    public static final TagKey<Biome> SURFACE_CONFIG_IS_NETHER = keyOf("surface_config/is_nether");
    public static final TagKey<Biome> SURFACE_CONFIG_IS_END = keyOf("surface_config/is_end");
    public static final TagKey<Biome> SURFACE_CONFIG_SWAMP = keyOf("surface_config/swamp");
    
    public static final TagKey<Biome> SURFACE_CONFIG_SAND = keyOf("surface_config/sand");
    public static final TagKey<Biome> SURFACE_CONFIG_RED_SAND = keyOf("surface_config/red_sand");
    public static final TagKey<Biome> SURFACE_CONFIG_BADLANDS = keyOf("surface_config/badlands");
    public static final TagKey<Biome> SURFACE_CONFIG_NETHER = keyOf("surface_config/nether");
    public static final TagKey<Biome> SURFACE_CONFIG_WARPED_NYLIUM = keyOf("surface_config/warped_nylium");
    public static final TagKey<Biome> SURFACE_CONFIG_CRIMSON_NYLIUM = keyOf("surface_config/crimson_nylium");
    public static final TagKey<Biome> SURFACE_CONFIG_BASALT = keyOf("surface_config/basalt");
    public static final TagKey<Biome> SURFACE_CONFIG_SOUL_SOIL = keyOf("surface_config/soul_soil");
    public static final TagKey<Biome> SURFACE_CONFIG_END = keyOf("surface_config/end");
    public static final TagKey<Biome> SURFACE_CONFIG_GRASS = keyOf("surface_config/grass");
    public static final TagKey<Biome> SURFACE_CONFIG_MUD = keyOf("surface_config/mud");
    public static final TagKey<Biome> SURFACE_CONFIG_MYCELIUM = keyOf("surface_config/mycelium");
    public static final TagKey<Biome> SURFACE_CONFIG_PODZOL = keyOf("surface_config/podzol");
    public static final TagKey<Biome> SURFACE_CONFIG_STONE = keyOf("surface_config/stone");
    public static final TagKey<Biome> SURFACE_CONFIG_SNOW = keyOf("surface_config/snow");
    public static final TagKey<Biome> SURFACE_CONFIG_SNOW_DIRT = keyOf("surface_config/snow_dirt");
    public static final TagKey<Biome> SURFACE_CONFIG_SNOW_PACKED_ICE = keyOf("surface_config/snow_packed_ice");
    public static final TagKey<Biome> SURFACE_CONFIG_SNOW_STONE = keyOf("surface_config/snow_stone");
    
    public ModernBetaTagProviderBiome(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BIOME, registriesFuture);
    }

    @Override
    protected void configure(WrapperLookup lookup) {
        this.configureModernBeta(lookup);
        this.configureVanilla(lookup);
        this.configureConventional(lookup);
    }
    
    private void configureModernBeta(WrapperLookup lookup) {
        /* Modern Beta Biome Tags */
        
        getOrCreateTagBuilder(IS_MODERN_BETA).add(
            ModernBetaBiomes.BETA_FOREST,
            ModernBetaBiomes.BETA_SHRUBLAND,
            ModernBetaBiomes.BETA_DESERT,
            ModernBetaBiomes.BETA_SAVANNA,
            ModernBetaBiomes.BETA_PLAINS,
            ModernBetaBiomes.BETA_SEASONAL_FOREST,
            ModernBetaBiomes.BETA_RAINFOREST,
            ModernBetaBiomes.BETA_SWAMPLAND,
            ModernBetaBiomes.BETA_TAIGA,
            ModernBetaBiomes.BETA_TUNDRA,
            ModernBetaBiomes.BETA_ICE_DESERT,
            ModernBetaBiomes.BETA_OCEAN,
            ModernBetaBiomes.BETA_LUKEWARM_OCEAN,
            ModernBetaBiomes.BETA_WARM_OCEAN,
            ModernBetaBiomes.BETA_COLD_OCEAN,
            ModernBetaBiomes.BETA_FROZEN_OCEAN,
            ModernBetaBiomes.BETA_SKY,
                
            ModernBetaBiomes.PE_FOREST,
            ModernBetaBiomes.PE_SHRUBLAND,
            ModernBetaBiomes.PE_DESERT,
            ModernBetaBiomes.PE_SAVANNA,
            ModernBetaBiomes.PE_PLAINS,
            ModernBetaBiomes.PE_SEASONAL_FOREST,
            ModernBetaBiomes.PE_RAINFOREST,
            ModernBetaBiomes.PE_SWAMPLAND,
            ModernBetaBiomes.PE_TAIGA,
            ModernBetaBiomes.PE_TUNDRA,
            ModernBetaBiomes.PE_ICE_DESERT,
            ModernBetaBiomes.PE_OCEAN,
            ModernBetaBiomes.PE_LUKEWARM_OCEAN,
            ModernBetaBiomes.PE_WARM_OCEAN,
            ModernBetaBiomes.PE_COLD_OCEAN,
            ModernBetaBiomes.PE_FROZEN_OCEAN,
            
            ModernBetaBiomes.ALPHA,
            ModernBetaBiomes.ALPHA_WINTER,
            
            ModernBetaBiomes.INFDEV_611,
            ModernBetaBiomes.INFDEV_420,
            ModernBetaBiomes.INFDEV_415,
            ModernBetaBiomes.INFDEV_227,
            
            ModernBetaBiomes.INDEV_NORMAL,
            ModernBetaBiomes.INDEV_HELL,
            ModernBetaBiomes.INDEV_PARADISE,
            ModernBetaBiomes.INDEV_WOODS
        );
        
        getOrCreateTagBuilder(IS_BETA).add(
            ModernBetaBiomes.BETA_FOREST,
            ModernBetaBiomes.BETA_SHRUBLAND,
            ModernBetaBiomes.BETA_DESERT,
            ModernBetaBiomes.BETA_SAVANNA,
            ModernBetaBiomes.BETA_PLAINS,
            ModernBetaBiomes.BETA_SEASONAL_FOREST,
            ModernBetaBiomes.BETA_RAINFOREST,
            ModernBetaBiomes.BETA_SWAMPLAND,
            ModernBetaBiomes.BETA_TAIGA,
            ModernBetaBiomes.BETA_TUNDRA,
            ModernBetaBiomes.BETA_ICE_DESERT,
            ModernBetaBiomes.BETA_OCEAN,
            ModernBetaBiomes.BETA_LUKEWARM_OCEAN,
            ModernBetaBiomes.BETA_WARM_OCEAN,
            ModernBetaBiomes.BETA_COLD_OCEAN,
            ModernBetaBiomes.BETA_FROZEN_OCEAN,
            ModernBetaBiomes.BETA_SKY
        );
        
        getOrCreateTagBuilder(IS_PE).add(
            ModernBetaBiomes.PE_FOREST,
            ModernBetaBiomes.PE_SHRUBLAND,
            ModernBetaBiomes.PE_DESERT,
            ModernBetaBiomes.PE_SAVANNA,
            ModernBetaBiomes.PE_PLAINS,
            ModernBetaBiomes.PE_SEASONAL_FOREST,
            ModernBetaBiomes.PE_RAINFOREST,
            ModernBetaBiomes.PE_SWAMPLAND,
            ModernBetaBiomes.PE_TAIGA,
            ModernBetaBiomes.PE_TUNDRA,
            ModernBetaBiomes.PE_ICE_DESERT,
            ModernBetaBiomes.PE_OCEAN,
            ModernBetaBiomes.PE_LUKEWARM_OCEAN,
            ModernBetaBiomes.PE_WARM_OCEAN,
            ModernBetaBiomes.PE_COLD_OCEAN,
            ModernBetaBiomes.PE_FROZEN_OCEAN
        );

        getOrCreateTagBuilder(IS_ALPHA).add(
            ModernBetaBiomes.ALPHA,
            ModernBetaBiomes.ALPHA_WINTER
        );

        getOrCreateTagBuilder(IS_INFDEV).add(
            ModernBetaBiomes.INFDEV_611,
            ModernBetaBiomes.INFDEV_420,
            ModernBetaBiomes.INFDEV_415,
            ModernBetaBiomes.INFDEV_227
        );

        getOrCreateTagBuilder(IS_INDEV).add(
            ModernBetaBiomes.INDEV_NORMAL,
            ModernBetaBiomes.INDEV_HELL,
            ModernBetaBiomes.INDEV_PARADISE,
            ModernBetaBiomes.INDEV_WOODS
        );
        
        getOrCreateTagBuilder(IS_FOREST).add(
            ModernBetaBiomes.BETA_FOREST,
            ModernBetaBiomes.PE_FOREST
        );
        
        getOrCreateTagBuilder(IS_SEASONAL_FOREST).add(
            ModernBetaBiomes.BETA_SEASONAL_FOREST,
            ModernBetaBiomes.PE_SEASONAL_FOREST
        );
        
        getOrCreateTagBuilder(IS_RAINFOREST).add(
            ModernBetaBiomes.BETA_RAINFOREST,
            ModernBetaBiomes.PE_RAINFOREST
        );

        getOrCreateTagBuilder(IS_DESERT).add(
            ModernBetaBiomes.BETA_DESERT,
            ModernBetaBiomes.PE_DESERT
        );
        
        getOrCreateTagBuilder(IS_PLAINS).add(
            ModernBetaBiomes.BETA_PLAINS,
            ModernBetaBiomes.PE_PLAINS
        );
        
        getOrCreateTagBuilder(IS_SHRUBLAND).add(
            ModernBetaBiomes.BETA_SHRUBLAND,
            ModernBetaBiomes.PE_SHRUBLAND
        );
        
        getOrCreateTagBuilder(IS_SAVANNA).add(
            ModernBetaBiomes.BETA_SAVANNA,
            ModernBetaBiomes.PE_SAVANNA
        );
        
        getOrCreateTagBuilder(IS_SWAMP).add(
            ModernBetaBiomes.BETA_SWAMPLAND,
            ModernBetaBiomes.PE_SWAMPLAND
        );
        
        getOrCreateTagBuilder(IS_TAIGA).add(
            ModernBetaBiomes.BETA_TAIGA,
            ModernBetaBiomes.PE_TAIGA
        );
        
        getOrCreateTagBuilder(IS_TUNDRA).add(
            ModernBetaBiomes.BETA_TUNDRA,
            ModernBetaBiomes.PE_TUNDRA,
            ModernBetaBiomes.BETA_ICE_DESERT,
            ModernBetaBiomes.PE_ICE_DESERT
        );
        
        getOrCreateTagBuilder(IS_OCEAN).add(
            ModernBetaBiomes.BETA_OCEAN,
            ModernBetaBiomes.BETA_LUKEWARM_OCEAN,
            ModernBetaBiomes.BETA_WARM_OCEAN,
            ModernBetaBiomes.BETA_COLD_OCEAN,
            ModernBetaBiomes.BETA_FROZEN_OCEAN,

            ModernBetaBiomes.PE_OCEAN,
            ModernBetaBiomes.PE_LUKEWARM_OCEAN,
            ModernBetaBiomes.PE_WARM_OCEAN,
            ModernBetaBiomes.PE_COLD_OCEAN,
            ModernBetaBiomes.PE_FROZEN_OCEAN
        );

        /* Modern Beta Biome Structure Tags */
        
        getOrCreateTagBuilder(INDEV_STRONGHOLD_HAS_STRUCTURE)
            .addTag(IS_INDEV);
        
        /* Modern Beta Surface Config Tags */
        
        getOrCreateTagBuilder(SURFACE_CONFIG_SAND)
            .addOptionalTag(SURFACE_CONFIG_IS_DESERT)
            .add(
                ModernBetaBiomes.BETA_DESERT,
                ModernBetaBiomes.PE_DESERT,
                BiomeKeys.DESERT,
                BiomeKeys.BEACH,
                BiomeKeys.SNOWY_BEACH
            );
        
        getOrCreateTagBuilder(SURFACE_CONFIG_RED_SAND);
        
        getOrCreateTagBuilder(SURFACE_CONFIG_BADLANDS)
            .addOptionalTag(SURFACE_CONFIG_IS_BADLANDS)
            .add(
                BiomeKeys.BADLANDS,
                BiomeKeys.ERODED_BADLANDS,
                BiomeKeys.WOODED_BADLANDS
            );
    
        getOrCreateTagBuilder(SURFACE_CONFIG_NETHER)
            .addOptionalTag(SURFACE_CONFIG_IS_NETHER)
            .add(BiomeKeys.NETHER_WASTES);
        
        getOrCreateTagBuilder(SURFACE_CONFIG_WARPED_NYLIUM)
            .add(BiomeKeys.WARPED_FOREST);
        
        getOrCreateTagBuilder(SURFACE_CONFIG_CRIMSON_NYLIUM)
            .add(BiomeKeys.CRIMSON_FOREST);
        
        getOrCreateTagBuilder(SURFACE_CONFIG_BASALT)
            .add(BiomeKeys.BASALT_DELTAS);
        
        getOrCreateTagBuilder(SURFACE_CONFIG_SOUL_SOIL)
            .add(BiomeKeys.SOUL_SAND_VALLEY);
        
        getOrCreateTagBuilder(SURFACE_CONFIG_END)
            .addOptionalTag(SURFACE_CONFIG_IS_END)
            .add(
                BiomeKeys.THE_END,
                BiomeKeys.END_BARRENS,
                BiomeKeys.END_HIGHLANDS,
                BiomeKeys.END_MIDLANDS,
                BiomeKeys.SMALL_END_ISLANDS
            );
        
        getOrCreateTagBuilder(SURFACE_CONFIG_GRASS)
            .addOptionalTag(SURFACE_CONFIG_SWAMP)
            .add(BiomeKeys.SWAMP);
        
        getOrCreateTagBuilder(SURFACE_CONFIG_MUD)
            .add(BiomeKeys.MANGROVE_SWAMP);
        
        getOrCreateTagBuilder(SURFACE_CONFIG_MYCELIUM)
            .add(BiomeKeys.MUSHROOM_FIELDS);
        
        getOrCreateTagBuilder(SURFACE_CONFIG_PODZOL)
            .add(
                BiomeKeys.OLD_GROWTH_PINE_TAIGA,
                BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA
            );
        
        getOrCreateTagBuilder(SURFACE_CONFIG_STONE)
            .add(
                BiomeKeys.STONY_PEAKS,
                BiomeKeys.STONY_SHORE
            );
        
        getOrCreateTagBuilder(SURFACE_CONFIG_SNOW)
            .add(BiomeKeys.SNOWY_SLOPES);
        
        getOrCreateTagBuilder(SURFACE_CONFIG_SNOW_DIRT)
            .add(
                BiomeKeys.GROVE,
                BiomeKeys.ICE_SPIKES
            );
        
        getOrCreateTagBuilder(SURFACE_CONFIG_SNOW_PACKED_ICE)
            .add(BiomeKeys.FROZEN_PEAKS);

        getOrCreateTagBuilder(SURFACE_CONFIG_SNOW_STONE)
            .add(BiomeKeys.JAGGED_PEAKS);
    }
    
    private void configureVanilla(WrapperLookup lookup) {
        /* Vanilla Biome Tags */
        
        getOrCreateTagBuilder(BiomeTags.IS_OVERWORLD)
            .addTag(IS_MODERN_BETA);
        
        getOrCreateTagBuilder(BiomeTags.IS_DEEP_OCEAN)
            .addTag(IS_OCEAN);
        
        getOrCreateTagBuilder(BiomeTags.IS_FOREST)
            .addTag(IS_FOREST)
            .addTag(IS_SEASONAL_FOREST);
        
        getOrCreateTagBuilder(BiomeTags.IS_JUNGLE)
            .addTag(IS_RAINFOREST);
        
        getOrCreateTagBuilder(BiomeTags.IS_OCEAN)
            .addTag(IS_OCEAN);
        
        getOrCreateTagBuilder(BiomeTags.IS_TAIGA)
            .addTag(IS_TAIGA);
        
        /* Vanilla Biome Structure Tags */
        
        getOrCreateTagBuilder(BiomeTags.BURIED_TREASURE_HAS_STRUCTURE)
            .addTag(IS_OCEAN);
        
        getOrCreateTagBuilder(BiomeTags.DESERT_PYRAMID_HAS_STRUCTURE)
            .addTag(IS_DESERT);
        
        getOrCreateTagBuilder(BiomeTags.IGLOO_HAS_STRUCTURE)
            .addTag(IS_TUNDRA);
        
        getOrCreateTagBuilder(BiomeTags.JUNGLE_TEMPLE_HAS_STRUCTURE)
            .addTag(IS_RAINFOREST);
        
        getOrCreateTagBuilder(BiomeTags.MINESHAFT_HAS_STRUCTURE)
            .addTag(IS_BETA)
            .addTag(IS_PE)
            .addTag(IS_ALPHA)
            .addTag(IS_INFDEV)
            .addTag(IS_INDEV);

        getOrCreateTagBuilder(BiomeTags.OCEAN_RUIN_COLD_HAS_STRUCTURE).add(
            ModernBetaBiomes.BETA_OCEAN,
            ModernBetaBiomes.BETA_COLD_OCEAN,
            ModernBetaBiomes.BETA_FROZEN_OCEAN,

            ModernBetaBiomes.PE_OCEAN,
            ModernBetaBiomes.PE_COLD_OCEAN,
            ModernBetaBiomes.PE_FROZEN_OCEAN
        );
        
        getOrCreateTagBuilder(BiomeTags.OCEAN_RUIN_WARM_HAS_STRUCTURE).add(
            ModernBetaBiomes.BETA_LUKEWARM_OCEAN,
            ModernBetaBiomes.BETA_WARM_OCEAN,

            ModernBetaBiomes.PE_LUKEWARM_OCEAN,
            ModernBetaBiomes.PE_WARM_OCEAN
        );
        
        getOrCreateTagBuilder(BiomeTags.PILLAGER_OUTPOST_HAS_STRUCTURE)
            .addTag(IS_DESERT)
            .addTag(IS_PLAINS)
            .addTag(IS_SAVANNA)
            .addTag(IS_SWAMP)
            .addTag(IS_TUNDRA)
            .add(ModernBetaBiomes.BETA_SKY);
        
        getOrCreateTagBuilder(BiomeTags.RUINED_PORTAL_DESERT_HAS_STRUCTURE)
            .addTag(IS_DESERT);
        
        getOrCreateTagBuilder(BiomeTags.RUINED_PORTAL_STANDARD_HAS_STRUCTURE)
            .addTag(IS_PLAINS)
            .addTag(IS_SAVANNA)
            .addTag(IS_TUNDRA)
            .add(ModernBetaBiomes.BETA_SKY);
        
        getOrCreateTagBuilder(BiomeTags.RUINED_PORTAL_SWAMP_HAS_STRUCTURE)
            .addTag(IS_SWAMP);
        
        getOrCreateTagBuilder(BiomeTags.STRONGHOLD_HAS_STRUCTURE)
            .addTag(IS_BETA)
            .addTag(IS_PE)
            .addTag(IS_ALPHA)
            .addTag(IS_INFDEV);
        
        getOrCreateTagBuilder(BiomeTags.SWAMP_HUT_HAS_STRUCTURE)
            .addTag(IS_SWAMP);
        
        getOrCreateTagBuilder(BiomeTags.VILLAGE_DESERT_HAS_STRUCTURE)
            .addTag(IS_DESERT);
        
        getOrCreateTagBuilder(BiomeTags.VILLAGE_PLAINS_HAS_STRUCTURE)
            .addTag(IS_PLAINS)
            .addTag(IS_SHRUBLAND)
            .addTag(IS_SAVANNA)
            .addTag(IS_ALPHA)
            .addTag(IS_INFDEV)
            .addTag(IS_INDEV);
        
        getOrCreateTagBuilder(BiomeTags.VILLAGE_SNOWY_HAS_STRUCTURE)
            .addTag(IS_TUNDRA);
        
        getOrCreateTagBuilder(BiomeTags.VILLAGE_TAIGA_HAS_STRUCTURE)
            .addTag(IS_TAIGA);
        
        getOrCreateTagBuilder(BiomeTags.WOODLAND_MANSION_HAS_STRUCTURE)
            .addTag(IS_SEASONAL_FOREST);
        
        /* Misc. Tags */
        
        getOrCreateTagBuilder(BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL).add(
            ModernBetaBiomes.BETA_WARM_OCEAN,
            ModernBetaBiomes.PE_WARM_OCEAN
        );
        
        getOrCreateTagBuilder(BiomeTags.POLAR_BEARS_SPAWN_ON_ALTERNATE_BLOCKS).add(
            ModernBetaBiomes.BETA_FROZEN_OCEAN,
            ModernBetaBiomes.PE_FROZEN_OCEAN
        );
        
        getOrCreateTagBuilder(BiomeTags.ALLOWS_SURFACE_SLIME_SPAWNS)
            .addTag(IS_SWAMP);
        
        getOrCreateTagBuilder(BiomeTags.TRAIL_RUINS_HAS_STRUCTURE)
            .add(
                ModernBetaBiomes.BETA_TAIGA,
                ModernBetaBiomes.BETA_RAINFOREST,
                
                ModernBetaBiomes.PE_TAIGA,
                ModernBetaBiomes.PE_RAINFOREST
            );
    }
    
    /*
     * For determining climate tags, see:
     * https://www.minecraftforum.net/forums/archive/alpha/alpha-survival-single-player/820956-biome-geography-algorithm-analysis-updated-11-4
     * 
     */
    private void configureConventional(WrapperLookup lookup) {
        getOrCreateTagBuilder(ConventionalBiomeTags.AQUATIC)
            .addTag(IS_OCEAN);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.AQUATIC_ICY).add(
            ModernBetaBiomes.BETA_FROZEN_OCEAN,
            ModernBetaBiomes.PE_FROZEN_OCEAN
        );
        
        getOrCreateTagBuilder(ConventionalBiomeTags.CLIMATE_COLD)
            .addTag(IS_TAIGA)
            .addTag(IS_TUNDRA);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.CLIMATE_DRY)
            .addTag(IS_DESERT)
            .addTag(IS_PLAINS)
            .addTag(IS_SAVANNA)
            .addTag(IS_SHRUBLAND)
            .addTag(IS_TUNDRA);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.CLIMATE_HOT)
            .addTag(IS_DESERT)
            .addTag(IS_PLAINS)
            .addTag(IS_SEASONAL_FOREST)
            .addTag(IS_RAINFOREST);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.CLIMATE_TEMPERATE)
            .addTag(IS_SAVANNA)
            .addTag(IS_SHRUBLAND)
            .addTag(IS_FOREST)
            .addTag(IS_SWAMP);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.CLIMATE_WET)
            .addTag(IS_OCEAN)
            .addTag(IS_SWAMP)
            .addTag(IS_RAINFOREST);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.DESERT)
            .addTag(IS_DESERT);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.FOREST)
            .addTag(IS_FOREST)
            .addTag(IS_SEASONAL_FOREST);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.IN_OVERWORLD)
            .addTag(IS_MODERN_BETA);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.JUNGLE)
            .addTag(IS_RAINFOREST);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.DEEP_OCEAN)
            .addTag(IS_OCEAN);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.PLAINS)
            .addTag(IS_PLAINS)
            .addTag(IS_SHRUBLAND);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.SAVANNA)
            .addTag(IS_SAVANNA);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.SHALLOW_OCEAN)
            .addTag(IS_OCEAN);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.SNOWY)
            .addTag(IS_TAIGA)
            .addTag(IS_TUNDRA);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.SNOWY_PLAINS)
            .addTag(IS_TUNDRA);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.SWAMP)
            .addTag(IS_SWAMP);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.TAIGA)
            .addTag(IS_TAIGA);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.TREE_CONIFEROUS)
            .addTag(IS_TAIGA);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.TREE_DECIDUOUS)
            .addTag(IS_FOREST)
            .addTag(IS_SEASONAL_FOREST);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.TREE_JUNGLE)
            .addTag(IS_RAINFOREST);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.TREE_SAVANNA)
            .addTag(IS_SAVANNA);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.VEGETATION_DENSE)
            .addTag(IS_RAINFOREST)
            .addTag(IS_PLAINS);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.VEGETATION_SPARSE)
            .addTag(IS_DESERT)
            .addTag(IS_SAVANNA)
            .addTag(IS_SHRUBLAND)
            .addTag(IS_TUNDRA);
    }
    
    private static TagKey<Biome> keyOf(String id) {
        return TagKey.of(RegistryKeys.BIOME, ModernBeta.createId(id));
    }
}