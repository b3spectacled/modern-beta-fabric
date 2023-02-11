package mod.bespectacled.modernbeta.world;

import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class ModernBetaWorldInitializer {
    public static void init(MinecraftServer server) {
        Registry<DimensionOptions> registryDimensionOptions = server.getCombinedDynamicRegistries().getCombinedRegistryManager().get(RegistryKeys.DIMENSION);
        long seed = server.getSaveProperties().getGeneratorOptions().getSeed();
        
        registryDimensionOptions.getEntrySet().forEach(entry -> {
            DimensionOptions dimensionOptions = entry.getValue();
            
            ChunkGenerator chunkGenerator = dimensionOptions.chunkGenerator();
            BiomeSource biomeSource = chunkGenerator.getBiomeSource();
            
            if (chunkGenerator instanceof ModernBetaChunkGenerator modernBetaChunkGenerator) {
                modernBetaChunkGenerator.initProvider(seed);
                modernBetaChunkGenerator.getChunkProvider().initForestOctaveNoise();
            }
            
            if (biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
                modernBetaBiomeSource.initProvider(seed);
            }
        });
    }
}
