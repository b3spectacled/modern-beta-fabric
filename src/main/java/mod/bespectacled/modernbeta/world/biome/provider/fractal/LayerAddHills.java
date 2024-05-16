package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.Map;

public class LayerAddHills extends Layer {
	private final RegistryEntryLookup<Biome> biomeLookup;
	private final Map<BiomeInfo, BiomeInfo> hillVariants;
	private final Map<BiomeInfo, BiomeInfo> mutatedVariants;
	private final Layer mutationLayout;
	private final int neighborRequirement;

	public LayerAddHills(long seed, Layer parent, Map<BiomeInfo, BiomeInfo> hillVariants,
						 RegistryEntryLookup<Biome> biomeLookup, int neighborRequirement) {
		this(seed, parent, hillVariants, biomeLookup, neighborRequirement, null, null);
	}

	public LayerAddHills(long seed, Layer parent, Map<BiomeInfo, BiomeInfo> hillVariants,
						 RegistryEntryLookup<Biome> biomeLookup, int neighborRequirement,
						 Layer mutationLayout, Map<BiomeInfo, BiomeInfo> mutatedVariants) {
		super(seed, parent);
		this.hillVariants = hillVariants;
		this.biomeLookup = biomeLookup;
		this.neighborRequirement = neighborRequirement;
		this.mutationLayout = mutationLayout;
		this.mutatedVariants = mutatedVariants;
	}

	@Override
	protected BiomeInfo[] getNewBiomes(int x, int z, int width, int length) {
		int inX = x - 1;
		int inZ = z - 1;
		int inWidth = width + 2;
		int inLength = length + 2;

		BiomeInfo[] mutationData = this.mutationLayout != null
			? this.mutationLayout.getBiomes(inX, inZ, inWidth, inLength) : null;

		return forEachWithNeighbors(x, z, width, length, (b, ix, iz, n) -> {
			int mutationType = mutationData != null ? mutationData[ix + 1 + (iz + 1) * inWidth].type() : 0;
			if (mutationType >= 2 && (mutationType - 2) % 29 == 1) {
				BiomeInfo mutatedVariant = this.mutatedVariants != null ? this.mutatedVariants.get(b) : null;
				return mutatedVariant != null ? mutatedVariant : b;
			}
			if (nextInt(3) != 0 && (mutationType - 2) % 29 != 0) return b;

			BiomeInfo hillVariant = hillVariants.getOrDefault(b, b);

			if (hillVariant.equals(BiomeInfo.fromLookup(this.biomeLookup, BiomeKeys.DEEP_OCEAN, 1)) && nextInt(3) == 0) {
				hillVariant = BiomeInfo.fromLookup(this.biomeLookup, nextInt(2) == 0 ? BiomeKeys.PLAINS : BiomeKeys.FOREST);
			} else if (BiomeKeys.FOREST.equals(hillVariant.biome().getKey().orElse(null))
				&& BiomeKeys.PLAINS.equals(b.biome().getKey().orElse(null))
				&& this.biomeLookup != null && mutationData != null && nextInt(3) == 0) {
				hillVariant = hillVariants.getOrDefault(BiomeInfo.fromLookup(this.biomeLookup, BiomeKeys.FOREST), hillVariant);
			} else if (BiomeKeys.OCEAN.equals(hillVariant.biome().getKey().orElse(null)) && hillVariant.type() == 1) {
				hillVariant = this.biomeLookup != null && mutationData != null
					? BiomeInfo.fromLookup(this.biomeLookup, BiomeKeys.DEEP_OCEAN) : hillVariant.withType(0);
			}

			if ((mutationType - 2) % 29 == 0 && hillVariant != b) {
				BiomeInfo mutatedVariant = this.mutatedVariants != null ? this.mutatedVariants.get(b) : null;
				return mutatedVariant != null ? mutatedVariant : b;
			}

			return countMatchingNeighbors(n, b) >= this.neighborRequirement ? hillVariant : b;
		});
	}
}
