package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

import java.util.Arrays;

public class LayerSingleBiome extends Layer {
    private final BiomeInfo land;

    public LayerSingleBiome(RegistryEntry<Biome> land) {
        super(0);
        this.land = BiomeInfo.of(land);
    }

    @Override
    public BiomeInfo[] getBiomes(int x, int z, int width, int length) {
        BiomeInfo[] output = new BiomeInfo[width * length];
        Arrays.fill(output, land);
        return output;
    }
}
