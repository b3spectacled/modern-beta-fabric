package com.bespectacled.modernbeta.mixin.client;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockRenderView;

import java.util.Random;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.beta.BetaClimateSampler;
import com.bespectacled.modernbeta.config.ModernBetaConfig;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.MutableBlockColors;

@Mixin(value = BlockColors.class, priority = 1)
public class MixinBlockColors implements MutableBlockColors {

    @Unique private static ModernBetaConfig BETA_CONFIG = ModernBeta.BETA_CONFIG;
    @Unique private static boolean useBetaColors = false;
    
    @Unique private static final BlockState GRASS = Blocks.GRASS.getDefaultState();
    @Unique private static final BlockState FERN = Blocks.FERN.getDefaultState();
    @Unique private static final BlockState TALL_GRASS = Blocks.TALL_GRASS.getDefaultState();
    @Unique private static final BlockState TALL_FERN = Blocks.LARGE_FERN.getDefaultState();
    
    @Unique private static final double[] TEMP_HUMID = new double[2];
    @Unique private static final PerlinOctaveNoise COLOR_NOISE = new PerlinOctaveNoise(new Random(), 1, true);

    @Unique
    public void setSeed(long seed) {
        BetaClimateSampler.INSTANCE.setSeed(seed);
    }

    @Unique
    public void setSeed(long seed, boolean isBetaWorld) {
        if (isBetaWorld)
            BetaClimateSampler.INSTANCE.setSeed(seed);
        
        useBetaColors = isBetaWorld;
    }

    @Dynamic("Reed color lambda method")
    @Inject(method = "method_1685", at = @At("HEAD"), cancellable = true)
    private static void onReedColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIdx,
            CallbackInfoReturnable<Integer> info) {
        if (BETA_CONFIG.renderingConfig.renderBetaBiomeColor)
            info.setReturnValue(0xFFFFFF);
    }

    @Dynamic("Two high grass color lambda method")
    @Inject(method = "method_1686", at = @At("HEAD"), cancellable = true)
    private static void onDoubleTallGrassColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIdx,
            CallbackInfoReturnable<Integer> info) {
        if (BETA_CONFIG.renderingConfig.renderBetaBiomeColor && useBetaColors)
            info.setReturnValue(getGrassColor(state, world, pos));
    }

    @Dynamic("Grass color lambda method")
    @Inject(method = "method_1693", at = @At("HEAD"), cancellable = true)
    private static void onGrassColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIdx,
            CallbackInfoReturnable<Integer> info) {
        if (BETA_CONFIG.renderingConfig.renderBetaBiomeColor && useBetaColors)
            info.setReturnValue(getGrassColor(state, world, pos));
    }

    @Dynamic("Foliage color lambda method")
    @Inject(method = "method_1692", at = @At("HEAD"), cancellable = true)
    private static void onFoliageColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIdx,
            CallbackInfoReturnable<Integer> info) {
        if (BETA_CONFIG.renderingConfig.renderBetaBiomeColor && useBetaColors)
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
        
        double tempModifier = MathHelper.clamp(1.0 - (256D - y) / 128D, 0.0, 0.5);
        //double tempModifier = 0.0;
        
        //double modifier = COLOR_NOISE.sample(x >> 2, z >> 2) * 0.1;

        if (state.equals(GRASS) || state.equals(FERN) || state.equals(TALL_GRASS) || state.equals(TALL_FERN)) {
            long shift = x * 0x2fc20f + z * 0x5d8875 + y;
            shift = shift * shift * 0x285b825L + shift * 11L;
            x = (int) ((long) x + (shift >> 14 & 31L));
            y = (int) ((long) y + (shift >> 19 & 31L));
            z = (int) ((long) z + (shift >> 24 & 31L));
        }

        BetaClimateSampler.INSTANCE.sampleTempHumid(TEMP_HUMID, x, z);
        return GrassColors.getColor(MathHelper.clamp(TEMP_HUMID[0] - tempModifier, 0.0, 1.0), TEMP_HUMID[1]);
    }

    @Unique
    private static int getFoliageColor(BlockRenderView world, BlockPos pos) {
        if (world == null || pos == null) { // Appears to enter here when loading color for inventory block
            return 4764952; // Default tint, from wiki
        }

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        
        double tempModifier = MathHelper.clamp(1.0 - (256D - y) / 128D, 0.0, 0.5);
        //double tempModifier = 0.0;

        BetaClimateSampler.INSTANCE.sampleTempHumid(TEMP_HUMID, x, z);
        return FoliageColors.getColor(MathHelper.clamp(TEMP_HUMID[0] - tempModifier, 0.0, 1.0), TEMP_HUMID[1]);
    }
}
