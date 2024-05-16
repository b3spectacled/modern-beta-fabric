package mod.bespectacled.modernbeta.world.biome.provider.fractal;

public class LayerInitMutation extends Layer {
	public LayerInitMutation(long seed, Layer parent) {
		super(seed, parent);
	}

	@Override
	protected BiomeInfo[] getNewBiomes(int x, int z, int width, int length) {
		return forEach(x, z, width, length, b -> b.biome().equals(DummyBiome.OCEAN) ? b : BiomeInfo.of(DummyBiome.RIVER).withType(nextInt(299999) + 2));
	}
}
