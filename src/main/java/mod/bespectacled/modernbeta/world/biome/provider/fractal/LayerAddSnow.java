package mod.bespectacled.modernbeta.world.biome.provider.fractal;

public class LayerAddSnow extends Layer {
	public LayerAddSnow(long seed, Layer parent) {
		super(seed, parent);
	}

	@Override
	protected BiomeInfo[] getNewBiomes(int x, int z, int width, int length) {
		return forEach(x, z, width, length, b ->
			!b.biome().equals(DummyBiome.OCEAN) && nextInt(5) == 0 ? DummyBiome.ICE_PLAINS.biomeInfo : b);
	}
}
