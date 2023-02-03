package mod.bespectacled.modernbeta.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.Structure.Context;
import net.minecraft.world.gen.structure.Structure.StructurePosition;

@Mixin(Structure.class)
public class MixinStructure {
    @Inject(method = "isBiomeValid", at = @At("HEAD"), cancellable = true)
    private static void injectIsBiomeValid(StructurePosition result, Context context, CallbackInfoReturnable<Boolean> info) {
        BlockPos blockPos = result.position();
        
        if (context.chunkGenerator() instanceof ModernBetaChunkGenerator chunkGenerator) {
            info.setReturnValue(context.biomePredicate().test(chunkGenerator.getInjectedBiomeAtBlock(blockPos.getX(), blockPos.getY(), blockPos.getZ(), context.noiseConfig().getMultiNoiseSampler())));
        }
    }
}
