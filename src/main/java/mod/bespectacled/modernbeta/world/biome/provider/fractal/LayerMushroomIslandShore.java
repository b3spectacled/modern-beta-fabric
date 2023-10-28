package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class LayerMushroomIslandShore extends Layer {
	private final BiomeInfo mushroomIsland, ocean;

	public LayerMushroomIslandShore(Layer parent, RegistryEntry<Biome> mushroomIsland, RegistryEntry<Biome> ocean) {
		super(0, parent);
		this.mushroomIsland = BiomeInfo.of(mushroomIsland);
		this.ocean = BiomeInfo.of(ocean);
	}

	@Override
	public BiomeInfo[] getBiomes(int x, int z, int width, int length) {
		return forEachWithNeighbors(x, z, width, length, (b, n) -> b.equals(mushroomIsland) && neighborsContain(n, ocean) ? b.asSpecial() : b);
	}
}
