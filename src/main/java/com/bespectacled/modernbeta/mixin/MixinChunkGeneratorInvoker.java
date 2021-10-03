package com.bespectacled.modernbeta.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.gen.chunk.ChunkGenerator;

@Mixin(ChunkGenerator.class)
public interface MixinChunkGeneratorInvoker {
    /*
    @Invoker("setStructureStart")
    public void invokeSetStructureStart(
        ConfiguredStructureFeature<?, ?> configuredStructureFeature, 
        DynamicRegistryManager dynamicRegistryManager, 
        StructureAccessor structureAccessor, 
        Chunk chunk, 
        StructureManager structureManager, 
        long seed,
        Biome biome
    );
    */
}
