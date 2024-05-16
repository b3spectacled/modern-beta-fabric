package mod.bespectacled.modernbeta.world.biome.provider.fractal;

public class LayerComputeRiver extends Layer {
	private final boolean convertOceans;

	public LayerComputeRiver(long seed, Layer parent, boolean convertOceans) {
		super(seed, parent);
		this.convertOceans = convertOceans;
	}

	@Override
	protected BiomeInfo[] getNewBiomes(int x, int z, int width, int length) {
		return forEachWithNeighbors(x, z, width, length, (b, ix, iz, n) ->
			(!this.convertOceans ? neighborsRiverBorder(n, b) : (
				(b.biome().equals(DummyBiome.OCEAN) || neighborsContain(n, DummyBiome.OCEAN.biomeInfo))
				|| !allNeighborsEqual(n, b)
			)) ? DummyBiome.RIVER.biomeInfo : BiomeInfo.of(null));
	}

	private static boolean neighborsRiverBorder(BiomeInfo[] neighbors, BiomeInfo match) {
		int matchType = match.type();
		if (matchType >= 2) matchType = matchType % 2 + 2;

		if (match.type() < 2) return false;
		for (BiomeInfo neighbor : neighbors) {
			int nType = neighbor.type();
			if (nType >= 2) nType = nType % 2 + 2;
			if (nType != matchType) return true;
		}
		return false;
	}
}
