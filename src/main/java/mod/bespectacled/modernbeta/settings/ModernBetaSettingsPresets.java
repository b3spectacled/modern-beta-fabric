package mod.bespectacled.modernbeta.settings;

import java.util.List;

import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomes;
import mod.bespectacled.modernbeta.world.biome.provider.climate.ClimateMapping;
import mod.bespectacled.modernbeta.world.biome.voronoi.VoronoiPointCaveBiome;
import mod.bespectacled.modernbeta.world.chunk.provider.indev.IndevTheme;
import mod.bespectacled.modernbeta.world.chunk.provider.indev.IndevType;

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
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();
        
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
        ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
        ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
        ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();
        
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
}
