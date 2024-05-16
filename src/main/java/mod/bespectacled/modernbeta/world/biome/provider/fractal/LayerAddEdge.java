package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import mod.bespectacled.modernbeta.data.ModernBetaTagProviderBiome;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.Map;

public class LayerAddEdge extends Layer {
	private final BiomeInfo beach, ocean, mushroomIsland;
	private final Map<BiomeInfo, BiomeInfo> edgeVariants;
	private final RegistryEntryLookup<Biome> biomeLookup;

	public LayerAddEdge(long seed, Layer parent, RegistryEntry<Biome> beach, RegistryEntry<Biome> ocean, RegistryEntry<Biome> mushroomIsland) {
		this(seed, parent, beach, ocean, mushroomIsland, null, null);
	}

	public LayerAddEdge(long seed, Layer parent, Map<BiomeInfo, BiomeInfo> edgeVariants, RegistryEntryLookup<Biome> biomeLookup) {
		this(seed, parent, null, null, null, edgeVariants, biomeLookup);
	}

	public LayerAddEdge(long seed, Layer parent, RegistryEntry<Biome> beach, RegistryEntry<Biome> ocean, RegistryEntry<Biome> mushroomIsland,
						Map<BiomeInfo, BiomeInfo> edgeVariants, RegistryEntryLookup<Biome> biomeLookup) {
		super(seed, parent);
		this.beach = BiomeInfo.of(beach);
		this.ocean = BiomeInfo.of(ocean);
		this.mushroomIsland = BiomeInfo.of(mushroomIsland);
		this.edgeVariants = edgeVariants;
		this.biomeLookup = biomeLookup;
	}

	@Override
	protected BiomeInfo[] getNewBiomes(int x, int z, int width, int length) {
		return forEachWithNeighbors(x, z, width, length, (b, ix, iz, n) -> {
			BiomeInfo edgeVariant;
			if (beach != null && b.equals(mushroomIsland)) {
				return b.asSpecial(neighborsContain(n, ocean));
			} else if (edgeVariants != null && (edgeVariant = edgeVariants.get(b)) != null) {
				if (this.biomeLookup != null && edgeVariant.equals(BiomeInfo.fromLookup(biomeLookup, BiomeKeys.WINDSWEPT_FOREST, -1))) {
					if (neighborsContain(n, BiomeInfo.fromLookup(biomeLookup, BiomeKeys.SNOWY_PLAINS)))
						return BiomeInfo.fromLookup(biomeLookup, BiomeKeys.WINDSWEPT_FOREST);
				} else if (this.biomeLookup != null && edgeVariant.equals(BiomeInfo.fromLookup(biomeLookup, BiomeKeys.SPARSE_JUNGLE, -1))) {
					if (neighborsContain(n, BiomeInfo.fromLookup(biomeLookup, BiomeKeys.DESERT))
						|| neighborsContain(n, BiomeInfo.fromLookup(biomeLookup, BiomeKeys.SNOWY_TAIGA))
						|| neighborsContain(n, BiomeInfo.fromLookup(biomeLookup, BiomeKeys.SNOWY_PLAINS)))
						return BiomeInfo.fromLookup(biomeLookup, BiomeKeys.PLAINS);
					if (neighborsContain(n, BiomeInfo.fromLookup(biomeLookup, BiomeKeys.JUNGLE)))
						return BiomeInfo.fromLookup(biomeLookup, BiomeKeys.SPARSE_JUNGLE);
				} else if (!allNeighborsEqual(n, b)) {
					return edgeVariant;
				}
			} else if (beach != null && !b.biome().isIn(ModernBetaTagProviderBiome.FRACTAL_NO_BEACHES) && neighborsContain(n, ocean)) {
				return beach;
			}
			return b;
		});
	}
}
