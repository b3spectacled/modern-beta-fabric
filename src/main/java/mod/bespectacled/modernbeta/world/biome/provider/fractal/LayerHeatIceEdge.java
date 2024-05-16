package mod.bespectacled.modernbeta.world.biome.provider.fractal;

public class LayerHeatIceEdge extends Layer {
    public LayerHeatIceEdge(long seed, Layer parent) {
        super(seed, parent);
    }

    @Override
    protected BiomeInfo[] getNewBiomes(int x, int z, int width, int length) {
        return forEachWithNeighbors(x, z, width, length, (input, ix, iz, neighbors) -> {
            if (!input.biome().equals(DummyBiome.CLIMATE) || input.type() != 4) return input;
            if (neighborsContain(neighbors, BiomeInfo.of(DummyBiome.CLIMATE, 2))
                || neighborsContain(neighbors, BiomeInfo.of(DummyBiome.CLIMATE, 1)))
                return input.withType(3);
            return input;
        });
    }
}
