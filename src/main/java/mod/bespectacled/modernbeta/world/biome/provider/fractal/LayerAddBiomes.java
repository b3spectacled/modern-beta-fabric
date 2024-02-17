package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

import java.util.List;

public class LayerAddBiomes extends Layer {
	private final List<BiomeInfo> biomes;
	private final List<BiomeInfo> preservedBiomes;
	private final BiomeInfo land, snowyLand;

	public LayerAddBiomes(long seed, Layer parent, List<RegistryEntry<Biome>> biomes, List<RegistryEntry<Biome>> preservedBiomes, RegistryEntry<Biome> land, RegistryEntry<Biome> snowyLand) {
		super(seed);
		this.parent = parent;
		this.biomes = biomes.stream().map(BiomeInfo::of).toList();
		this.preservedBiomes = preservedBiomes.stream().map(BiomeInfo::of).toList();
		this.land = BiomeInfo.of(land);
		this.snowyLand = BiomeInfo.of(snowyLand);
	}

	@Override
	public BiomeInfo[] getBiomes(int x, int z, int width, int length) {
		return forEach(x, z, width, length, i -> preservedBiomes.contains(i) ? i : i.equals(land) ? biomes.get(nextInt(biomes.size())) : snowyLand);
	}
}
