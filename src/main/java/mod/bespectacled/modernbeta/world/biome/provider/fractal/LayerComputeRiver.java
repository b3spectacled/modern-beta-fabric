package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class LayerComputeRiver extends Layer {
	private final BiomeInfo ocean;
	private final BiomeInfo river;

	public LayerComputeRiver(long seed, Layer parent, RegistryEntry<Biome> ocean, RegistryEntry<Biome> river) {
		super(seed, parent);
		this.ocean = BiomeInfo.of(ocean);
		this.river = BiomeInfo.of(river);
	}

	@Override
	public BiomeInfo[] getBiomes(int x, int z, int width, int length) {
		return forEachWithNeighbors(x, z, width, length, (b, n) -> b.equals(ocean) || neighborsContain(n, ocean) || !allNeighborsEqual(n, b) ? river : BiomeInfo.of(null));
	}
}
