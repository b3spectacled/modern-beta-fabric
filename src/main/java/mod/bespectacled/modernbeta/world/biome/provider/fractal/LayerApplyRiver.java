package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class LayerApplyRiver extends Layer {
	private final Layer riverLayout;
	private final BiomeInfo ocean, deepOcean, river, snowyLand, frozenRiver;
	private final RegistryEntry<Biome> mushroomIsland;

	public LayerApplyRiver(Layer parent, Layer riverLayout, RegistryEntry<Biome> ocean, RegistryEntry<Biome> deepOcean,
						   RegistryEntry<Biome> river, RegistryEntry<Biome> mushroomIsland,
						   RegistryEntry<Biome> snowyLand, RegistryEntry<Biome> frozenRiver) {
		super(0, parent);
		this.riverLayout = riverLayout;
		this.ocean = BiomeInfo.of(ocean);
		this.deepOcean = BiomeInfo.of(deepOcean);
		this.river = BiomeInfo.of(river);
		this.mushroomIsland = mushroomIsland;
		this.snowyLand = BiomeInfo.of(snowyLand);
		this.frozenRiver = BiomeInfo.of(frozenRiver);
	}

	@Override
	public void setWorldSeed(long seed) {
		this.riverLayout.setWorldSeed(seed);
		super.setWorldSeed(seed);
	}

	@Override
	protected BiomeInfo[] getNewBiomes(int x, int z, int width, int length) {
		BiomeInfo[] input = this.parent.getBiomes(x, z, width, length);
		BiomeInfo[] inputRiver = this.riverLayout.getBiomes(x, z, width, length);
		BiomeInfo[] output = new BiomeInfo[width * length];

		for (int i = 0; i < width * length; i++) {
			if (input[i].equals(ocean) || input[i].equals(deepOcean)) {
				output[i] = ocean;
			} else if (inputRiver[i].biome() != null) {
				if (input[i].equals(snowyLand)) {
					output[i] = frozenRiver;
				} else if (input[i].biome().equals(mushroomIsland)) {
					output[i] = input[i].asSpecial();
				} else if (inputRiver[i].biome().equals(DummyBiome.RIVER)) {
					output[i] = river;
				} else {
					output[i] = ocean;
				}
			} else {
				output[i] = input[i];
			}
		}

		return output;
	}
}
