package com.bespectacled.modernbeta.mixin;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.OceanMonumentFeature;

@Mixin(OceanMonumentFeature.class)
public class MixinOceanMonumentFeature {
    @Inject(method = "shouldStartAt", at = @At("HEAD"), cancellable = true)
    private void injectShouldStartAt(
        ChunkGenerator chunkGenerator,
        BiomeSource biomeSource,
        long worldSeed,
        ChunkRandom random,
        ChunkPos pos,
        Biome biome,
        ChunkPos arg5,
        DefaultFeatureConfig featureConfig,
        HeightLimitView world,
        CallbackInfoReturnable<Boolean> info
    ) {
        if (chunkGenerator instanceof OldChunkGenerator oldChunkGenerator) {
            int offsetX = pos.getOffsetX(9);
            int offsetZ = pos.getOffsetZ(9);
            
            boolean shouldStartAt = oldChunkGenerator.generatesMonuments();
            
            if (shouldStartAt) {
                Set<Biome> set0 = oldChunkGenerator.getBiomesInArea(offsetX, chunkGenerator.getSeaLevel(), offsetZ, 16);
                for (Biome b : set0) {
                    if (b.getGenerationSettings().hasStructureFeature((OceanMonumentFeature)(Object)this)) continue;
                    
                    shouldStartAt = false;
                }
                
                if (shouldStartAt) {
                    Set<Biome> set1 = oldChunkGenerator.getBiomesInArea(offsetX, chunkGenerator.getSeaLevel(), offsetZ, 29);
                    for (Biome b : set1) {
                        if (b.getCategory() == Biome.Category.OCEAN || b.getCategory() == Biome.Category.RIVER) continue;
                        
                        shouldStartAt = false;
                    }
                }
            }

            info.setReturnValue(shouldStartAt);
        }
    }
}
