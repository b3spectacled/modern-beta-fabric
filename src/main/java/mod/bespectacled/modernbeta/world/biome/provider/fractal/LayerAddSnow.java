package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class LayerAddSnow extends Layer {
	private final BiomeInfo ocean, snowyLand;

	public LayerAddSnow(long seed, Layer parent, RegistryEntry<Biome> ocean, RegistryEntry<Biome> snowyLand) {
		super(seed, parent);
		this.ocean = BiomeInfo.of(ocean);
		this.snowyLand = BiomeInfo.of(snowyLand);
	}

	@Override
	public BiomeInfo[] getBiomes(int x, int z, int width, int length) {
		return forEach(x, z, width, length, b -> b.equals(ocean) ? ocean : nextInt(5) == 0 ? snowyLand : b);
	}
}
