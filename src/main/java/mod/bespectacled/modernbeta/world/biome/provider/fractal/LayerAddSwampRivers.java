package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import mod.bespectacled.modernbeta.data.ModernBetaTagProviderBiome;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class LayerAddSwampRivers extends Layer {
	private final BiomeInfo river;

	public LayerAddSwampRivers(long seed, Layer parent, RegistryEntry<Biome> river) {
		super(seed, parent);
		this.river = BiomeInfo.of(river);
	}

	@Override
	public BiomeInfo[] getBiomes(int x, int z, int width, int length) {
		return forEach(x, z, width, length, b ->
			b.biome().isIn(ModernBetaTagProviderBiome.FRACTAL_SWAMP_RIVERS) && nextInt(6) == 0
			|| b.biome().isIn(ModernBetaTagProviderBiome.FRACTAL_JUNGLE_RIVERS) && nextInt(8) == 0
			? river : b);
	}
}
