package com.bespectacled.modernbeta.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.util.collection.IdList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockRenderView;

import java.util.Random;

import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.config.ModernBetaConfig;
import com.bespectacled.modernbeta.gen.BetaChunkGenerator;
import com.bespectacled.modernbeta.gen.SkylandsChunkGenerator;
import com.bespectacled.modernbeta.noise.BetaNoiseGeneratorOctaves2;

@Mixin(value = BlockColors.class, priority = 1) 
public class MixinBlockColors {

    private static BetaNoiseGeneratorOctaves2 tempNoiseOctaves = new BetaNoiseGeneratorOctaves2(new Random(0 * 9871L), 4);
    private static BetaNoiseGeneratorOctaves2 humidNoiseOctaves = new BetaNoiseGeneratorOctaves2(new Random(0 * 39811L), 4);
    private static BetaNoiseGeneratorOctaves2 noiseOctaves = new BetaNoiseGeneratorOctaves2(new Random(0 * 543321L), 2);
    
    private double temps[];
    private double humids[];
    private double noises[];
    
    private static Long SEED = null;
    private static String CUR_GEN = "";
    private static final boolean renderBetaColor = ModernBetaConfig.loadConfig().render_beta_grass_color;
    
    /*
    @Dynamic("Reed grass color lambda method")
    @Inject(method = "method_1685", at = @At("HEAD"), cancellable = true)
    private static void onReedColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIdx, CallbackInfoReturnable<Integer> info) {
        info.setReturnValue(16724639);
    }
    */
    
    @Dynamic("Two high grass color lambda method")
    @Inject(method = "method_1686", at = @At("HEAD"), cancellable = true)
    private static void onDoubleTallGrassColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIdx, CallbackInfoReturnable<Integer> info) {
        if (renderBetaColor) info.setReturnValue(getGrassColor(state, world, pos));
    }
    
    @Dynamic("Grass color lambda method")
    @Inject(method = "method_1693", at = @At("HEAD"), cancellable = true)
    private static void onGrassColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIdx, CallbackInfoReturnable<Integer> info) {
        if (renderBetaColor) info.setReturnValue(getGrassColor(state, world, pos));
    }
    
    @Dynamic("Foliage color lambda method")
    @Inject(method = "method_1692", at = @At("HEAD"), cancellable = true)
    private static void onFoliageColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIdx, CallbackInfoReturnable<Integer> info) {
        if (renderBetaColor) info.setReturnValue(getFoliageColor(world, pos));
    }
    
    
    @Unique
    private static int getGrassColor(BlockState state, BlockRenderView world, BlockPos pos) {
        if (world == null || pos == null) { // Appears to enter here when loading color for inventory block
            return 8174955; // Default tint, from wiki
        }

        if (SEED == null || ModernBeta.GEN != CUR_GEN || ModernBeta.SEED != SEED) {
            ModernBeta.LOGGER.log(Level.INFO, "Seed or gen changed. Re-initing block colors...");
            
            switch(ModernBeta.GEN) {
                case "beta": 
                    SEED = BetaChunkGenerator.seed;
                    break;
                case "skylands":
                    SEED = SkylandsChunkGenerator.seed;
                    break;
                default:
                    SEED = 0L;
            }
            
            initOctaves(SEED);
            CUR_GEN = ModernBeta.GEN;
        }
        
        
        double[] tempHumids;
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        
        if (state.equals(Blocks.GRASS.getDefaultState()) || 
            state.equals(Blocks.FERN.getDefaultState()) ||
            state.equals(Blocks.TALL_GRASS.getDefaultState()) ||
            state.equals(Blocks.LARGE_FERN.getDefaultState())
        ) {
            long shift = x * 0x2fc20f + z * 0x5d8875 + y;
            shift = shift * shift * 0x285b825L + shift * 11L;
            x = (int)((long)x + (shift >> 14 & 31L));
            y = (int)((long)y + (shift >> 19 & 31L));
            z = (int)((long)z + (shift >> 24 & 31L));
        }
        
        tempHumids = getTempHumid(x, z);
        return GrassColors.getColor(tempHumids[0], tempHumids[1]);
    }

    @Unique
    private static int getFoliageColor(BlockRenderView world, BlockPos pos) {
        if (world == null || pos == null) { // Appears to enter here when loading color for inventory block
            return 4764952; // Default tint, from wiki
        }
        
        if (SEED == null || ModernBeta.GEN != CUR_GEN || ModernBeta.SEED != SEED) {
            ModernBeta.LOGGER.log(Level.INFO, "Seed or gen changed. Re-initing block colors...");
            
            switch(ModernBeta.GEN) {
                case "beta": 
                    SEED = BetaChunkGenerator.seed;
                    break;
                case "skylands":
                    SEED = SkylandsChunkGenerator.seed;
                    break;
                default:
                    SEED = 0L;
            }
            
            initOctaves(SEED);
            CUR_GEN = ModernBeta.GEN;
        }
        
        double[] tempHumids;
        int x = pos.getX();
        int z = pos.getZ();
        
        tempHumids = getTempHumid(x, z);
        return FoliageColors.getColor(tempHumids[0], tempHumids[1]);
    }
  
    @Unique
    private static void initOctaves(long seed) {
        tempNoiseOctaves = new BetaNoiseGeneratorOctaves2(new Random(seed * 9871L), 4);
        humidNoiseOctaves = new BetaNoiseGeneratorOctaves2(new Random(seed * 39811L), 4);
        noiseOctaves = new BetaNoiseGeneratorOctaves2(new Random(seed * 0x84a59L), 2);
    }
    
    @Unique
    private static double[] getTempHumid(int x, int z) {
        double[] temps = null;
        double[] humids = null;
        double[] noises = null;
        
        temps = tempNoiseOctaves.func_4112_a(temps, x, z, 1, 1, 0.02500000037252903D, 0.02500000037252903D, 0.25D);
        humids = humidNoiseOctaves.func_4112_a(humids, x, z, 1, 1, 0.05000000074505806D, 0.05000000074505806D, 0.33333333333333331D);
        noises = noiseOctaves.func_4112_a(noises, x, z, 1, 1, 0.25D, 0.25D, 0.58823529411764708D);
        
        double d = noises[0] * 1.1000000000000001D + 0.5D;
        double d1 = 0.01D;
        double d2 = 1.0D - d1;
        
        double temp = (temps[0] * 0.14999999999999999D + 0.69999999999999996D) * d2 + d * d1;
        
        d1 = 0.002D;
        d2 = 1.0D - d1;
        
        double humid = (humids[0] * 0.14999999999999999D + 0.5D) * d2 + d * d1;
        
        temp = 1.0D - (1.0D - temp) * (1.0D - temp);
        if(temp < 0.0D) {
            temp = 0.0D;
        }
        if(humid < 0.0D) {
            humid = 0.0D;
        }
        if(temp > 1.0D) {
            temp = 1.0D;
        }
        if(humid > 1.0D) {
            humid = 1.0D;
        }
        
        return new double[]{temp, humid};
    }
    
}
