package mod.bespectacled.modernbeta.world.carver;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.gen.carver.CarverContext;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.surfacebuilder.MaterialRules.MaterialRule;

public class ModernBetaCarverContext extends CarverContext {
    private final NoiseChunkGenerator chunkGenerator;
    
    public ModernBetaCarverContext(
        NoiseChunkGenerator noiseChunkGenerator,
        DynamicRegistryManager registryManager,
        HeightLimitView heightLimitView,
        ChunkNoiseSampler chunkNoiseSampler,
        NoiseConfig noiseConfig,
        MaterialRule materialRule
    ) {
        super(noiseChunkGenerator, registryManager, heightLimitView, chunkNoiseSampler, noiseConfig, materialRule);
        
        this.chunkGenerator = noiseChunkGenerator;
    }

    public NoiseChunkGenerator getChunkGenerator() {
        return this.chunkGenerator;
    }
}
