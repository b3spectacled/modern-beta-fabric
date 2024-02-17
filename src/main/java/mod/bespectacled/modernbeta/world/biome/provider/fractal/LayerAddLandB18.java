package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class LayerAddLandB18 extends Layer {
	private final BiomeInfo ocean, land, frozenOcean, snowyLand;

	public LayerAddLandB18(long seed, Layer parent, RegistryEntry<Biome> ocean, RegistryEntry<Biome> land, RegistryEntry<Biome> frozenOcean, RegistryEntry<Biome> snowyLand) {
		super(seed);
		this.parent = parent;
		this.ocean = BiomeInfo.of(ocean);
		this.land = BiomeInfo.of(land);
		this.frozenOcean = BiomeInfo.of(frozenOcean);
		this.snowyLand = BiomeInfo.of(snowyLand);
	}

	@Override
	public BiomeInfo[] getBiomes(int x, int z, int width, int length) {
		return this.forEachWithNeighbors(x, z, width, length, (input, neighbors) -> {
			if (!input.equals(ocean) || allNeighborsEqual(neighbors, ocean)) {
				if (!input.equals(land) || allNeighborsEqual(neighbors, land)) {
					return input;
				} else {
					boolean isLand = 1 - nextInt(5) / 4 > 0;
					return input.equals(snowyLand)
							? isLand ? snowyLand : frozenOcean
							: isLand ? land : ocean;
				}
			} else {
				boolean isLand = nextInt(3) / 2 > 0;
				return input.equals(snowyLand)
						? isLand ? snowyLand : frozenOcean
						: isLand ? land : ocean;
			}
		}, true);
	}
}
