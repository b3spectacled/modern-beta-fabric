package mod.bespectacled.modernbeta.data;

import java.util.concurrent.CompletableFuture;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class ModernBetaBiomeTagProvider extends FabricTagProvider<Biome> {
    public static final TagKey<Biome> IS_BETA = TagKey.of(RegistryKeys.BIOME, ModernBeta.createId("is_beta"));
    public static final TagKey<Biome> IS_PE = TagKey.of(RegistryKeys.BIOME, ModernBeta.createId("is_pe"));
    public static final TagKey<Biome> IS_ALPHA = TagKey.of(RegistryKeys.BIOME, ModernBeta.createId("is_alpha"));
    public static final TagKey<Biome> IS_INFDEV = TagKey.of(RegistryKeys.BIOME, ModernBeta.createId("is_infdev"));
    public static final TagKey<Biome> IS_INDEV = TagKey.of(RegistryKeys.BIOME, ModernBeta.createId("is_indev"));
    
    public static final TagKey<Biome> IS_FOREST = TagKey.of(RegistryKeys.BIOME, ModernBeta.createId("is_forest"));
    public static final TagKey<Biome> IS_SEASONAL_FOREST = TagKey.of(RegistryKeys.BIOME, ModernBeta.createId("is_seasonal_forest"));
    public static final TagKey<Biome> IS_RAINFOREST = TagKey.of(RegistryKeys.BIOME, ModernBeta.createId("is_rainforest"));
    public static final TagKey<Biome> IS_DESERT = TagKey.of(RegistryKeys.BIOME, ModernBeta.createId("is_desert"));
    public static final TagKey<Biome> IS_PLAINS = TagKey.of(RegistryKeys.BIOME, ModernBeta.createId("is_plains"));
    public static final TagKey<Biome> IS_SHRUBLAND = TagKey.of(RegistryKeys.BIOME, ModernBeta.createId("is_shrubland"));
    public static final TagKey<Biome> IS_SAVANNA = TagKey.of(RegistryKeys.BIOME, ModernBeta.createId("is_savanna"));
    public static final TagKey<Biome> IS_SWAMP = TagKey.of(RegistryKeys.BIOME, ModernBeta.createId("is_swamp"));
    public static final TagKey<Biome> IS_TAIGA = TagKey.of(RegistryKeys.BIOME, ModernBeta.createId("is_taiga"));
    public static final TagKey<Biome> IS_TUNDRA = TagKey.of(RegistryKeys.BIOME, ModernBeta.createId("is_tundra"));
    public static final TagKey<Biome> IS_OCEAN = TagKey.of(RegistryKeys.BIOME, ModernBeta.createId("is_ocean"));
    
    public static final TagKey<Biome> INDEV_STRONGHOLD_HAS_STRUCTURE = TagKey.of(RegistryKeys.BIOME, ModernBeta.createId("has_structure/indev_stronghold"));
    
    public ModernBetaBiomeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BIOME, registriesFuture);
    }

    @Override
    protected void configure(WrapperLookup arg) {
        /* Modern Beta Biome Tags */
        
        getOrCreateTagBuilder(IS_BETA).add(
            ModernBetaBiomes.BETA_FOREST_ID,
            ModernBetaBiomes.BETA_SHRUBLAND_ID,
            ModernBetaBiomes.BETA_DESERT_ID,
            ModernBetaBiomes.BETA_SAVANNA_ID,
            ModernBetaBiomes.BETA_PLAINS_ID,
            ModernBetaBiomes.BETA_SEASONAL_FOREST_ID,
            ModernBetaBiomes.BETA_RAINFOREST_ID,
            ModernBetaBiomes.BETA_SWAMPLAND_ID,
            ModernBetaBiomes.BETA_TAIGA_ID,
            ModernBetaBiomes.BETA_TUNDRA_ID,
            ModernBetaBiomes.BETA_ICE_DESERT_ID,
            ModernBetaBiomes.BETA_OCEAN_ID,
            ModernBetaBiomes.BETA_LUKEWARM_OCEAN_ID,
            ModernBetaBiomes.BETA_WARM_OCEAN_ID,
            ModernBetaBiomes.BETA_COLD_OCEAN_ID,
            ModernBetaBiomes.BETA_FROZEN_OCEAN_ID,
            ModernBetaBiomes.BETA_SKY_ID
        );
        
        getOrCreateTagBuilder(IS_PE).add(
            ModernBetaBiomes.PE_FOREST_ID,
            ModernBetaBiomes.PE_SHRUBLAND_ID,
            ModernBetaBiomes.PE_DESERT_ID,
            ModernBetaBiomes.PE_SAVANNA_ID,
            ModernBetaBiomes.PE_PLAINS_ID,
            ModernBetaBiomes.PE_SEASONAL_FOREST_ID,
            ModernBetaBiomes.PE_RAINFOREST_ID,
            ModernBetaBiomes.PE_SWAMPLAND_ID,
            ModernBetaBiomes.PE_TAIGA_ID,
            ModernBetaBiomes.PE_TUNDRA_ID,
            ModernBetaBiomes.PE_ICE_DESERT_ID,
            ModernBetaBiomes.PE_OCEAN_ID,
            ModernBetaBiomes.PE_LUKEWARM_OCEAN_ID,
            ModernBetaBiomes.PE_WARM_OCEAN_ID,
            ModernBetaBiomes.PE_COLD_OCEAN_ID,
            ModernBetaBiomes.PE_FROZEN_OCEAN_ID
        );

        getOrCreateTagBuilder(IS_ALPHA).add(
            ModernBetaBiomes.ALPHA_ID,
            ModernBetaBiomes.ALPHA_WINTER_ID
        );

        getOrCreateTagBuilder(IS_INFDEV).add(
            ModernBetaBiomes.INFDEV_611_ID,
            ModernBetaBiomes.INFDEV_420_ID,
            ModernBetaBiomes.INFDEV_415_ID,
            ModernBetaBiomes.INFDEV_227_ID
        );

        getOrCreateTagBuilder(IS_INDEV).add(
            ModernBetaBiomes.INDEV_NORMAL_ID,
            ModernBetaBiomes.INDEV_HELL_ID,
            ModernBetaBiomes.INDEV_PARADISE_ID,
            ModernBetaBiomes.INDEV_WOODS_ID
        );
        
        getOrCreateTagBuilder(IS_FOREST).add(
            keyOf(ModernBetaBiomes.BETA_FOREST_ID),
            keyOf(ModernBetaBiomes.PE_FOREST_ID)
        );
        
        getOrCreateTagBuilder(IS_SEASONAL_FOREST).add(
            keyOf(ModernBetaBiomes.BETA_SEASONAL_FOREST_ID),
            keyOf(ModernBetaBiomes.PE_SEASONAL_FOREST_ID)
        );
        
        getOrCreateTagBuilder(IS_RAINFOREST).add(
            keyOf(ModernBetaBiomes.BETA_RAINFOREST_ID),
            keyOf(ModernBetaBiomes.PE_RAINFOREST_ID)
        );

        getOrCreateTagBuilder(IS_DESERT).add(
            keyOf(ModernBetaBiomes.BETA_DESERT_ID),
            keyOf(ModernBetaBiomes.PE_DESERT_ID)
        );
        
        getOrCreateTagBuilder(IS_PLAINS).add(
            keyOf(ModernBetaBiomes.BETA_PLAINS_ID),
            keyOf(ModernBetaBiomes.PE_PLAINS_ID)
        );
        
        getOrCreateTagBuilder(IS_SHRUBLAND).add(
            keyOf(ModernBetaBiomes.BETA_SHRUBLAND_ID),
            keyOf(ModernBetaBiomes.PE_SHRUBLAND_ID)
        );
        
        getOrCreateTagBuilder(IS_SAVANNA).add(
            keyOf(ModernBetaBiomes.BETA_SAVANNA_ID),
            keyOf(ModernBetaBiomes.PE_SAVANNA_ID)
        );
        
        getOrCreateTagBuilder(IS_SWAMP).add(
            keyOf(ModernBetaBiomes.BETA_SWAMPLAND_ID),
            keyOf(ModernBetaBiomes.PE_SWAMPLAND_ID)
        );
        
        getOrCreateTagBuilder(IS_TAIGA).add(
            keyOf(ModernBetaBiomes.BETA_TAIGA_ID),
            keyOf(ModernBetaBiomes.PE_TAIGA_ID)
        );
        
        getOrCreateTagBuilder(IS_TUNDRA).add(
            keyOf(ModernBetaBiomes.BETA_TUNDRA_ID),
            keyOf(ModernBetaBiomes.PE_TUNDRA_ID),
            keyOf(ModernBetaBiomes.BETA_ICE_DESERT_ID),
            keyOf(ModernBetaBiomes.PE_ICE_DESERT_ID)
        );
        
        getOrCreateTagBuilder(IS_OCEAN).add(
            keyOf(ModernBetaBiomes.BETA_OCEAN_ID),
            keyOf(ModernBetaBiomes.BETA_LUKEWARM_OCEAN_ID),
            keyOf(ModernBetaBiomes.BETA_WARM_OCEAN_ID),
            keyOf(ModernBetaBiomes.BETA_COLD_OCEAN_ID),
            keyOf(ModernBetaBiomes.BETA_FROZEN_OCEAN_ID),

            keyOf(ModernBetaBiomes.PE_OCEAN_ID),
            keyOf(ModernBetaBiomes.PE_LUKEWARM_OCEAN_ID),
            keyOf(ModernBetaBiomes.PE_WARM_OCEAN_ID),
            keyOf(ModernBetaBiomes.PE_COLD_OCEAN_ID),
            keyOf(ModernBetaBiomes.PE_FROZEN_OCEAN_ID)
        );
        
        /* Vanilla Biome Tags */
        
        getOrCreateTagBuilder(BiomeTags.IS_DEEP_OCEAN)
            .addTag(IS_OCEAN);
        
        getOrCreateTagBuilder(BiomeTags.IS_FOREST)
            .addTag(IS_FOREST)
            .addTag(IS_SEASONAL_FOREST)
            .addTag(IS_ALPHA)
            .addTag(IS_INFDEV)
            .addTag(IS_INDEV);
        
        getOrCreateTagBuilder(BiomeTags.IS_JUNGLE)
            .addTag(IS_RAINFOREST);
        
        getOrCreateTagBuilder(BiomeTags.IS_OCEAN)
            .addTag(IS_OCEAN);
        
        getOrCreateTagBuilder(BiomeTags.IS_TAIGA)
            .addTag(IS_TAIGA);
        
        /* Modern Beta Biome Structure Tags */
        
        getOrCreateTagBuilder(INDEV_STRONGHOLD_HAS_STRUCTURE)
            .addTag(IS_INDEV);
        
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
            keyOf(ModernBetaBiomes.BETA_OCEAN_ID),
            keyOf(ModernBetaBiomes.BETA_COLD_OCEAN_ID),
            keyOf(ModernBetaBiomes.BETA_FROZEN_OCEAN_ID),

            keyOf(ModernBetaBiomes.PE_OCEAN_ID),
            keyOf(ModernBetaBiomes.PE_COLD_OCEAN_ID),
            keyOf(ModernBetaBiomes.PE_FROZEN_OCEAN_ID)
        );
        
        getOrCreateTagBuilder(BiomeTags.OCEAN_RUIN_WARM_HAS_STRUCTURE).add(
            keyOf(ModernBetaBiomes.BETA_LUKEWARM_OCEAN_ID),
            keyOf(ModernBetaBiomes.BETA_WARM_OCEAN_ID),

            keyOf(ModernBetaBiomes.PE_LUKEWARM_OCEAN_ID),
            keyOf(ModernBetaBiomes.PE_WARM_OCEAN_ID)
        );
        
        getOrCreateTagBuilder(BiomeTags.PILLAGER_OUTPOST_HAS_STRUCTURE)
            .addTag(IS_DESERT)
            .addTag(IS_PLAINS)
            .addTag(IS_SAVANNA)
            .addTag(IS_SWAMP)
            .addTag(IS_TUNDRA)
            .add(ModernBetaBiomes.BETA_SKY_ID);
        
        getOrCreateTagBuilder(BiomeTags.RUINED_PORTAL_DESERT_HAS_STRUCTURE)
            .addTag(IS_DESERT);
        
        getOrCreateTagBuilder(BiomeTags.RUINED_PORTAL_STANDARD_HAS_STRUCTURE)
            .addTag(IS_PLAINS)
            .addTag(IS_SAVANNA)
            .addTag(IS_TUNDRA)
            .add(ModernBetaBiomes.BETA_SKY_ID);
        
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
    }
    
    private static RegistryKey<Biome> keyOf(Identifier id) {
        return RegistryKey.of(RegistryKeys.BIOME, id);
    }
}