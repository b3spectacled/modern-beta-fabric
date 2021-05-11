package com.bespectacled.modernbeta.mixin.client;

import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.biome.BetaColorResolver;
import com.bespectacled.modernbeta.util.MutableBlockColors;

@Mixin(value = BlockColors.class, priority = 1)
public class MixinBlockColors implements MutableBlockColors {
    @Unique private static boolean useBetaColors = false;

    @Unique
    @Override
    public void setSeed(long seed, boolean isBetaWorld) {
        if (isBetaWorld)
            BetaColorResolver.setSeed(seed);
        
        useBetaColors = isBetaWorld;
    }

    @Dynamic("Reed color lambda method")
    @Inject(method = "method_1685", at = @At("HEAD"), cancellable = true)
    private static void onReedColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIdx, CallbackInfoReturnable<Integer> info) {
        if (ModernBeta.RENDER_CONFIG.renderBetaBiomeColor)
            info.setReturnValue(0xFFFFFF); // Do not apply biome color to reed texture.
    }

    @Dynamic("Two high grass color lambda method")
    @Inject(method = "method_1686", at = @At("HEAD"), cancellable = true)
    private static void onDoubleTallGrassColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIdx, CallbackInfoReturnable<Integer> info) {
        if (ModernBeta.RENDER_CONFIG.renderBetaBiomeColor && useBetaColors)
            info.setReturnValue(BetaColorResolver.getGrassColor(state, world, pos));
    }

    @Dynamic("Grass color lambda method")
    @Inject(method = "method_1693", at = @At("HEAD"), cancellable = true)
    private static void onGrassColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIdx, CallbackInfoReturnable<Integer> info) {
        if (ModernBeta.RENDER_CONFIG.renderBetaBiomeColor && useBetaColors)
            info.setReturnValue(BetaColorResolver.getGrassColor(state, world, pos));
    }

    @Dynamic("Foliage color lambda method")
    @Inject(method = "method_1692", at = @At("HEAD"), cancellable = true)
    private static void onFoliageColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIdx, CallbackInfoReturnable<Integer> info) {
        if (ModernBeta.RENDER_CONFIG.renderBetaBiomeColor && useBetaColors)
            info.setReturnValue(BetaColorResolver.getFoliageColor(world, pos));
    }
}
