package mod.bespectacled.modernbeta.api.world.chunk;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.world.biome.HeightConfig;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.biome.provider.fractal.BiomeInfo;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.util.math.MathHelper;
import org.slf4j.event.Level;

import java.util.HashMap;
import java.util.Map;

public abstract class ChunkProviderForcedHeight extends ChunkProviderNoise {
    private static final float[] BIOME_HEIGHT_WEIGHTS = new float[25];

    private final Map<BiomeInfo, HeightConfig> heightOverrideCache = new HashMap<>();

    static {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                float value = 10.0F / MathHelper.sqrt((float)(x * x + z * z) + 0.2F);
                BIOME_HEIGHT_WEIGHTS[x + 2 + (z + 2) * 5] = value;
            }
        }
    }

    public ChunkProviderForcedHeight(ModernBetaChunkGenerator chunkGenerator, long seed) {
        super(chunkGenerator, seed);
    }

    public BiomeInfo getBiomeInfo(int biomeX, int biomeZ) {
        if (chunkGenerator.getBiomeSource() instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
            return modernBetaBiomeSource.getBiomeForHeightGen(biomeX, 16, biomeZ);
        } else {
            return BiomeInfo.of(this.getBiome(biomeX, 16, biomeZ, null));
        }
    }

    public HeightConfig getHeightConfig(BiomeInfo biomeInfo) {
        HeightConfig config = HeightConfig.getHeightConfig(biomeInfo);
        String id = biomeInfo.getId();
        if (this.chunkSettings.releaseHeightOverrides.containsKey(id)) {
            HeightConfig fallbackConfig = config;
            config = this.heightOverrideCache.computeIfAbsent(biomeInfo, k -> {
                String heightConfigString = this.chunkSettings.releaseHeightOverrides.get(id);
                String[] heightConfigPair = heightConfigString.split(";");
                try {
                    float scale = Float.parseFloat(heightConfigPair[0]);
                    float depth = Float.parseFloat(heightConfigPair[1]);
                    return new HeightConfig(scale, depth);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {
                    ModernBeta.log(Level.WARN, String.format("Invalid height config \"%s\"", heightConfigString));
                    return fallbackConfig;
                }
            });
        }

        return config;
    }

    public HeightConfig getRawHeightConfigAt(int x, int z) {
        return this.getHeightConfig(this.getBiomeInfo(x, z));
    }

    public HeightConfig getHeightConfigAt(int noiseX, int noiseZ) {
        float scale = 0.0F;
        float depth = 0.0F;
        float totalWeight = 0.0F;

        BiomeInfo biome = this.getBiomeInfo(noiseX, noiseZ);
        double minSurfaceHeight = this.getHeightConfig(biome).depth();

        for (int biomeX = -2; biomeX <= 2; biomeX++) {
            for (int biomeZ = -2; biomeZ <= 2; biomeZ++) {
                biome = this.getBiomeInfo(noiseX + biomeX, noiseZ + biomeZ);
                HeightConfig heightConfig = this.getHeightConfig(biome);

                float thisScale = this.chunkSettings.releaseBiomeScaleOffset + heightConfig.scale() * this.chunkSettings.releaseBiomeScaleWeight;
                float thisDepth = this.chunkSettings.releaseBiomeDepthOffset + heightConfig.depth() * this.chunkSettings.releaseBiomeDepthWeight;

                float weight = BIOME_HEIGHT_WEIGHTS[biomeX + 2 + (biomeZ + 2) * 5] / (thisDepth + 2.0F);
                if (heightConfig.depth() > minSurfaceHeight) {
                    weight /= 2.0F;
                }

                scale += thisScale * weight;
                depth += thisDepth * weight;
                totalWeight += weight;
            }
        }

        scale /= totalWeight;
        depth /= totalWeight;
        scale = scale * 0.9F + 0.1F;
        depth = (depth * 4.0F - 1.0F) / 8.0F;

        return new HeightConfig(depth, scale);
    }
}
