package mod.bespectacled.modernbeta.world.biome.provider.fractal;

public class LayerZoom extends LayerZoomBase {
	public LayerZoom(long seed, Layer parent) {
		super(seed, parent);
	}

	@Override
	protected BiomeInfo interpolate(BiomeInfo a, BiomeInfo b) {
		return nextInt(2) == 0 ? a : b;
	}

	@Override
	protected BiomeInfo interpolate(BiomeInfo a, BiomeInfo b, BiomeInfo c, BiomeInfo d) {
		if(b.equals(c) && c.equals(d)) {
			return b;
		} else if(a.equals(b) && a.equals(c)) {
			return a;
		} else if(a.equals(b) && a.equals(d)) {
			return a;
		} else if(a.equals(c) && a.equals(d)) {
			return a;
		} else if(a.equals(b) && !c.equals(d)) {
			return a;
		} else if(a.equals(c) && !b.equals(d)) {
			return a;
		} else if(a.equals(d) && !b.equals(c)) {
			return a;
		} else if(b.equals(c) && !a.equals(d)) {
			return b;
		} else if(b.equals(d) && !a.equals(c)) {
			return b;
		} else if(c.equals(d) && !a.equals(b)) {
			return c;
		} else {
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

	public static LayerZoom multi(long seed, Layer parent, int count) {
		for (int i = 0; i < count; i++) {
			parent = new LayerZoom(seed, parent);
			seed++;
		}

		return (LayerZoom) parent;
	}
}
