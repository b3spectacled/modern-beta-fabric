package mod.bespectacled.modernbeta.world.biome.provider.fractal;

public class LayerCoolWarmEdge extends Layer {
    public LayerCoolWarmEdge(long seed, Layer parent) {
        super(seed, parent);
    }

    @Override
    protected BiomeInfo[] getNewBiomes(int x, int z, int width, int length) {
        return forEachWithNeighbors(x, z, width, length, (input, ix, iz, neighbors) -> {
            if (!input.biome().equals(DummyBiome.CLIMATE) || input.type() != 1) return input;
            if (neighborsContain(neighbors, BiomeInfo.of(DummyBiome.CLIMATE, 3))
                || neighborsContain(neighbors, BiomeInfo.of(DummyBiome.CLIMATE, 4)))
                return input.withType(2);
            return input;
        });
    }
}
