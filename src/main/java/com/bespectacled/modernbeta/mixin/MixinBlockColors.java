package com.bespectacled.modernbeta.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.config.ModernBetaConfig;
import com.bespectacled.modernbeta.util.BiomeMath;
import com.bespectacled.modernbeta.util.MutableBlockColors;

@Mixin(value = BlockColors.class, priority = 1)
public class MixinBlockColors implements MutableBlockColors {

    @Unique
    private static ModernBetaConfig BETA_CONFIG = ModernBeta.BETA_CONFIG;

    private static boolean defaultColors = false;

    @Unique
    @Override
    public void setSeed(long seed) {
        BiomeMath.setSeed(BETA_CONFIG.fixedSeed != 0L ? BETA_CONFIG.fixedSeed : seed);
    }

    @Unique
    @Override
    public void setSeed(long seed, boolean useDefaultColors) {
        if (!useDefaultColors)
            BiomeMath.setSeed(BETA_CONFIG.fixedSeed != 0L ? BETA_CONFIG.fixedSeed : seed);
        else
            defaultColors = useDefaultColors;
    }

    @Dynamic("Reed color lambda method")
    @Inject(method = "method_1685", at = @At("HEAD"), cancellable = true)
    private static void onReedColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIdx,
            CallbackInfoReturnable<Integer> info) {
        if (BETA_CONFIG.renderBetaBiomeColor && !defaultColors)
            info.setReturnValue(0xFFFFFF);
    }

    @Dynamic("Two high grass color lambda method")
    @Inject(method = "method_1686", at = @At("HEAD"), cancellable = true)
    private static void onDoubleTallGrassColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIdx,
            CallbackInfoReturnable<Integer> info) {
        if (BETA_CONFIG.renderBetaBiomeColor && !defaultColors)
            info.setReturnValue(getGrassColor(state, world, pos));
    }

    @Dynamic("Grass color lambda method")
    @Inject(method = "method_1693", at = @At("HEAD"), cancellable = true)
    private static void onGrassColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIdx,
            CallbackInfoReturnable<Integer> info) {
        if (BETA_CONFIG.renderBetaBiomeColor && !defaultColors)
            info.setReturnValue(getGrassColor(state, world, pos));
    }

    @Dynamic("Foliage color lambda method")
    @Inject(method = "method_1692", at = @At("HEAD"), cancellable = true)
    private static void onFoliageColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIdx,
            CallbackInfoReturnable<Integer> info) {
        if (BETA_CONFIG.renderBetaBiomeColor && !defaultColors)
            info.setReturnValue(getFoliageColor(world, pos));
    }

    @Unique
    private static int getGrassColor(BlockState state, BlockRenderView world, BlockPos pos) {
        if (world == null || pos == null) { // Appears to enter here when loading color for inventory block
            return 8174955; // Default tint, from wiki
        }

        double[] tempHumids;
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        if (state.equals(Blocks.GRASS.getDefaultState()) || state.equals(Blocks.FERN.getDefaultState())
                || state.equals(Blocks.TALL_GRASS.getDefaultState())
                || state.equals(Blocks.LARGE_FERN.getDefaultState())) {
            long shift = x * 0x2fc20f + z * 0x5d8875 + y;
            shift = shift * shift * 0x285b825L + shift * 11L;
            x = (int) ((long) x + (shift >> 14 & 31L));
            y = (int) ((long) y + (shift >> 19 & 31L));
            z = (int) ((long) z + (shift >> 24 & 31L));
        }

        tempHumids = BiomeMath.fetchTempHumidAtPoint(x, z);
        return GrassColors.getColor(tempHumids[0], tempHumids[1]);
    }

    @Unique
    private static int getFoliageColor(BlockRenderView world, BlockPos pos) {
        if (world == null || pos == null) { // Appears to enter here when loading color for inventory block
            return 4764952; // Default tint, from wiki
        }

        double[] tempHumids;
        int x = pos.getX();
        int z = pos.getZ();

        tempHumids = BiomeMath.fetchTempHumidAtPoint(x, z);
        return FoliageColors.getColor(tempHumids[0], tempHumids[1]);
    }
}
