package com.bespectacled.modernbeta.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.gen.NoiseColumnSampler;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;

@Mixin(NoiseColumnSampler.class)
public interface MixinNoiseColumnSamplerInvoker {
    @Invoker("createAquiferSampler")
    public AquiferSampler invokeCreateAquiferSampler(
        ChunkNoiseSampler chunkNoiseSampler, 
        int x, 
        int z, 
        int minimumY, 
        int height, 
        AquiferSampler.FluidLevelSampler fluidLevelSampler, 
        boolean hasAquifers
    );
}
