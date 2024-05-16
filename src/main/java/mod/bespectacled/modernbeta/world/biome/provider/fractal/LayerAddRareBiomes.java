package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import java.util.Map;

public class LayerAddRareBiomes extends Layer {
    private final Map<BiomeInfo, BiomeInfo> variants;

    public LayerAddRareBiomes(long seed, Layer parent, Map<BiomeInfo, BiomeInfo> variants) {
        super(seed, parent);
        this.variants = variants;
    }

    @Override
    protected BiomeInfo[] getNewBiomes(int x, int z, int width, int length) {
        return forEach(x, z, width, length, b -> nextInt(57) == 0 ? variants.getOrDefault(b, b) : b);
    }
}
