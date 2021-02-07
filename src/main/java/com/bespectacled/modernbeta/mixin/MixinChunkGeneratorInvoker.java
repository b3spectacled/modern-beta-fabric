package com.bespectacled.modernbeta.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.structure.StructureManager;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;

@Mixin(ChunkGenerator.class)
public interface MixinChunkGeneratorInvoker {
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
}
