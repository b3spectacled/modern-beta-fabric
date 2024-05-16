package mod.bespectacled.modernbeta.world.biome.provider.fractal;

public class LayerReduceOcean extends Layer {
	public LayerReduceOcean(long seed, Layer parent) {
		super(seed, parent);
	}

	@Override
	protected BiomeInfo[] getNewBiomes(int x, int z, int width, int length) {
		return this.forEachWithNeighbors(x, z, width, length, (input, ix, iz, neighbors) ->
				input.equals(DummyBiome.OCEAN.biomeInfo) && allNeighborsEqual(neighbors, DummyBiome.OCEAN.biomeInfo)
					? DummyBiome.PLAINS.biomeInfo : input);
	}
}
