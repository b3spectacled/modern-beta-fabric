package mod.bespectacled.modernbeta.world.chunk;

import net.minecraft.util.math.Spline;
import net.minecraft.world.biome.source.util.VanillaTerrainParameters;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.chunk.NoiseSamplingConfig;
import net.minecraft.world.gen.chunk.SlideConfig;

public class ModernBetaChunkGeneratorConfig {
    public static final NoiseSamplingConfig MODERN_BETA_SAMPLING_CONFIG;
    public static final GenerationShapeConfig MODERN_BETA_SHAPE_CONFIG;
    
    static {
        MODERN_BETA_SAMPLING_CONFIG = new NoiseSamplingConfig(1.0, 1.0, 80.0, 160.0);
        
        // Noise is scaled down by 128.0 in 1.18+,
        // so old slide targets are scaled down accordingly.
        // e.g. 15 / 128.0 = 0.1171875
        
        MODERN_BETA_SHAPE_CONFIG = GenerationShapeConfig.create(
            -64,
            192,
            MODERN_BETA_SAMPLING_CONFIG, 
            new SlideConfig(-0.078125, 3, 0),
            new SlideConfig(0.1171875, 3, 0),
            1, 
            2,
            new VanillaTerrainParameters(Spline.fixedFloatFunction(0.0f), Spline.fixedFloatFunction(0.0f), Spline.fixedFloatFunction(0.0f))
        );
    }
}
