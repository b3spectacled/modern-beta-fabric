package mod.bespectacled.modernbeta.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.feature.BetaFreezeTopLayerFeature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FreezeTopLayerFeature;
import net.minecraft.world.gen.feature.util.FeatureContext;

/*
 * Mixin is unfortunately needed since vanilla biomes mixed with Beta/PE biomes may confuse feature decorator,
 * and cause some chunks to not properly generate Beta/PE climate-influenced snow/ice layer.
 * 
 * Beta biomes + vanilla cave biomes is a situation where this would commonly occur.
 * 
 * TODO: Revisit this in a little to see if still necessary.
 * 
 * To Test --
 * Version: 6.0+1.19.4
 * World Preset: Beta Realistic
 * Seed: 5078905799035165917
 * Coord: -450 200 -300
 * 
 */
@Mixin(FreezeTopLayerFeature.class)
public class MixinFreezeTopLayerFeature {
    @Inject(method = "generate", at = @At("HEAD"), cancellable = true)
    private void injectGenerate(FeatureContext<DefaultFeatureConfig> context, CallbackInfoReturnable<Boolean> info) {
        StructureWorldAccess world = context.getWorld();
        BlockPos pos = context.getOrigin();
        
        ChunkGenerator chunkGenerator = context.getGenerator();
        BiomeSource biomeSource = chunkGenerator.getBiomeSource();
        
        if (biomeSource instanceof ModernBetaBiomeSource) {
            BetaFreezeTopLayerFeature.setFreezeTopLayer(world, pos, biomeSource);
            
            info.setReturnValue(true);
        }
    }
}
