package mod.bespectacled.modernbeta.world.biome.provider.fractal;

public class LayerDeepenOcean extends Layer {
    public LayerDeepenOcean(long seed, Layer parent) {
        super(seed, parent);
    }

    @Override
    protected BiomeInfo[] getNewBiomes(int x, int z, int width, int length) {
        return forEachWithNeighbors(x, z, width, length, (input, ix, iz, neighbors) ->
            input.equals(DummyBiome.OCEAN.biomeInfo) && allNeighborsEqual(neighbors, DummyBiome.OCEAN.biomeInfo)
                    ? DummyBiome.DEEP_OCEAN.biomeInfo : input);
    }
}
