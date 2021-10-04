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
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.OceanMonumentFeature;
import net.minecraft.world.gen.random.ChunkRandom;

@Mixin(OceanMonumentFeature.class)
public class MixinOceanMonumentFeature {
    @Inject(method = "shouldStartAt", at = @At("HEAD"), cancellable = true)
    private void injectShouldStartAt(
        ChunkGenerator chunkGenerator,
        BiomeSource biomeSource,
        long worldSeed,
        ChunkRandom random,
        ChunkPos pos, 
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
                Set<Biome> set = oldChunkGenerator.getBiomesInArea(offsetX, chunkGenerator.getSeaLevel(), offsetZ, 29);
                
                for (Biome biome : set) {
                    if (biome.getCategory() == Biome.Category.OCEAN || biome.getCategory() == Biome.Category.RIVER) continue;
                    
                    shouldStartAt = false;
                }
            }
            
            
            info.setReturnValue(shouldStartAt);
        }
    }
}
