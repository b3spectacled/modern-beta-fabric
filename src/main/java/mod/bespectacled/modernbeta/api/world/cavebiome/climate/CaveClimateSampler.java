package mod.bespectacled.modernbeta.api.world.cavebiome.climate;

public interface CaveClimateSampler {
    /**
     * Sample temperature/rainfall values in range [-1.0, 1.0] given block coordinates.
     * 
     * @param x x-coordinate in block coordinates.
     * @param y y-coordinate in block coordinates.
     * @param z z-coordinate in block coordinates.
     * 
     * @return A cave clime containing temperature/rainfall/depth values in range [-1.0, 1.0] sampled at position.
     */
    CaveClime sample(int x, int y, int z);
}
