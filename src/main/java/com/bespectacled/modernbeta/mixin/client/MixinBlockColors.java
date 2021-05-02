package com.bespectacled.modernbeta.mixin.client;

import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockRenderView;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.biome.BetaClimateResolver;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.MutableBlockColors;

@Mixin(value = BlockColors.class, priority = 1)
public class MixinBlockColors implements MutableBlockColors {
    @Unique private static boolean useBetaColors = false;
    
    @Unique private static final BetaClimateResolverWrapper BETA_CLIMATE_RESOLVER = new BetaClimateResolverWrapper();

    @Unique
    public void setSeed(long seed) {
        BETA_CLIMATE_RESOLVER.setSeed(seed);
    }

    @Unique
    public void setSeed(long seed, boolean isBetaWorld) {
        if (isBetaWorld)
            BETA_CLIMATE_RESOLVER.setSeed(seed);
        
        useBetaColors = isBetaWorld;
    }

    @Dynamic("Reed color lambda method")
    @Inject(method = "method_1685", at = @At("HEAD"), cancellable = true)
    private static void onReedColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIdx,
            CallbackInfoReturnable<Integer> info) {
        if (ModernBeta.RENDER_CONFIG.renderBetaBiomeColor)
            info.setReturnValue(0xFFFFFF);
    }

    @Dynamic("Two high grass color lambda method")
    @Inject(method = "method_1686", at = @At("HEAD"), cancellable = true)
    private static void onDoubleTallGrassColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIdx,
            CallbackInfoReturnable<Integer> info) {
        if (ModernBeta.RENDER_CONFIG.renderBetaBiomeColor && useBetaColors)
            info.setReturnValue(getGrassColor(state, world, pos));
    }

    @Dynamic("Grass color lambda method")
    @Inject(method = "method_1693", at = @At("HEAD"), cancellable = true)
    private static void onGrassColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIdx,
            CallbackInfoReturnable<Integer> info) {
        if (ModernBeta.RENDER_CONFIG.renderBetaBiomeColor && useBetaColors)
            info.setReturnValue(getGrassColor(state, world, pos));
    }

    @Dynamic("Foliage color lambda method")
    @Inject(method = "method_1692", at = @At("HEAD"), cancellable = true)
    private static void onFoliageColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIdx,
            CallbackInfoReturnable<Integer> info) {
        if (ModernBeta.RENDER_CONFIG.renderBetaBiomeColor && useBetaColors)
            info.setReturnValue(getFoliageColor(world, pos));
    }

    @Unique
    private static int getGrassColor(BlockState state, BlockRenderView world, BlockPos pos) {
        if (world == null || pos == null) { // Appears to enter here when loading color for inventory block
            return 8174955; // Default tint, from wiki
        }

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        
        //double tempModifier = getTempOffset(y);
        double tempModifier = 0.0;

        if (state.equals(BlockStates.GRASS) ||
            state.equals(BlockStates.FERN) || 
            state.equals(BlockStates.TALL_GRASS) || 
            state.equals(BlockStates.TALL_FERN)
        ) {
            long shift = x * 0x2fc20f + z * 0x5d8875 + y;
            shift = shift * shift * 0x285b825L + shift * 11L;
            x = (int) ((long) x + (shift >> 14 & 31L));
            y = (int) ((long) y + (shift >> 19 & 31L));
            z = (int) ((long) z + (shift >> 24 & 31L));
        }

        double temp = BETA_CLIMATE_RESOLVER.sampleTemp(x, z);
        double humid = BETA_CLIMATE_RESOLVER.sampleHumid(x, z);

        return GrassColors.getColor(MathHelper.clamp(temp - tempModifier, 0.0, 1.0), humid);
    }

    @Unique
    private static int getFoliageColor(BlockRenderView world, BlockPos pos) {
        if (world == null || pos == null) { // Appears to enter here when loading color for inventory block
            return 4764952; // Default tint, from wiki
        }

        int x = pos.getX();
        //int y = pos.getY();
        int z = pos.getZ();
        
        //double tempModifier = getTempOffset(y);
        double tempModifier = 0.0;

        double temp = BETA_CLIMATE_RESOLVER.sampleTemp(x, z);
        double humid = BETA_CLIMATE_RESOLVER.sampleHumid(x, z);
        
        return FoliageColors.getColor(MathHelper.clamp(temp - tempModifier, 0.0, 1.0), humid);
    }
    
    @Unique
    private static double getTempOffset(int y) {
        return MathHelper.clamp(1.0 - (256D - y) / 128D, 0.0, 0.5);
    }
    
    @Unique
    private static class BetaClimateResolverWrapper implements BetaClimateResolver {}
}
