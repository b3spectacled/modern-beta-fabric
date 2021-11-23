package com.bespectacled.modernbeta.mixin;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;

import net.minecraft.util.math.ChunkPos;
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
        int chunkX,
        int chunkZ,
        Biome biome,
        ChunkPos pos,
        DefaultFeatureConfig featureConfig,
        CallbackInfoReturnable<Boolean> info
    ) {
        if (chunkGenerator instanceof OldChunkGenerator oldChunkGenerator) {
            int offsetX = (chunkX << 4) + 9;
            int offsetZ = (chunkZ << 4) + 9; 
                    
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
