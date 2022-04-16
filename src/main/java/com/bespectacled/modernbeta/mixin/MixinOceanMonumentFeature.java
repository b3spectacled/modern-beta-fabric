package com.bespectacled.modernbeta.mixin;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;

import net.minecraft.structure.StructureGeneratorFactory;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.OceanMonumentFeature;

@Mixin(OceanMonumentFeature.class)
public class MixinOceanMonumentFeature {
    @SuppressWarnings("deprecation")
    @Inject(method = "canGenerate", at = @At("HEAD"), cancellable = true) 
    private static void injectCanGenerate(StructureGeneratorFactory.Context<DefaultFeatureConfig> structureInfo, CallbackInfoReturnable<Boolean> info) {
        ChunkGenerator chunkGenerator = structureInfo.chunkGenerator();
        
        if (chunkGenerator instanceof OldChunkGenerator oldChunkGenerator) {
            int offsetX = structureInfo.chunkPos().getOffsetX(9);
            int offsetZ = structureInfo.chunkPos().getOffsetZ(9);
            
            boolean shouldStartAt = oldChunkGenerator.generatesMonuments();
            
            if (shouldStartAt) {
                Set<RegistryEntry<Biome>> set = oldChunkGenerator.getBiomesInArea(offsetX, chunkGenerator.getSeaLevel(), offsetZ, 29);
                
                for (RegistryEntry<Biome> biome : set) {
                    if (Biome.getCategory(biome) == Biome.Category.OCEAN || Biome.getCategory(biome) == Biome.Category.RIVER) continue;
                    
                    shouldStartAt = false;
                }
            }
            
            
            info.setReturnValue(shouldStartAt);
        }
    }
}
