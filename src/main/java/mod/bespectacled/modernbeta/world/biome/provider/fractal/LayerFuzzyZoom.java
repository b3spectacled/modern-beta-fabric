package mod.bespectacled.modernbeta.world.biome.provider.fractal;

public class LayerFuzzyZoom extends LayerZoomBase {
	public LayerFuzzyZoom(long seed, Layer parent) {
		super(seed, parent);
	}

	@Override
	protected BiomeInfo interpolate(BiomeInfo a, BiomeInfo b) {
		return this.nextInt(2) == 0 ? a : b;
	}

	@Override
	protected BiomeInfo interpolate(BiomeInfo a, BiomeInfo b, BiomeInfo c, BiomeInfo d) {
		int choice = this.nextInt(4);
		return switch (choice) {
			case 0 -> a;
			case 1 -> b;
			case 2 -> c;
			case 3 -> d;
			default -> throw new IllegalStateException("Unexpected value: " + choice);
		};
	}
}
