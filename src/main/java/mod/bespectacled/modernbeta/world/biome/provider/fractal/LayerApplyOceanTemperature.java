package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.Map;

public class LayerApplyOceanTemperature extends Layer {
    private final Layer biomes;
    private final Layer ocean;

    private final BiomeInfo trueWarmOcean;
    private final BiomeInfo trueLukewarmOcean;
    private final BiomeInfo trueOcean;
    private final BiomeInfo trueColdOcean;
    private final BiomeInfo trueFrozenOcean;
    private final BiomeInfo trueDeepOcean;
    private final BiomeInfo trueDeepLukewarmOcean;
    private final BiomeInfo trueDeepColdOcean;
    private final BiomeInfo trueDeepFrozenOcean;
    private final Map<BiomeInfo, BiomeInfo> dummyConversionMap;
    private final Map<BiomeInfo, BiomeInfo> dummyDeepConversionMap;

    public LayerApplyOceanTemperature(Layer biomes, Layer ocean, RegistryEntryLookup<Biome> biomeLookup) {
        super(0, biomes);
        this.biomes = biomes;
        this.ocean = ocean;

        this.trueWarmOcean = BiomeInfo.of(biomeLookup.getOrThrow(BiomeKeys.WARM_OCEAN));
        this.trueLukewarmOcean = BiomeInfo.of(biomeLookup.getOrThrow(BiomeKeys.LUKEWARM_OCEAN));
        this.trueOcean = BiomeInfo.of(biomeLookup.getOrThrow(BiomeKeys.OCEAN));
        this.trueColdOcean = BiomeInfo.of(biomeLookup.getOrThrow(BiomeKeys.COLD_OCEAN));
        this.trueFrozenOcean = BiomeInfo.of(biomeLookup.getOrThrow(BiomeKeys.FROZEN_OCEAN));
        this.trueDeepLukewarmOcean = BiomeInfo.of(biomeLookup.getOrThrow(BiomeKeys.DEEP_LUKEWARM_OCEAN));
        this.trueDeepOcean = BiomeInfo.of(biomeLookup.getOrThrow(BiomeKeys.DEEP_OCEAN));
        this.trueDeepColdOcean = BiomeInfo.of(biomeLookup.getOrThrow(BiomeKeys.DEEP_COLD_OCEAN));
        this.trueDeepFrozenOcean = BiomeInfo.of(biomeLookup.getOrThrow(BiomeKeys.DEEP_FROZEN_OCEAN));

        this.dummyConversionMap = Map.ofEntries(
            Map.entry(DummyBiome.WARM_OCEAN.biomeInfo, this.trueWarmOcean),
            Map.entry(DummyBiome.LUKEWARM_OCEAN.biomeInfo, this.trueLukewarmOcean),
            Map.entry(DummyBiome.OCEAN.biomeInfo, this.trueOcean),
            Map.entry(DummyBiome.COLD_OCEAN.biomeInfo, this.trueColdOcean),
            Map.entry(DummyBiome.FROZEN_OCEAN.biomeInfo, this.trueFrozenOcean)
        );
        this.dummyDeepConversionMap = Map.ofEntries(
            Map.entry(DummyBiome.WARM_OCEAN.biomeInfo, this.trueWarmOcean),
            Map.entry(DummyBiome.LUKEWARM_OCEAN.biomeInfo, this.trueDeepLukewarmOcean),
            Map.entry(DummyBiome.OCEAN.biomeInfo, this.trueDeepOcean),
            Map.entry(DummyBiome.COLD_OCEAN.biomeInfo, this.trueDeepColdOcean),
            Map.entry(DummyBiome.FROZEN_OCEAN.biomeInfo, this.trueDeepFrozenOcean)
        );
    }

    @Override
    public void setWorldSeed(long seed) {
        super.setWorldSeed(seed);
        this.ocean.setWorldSeed(seed);
    }

    @Override
    protected BiomeInfo[] getNewBiomes(int x, int z, int width, int length) {
        int bX = x - 8;
        int bZ = z - 8;
        int bWidth = width + 16;
        int bLength = length + 16;

        BiomeInfo[] biomes = this.biomes.getBiomes(bX, bZ, bWidth, bLength);
        BiomeInfo[] ocean = this.ocean.getBiomes(x, z, width, length);
        BiomeInfo[] output = new BiomeInfo[width * length];

        for (int zz = 0; zz < length; zz++) {
            biomeLoop: for (int xx = 0; xx < width; xx++) {
                BiomeInfo biomeHere = biomes[(xx + 8) + (zz + 8) * bWidth];
                BiomeInfo oceanHere = ocean[xx + zz * width];

                if (!biomeHere.biome().isIn(BiomeTags.IS_OCEAN)) {
                    output[xx + zz * width] = biomeHere;
                    continue;
                }

                if (oceanHere.biome().equals(DummyBiome.WARM_OCEAN)
                        || oceanHere.biome().equals(DummyBiome.FROZEN_OCEAN)) {
                    for (int sx = -8; sx <= 8; sx += 4) {
                        for (int sz = -8; sz <= 8; sz += 4) {
                            BiomeInfo biomeThere = biomes[(sx + 8) + (sz + 8) * bWidth];
                            if (!biomeThere.biome().isIn(BiomeTags.IS_OCEAN)) {
                                if (oceanHere.biome().equals(DummyBiome.WARM_OCEAN)) {
                                    output[xx + zz * width] = this.trueLukewarmOcean;
                                } else if (oceanHere.biome().equals(DummyBiome.FROZEN_OCEAN)) {
                                    output[xx + zz * width] = this.trueColdOcean;
                                }
                                continue biomeLoop;
                            }
                        }
                    }
                }

                output[xx + zz * width] = (biomeHere.equals(this.trueDeepOcean)
                    ? this.dummyDeepConversionMap : this.dummyConversionMap)
                    .get(oceanHere);
            }
        }

        return output;
    }
}
