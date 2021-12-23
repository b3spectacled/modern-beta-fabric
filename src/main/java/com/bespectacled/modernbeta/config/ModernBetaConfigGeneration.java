package com.bespectacled.modernbeta.config;

import com.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import com.bespectacled.modernbeta.world.gen.provider.indev.IndevTheme;
import com.bespectacled.modernbeta.world.gen.provider.indev.IndevType;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "config_generation")
public class ModernBetaConfigGeneration implements ConfigData {
    // General
    public String worldType = ModernBetaBuiltInTypes.Chunk.BETA.name;
    
    // Infinite
    public boolean generateOceanShrines = true;
    public boolean generateMonuments = false;
    public boolean sampleClimate = true;
    public boolean generateDeepslate = true;

    // Infdev 227
    public boolean generateInfdevPyramid = true;
    public boolean generateInfdevWall = true;

    // Classic
    public int levelWidth = 256;
    public int levelLength = 256;
    public int levelHeight = 128;
    public float caveRadius = 1.0f;

    // Indev
    public String levelType = IndevType.ISLAND.getName();
    public String levelTheme = IndevTheme.NORMAL.getName();
    
    // Islands
    public boolean generateOuterIslands = true;
    public int centerIslandRadius = 16;
    public float centerIslandFalloff = 4.0F;
    public int centerOceanLerpDistance = 16;
    public int centerOceanRadius = 64;
    public float outerIslandNoiseScale = 300F;
    public float outerIslandNoiseOffset = 0.25F;
}