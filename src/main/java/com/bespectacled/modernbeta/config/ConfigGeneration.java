package com.bespectacled.modernbeta.config;

import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.world.gen.provider.indev.IndevTheme;
import com.bespectacled.modernbeta.world.gen.provider.indev.IndevType;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "generation_config")
public class ConfigGeneration implements ConfigData {
    
    public GeneralGenConfig generalGenConfig = new GeneralGenConfig();
    
    public InfGenConfig infGenConfig = new InfGenConfig();
    
    public Inf227GenConfig inf227GenConfig = new Inf227GenConfig();
    
    public IndevGenConfig indevGenConfig = new IndevGenConfig();
    
    public IslandGenConfig islandGenConfig = new IslandGenConfig();
    
    public static class GeneralGenConfig {
        public String worldType = BuiltInTypes.Chunk.BETA.name;
    }
    
    public static class InfGenConfig {
        public boolean generateOceans = true;
        public boolean generateOceanShrines = true;
    }
    
    public static class Inf227GenConfig {
        public boolean generateInfdevPyramid = true;
        public boolean generateInfdevWall = true;
    }
    
    public static class IndevGenConfig {
        public String indevLevelType = IndevType.ISLAND.getName();
        public String indevLevelTheme = IndevTheme.NORMAL.getName();
        public int indevLevelWidth = 256;
        public int indevLevelLength = 256;
        public int indevLevelHeight = 128;
        public float indevCaveRadius = 1.0f;
    }
    
    public static class IslandGenConfig {
        public boolean generateOuterIslands = true;
        public int centerIslandRadius = 16;
        public float centerIslandFalloff = 4.0F;
        public int centerOceanLerpDistance = 16;
        public int centerOceanRadius = 64;
        public float outerIslandNoiseScale = 300F;
        public float outerIslandNoiseOffset = 0.25F;
    }
}