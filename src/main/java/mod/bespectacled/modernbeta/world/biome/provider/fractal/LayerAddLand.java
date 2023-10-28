package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class LayerAddLand extends Layer {
	private final BiomeInfo ocean, land, frozenOcean, snowyLand;

	public LayerAddLand(long seed, Layer parent, RegistryEntry<Biome> ocean, RegistryEntry<Biome> land, RegistryEntry<Biome> frozenOcean, RegistryEntry<Biome> snowyLand) {
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
				if (!input.equals(ocean) && neighborsContain(neighbors, ocean) && nextInt(5) == 0) {
					return input.equals(snowyLand) ? frozenOcean : ocean;
				} else {
					return input;
				}
			} else {
				int landSampleChance = 1;
				BiomeInfo sampledLand = land;

				for (BiomeInfo neighbor : neighbors) {
					if (!neighbor.equals(ocean) && nextInt(landSampleChance++) == 0) {
						sampledLand = neighbor;
					}
				}

				if (nextInt(3) == 0) {
					return sampledLand;
				} else if (sampledLand.equals(snowyLand)) {
					return frozenOcean;
				} else {
					return ocean;
				}
			}
		}, true);
	}
}
