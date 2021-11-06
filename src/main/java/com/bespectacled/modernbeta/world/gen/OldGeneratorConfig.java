package com.bespectacled.modernbeta.world.gen;

import java.util.Optional;

import com.google.common.collect.Maps;

import net.minecraft.world.biome.source.util.VanillaTerrainParametersCreator;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.chunk.NoiseSamplingConfig;
import net.minecraft.world.gen.chunk.SlideConfig;
import net.minecraft.world.gen.chunk.StrongholdConfig;
import net.minecraft.world.gen.chunk.StructuresConfig;

public class OldGeneratorConfig {
    public static final StructuresConfig STRUCTURES;
    public static final Optional<StrongholdConfig> INDEV_STRONGHOLD;
    public static final StructuresConfig INDEV_STRUCTURES;
    
    public static final NoiseSamplingConfig BETA_SAMPLING_CONFIG;
    public static final NoiseSamplingConfig ALPHA_SAMPLING_CONFIG;
    public static final NoiseSamplingConfig SKYLANDS_SAMPLING_CONFIG;
    public static final NoiseSamplingConfig INFDEV_611_SAMPLING_CONFIG;
    public static final NoiseSamplingConfig INFDEV_420_SAMPLING_CONFIG; 
    public static final NoiseSamplingConfig INFDEV_415_SAMPLING_CONFIG;
    public static final NoiseSamplingConfig PE_SAMPLING_CONFIG;
    
    public static final GenerationShapeConfig BETA_SHAPE_CONFIG;
    public static final GenerationShapeConfig ALPHA_SHAPE_CONFIG;
    public static final GenerationShapeConfig SKYLANDS_SHAPE_CONFIG;
    public static final GenerationShapeConfig INFDEV_611_SHAPE_CONFIG;
    public static final GenerationShapeConfig INFDEV_415_SHAPE_CONFIG;
    public static final GenerationShapeConfig INFDEV_420_SHAPE_CONFIG;
    public static final GenerationShapeConfig INDEV_SHAPE_CONFIG;
    public static final GenerationShapeConfig PE_SHAPE_CONFIG;
    
    static {
        STRUCTURES = new StructuresConfig(true);
        INDEV_STRONGHOLD = Optional.of(new StrongholdConfig(0, 0, 1));
        INDEV_STRUCTURES = new StructuresConfig(INDEV_STRONGHOLD, Maps.newHashMap(StructuresConfig.DEFAULT_STRUCTURES));

        BETA_SAMPLING_CONFIG = new NoiseSamplingConfig(1.0, 1.0, 80.0, 160.0);
        ALPHA_SAMPLING_CONFIG = new NoiseSamplingConfig(1.0, 1.0, 80.0, 160.0);
        SKYLANDS_SAMPLING_CONFIG = new NoiseSamplingConfig(2.0, 1.0, 80.0, 160.0);
        INFDEV_611_SAMPLING_CONFIG = new NoiseSamplingConfig(1.0, 1.0, 80.0, 160.0);
        INFDEV_415_SAMPLING_CONFIG = new NoiseSamplingConfig(1.0, 1.0, 80.0, 400.0);
        INFDEV_420_SAMPLING_CONFIG = new NoiseSamplingConfig(1.0, 1.0, 80.0, 160.0);
        PE_SAMPLING_CONFIG = new NoiseSamplingConfig(1.0, 1.0, 80.0, 160.0); 
        
        // Noise is scaled down by 128.0 in 1.18+,
        // so old slide targets are scaled down accordingly.
        // e.g. 15 / 128.0 = 0.1171875
        
        BETA_SHAPE_CONFIG = GenerationShapeConfig.create(
            -64,
            192,
            BETA_SAMPLING_CONFIG, 
            new SlideConfig(-0.078125, 3, 0),
            new SlideConfig(0.1171875, 3, 0),
            1, 
            2,
            false,
            false,
            false,
            VanillaTerrainParametersCreator.createSurfaceParameters()
        );
        
        ALPHA_SHAPE_CONFIG = GenerationShapeConfig.create(
            -64,
            192,
            ALPHA_SAMPLING_CONFIG, 
            new SlideConfig(-0.078125, 3, 0),
            new SlideConfig(0.1171875, 3, 0),
            1, 
            2, 
            false,
            false,
            false,
            VanillaTerrainParametersCreator.createSurfaceParameters()
        );
        
        SKYLANDS_SHAPE_CONFIG = GenerationShapeConfig.create(
            0,
            128,
            SKYLANDS_SAMPLING_CONFIG, 
            new SlideConfig(-0.234375, 31, 0),
            new SlideConfig(-0.234375, 7, 1),
            2, 
            1, 
            false,
            false,
            false,
            VanillaTerrainParametersCreator.createSurfaceParameters()
        );
        
        INFDEV_611_SHAPE_CONFIG = GenerationShapeConfig.create(
            -64,
            192,
            INFDEV_611_SAMPLING_CONFIG, 
            new SlideConfig(-0.078125, 3, 0),
            new SlideConfig(0.1171875, 3, 0),
            1, 
            2, 
            false,
            false,
            false,
            VanillaTerrainParametersCreator.createSurfaceParameters()
        );
        
        INFDEV_420_SHAPE_CONFIG = GenerationShapeConfig.create(
            -64,
            192,
            INFDEV_420_SAMPLING_CONFIG, 
            new SlideConfig(0, Integer.MIN_VALUE, 0), 
            new SlideConfig(0.1171875, 3, 0),
            1, 
            2, 
            false,
            false,
            false,
            VanillaTerrainParametersCreator.createSurfaceParameters()
        );
        
        INFDEV_415_SHAPE_CONFIG = GenerationShapeConfig.create(
            -64,
            192,
            INFDEV_415_SAMPLING_CONFIG, 
            new SlideConfig(0, Integer.MIN_VALUE, 0), 
            new SlideConfig(0.1171875, 3, 0),
            1, 
            1, 
            false,
            false,
            false,
            VanillaTerrainParametersCreator.createSurfaceParameters()
        );
        
        INDEV_SHAPE_CONFIG = GenerationShapeConfig.create(
            0, 
            320, 
            INFDEV_415_SAMPLING_CONFIG, 
            new SlideConfig(0, Integer.MIN_VALUE, 0), 
            new SlideConfig(0, Integer.MIN_VALUE, 0),
            1, 
            1, 
            false,
            false,
            false,
            VanillaTerrainParametersCreator.createSurfaceParameters()
        );
        
        PE_SHAPE_CONFIG = GenerationShapeConfig.create(
            -64,
            192,
            PE_SAMPLING_CONFIG, 
            new SlideConfig(-0.078125, 3, 0),
            new SlideConfig(0.1171875, 3, 0),
            1, 
            2, 
            false,
            false,
            false,
            VanillaTerrainParametersCreator.createSurfaceParameters()
        );
    }
}
