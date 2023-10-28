package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import mod.bespectacled.modernbeta.data.ModernBetaTagProviderBiome;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

import java.util.List;

public class LayerAddShore extends Layer {
	private final BiomeInfo beach, ocean, mushroomIsland;

	public LayerAddShore(long seed, Layer parent, RegistryEntry<Biome> beach, RegistryEntry<Biome> ocean, RegistryEntry<Biome> mushroomIsland) {
		super(seed, parent);
		this.beach = BiomeInfo.of(beach);
		this.ocean = BiomeInfo.of(ocean);
		this.mushroomIsland = BiomeInfo.of(mushroomIsland);
	}

	@Override
	public BiomeInfo[] getBiomes(int x, int z, int width, int length) {
		return forEachWithNeighbors(x, z, width, length, (b, n) -> {
			if (b.equals(mushroomIsland)) {
				return b.asSpecial(neighborsContain(n, ocean));
			} else if (b.biome().isIn(ModernBetaTagProviderBiome.HEIGHT_CONFIG_EXTREME_HILLS_EDGE)) {
				return b.asSpecial(!allNeighborsEqual(n, b));
			} else if (!b.biome().isIn(ModernBetaTagProviderBiome.FRACTAL_NO_BEACHES) && neighborsContain(n, ocean)) {
				return beach;
			}
			return b;
		});
	}
}
