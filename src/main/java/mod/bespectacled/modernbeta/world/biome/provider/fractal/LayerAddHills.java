package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import java.util.Map;

public class LayerAddHills extends Layer {
	private final Map<BiomeInfo, BiomeInfo> hillVariants;

	public LayerAddHills(long seed, Layer parent, Map<BiomeInfo, BiomeInfo> hillVariants) {
		super(seed, parent);
		this.hillVariants = hillVariants;
	}

	@Override
	public BiomeInfo[] getBiomes(int x, int z, int width, int length) {
		return forEachWithNeighbors(x, z, width, length, (b, n) -> allNeighborsEqual(n, b) && nextInt(3) == 0 ? hillVariants.getOrDefault(b, b) : b);
	}
}
