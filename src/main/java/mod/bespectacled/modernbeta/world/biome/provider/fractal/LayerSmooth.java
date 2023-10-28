package mod.bespectacled.modernbeta.world.biome.provider.fractal;

public class LayerSmooth extends Layer {
	public LayerSmooth(long seed, Layer parent) {
		super(seed);
		this.parent = parent;
	}

	@Override
	public BiomeInfo[] getBiomes(int x, int z, int width, int length) {
		return forEachWithNeighbors(x, z, width, length, (input, neighbors) -> {
			if (neighbors[0].equals(neighbors[1]) && neighbors[2].equals(neighbors[3])) {
				return nextInt(2) == 0 ? neighbors[0] : neighbors[2];
			} else if (neighbors[2].equals(neighbors[3])) {
				return neighbors[2];
			} else if (neighbors[0].equals(neighbors[1])) {
				return neighbors[0];
			} else {
				return input;
			}
		});
	}
}
