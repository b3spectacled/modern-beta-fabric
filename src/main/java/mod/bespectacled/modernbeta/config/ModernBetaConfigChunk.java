package mod.bespectacled.modernbeta.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import mod.bespectacled.modernbeta.world.chunk.provider.indev.IndevTheme;
import mod.bespectacled.modernbeta.world.chunk.provider.indev.IndevType;

@Config(name = "config_chunk")
public class ModernBetaConfigChunk implements ConfigData {
    public String chunkProvider = ModernBetaBuiltInTypes.Chunk.BETA.name;
    
    public boolean useDeepslate = true;
    public int deepslateMinY = 0;
    public int deepslateMaxY = 8;
    public String deepslateBlock = "minecraft:deepslate";

    public String noisePostProcessor = ModernBetaBuiltInTypes.NoisePostProcessor.NONE.name;
    public float noiseCoordinateScale = 684.412f;
    public float noiseHeightScale = 684.412f;
    public float noiseUpperLimitScale = 512.0f;
    public float noiseLowerLimitScale = 512.0f;;
    public float noiseDepthNoiseScaleX = 200.0f;
    public float noiseDepthNoiseScaleZ = 200.0f;
    public float noiseMainNoiseScaleX = 80.0f;
    public float noiseMainNoiseScaleY = 160.0f;
    public float noiseMainNoiseScaleZ = 80.0f;
    public float noiseBaseSize = 8.5f;
    public float noiseStretchY = 12.0f;
    
    public int noiseTopSlideTarget = -10;
    public int noiseTopSlideSize = 3;
    public int noiseTopSlideOffset = 0;
    
    public int noiseBottomSlideTarget = 15;
    public int noiseBottomSlideSize = 3;
    public int noiseBottomSlideOffset = 0;

    public boolean infdevUsePyramid = true;
    public boolean infdevUseWall = true;

    public String indevLevelType = IndevType.ISLAND.getName();
    public String indevLevelTheme = IndevTheme.NORMAL.getName();
    public int indevLevelWidth = 256;
    public int indevLevelLength = 256;
    public int indevLevelHeight = 128;
    public float indevCaveRadius = 1.0f;

    public boolean islesUseIslands = false;
    public boolean islesUseOuterIslands = true;
    public float islesMaxOceanDepth = 200.0F;
    public float islesCenterIslandFalloff = 4.0F;
    public int islesCenterIslandRadius = 16;
    public int islesCenterOceanFalloffDistance = 16;
    public int islesCenterOceanRadius = 64;
    public float islesOuterIslandNoiseScale = 300F;
    public float islesOuterIslandNoiseOffset = 0.25F;
}