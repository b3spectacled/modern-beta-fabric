package com.bespectacled.modernbeta.api.world.biome;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.bespectacled.modernbeta.util.BlockStates;

import net.minecraft.block.BlockState;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockRenderView;

public enum BetaColorResolver {;
    
    private static final HashSet<BlockState> GRASS_BLOCKS = Stream.of(
        BlockStates.GRASS,
        BlockStates.FERN,
        BlockStates.TALL_GRASS,
        BlockStates.TALL_FERN
    ).collect(Collectors.toCollection(HashSet::new));
    
    public static void setSeed(long seed) {
        BetaClimateSampler.INSTANCE.setSeed(seed);
    }
    
    public static int getGrassColor(BlockState state, BlockRenderView world, BlockPos pos) {
        if (world == null || pos == null) { // Appears to enter here when loading color for inventory block
            return 8174955; // Default tint, from wiki
        }

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        if (GRASS_BLOCKS.contains(state)) {
            long shift = x * 0x2fc20f + z * 0x5d8875 + y;
            shift = shift * shift * 0x285b825L + shift * 11L;
            x = (int) ((long) x + (shift >> 14 & 31L));
            y = (int) ((long) y + (shift >> 19 & 31L));
            z = (int) ((long) z + (shift >> 24 & 31L));
        }

        double temp = BetaClimateSampler.INSTANCE.sampleTemp(x, z);
        double rain = BetaClimateSampler.INSTANCE.sampleRain(x, z);

        return GrassColors.getColor(temp, rain);
    }
    
    public static int getFoliageColor(BlockRenderView world, BlockPos pos) {
        if (world == null || pos == null) { // Appears to enter here when loading color for inventory block
            return 4764952; // Default tint, from wiki
        }

        int x = pos.getX();
        int z = pos.getZ();
        
        double temp = BetaClimateSampler.INSTANCE.sampleTemp(x, z);
        double rain = BetaClimateSampler.INSTANCE.sampleRain(x, z);
        
        return FoliageColors.getColor(temp, rain);
    }
    
    @SuppressWarnings("unused")
    private static double getTempOffset(int y) {
        return MathHelper.clamp(1.0 - (256D - y) / 128D, 0.0, 0.5);
    }
}
