package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class LayerInitRiver extends Layer {
	private final BiomeInfo ocean;
	private final RegistryEntry<Biome> river;

	public LayerInitRiver(long seed, Layer parent, RegistryEntry<Biome> ocean, RegistryEntry<Biome> river) {
		super(seed, parent);
		this.ocean = BiomeInfo.of(ocean);
		this.river = river;
	}

	@Override
	public BiomeInfo[] getBiomes(int x, int z, int width, int length) {
		return forEach(x, z, width, length, b -> b.equals(ocean) ? b : BiomeInfo.of(river).asSpecial(nextInt(2) == 1));
	}
}
