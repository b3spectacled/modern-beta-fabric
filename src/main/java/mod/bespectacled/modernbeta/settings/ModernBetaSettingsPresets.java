package mod.bespectacled.modernbeta.settings;

import java.util.List;
import java.util.Map;

import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomes;
import mod.bespectacled.modernbeta.world.biome.provider.climate.ClimateMapping;
import mod.bespectacled.modernbeta.world.biome.voronoi.VoronoiPointBiome;
import mod.bespectacled.modernbeta.world.biome.voronoi.VoronoiPointCaveBiome;
import mod.bespectacled.modernbeta.world.chunk.provider.indev.IndevTheme;
import mod.bespectacled.modernbeta.world.chunk.provider.indev.IndevType;
import mod.bespectacled.modernbeta.world.chunk.provider.island.IslandShape;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.biome.BiomeKeys;

public class ModernBetaSettingsPresets {
    public static final ModernBetaSettingsPreset PRESET_BETA = presetBeta();
    public static final ModernBetaSettingsPreset PRESET_ALPHA = presetAlpha();
    public static final ModernBetaSettingsPreset PRESET_SKYLANDS = presetSkylands();
    public static final ModernBetaSettingsPreset PRESET_INFDEV_415 = presetInfdev415();
    public static final ModernBetaSettingsPreset PRESET_INFDEV_420 = presetInfdev420();
    public static final ModernBetaSettingsPreset PRESET_INFDEV_611 = presetInfdev611();
    public static final ModernBetaSettingsPreset PRESET_INFDEV_227 = presetInfdev227();
    public static final ModernBetaSettingsPreset PRESET_INDEV = presetIndev();
    public static final ModernBetaSettingsPreset PRESET_CLASSIC = presetClassic();
    public static final ModernBetaSettingsPreset PRESET_PE = presetPE();
    public static final ModernBetaSettingsPreset PRESET_BETA_1_8_1 = presetBeta181();
    public static final ModernBetaSettingsPreset PRESET_RELEASE_1_0_0 = preset100();
    public static final ModernBetaSettingsPreset PRESET_RELEASE_1_1 = preset11();
    public static final ModernBetaSettingsPreset PRESET_RELEASE_1_2_5 = preset125();
    public static final ModernBetaSettingsPreset PRESET_RELEASE_1_6_4 = preset164();

    public static final ModernBetaSettingsPreset PRESET_BETA_SKYLANDS = presetBetaSkylands();
    public static final ModernBetaSettingsPreset PRESET_BETA_ISLES = presetBetaIsles();
    public static final ModernBetaSettingsPreset PRESET_BETA_ISLE_LAND = presetBetaIsleLand();
    public static final ModernBetaSettingsPreset PRESET_BETA_CAVE_DELIGHT = presetBetaCaveDelight();
    public static final ModernBetaSettingsPreset PRESET_BETA_CAVE_CHAOS = presetBetaCaveChaos();
    public static final ModernBetaSettingsPreset PRESET_BETA_LARGE_BIOMES = presetBetaLargeBiomes();
    public static final ModernBetaSettingsPreset PRESET_BETA_XBOX_LEGACY = presetBetaXboxLegacy();
    public static final ModernBetaSettingsPreset PRESET_BETA_SURVIVAL_ISLAND = presetBetaSurvivalIsland();
    public static final ModernBetaSettingsPreset PRESET_BETA_VANILLA = presetBetaVanilla();
    public static final ModernBetaSettingsPreset PRESET_BETA_HYBRID = presetBetaHybrid();
    public static final ModernBetaSettingsPreset PRESET_RELEASE_HYBRID = presetReleaseHybrid();
    public static final ModernBetaSettingsPreset PRESET_ALPHA_WINTER = presetAlphaWinter();
    public static final ModernBetaSettingsPreset PRESET_INDEV_PARADISE = presetIndevParadise();
    public static final ModernBetaSettingsPreset PRESET_INDEV_WOODS = presetIndevWoods();
    public static final ModernBetaSettingsPreset PRESET_INDEV_HELL = presetIndevHell();

    private static ModernBetaSettingsPreset presetBeta() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();
        
        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.BETA.id;
        settingsChunk.useDeepslate = true;
        settingsChunk.deepslateMinY = 0;
        settingsChunk.deepslateMaxY = 8;
        settingsChunk.deepslateBlock = "minecraft:deepslate";
        settingsChunk.noiseCoordinateScale = 684.412f;
        settingsChunk.noiseHeightScale = 684.412f;
        settingsChunk.noiseUpperLimitScale = 512f;
        settingsChunk.noiseLowerLimitScale = 512f;
        settingsChunk.noiseDepthNoiseScaleX = 200;
        settingsChunk.noiseDepthNoiseScaleZ = 200;
        settingsChunk.noiseMainNoiseScaleX = 80f;
        settingsChunk.noiseMainNoiseScaleY = 160f;
        settingsChunk.noiseMainNoiseScaleZ = 80f;
        settingsChunk.noiseBaseSize = 8.5f;
        settingsChunk.noiseStretchY = 12.0f;
        settingsChunk.noiseTopSlideTarget = -10;
        settingsChunk.noiseTopSlideSize = 3;
        settingsChunk.noiseTopSlideOffset = 0;
        settingsChunk.noiseBottomSlideTarget = 15;
        settingsChunk.noiseBottomSlideSize = 3;
        settingsChunk.noiseBottomSlideOffset = 0;
        
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.BETA.id;
        settingsBiome.climateTempNoiseScale = 0.025f;
        settingsBiome.climateRainNoiseScale = 0.05f;
        settingsBiome.climateDetailNoiseScale = 0.25f;
        settingsBiome.climateMappings = ModernBetaSettingsBiome.Builder.createClimateMapping(
            new ClimateMapping(
                ModernBetaBiomes.BETA_DESERT.getValue().toString(),
                ModernBetaBiomes.BETA_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.BETA_FOREST.getValue().toString(),
                ModernBetaBiomes.BETA_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.BETA_TUNDRA.getValue().toString(),
                ModernBetaBiomes.BETA_FROZEN_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.BETA_PLAINS.getValue().toString(),
                ModernBetaBiomes.BETA_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.BETA_RAINFOREST.getValue().toString(),
                ModernBetaBiomes.BETA_WARM_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.BETA_SAVANNA.getValue().toString(),
                ModernBetaBiomes.BETA_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.BETA_SHRUBLAND.getValue().toString(),
                ModernBetaBiomes.BETA_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.BETA_SEASONAL_FOREST.getValue().toString(),
                ModernBetaBiomes.BETA_LUKEWARM_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.BETA_SWAMPLAND.getValue().toString(),
                ModernBetaBiomes.BETA_COLD_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.BETA_TAIGA.getValue().toString(),
                ModernBetaBiomes.BETA_FROZEN_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.BETA_TUNDRA.getValue().toString(),
                ModernBetaBiomes.BETA_FROZEN_OCEAN.getValue().toString()
            )
        );
        
        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.VORONOI.id;
        settingsCaveBiome.voronoiHorizontalNoiseScale = 32.0f;
        settingsCaveBiome.voronoiVerticalNoiseScale = 16.0f;
        settingsCaveBiome.voronoiDepthMinY = -64;
        settingsCaveBiome.voronoiDepthMaxY = 64;
        settingsCaveBiome.voronoiPoints = List.of(
            new VoronoiPointCaveBiome("", 0.0, 0.5, 0.75),
            new VoronoiPointCaveBiome("minecraft:lush_caves", 0.1, 0.5, 0.75),
            new VoronoiPointCaveBiome("", 0.5, 0.5, 0.75),
            new VoronoiPointCaveBiome("minecraft:dripstone_caves", 0.9, 0.5, 0.75),
            new VoronoiPointCaveBiome("", 1.0, 0.5, 0.75),

            new VoronoiPointCaveBiome("", 0.0, 0.5, 0.25),
            new VoronoiPointCaveBiome("minecraft:lush_caves", 0.2, 0.5, 0.25),
            new VoronoiPointCaveBiome("", 0.4, 0.5, 0.25),
            new VoronoiPointCaveBiome("minecraft:deep_dark", 0.5, 0.5, 0.25),
            new VoronoiPointCaveBiome("", 0.6, 0.5, 0.25),
            new VoronoiPointCaveBiome("minecraft:dripstone_caves", 0.8, 0.5, 0.25),
            new VoronoiPointCaveBiome("", 1.0, 0.5, 0.25)
        );
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetAlpha() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();
        
        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.ALPHA.id;
        settingsChunk.useDeepslate = false;
        settingsChunk.noiseCoordinateScale = 684.412f;
        settingsChunk.noiseHeightScale = 684.412f;
        settingsChunk.noiseUpperLimitScale = 512f;
        settingsChunk.noiseLowerLimitScale = 512f;
        settingsChunk.noiseDepthNoiseScaleX = 100;
        settingsChunk.noiseDepthNoiseScaleZ = 100;
        settingsChunk.noiseMainNoiseScaleX = 80f;
        settingsChunk.noiseMainNoiseScaleY = 160f;
        settingsChunk.noiseMainNoiseScaleZ = 80f;
        settingsChunk.noiseBaseSize = 8.5f;
        settingsChunk.noiseStretchY = 12.0f;
        settingsChunk.noiseTopSlideTarget = -10;
        settingsChunk.noiseTopSlideSize = 3;
        settingsChunk.noiseTopSlideOffset = 0;
        settingsChunk.noiseBottomSlideTarget = 15;
        settingsChunk.noiseBottomSlideSize = 3;
        settingsChunk.noiseBottomSlideOffset = 0;
        
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.SINGLE.id;
        settingsBiome.singleBiome = ModernBetaBiomes.ALPHA.getValue().toString();
        
        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetSkylands() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();
        
        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.SKYLANDS.id;
        settingsChunk.useDeepslate = false;
        settingsChunk.noiseCoordinateScale = 1368.824f;
        settingsChunk.noiseHeightScale = 684.412f;
        settingsChunk.noiseUpperLimitScale = 512f;
        settingsChunk.noiseLowerLimitScale = 512f;
        settingsChunk.noiseDepthNoiseScaleX = 100;
        settingsChunk.noiseDepthNoiseScaleZ = 100;
        settingsChunk.noiseMainNoiseScaleX = 80f;
        settingsChunk.noiseMainNoiseScaleY = 160f;
        settingsChunk.noiseMainNoiseScaleZ = 80f;
        settingsChunk.noiseBaseSize = 8.5f;
        settingsChunk.noiseStretchY = 12.0f;
        settingsChunk.noiseTopSlideTarget = -30;
        settingsChunk.noiseTopSlideSize = 31;
        settingsChunk.noiseTopSlideOffset = 0;
        settingsChunk.noiseBottomSlideTarget = -30;
        settingsChunk.noiseBottomSlideSize = 7;
        settingsChunk.noiseBottomSlideOffset = 1;
        
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.SINGLE.id;
        settingsBiome.singleBiome = ModernBetaBiomes.BETA_SKY.getValue().toString();
        settingsBiome.useOceanBiomes = false;
        
        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
         );
    }
    
    private static ModernBetaSettingsPreset presetInfdev415() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();
        
        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.INFDEV_415.id;
        settingsChunk.useDeepslate = false;
        settingsChunk.useCaves = false;
        settingsChunk.noiseCoordinateScale = 684.412f;
        settingsChunk.noiseHeightScale = 984.412f;
        settingsChunk.noiseUpperLimitScale = 512f;
        settingsChunk.noiseLowerLimitScale = 512f;
        settingsChunk.noiseMainNoiseScaleX = 80f;
        settingsChunk.noiseMainNoiseScaleY = 400f;
        settingsChunk.noiseMainNoiseScaleZ = 80f;
        settingsChunk.noiseTopSlideTarget = 0;
        settingsChunk.noiseTopSlideSize = 0;
        settingsChunk.noiseTopSlideOffset = 0;
        settingsChunk.noiseBottomSlideTarget = 0;
        settingsChunk.noiseBottomSlideSize = 0;
        settingsChunk.noiseBottomSlideOffset = 0;
        
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.SINGLE.id;
        settingsBiome.singleBiome = ModernBetaBiomes.INFDEV_415.getValue().toString();
        
        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
         );
    }
    
    private static ModernBetaSettingsPreset presetInfdev420() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();
        
        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.INFDEV_420.id;
        settingsChunk.useDeepslate = false;
        settingsChunk.noiseCoordinateScale = 684.412f;
        settingsChunk.noiseHeightScale = 684.412f;
        settingsChunk.noiseUpperLimitScale = 512f;
        settingsChunk.noiseLowerLimitScale = 512f;
        settingsChunk.noiseMainNoiseScaleX = 80f;
        settingsChunk.noiseMainNoiseScaleY = 160f;
        settingsChunk.noiseMainNoiseScaleZ = 80f;
        settingsChunk.noiseBaseSize = 8.5f;
        settingsChunk.noiseStretchY = 12.0f;
        settingsChunk.noiseTopSlideTarget = 0;
        settingsChunk.noiseTopSlideSize = 0;
        settingsChunk.noiseTopSlideOffset = 0;
        settingsChunk.noiseBottomSlideTarget = 0;
        settingsChunk.noiseBottomSlideSize = 0;
        settingsChunk.noiseBottomSlideOffset = 0;
        
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.SINGLE.id;
        settingsBiome.singleBiome = ModernBetaBiomes.INFDEV_420.getValue().toString();
        
        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
         );
    }
    
    private static ModernBetaSettingsPreset presetInfdev611() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();
        
        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.INFDEV_611.id;
        settingsChunk.useDeepslate = false;
        settingsChunk.noiseCoordinateScale = 684.412f;
        settingsChunk.noiseHeightScale = 684.412f;
        settingsChunk.noiseUpperLimitScale = 512f;
        settingsChunk.noiseLowerLimitScale = 512f;
        settingsChunk.noiseDepthNoiseScaleX = 100;
        settingsChunk.noiseDepthNoiseScaleZ = 100;
        settingsChunk.noiseMainNoiseScaleX = 80f;
        settingsChunk.noiseMainNoiseScaleY = 160f;
        settingsChunk.noiseMainNoiseScaleZ = 80f;
        settingsChunk.noiseBaseSize = 8.5f;
        settingsChunk.noiseStretchY = 12.0f;
        settingsChunk.noiseTopSlideTarget = -10;
        settingsChunk.noiseTopSlideSize = 3;
        settingsChunk.noiseTopSlideOffset = 0;
        settingsChunk.noiseBottomSlideTarget = 15;
        settingsChunk.noiseBottomSlideSize = 3;
        settingsChunk.noiseBottomSlideOffset = 0;
        
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.SINGLE.id;
        settingsBiome.singleBiome = ModernBetaBiomes.INFDEV_611.getValue().toString();
        
        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetInfdev227() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();
        
        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.INFDEV_227.id;
        settingsChunk.useDeepslate = false;
        settingsChunk.useCaves = false;
        settingsChunk.infdevUsePyramid = true;
        settingsChunk.infdevUseWall = true;
        
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.SINGLE.id;
        settingsBiome.singleBiome = ModernBetaBiomes.INFDEV_227.getValue().toString();
        
        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetIndev() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();
        
        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.INDEV.id;
        settingsChunk.useDeepslate = false;
        settingsChunk.useCaves = false;
        settingsChunk.indevLevelTheme = IndevTheme.NORMAL.getId();
        settingsChunk.indevLevelType = IndevType.ISLAND.getId();
        settingsChunk.indevLevelWidth = 256;
        settingsChunk.indevLevelLength = 256;
        settingsChunk.indevLevelHeight = 128;
        settingsChunk.indevCaveRadius = 1.0f;
        settingsChunk.indevUseCaves = true;
        
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.SINGLE.id;
        settingsBiome.singleBiome = ModernBetaBiomes.INDEV_NORMAL.getValue().toString();
        
        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetClassic() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();
        
        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.CLASSIC_0_30.id;
        settingsChunk.useDeepslate = false;
        settingsChunk.useCaves = false;
        settingsChunk.indevLevelWidth = 256;
        settingsChunk.indevLevelLength = 256;
        settingsChunk.indevLevelHeight = 128;
        settingsChunk.indevCaveRadius = 1.0f;
        settingsChunk.indevUseCaves = true;
        
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.SINGLE.id;
        settingsBiome.singleBiome = ModernBetaBiomes.INDEV_NORMAL.getValue().toString();
        
        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetPE() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();
        
        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.PE.id;
        settingsChunk.useDeepslate = false;
        settingsChunk.useCaves = false;
        settingsChunk.noiseCoordinateScale = 684.412f;
        settingsChunk.noiseHeightScale = 684.412f;
        settingsChunk.noiseUpperLimitScale = 512f;
        settingsChunk.noiseLowerLimitScale = 512f;
        settingsChunk.noiseDepthNoiseScaleX = 200;
        settingsChunk.noiseDepthNoiseScaleZ = 200;
        settingsChunk.noiseMainNoiseScaleX = 80f;
        settingsChunk.noiseMainNoiseScaleY = 160f;
        settingsChunk.noiseMainNoiseScaleZ = 80f;
        settingsChunk.noiseBaseSize = 8.5f;
        settingsChunk.noiseStretchY = 12.0f;
        settingsChunk.noiseTopSlideTarget = -10;
        settingsChunk.noiseTopSlideSize = 3;
        settingsChunk.noiseTopSlideOffset = 0;
        settingsChunk.noiseBottomSlideTarget = 15;
        settingsChunk.noiseBottomSlideSize = 3;
        settingsChunk.noiseBottomSlideOffset = 0;
        
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.PE.id;
        settingsBiome.climateTempNoiseScale = 0.025f;
        settingsBiome.climateRainNoiseScale = 0.05f;
        settingsBiome.climateDetailNoiseScale = 0.25f;
        settingsBiome.climateMappings = ModernBetaSettingsBiome.Builder.createClimateMapping(
            new ClimateMapping(
                ModernBetaBiomes.PE_DESERT.getValue().toString(),
                ModernBetaBiomes.PE_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.PE_FOREST.getValue().toString(),
                ModernBetaBiomes.PE_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.PE_TUNDRA.getValue().toString(),
                ModernBetaBiomes.PE_FROZEN_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.PE_PLAINS.getValue().toString(),
                ModernBetaBiomes.PE_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.PE_RAINFOREST.getValue().toString(),
                ModernBetaBiomes.PE_WARM_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.PE_SAVANNA.getValue().toString(),
                ModernBetaBiomes.PE_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.PE_SHRUBLAND.getValue().toString(),
                ModernBetaBiomes.PE_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.PE_SEASONAL_FOREST.getValue().toString(),
                ModernBetaBiomes.PE_LUKEWARM_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.PE_SWAMPLAND.getValue().toString(),
                ModernBetaBiomes.PE_COLD_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.PE_TAIGA.getValue().toString(),
                ModernBetaBiomes.PE_FROZEN_OCEAN.getValue().toString()
            ),
            new ClimateMapping(
                ModernBetaBiomes.PE_TUNDRA.getValue().toString(),
                ModernBetaBiomes.PE_FROZEN_OCEAN.getValue().toString()
            )
        );
        
        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetBetaSkylands() {
        ModernBetaSettingsPreset initial = presetSkylands();
        
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        NbtCompound compoundBiome = initial.settingsBiome().toCompound();
        NbtCompound compoundCaveBiome = initial.settingsCaveBiome().toCompound();
        
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);
        
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.BETA.id;
        settingsBiome.useOceanBiomes = false;
        
        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.VORONOI.id;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetBetaIsles() {
        ModernBetaSettingsPreset initial = presetBeta();
        
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        NbtCompound compoundBiome = initial.settingsBiome().toCompound();
        NbtCompound compoundCaveBiome = initial.settingsCaveBiome().toCompound();
        
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);
        
        settingsChunk.islesUseIslands = true;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetBetaIsleLand() {
        ModernBetaSettingsPreset initial = presetBeta();
        
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        NbtCompound compoundBiome = initial.settingsBiome().toCompound();
        NbtCompound compoundCaveBiome = initial.settingsCaveBiome().toCompound();
        
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);
        
        settingsChunk.noiseCoordinateScale = 3000.0f;
        settingsChunk.noiseHeightScale = 6000.0f;
        settingsChunk.noiseStretchY = 10.0f;
        settingsChunk.noiseUpperLimitScale = 250.0f;
        settingsChunk.noiseLowerLimitScale = 512.0f;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    

    private static ModernBetaSettingsPreset presetBetaCaveDelight() {
        ModernBetaSettingsPreset initial = presetBeta();
        
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        NbtCompound compoundBiome = initial.settingsBiome().toCompound();
        NbtCompound compoundCaveBiome = initial.settingsCaveBiome().toCompound();
        
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);
        
        settingsChunk.noiseMainNoiseScaleX = 5000.0f;
        settingsChunk.noiseMainNoiseScaleY = 1000.0f;
        settingsChunk.noiseMainNoiseScaleZ = 5000.0f;
        settingsChunk.noiseStretchY = 5.0f;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetBetaCaveChaos() {
        ModernBetaSettingsPreset initial = presetBeta();
        
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        NbtCompound compoundBiome = initial.settingsBiome().toCompound();
        NbtCompound compoundCaveBiome = initial.settingsCaveBiome().toCompound();
        
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);
        
        settingsChunk.noiseUpperLimitScale = 2.0f;
        settingsChunk.noiseLowerLimitScale = 64.0f;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetBetaLargeBiomes() {
        ModernBetaSettingsPreset initial = presetBeta();
        
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        NbtCompound compoundBiome = initial.settingsBiome().toCompound();
        NbtCompound compoundCaveBiome = initial.settingsCaveBiome().toCompound();
        
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);
        
        settingsBiome.climateTempNoiseScale = 0.025f / 4.0f;
        settingsBiome.climateRainNoiseScale = 0.05f / 4.0f;
        settingsBiome.climateDetailNoiseScale = 0.25f / 2.0f;
        
        settingsCaveBiome.voronoiHorizontalNoiseScale = 32.0f * 4.0f;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetBetaXboxLegacy() {
        ModernBetaSettingsPreset initial = presetBeta();
        
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        NbtCompound compoundBiome = initial.settingsBiome().toCompound();
        NbtCompound compoundCaveBiome = initial.settingsCaveBiome().toCompound();
        
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);
        
        settingsChunk.islesUseIslands = true;
        settingsChunk.islesUseOuterIslands = false;
        settingsChunk.islesCenterIslandShape = IslandShape.SQUARE.getId();
        settingsChunk.islesCenterIslandRadius = 25;
        settingsChunk.islesCenterIslandFalloffDistance = 2;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetBetaSurvivalIsland() {
        ModernBetaSettingsPreset initial = presetBeta();
        
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        NbtCompound compoundBiome = initial.settingsBiome().toCompound();
        NbtCompound compoundCaveBiome = initial.settingsCaveBiome().toCompound();
        
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);
        
        settingsChunk.islesUseIslands = true;
        settingsChunk.islesUseOuterIslands = false;
        settingsChunk.islesCenterIslandRadius = 1;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetBetaVanilla() {
        ModernBetaSettingsPreset initial = presetBeta();
        
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        NbtCompound compoundBiome = initial.settingsBiome().toCompound();
        NbtCompound compoundCaveBiome = initial.settingsCaveBiome().toCompound();
        
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);
        
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.VORONOI.id;
        settingsBiome.climateTempNoiseScale = 0.025f / 3.0f;
        settingsBiome.climateRainNoiseScale = 0.05f / 3.0f;
        settingsBiome.climateDetailNoiseScale = 0.25f / 1.5f;
        settingsBiome.voronoiPoints = List.of(
                // Standard Biomes

               new VoronoiPointBiome(
                   BiomeKeys.DESERT.getValue().toString(),
                   BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                   0.9, 0.1, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.PLAINS.getValue().toString(),
                   BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                   0.9, 0.3, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.FOREST.getValue().toString(),
                   BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                   0.9, 0.5, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.FOREST.getValue().toString(),
                   BiomeKeys.WARM_OCEAN.getValue().toString(),
                   BiomeKeys.WARM_OCEAN.getValue().toString(),
                   0.9, 0.7, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.JUNGLE.getValue().toString(),
                   BiomeKeys.WARM_OCEAN.getValue().toString(),
                   BiomeKeys.WARM_OCEAN.getValue().toString(),
                   0.9, 0.9, 0.5
               ),

               new VoronoiPointBiome(
                   BiomeKeys.SAVANNA.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.1, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.PLAINS.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.3, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.FOREST.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.5, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.FOREST.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.7, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.FOREST.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.9, 0.5
               ),

               new VoronoiPointBiome(
                   BiomeKeys.PLAINS.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.1, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.PLAINS.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.3, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.BIRCH_FOREST.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.5, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.BIRCH_FOREST.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.7, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SWAMP.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.9, 0.5
               ),

               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.3, 0.1, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.TAIGA.getValue().toString(),
                   BiomeKeys.COLD_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_COLD_OCEAN.getValue().toString(),
                   0.3, 0.3, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.TAIGA.getValue().toString(),
                   BiomeKeys.COLD_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_COLD_OCEAN.getValue().toString(),
                   0.3, 0.5, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_TAIGA.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.3, 0.7, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_TAIGA.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.3, 0.9, 0.5
               ),

               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.1, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.3, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.5, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.7, 0.5
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.9, 0.5
               ),
               
               // Mutated Biomes

               new VoronoiPointBiome(
                   BiomeKeys.DESERT.getValue().toString(),
                   BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                   0.9, 0.1, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SUNFLOWER_PLAINS.getValue().toString(),
                   BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                   0.9, 0.3, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.DARK_FOREST.getValue().toString(),
                   BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                   0.9, 0.5, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.DARK_FOREST.getValue().toString(),
                   BiomeKeys.WARM_OCEAN.getValue().toString(),
                   BiomeKeys.WARM_OCEAN.getValue().toString(),
                   0.9, 0.7, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.BAMBOO_JUNGLE.getValue().toString(),
                   BiomeKeys.WARM_OCEAN.getValue().toString(),
                   BiomeKeys.WARM_OCEAN.getValue().toString(),
                   0.9, 0.9, 0.2
               ),

               new VoronoiPointBiome(
                   BiomeKeys.SAVANNA.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.1, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.MEADOW.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.3, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.FLOWER_FOREST.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.5, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.FLOWER_FOREST.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.7, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.FLOWER_FOREST.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.9, 0.2
               ),

               new VoronoiPointBiome(
                   BiomeKeys.MEADOW.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.1, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.MEADOW.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.3, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.CHERRY_GROVE.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.5, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.CHERRY_GROVE.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.7, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.MANGROVE_SWAMP.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.9, 0.2
               ),

               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.3, 0.1, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.OLD_GROWTH_PINE_TAIGA.getValue().toString(),
                   BiomeKeys.COLD_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_COLD_OCEAN.getValue().toString(),
                   0.3, 0.3, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.OLD_GROWTH_PINE_TAIGA.getValue().toString(),
                   BiomeKeys.COLD_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_COLD_OCEAN.getValue().toString(),
                   0.3, 0.5, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.GROVE.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.3, 0.7, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.GROVE.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.3, 0.9, 0.2
               ),

               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.1, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.3, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.5, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_SLOPES.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.7, 0.2
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_SLOPES.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.9, 0.2
               ),
               
               // Mutated Biomes 2

               new VoronoiPointBiome(
                   BiomeKeys.BADLANDS.getValue().toString(),
                   BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                   0.9, 0.1, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.PLAINS.getValue().toString(),
                   BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                   0.9, 0.3, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SPARSE_JUNGLE.getValue().toString(),
                   BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                   0.9, 0.5, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SPARSE_JUNGLE.getValue().toString(),
                   BiomeKeys.WARM_OCEAN.getValue().toString(),
                   BiomeKeys.WARM_OCEAN.getValue().toString(),
                   0.9, 0.7, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.MUSHROOM_FIELDS.getValue().toString(),
                   BiomeKeys.WARM_OCEAN.getValue().toString(),
                   BiomeKeys.WARM_OCEAN.getValue().toString(),
                   0.9, 0.9, 0.8
               ),

               new VoronoiPointBiome(
                   BiomeKeys.SAVANNA.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.1, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.PLAINS.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.3, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.DARK_FOREST.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.5, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.DARK_FOREST.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.7, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.DARK_FOREST.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.7, 0.9, 0.8
               ),

               new VoronoiPointBiome(
                   BiomeKeys.PLAINS.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.1, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.PLAINS.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.3, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.OLD_GROWTH_BIRCH_FOREST.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.5, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.OLD_GROWTH_BIRCH_FOREST.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.7, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.MANGROVE_SWAMP.getValue().toString(),
                   BiomeKeys.OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_OCEAN.getValue().toString(),
                   0.5, 0.9, 0.8
               ),

               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.3, 0.1, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA.getValue().toString(),
                   BiomeKeys.COLD_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_COLD_OCEAN.getValue().toString(),
                   0.3, 0.3, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA.getValue().toString(),
                   BiomeKeys.COLD_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_COLD_OCEAN.getValue().toString(),
                   0.3, 0.5, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.GROVE.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.3, 0.7, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.GROVE.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.3, 0.9, 0.8
               ),

               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.1, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.3, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.5, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.ICE_SPIKES.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.7, 0.8
               ),
               new VoronoiPointBiome(
                   BiomeKeys.ICE_SPIKES.getValue().toString(),
                   BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                   BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                   0.1, 0.9, 0.8
               )
           );
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetAlphaWinter() {
        ModernBetaSettingsPreset initial = presetAlpha();
        
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        NbtCompound compoundBiome = initial.settingsBiome().toCompound();
        NbtCompound compoundCaveBiome = initial.settingsCaveBiome().toCompound();
        
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);
        
        settingsBiome.singleBiome = ModernBetaBiomes.ALPHA_WINTER.getValue().toString();
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetIndevParadise() {
        ModernBetaSettingsPreset initial = presetIndev();
        
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        NbtCompound compoundBiome = initial.settingsBiome().toCompound();
        NbtCompound compoundCaveBiome = initial.settingsCaveBiome().toCompound();
        
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);
        
        settingsChunk.indevLevelTheme = IndevTheme.PARADISE.getId();
        
        settingsBiome.singleBiome = ModernBetaBiomes.INDEV_PARADISE.getValue().toString();
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetIndevWoods() {
        ModernBetaSettingsPreset initial = presetIndev();
        
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        NbtCompound compoundBiome = initial.settingsBiome().toCompound();
        NbtCompound compoundCaveBiome = initial.settingsCaveBiome().toCompound();
        
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);
        
        settingsChunk.indevLevelTheme = IndevTheme.WOODS.getId();
        
        settingsBiome.singleBiome = ModernBetaBiomes.INDEV_WOODS.getValue().toString();
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetIndevHell() {
        ModernBetaSettingsPreset initial = presetIndev();
        
        NbtCompound compoundChunk = initial.settingsChunk().toCompound();
        NbtCompound compoundBiome = initial.settingsBiome().toCompound();
        NbtCompound compoundCaveBiome = initial.settingsCaveBiome().toCompound();
        
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);
        
        settingsChunk.indevLevelTheme = IndevTheme.HELL.getId();
        
        settingsBiome.singleBiome = ModernBetaBiomes.INDEV_HELL.getValue().toString();
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }

    private static ModernBetaSettingsPreset presetBeta181() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.EARLY_RELEASE.id;
        settingsChunk.releaseExtraHillHeight = false;
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.FRACTAL.id;
        settingsBiome.fractalBiomes = List.of(
            "minecraft:desert",
            "minecraft:forest",
            "modern_beta:late_beta_extreme_hills",
            "modern_beta:late_beta_swampland",
            "modern_beta:late_beta_plains",
            "modern_beta:late_beta_taiga"
        );

        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }

    private static ModernBetaSettingsPreset preset100() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.EARLY_RELEASE.id;
        settingsChunk.releaseExtraHillHeight = false;
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.FRACTAL.id;
        settingsBiome.fractalBiomes = List.of(
            "minecraft:desert",
            "minecraft:forest",
            "modern_beta:late_beta_extreme_hills",
            "modern_beta:early_release_swampland",
            "modern_beta:late_beta_plains",
            "modern_beta:late_beta_taiga"
        );
        settingsBiome.fractalLargerIslands = false;
        settingsBiome.fractalAddSnow = true;
        settingsBiome.fractalAddMushroomIslands = true;

        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }

    private static ModernBetaSettingsPreset preset11() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.EARLY_RELEASE.id;
        settingsChunk.releaseExtraHillHeight = false;
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.FRACTAL.id;
        settingsBiome.fractalBiomes = List.of(
            "minecraft:desert",
            "minecraft:forest",
            "modern_beta:early_release_extreme_hills",
            "modern_beta:early_release_swampland",
            "modern_beta:late_beta_plains",
            "modern_beta:early_release_taiga"
        );
        settingsBiome.fractalLargerIslands = false;
        settingsBiome.fractalAddSnow = true;
        settingsBiome.fractalAddMushroomIslands = true;
        settingsBiome.fractalAddBeaches = true;
        settingsBiome.fractalAddHills = true;
        settingsBiome.fractalAddSwampRivers = true;

        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }

    private static ModernBetaSettingsPreset preset125() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.EARLY_RELEASE.id;
        settingsChunk.releaseExtraHillHeight = false;
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.FRACTAL.id;
        settingsBiome.fractalBiomes = List.of(
            "minecraft:desert",
            "minecraft:forest",
            "modern_beta:early_release_extreme_hills",
            "modern_beta:early_release_swampland",
            "modern_beta:late_beta_plains",
            "modern_beta:early_release_taiga",
            "minecraft:jungle"
        );
        settingsBiome.fractalLargerIslands = false;
        settingsBiome.fractalAddSnow = true;
        settingsBiome.fractalAddMushroomIslands = true;
        settingsBiome.fractalAddBeaches = true;
        settingsBiome.fractalAddHills = true;
        settingsBiome.fractalAddSwampRivers = true;

        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }

    private static ModernBetaSettingsPreset preset164() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.EARLY_RELEASE.id;
        settingsChunk.releaseExtraHillHeight = true;
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.FRACTAL.id;
        settingsBiome.fractalBiomes = List.of(
            "minecraft:desert",
            "minecraft:forest",
            "modern_beta:early_release_extreme_hills",
            "modern_beta:early_release_swampland",
            "modern_beta:late_beta_plains",
            "modern_beta:early_release_taiga",
            "minecraft:jungle"
        );
        settingsBiome.fractalLargerIslands = false;
        settingsBiome.fractalAddSnow = true;
        settingsBiome.fractalAddMushroomIslands = true;
        settingsBiome.fractalAddBeaches = true;
        settingsBiome.fractalAddHills = true;
        settingsBiome.fractalAddSwampRivers = true;

        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }

    private static ModernBetaSettingsPreset presetReleaseHybrid() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.EARLY_RELEASE.id;
        settingsChunk.releaseExtraHillHeight = true;
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.FRACTAL.id;
        settingsBiome.fractalBiomes = List.of(
            "minecraft:desert",
            "minecraft:forest",
            "minecraft:windswept_hills",
            "minecraft:swamp",
            "minecraft:plains",
            "minecraft:taiga",
            "minecraft:jungle",
            "minecraft:savanna",
            "minecraft:cherry_grove",
            "minecraft:dark_forest",
            "minecraft:birch_forest",
            "minecraft:old_growth_birch_forest",
            "minecraft:mangrove_swamp",
            "minecraft:flower_forest",
            "minecraft:sunflower_plains",
            "minecraft:old_growth_spruce_taiga",
            "minecraft:sparse_jungle"
        );
        settingsBiome.fractalHillVariants = Map.ofEntries(
            Map.entry("minecraft:desert", "*minecraft:desert"),
            Map.entry("minecraft:forest", "*minecraft:forest"),
            Map.entry("minecraft:windswept_hills", "minecraft:windswept_forest"),
            Map.entry("minecraft:swamp", "*minecraft:swamp"),
            Map.entry("minecraft:plains", "minecraft:forest"),
            Map.entry("minecraft:taiga", "*minecraft:taiga"),
            Map.entry("minecraft:jungle", "*minecraft:jungle"),
            Map.entry("minecraft:snowy_taiga", "*minecraft:snowy_taiga"),
            Map.entry("minecraft:savanna", "*minecraft:savanna"),
            Map.entry("minecraft:cherry_grove", "minecraft:plains"),
            Map.entry("minecraft:dark_forest", "minecraft:plains"),
            Map.entry("minecraft:birch_forest", "*minecraft:birch_forest"),
            Map.entry("minecraft:old_growth_birch_forest", "*minecraft:old_growth_birch_forest"),
            Map.entry("minecraft:wooded_badlands", "*minecraft:wooded_badlands"),
            Map.entry("minecraft:mangrove_swamp", "*minecraft:mangrove_swamp"),
            Map.entry("minecraft:flower_forest", "*minecraft:flower_forest"),
            Map.entry("minecraft:sparse_jungle", "minecraft:jungle")
        );
        settingsBiome.fractalSubVariants = Map.ofEntries(
            Map.entry("minecraft:snowy_plains", List.of(
                "minecraft:snowy_plains",
                "minecraft:snowy_taiga",
                "minecraft:snowy_plains",
                "minecraft:snowy_taiga",
                "minecraft:snowy_plains",
                "minecraft:ice_spikes"
            )),
            Map.entry("minecraft:savanna", List.of(
                "minecraft:savanna",
                "minecraft:savanna",
                "minecraft:windswept_savanna",
                "minecraft:savanna",
                "minecraft:savanna"
            ))
        );
        settingsBiome.fractalPlains = "minecraft:plains";
        settingsBiome.fractalIcePlains = "minecraft:snowy_plains";
        settingsBiome.fractalSubVariantScale = 1;
        settingsBiome.fractalLargerIslands = false;
        settingsBiome.fractalAddSnow = true;
        settingsBiome.fractalAddMushroomIslands = true;
        settingsBiome.fractalAddBeaches = true;
        settingsBiome.fractalAddHills = true;
        settingsBiome.fractalAddSwampRivers = true;

        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }

    private static ModernBetaSettingsPreset presetBetaHybrid() {
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.EARLY_RELEASE.id;
        settingsChunk.releaseExtraHillHeight = true;
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.FRACTAL.id;
        settingsBiome.fractalBiomes = List.of(
            "modern_beta:beta_desert",
            "modern_beta:beta_seasonal_forest",
            "modern_beta:beta_shrubland",
            "modern_beta:beta_savanna",
            "modern_beta:beta_plains",
            "modern_beta:beta_rainforest",
            "modern_beta:beta_forest",
            "modern_beta:beta_swampland"
        );
        settingsBiome.fractalSubVariants = Map.ofEntries(
            Map.entry("modern_beta:beta_tundra", List.of(
                "modern_beta:beta_tundra",
                "modern_beta:beta_tundra",
                "modern_beta:beta_taiga"
            ))
        );
        settingsBiome.fractalPlains = "modern_beta:beta_plains";
        settingsBiome.fractalIcePlains = "modern_beta:beta_tundra";
        settingsBiome.fractalSubVariantScale = 0;
        settingsBiome.fractalLargerIslands = false;
        settingsBiome.fractalAddSnow = true;
        settingsBiome.fractalAddBeaches = true;
        settingsBiome.fractalAddHills = true;

        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
}
