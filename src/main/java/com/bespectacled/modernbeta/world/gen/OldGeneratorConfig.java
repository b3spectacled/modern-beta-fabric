package com.bespectacled.modernbeta.world.gen;

import java.util.Optional;

import com.google.common.collect.Maps;

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
    public static final NoiseSamplingConfig INFDEV_415_SAMPLING_CONFIG;
    public static final NoiseSamplingConfig PE_SAMPLING_CONFIG;
    
    public static final GenerationShapeConfig BETA_SHAPE_CONFIG;
    public static final GenerationShapeConfig ALPHA_SHAPE_CONFIG;
    public static final GenerationShapeConfig SKYLANDS_SHAPE_CONFIG;
    public static final GenerationShapeConfig INFDEV_611_SHAPE_CONFIG;
    public static final GenerationShapeConfig INFDEV_415_SHAPE_CONFIG;
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
        PE_SAMPLING_CONFIG = new NoiseSamplingConfig(1.0, 1.0, 80.0, 160.0); 
        
        BETA_SHAPE_CONFIG = GenerationShapeConfig.create(
            0,
            128,
            BETA_SAMPLING_CONFIG, 
            new SlideConfig(-10, 3, 0), 
            new SlideConfig(15, 3, 0),
            1, 
            2, 
            1, 
            -0.46875, 
            true, 
            false,
            false, 
            false
        );
        
        ALPHA_SHAPE_CONFIG = GenerationShapeConfig.create(
            0,
            128,  
            ALPHA_SAMPLING_CONFIG, 
            new SlideConfig(-10, 3, 0), 
            new SlideConfig(15, 3, 0),
            1, 
            2, 
            1, 
            -0.46875, 
            true, 
            false, 
            false, 
            false
        );
        
        SKYLANDS_SHAPE_CONFIG = GenerationShapeConfig.create(
            0, 
            128, 
            SKYLANDS_SAMPLING_CONFIG, 
            new SlideConfig(-30, 31, 0), 
            new SlideConfig(-30, 7, 0),
            2, 
            1, 
            1, 
            -0.46875, 
            true, 
            false, 
            false, 
            false
        );
        
        INFDEV_611_SHAPE_CONFIG = GenerationShapeConfig.create(
            0,
            128,  
            INFDEV_611_SAMPLING_CONFIG, 
            new SlideConfig(-10, 3, 0), 
            new SlideConfig(15, 3, 0),
            1, 
            2, 
            1, 
            -0.46875, 
            true, 
            false, 
            false, 
            false
        );
        
        INFDEV_415_SHAPE_CONFIG = GenerationShapeConfig.create(
            0,
            128,
            INFDEV_415_SAMPLING_CONFIG, 
            new SlideConfig(0, 0, 0), 
            new SlideConfig(15, 3, 0),
            1, 
            1, 
            1, 
            -0.46875, 
            true, 
            false, 
            false, 
            false
        );
        
        INDEV_SHAPE_CONFIG = GenerationShapeConfig.create(
            0, 
            256, 
            INFDEV_415_SAMPLING_CONFIG, 
            new SlideConfig(0, 0, 0), 
            new SlideConfig(0, 0, 0),
            1, 
            1, 
            1, 
            -0.46875, 
            true, 
            false, 
            false, 
            false
        );
        
        PE_SHAPE_CONFIG = GenerationShapeConfig.create(
            0,
            128,
            PE_SAMPLING_CONFIG, 
            new SlideConfig(-10, 3, 0), 
            new SlideConfig(15, 3, 0),
            1, 
            2, 
            1, 
            -0.46875, 
            true, 
            false,
            false, 
            false
        );
    }
}
