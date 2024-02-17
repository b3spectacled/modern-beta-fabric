package mod.bespectacled.modernbeta.world.biome.provider.fractal;

public abstract class LayerZoomBase extends Layer {
	public LayerZoomBase(long seed, Layer parent) {
		super(seed);
		super.parent = parent;
	}

	@Override
	public BiomeInfo[] getBiomes(int x, int z, int width, int length) {
		int zoomedX = x >> 1;
		int zoomedZ = z >> 1;
		int zoomedWidth = (width >> 1) + 3;
		int zoomedLength = (length >> 1) + 3;
		BiomeInfo[] input = this.parent.getBiomes(zoomedX, zoomedZ, zoomedWidth, zoomedLength);
		BiomeInfo[] zoom = new BiomeInfo[zoomedWidth * 2 * zoomedLength * 2];
		int doubleWidth = zoomedWidth << 1;

		for(int zz = 0; zz < zoomedLength - 1; ++zz) {
			int doubleZ = zz << 1;
			int i = doubleZ * doubleWidth;
			BiomeInfo pointA = input[zz * zoomedWidth];
			BiomeInfo pointB = input[(zz + 1) * zoomedWidth];

			for(int xx = 0; xx < zoomedWidth - 1; ++xx) {
				this.setChunkSeed((long) xx + zoomedX << 1, (long) zz + zoomedZ << 1);
				BiomeInfo pointC = input[xx + 1 + zz * zoomedWidth];
				BiomeInfo pointD = input[xx + 1 + (zz + 1) * zoomedWidth];
				zoom[i] = pointA;
				zoom[i++ + doubleWidth] = this.interpolate(pointA, pointB);
				zoom[i] = this.interpolate(pointA, pointC);
				zoom[i++ + doubleWidth] = this.interpolate(pointA, pointC, pointB, pointD);
				pointA = pointC;
				pointB = pointD;
			}
		}

		BiomeInfo[] output = new BiomeInfo[width * length];

		for(int zz = 0; zz < length; ++zz) {
			System.arraycopy(zoom, (zz + (z & 1)) * (zoomedWidth << 1) + (x & 1), output, zz * width, width);
		}

		return output;
	}

	protected abstract BiomeInfo interpolate(BiomeInfo a, BiomeInfo b);

	protected abstract BiomeInfo interpolate(BiomeInfo a, BiomeInfo b, BiomeInfo c, BiomeInfo d);
}
