package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class LayerApplyRiver extends Layer {
	private final Layer riverLayout;
	private final BiomeInfo ocean, snowyLand, frozenRiver;
	private final RegistryEntry<Biome> mushroomIsland;

	public LayerApplyRiver(Layer parent, Layer riverLayout, RegistryEntry<Biome> ocean, RegistryEntry<Biome> mushroomIsland, RegistryEntry<Biome> snowyLand, RegistryEntry<Biome> frozenRiver) {
		super(0, parent);
		this.riverLayout = riverLayout;
		this.ocean = BiomeInfo.of(ocean);
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
	public BiomeInfo[] getBiomes(int x, int z, int width, int length) {
		BiomeInfo[] input = this.parent.getBiomes(x, z, width, length);
		BiomeInfo[] inputRiver = this.riverLayout.getBiomes(x, z, width, length);
		BiomeInfo[] output = new BiomeInfo[width * length];

		for (int i = 0; i < width * length; i++) {
			if (input[i].equals(ocean)) {
				output[i] = ocean;
			} else if (inputRiver[i].biome() != null) {
				if (input[i].equals(snowyLand)) {
					output[i] = frozenRiver;
				} else if (input[i].biome().equals(mushroomIsland)) {
					output[i] = input[i].asSpecial();
				} else {
					output[i] = inputRiver[i];
				}
			} else {
				output[i] = input[i];
			}
		}

		return output;
	}
}
