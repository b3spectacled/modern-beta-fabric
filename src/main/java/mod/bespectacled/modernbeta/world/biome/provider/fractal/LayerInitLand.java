package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class LayerInitLand extends Layer {
	private final BiomeInfo ocean, land;

	public LayerInitLand(long seed, RegistryEntry<Biome> ocean, RegistryEntry<Biome> land) {
		super(seed);
		this.ocean = BiomeInfo.of(ocean);
		this.land = BiomeInfo.of(land);
	}

	@Override
	public BiomeInfo[] getBiomes(int x, int z, int width, int length) {
		BiomeInfo[] output = this.forEach(x, z, width, length, i -> nextInt(10) == 0 ? land : ocean);

		if (x > -width && x <= 0 && z > -length && z <= 0) {
			output[-x + -z * width] = land;
		}

		return output;
	}
}
