package mod.bespectacled.modernbeta.mixin;

import java.util.Optional;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.structure.OceanMonumentStructure;
import net.minecraft.world.gen.structure.Structure;

@Mixin(OceanMonumentStructure.class)
public class MixinOceanMonumentFeature {
    @Inject(method = "getStructurePosition", at = @At("HEAD"), cancellable = true) 
    private void injectGetStructurePosition(Structure.Context context, CallbackInfoReturnable<Optional<Structure.StructurePosition>> info) {
        ChunkGenerator chunkGenerator = context.chunkGenerator();
        
        if (chunkGenerator instanceof ModernBetaChunkGenerator oldChunkGenerator) {
            int offsetX = context.chunkPos().getOffsetX(9);
            int offsetZ = context.chunkPos().getOffsetZ(9);
            
            boolean shouldStartAt = true;
            
            if (shouldStartAt) {
                
                Set<RegistryEntry<Biome>> set = oldChunkGenerator.getBiomesInArea(
                    offsetX,
                    chunkGenerator.getSeaLevel(),
                    offsetZ,
                    29,
                    context.noiseConfig().getMultiNoiseSampler()
                );
                
                for (RegistryEntry<Biome> biome : set) {
                    if (biome.isIn(BiomeTags.REQUIRED_OCEAN_MONUMENT_SURROUNDING)) continue;
                    
                    info.setReturnValue(Optional.empty());
                }
            }
        }
    }
    
    /*
     * public Optional<Structure.StructurePosition> getStructurePosition(Structure.Context context) {
        int i = context.chunkPos().getOffsetX(9);
        int j = context.chunkPos().getOffsetZ(9);
        Set<RegistryEntry<Biome>> set = context.biomeSource().getBiomesInArea(i, context.chunkGenerator().getSeaLevel(), j, 29, context.noiseConfig().getMultiNoiseSampler());
        for (RegistryEntry<Biome> lv : set) {
            if (lv.isIn(BiomeTags.REQUIRED_OCEAN_MONUMENT_SURROUNDING)) continue;
            return Optional.empty();
        }
        return OceanMonumentStructure.getStructurePosition(context, Heightmap.Type.OCEAN_FLOOR_WG, collector -> OceanMonumentStructure.addPieces(collector, context));
    }
     */
}
