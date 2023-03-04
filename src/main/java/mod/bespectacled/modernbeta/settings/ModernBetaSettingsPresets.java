package mod.bespectacled.modernbeta.settings;

import java.util.List;

import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import mod.bespectacled.modernbeta.config.ModernBetaConfigBiome;
import mod.bespectacled.modernbeta.config.ModernBetaConfigBiome.ConfigClimateMapping;
import mod.bespectacled.modernbeta.config.ModernBetaConfigCaveBiome.ConfigVoronoiPoint;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsPreset.SettingsType;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomes;
import mod.bespectacled.modernbeta.world.chunk.provider.indev.IndevTheme;
import mod.bespectacled.modernbeta.world.chunk.provider.indev.IndevType;
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
    
    /*
     * TODO:
     * In 1.20, move default settings directly into Settings builders,
     * and remove config files completely.
     * 
     */
    private static ModernBetaSettingsPreset presetDefault() {
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
        
        settingsChunk.infdevUsePyramid = true;
        settingsChunk.infdevUseWall = true;
        
        settingsChunk.indevLevelType = IndevType.ISLAND.getName();
        settingsChunk.indevLevelTheme = IndevTheme.NORMAL.getName();
        settingsChunk.indevLevelWidth = 256;
        settingsChunk.indevLevelLength = 256;
        settingsChunk.indevLevelHeight = 128;
        settingsChunk.indevCaveRadius = 1.0f;
        
        settingsChunk.islesUseIslands = false;
        settingsChunk.islesUseOuterIslands = true;
        settingsChunk.islesMaxOceanDepth = 200.0F;
        settingsChunk.islesCenterIslandFalloff = 4.0F;
        settingsChunk.islesCenterIslandRadius = 16;
        settingsChunk.islesCenterOceanFalloffDistance = 16;
        settingsChunk.islesCenterOceanRadius = 64;
        settingsChunk.islesOuterIslandNoiseScale = 300F;
        settingsChunk.islesOuterIslandNoiseOffset = 0.25F;
        
        settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.BETA.id;
        settingsBiome.singleBiome = ModernBetaBiomes.ALPHA.getValue().toString();
        settingsBiome.useOceanBiomes = true;
        
        settingsBiome.climateTempNoiseScale = 0.025f;
        settingsBiome.climateRainNoiseScale = 0.05f;
        settingsBiome.climateDetailNoiseScale = 0.25f;
        settingsBiome.climateMappings = ModernBetaConfigBiome.createClimateMapping(
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
        settingsBiome.voronoiPoints = List.of(
            new ModernBetaConfigBiome.ConfigVoronoiPoint(
                BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                0.0, 0.0
            ),
            new ModernBetaConfigBiome.ConfigVoronoiPoint(
                BiomeKeys.SNOWY_TAIGA.getValue().toString(),
                BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                0.0, 0.5
            ),
            new ModernBetaConfigBiome.ConfigVoronoiPoint(
                BiomeKeys.SWAMP.getValue().toString(),
                BiomeKeys.COLD_OCEAN.getValue().toString(),
                0.0, 1.0
            ),
            new ModernBetaConfigBiome.ConfigVoronoiPoint(
                BiomeKeys.SAVANNA.getValue().toString(),
                BiomeKeys.OCEAN.getValue().toString(),
                0.5, 0.0
            ),
            new ModernBetaConfigBiome.ConfigVoronoiPoint(
                BiomeKeys.FOREST.getValue().toString(),
                BiomeKeys.OCEAN.getValue().toString(),
                0.5, 0.5
            ),
            new ModernBetaConfigBiome.ConfigVoronoiPoint(
                BiomeKeys.PLAINS.getValue().toString(),
                BiomeKeys.OCEAN.getValue().toString(),
                0.5, 1.0
            ),
            new ModernBetaConfigBiome.ConfigVoronoiPoint(
                BiomeKeys.DESERT.getValue().toString(),
                BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                1.0, 0.0
            ),
            new ModernBetaConfigBiome.ConfigVoronoiPoint(
                BiomeKeys.DARK_FOREST.getValue().toString(),
                BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                1.0, 0.5
            ),
            new ModernBetaConfigBiome.ConfigVoronoiPoint(
                BiomeKeys.JUNGLE.getValue().toString(),
                BiomeKeys.WARM_OCEAN.getValue().toString(),
                1.0, 1.0
            )
        );
        
        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.VORONOI.id;
        settingsCaveBiome.singleBiome = BiomeKeys.LUSH_CAVES.getValue().toString();
        
        settingsCaveBiome.voronoiHorizontalNoiseScale = 32.0f;
        settingsCaveBiome.voronoiVerticalNoiseScale = 16.0f;
        settingsCaveBiome.voronoiDepthMinY = -64;
        settingsCaveBiome.voronoiDepthMaxY = 64;
        settingsCaveBiome.voronoiPoints = List.of(
            new ConfigVoronoiPoint("", 0.0, 0.5, 0.75),
            new ConfigVoronoiPoint("minecraft:lush_caves", 0.1, 0.5, 0.75),
            new ConfigVoronoiPoint("", 0.5, 0.5, 0.75),
            new ConfigVoronoiPoint("minecraft:dripstone_caves", 0.9, 0.5, 0.75),
            new ConfigVoronoiPoint("", 1.0, 0.5, 0.75),

            new ConfigVoronoiPoint("", 0.0, 0.5, 0.25),
            new ConfigVoronoiPoint("minecraft:lush_caves", 0.2, 0.5, 0.25),
            new ConfigVoronoiPoint("", 0.4, 0.5, 0.25),
            new ConfigVoronoiPoint("minecraft:deep_dark", 0.5, 0.5, 0.25),
            new ConfigVoronoiPoint("", 0.6, 0.5, 0.25),
            new ConfigVoronoiPoint("minecraft:dripstone_caves", 0.8, 0.5, 0.25),
            new ConfigVoronoiPoint("", 1.0, 0.5, 0.25)
        );
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetBeta() {
        ModernBetaSettingsPreset defaultPreset = presetDefault();
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder(defaultPreset.getCompound(SettingsType.CHUNK));
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder(defaultPreset.getCompound(SettingsType.BIOME));
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder(defaultPreset.getCompound(SettingsType.CAVE_BIOME));
        
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
        settingsBiome.climateMappings = ModernBetaConfigBiome.createClimateMapping(
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
        
        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.VORONOI.id;
        settingsCaveBiome.voronoiHorizontalNoiseScale = 32.0f;
        settingsCaveBiome.voronoiVerticalNoiseScale = 16.0f;
        settingsCaveBiome.voronoiDepthMinY = -64;
        settingsCaveBiome.voronoiDepthMaxY = 64;
        settingsCaveBiome.voronoiPoints = List.of(
            new ConfigVoronoiPoint("", 0.0, 0.5, 0.75),
            new ConfigVoronoiPoint("minecraft:lush_caves", 0.1, 0.5, 0.75),
            new ConfigVoronoiPoint("", 0.5, 0.5, 0.75),
            new ConfigVoronoiPoint("minecraft:dripstone_caves", 0.9, 0.5, 0.75),
            new ConfigVoronoiPoint("", 1.0, 0.5, 0.75),

            new ConfigVoronoiPoint("", 0.0, 0.5, 0.25),
            new ConfigVoronoiPoint("minecraft:lush_caves", 0.2, 0.5, 0.25),
            new ConfigVoronoiPoint("", 0.4, 0.5, 0.25),
            new ConfigVoronoiPoint("minecraft:deep_dark", 0.5, 0.5, 0.25),
            new ConfigVoronoiPoint("", 0.6, 0.5, 0.25),
            new ConfigVoronoiPoint("minecraft:dripstone_caves", 0.8, 0.5, 0.25),
            new ConfigVoronoiPoint("", 1.0, 0.5, 0.25)
        );
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
        );
    }
    
    private static ModernBetaSettingsPreset presetAlpha() {
        ModernBetaSettingsPreset defaultPreset = presetDefault();
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder(defaultPreset.getCompound(SettingsType.CHUNK));
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder(defaultPreset.getCompound(SettingsType.BIOME));
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder(defaultPreset.getCompound(SettingsType.CAVE_BIOME));
        
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
        ModernBetaSettingsPreset defaultPreset = presetDefault();
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder(defaultPreset.getCompound(SettingsType.CHUNK));
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder(defaultPreset.getCompound(SettingsType.BIOME));
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder(defaultPreset.getCompound(SettingsType.CAVE_BIOME));
        
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
        
        settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;
        
        return new ModernBetaSettingsPreset(
            settingsChunk.build(),
            settingsBiome.build(),
            settingsCaveBiome.build()
         );
    }
    
    private static ModernBetaSettingsPreset presetInfdev415() {
        ModernBetaSettingsPreset defaultPreset = presetDefault();
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder(defaultPreset.getCompound(SettingsType.CHUNK));
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder(defaultPreset.getCompound(SettingsType.BIOME));
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder(defaultPreset.getCompound(SettingsType.CAVE_BIOME));
        
        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.INFDEV_415.id;
        settingsChunk.useDeepslate = false;
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
        ModernBetaSettingsPreset defaultPreset = presetDefault();
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder(defaultPreset.getCompound(SettingsType.CHUNK));
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder(defaultPreset.getCompound(SettingsType.BIOME));
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder(defaultPreset.getCompound(SettingsType.CAVE_BIOME));
        
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
        ModernBetaSettingsPreset defaultPreset = presetDefault();
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder(defaultPreset.getCompound(SettingsType.CHUNK));
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder(defaultPreset.getCompound(SettingsType.BIOME));
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder(defaultPreset.getCompound(SettingsType.CAVE_BIOME));
        
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
        ModernBetaSettingsPreset defaultPreset = presetDefault();
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder(defaultPreset.getCompound(SettingsType.CHUNK));
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder(defaultPreset.getCompound(SettingsType.BIOME));
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder(defaultPreset.getCompound(SettingsType.CAVE_BIOME));
        
        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.INFDEV_227.id;
        settingsChunk.useDeepslate = false;
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
        ModernBetaSettingsPreset defaultPreset = presetDefault();
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder(defaultPreset.getCompound(SettingsType.CHUNK));
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder(defaultPreset.getCompound(SettingsType.BIOME));
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder(defaultPreset.getCompound(SettingsType.CAVE_BIOME));
        
        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.INDEV.id;
        settingsChunk.useDeepslate = false;
        settingsChunk.indevLevelTheme = IndevTheme.NORMAL.getName();
        settingsChunk.indevLevelType = IndevType.ISLAND.getName();
        settingsChunk.indevLevelWidth = 256;
        settingsChunk.indevLevelLength = 256;
        settingsChunk.indevLevelHeight = 128;
        settingsChunk.indevCaveRadius = 1.0f;
        
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
        ModernBetaSettingsPreset defaultPreset = presetDefault();
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder(defaultPreset.getCompound(SettingsType.CHUNK));
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder(defaultPreset.getCompound(SettingsType.BIOME));
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder(defaultPreset.getCompound(SettingsType.CAVE_BIOME));
        
        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.CLASSIC_0_30.id;
        settingsChunk.useDeepslate = false;
        settingsChunk.indevLevelWidth = 256;
        settingsChunk.indevLevelLength = 256;
        settingsChunk.indevLevelHeight = 128;
        settingsChunk.indevCaveRadius = 1.0f;
        
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
        ModernBetaSettingsPreset defaultPreset = presetDefault();
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder(defaultPreset.getCompound(SettingsType.CHUNK));
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder(defaultPreset.getCompound(SettingsType.BIOME));
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder(defaultPreset.getCompound(SettingsType.CAVE_BIOME));
        
        settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.PE.id;
        settingsChunk.useDeepslate = false;
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
        settingsBiome.climateMappings = ModernBetaConfigBiome.createClimateMapping(
            new ConfigClimateMapping(
                ModernBetaBiomes.PE_DESERT.getValue().toString(),
                ModernBetaBiomes.PE_OCEAN.getValue().toString()
            ),
            new ConfigClimateMapping(
                ModernBetaBiomes.PE_FOREST.getValue().toString(),
                ModernBetaBiomes.PE_OCEAN.getValue().toString()
            ),
            new ConfigClimateMapping(
                ModernBetaBiomes.PE_TUNDRA.getValue().toString(),
                ModernBetaBiomes.PE_FROZEN_OCEAN.getValue().toString()
            ),
            new ConfigClimateMapping(
                ModernBetaBiomes.PE_PLAINS.getValue().toString(),
                ModernBetaBiomes.PE_OCEAN.getValue().toString()
            ),
            new ConfigClimateMapping(
                ModernBetaBiomes.PE_RAINFOREST.getValue().toString(),
                ModernBetaBiomes.PE_WARM_OCEAN.getValue().toString()
            ),
            new ConfigClimateMapping(
                ModernBetaBiomes.PE_SAVANNA.getValue().toString(),
                ModernBetaBiomes.PE_OCEAN.getValue().toString()
            ),
            new ConfigClimateMapping(
                ModernBetaBiomes.PE_SHRUBLAND.getValue().toString(),
                ModernBetaBiomes.PE_OCEAN.getValue().toString()
            ),
            new ConfigClimateMapping(
                ModernBetaBiomes.PE_SEASONAL_FOREST.getValue().toString(),
                ModernBetaBiomes.PE_LUKEWARM_OCEAN.getValue().toString()
            ),
            new ConfigClimateMapping(
                ModernBetaBiomes.PE_SWAMPLAND.getValue().toString(),
                ModernBetaBiomes.PE_COLD_OCEAN.getValue().toString()
            ),
            new ConfigClimateMapping(
                ModernBetaBiomes.PE_TAIGA.getValue().toString(),
                ModernBetaBiomes.PE_FROZEN_OCEAN.getValue().toString()
            ),
            new ConfigClimateMapping(
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
}
