package mod.bespectacled.modernbeta.api.world.gen.noise;

public interface NoisePostProcessor {
    public static final NoisePostProcessor DEFAULT = (noise, noiseX, noiseY, noiseZ) -> noise;
    
    double sample(double noise, int noiseX, int noiseY, int noiseZ);
}
