package mod.bespectacled.modernbeta.world.biome.provider.fractal;

public class LayerAddClimate extends Layer {
	public LayerAddClimate(long seed, Layer parent) {
		super(seed, parent);
	}

	@Override
	protected BiomeInfo[] getNewBiomes(int x, int z, int width, int length) {
		return forEach(x, z, width, length, b -> {
			if (b.equals(DummyBiome.OCEAN.biomeInfo)) return b;
			int id = nextInt(6);
			return BiomeInfo.of(DummyBiome.CLIMATE, switch (id) {
				case 0 -> 4;
				case 1 -> 3;
				default -> 1;
			});
		});
	}
}
