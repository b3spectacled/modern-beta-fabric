package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class LayerAddMushroomIsland extends Layer {
	private final BiomeInfo ocean, mushroomIsland;

	public LayerAddMushroomIsland(long seed, Layer parent, RegistryEntry<Biome> ocean, RegistryEntry<Biome> mushroomIsland) {
		super(seed, parent);
		this.ocean = BiomeInfo.of(ocean);
		this.mushroomIsland = BiomeInfo.of(mushroomIsland);
	}

	@Override
	public BiomeInfo[] getBiomes(int x, int z, int width, int length) {
		return forEachWithNeighbors(x, z, width, length,
			(b, n) -> b.equals(ocean) && allNeighborsEqual(n, ocean) && nextInt(100) == 0 ? mushroomIsland : b, true);
	}
}
