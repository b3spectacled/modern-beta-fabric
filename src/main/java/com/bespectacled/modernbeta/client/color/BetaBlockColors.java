package com.bespectacled.modernbeta.client.color;

import java.util.Arrays;
import java.util.Optional;

import com.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import com.bespectacled.modernbeta.api.world.biome.climate.Clime;

import net.minecraft.block.BlockState;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockRenderView;

public final class BetaBlockColors {
    public static final BetaBlockColors INSTANCE = new BetaBlockColors();
    
    private static final int WORLD_MIN_Y = -64;
    private static final int WORLD_HEIGHT = 384;
    private static final double[] TEMP_OFFSETS = new double[WORLD_HEIGHT];
    
    private Optional<ClimateSampler> climateSampler;
    
    private BetaBlockColors() {
        this.climateSampler = Optional.empty();
    }
    
    public void setSeed(long seed, Optional<ClimateSampler> climateSampler) {
        this.climateSampler = climateSampler;
    }
    
    public int getGrassColor(BlockState state, BlockRenderView view, BlockPos pos, int tintNdx) {
        if (view == null || pos == null) { // Appears to enter here when loading color for inventory block
            return 8174955; // Default tint, from wiki
        }
        
        if (this.climateSampler.isPresent() && this.climateSampler.get().sampleBiomeColor()) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            Clime clime = this.climateSampler.get().sampleClime(x, z);
            double temp = MathHelper.clamp(clime.temp() + getTempOffset(y), 0.0, 1.0);
            double rain = clime.rain();

            return GrassColors.getColor(temp, rain);
        }
        
        return BiomeColors.getGrassColor(view, pos);
    }
    
    public int getTallGrassColor(BlockState state, BlockRenderView view, BlockPos pos, int tintNdx) {
        if (view == null || pos == null) { // Appears to enter here when loading color for inventory block
            return 8174955; // Default tint, from wiki
        }
        
        if (this.climateSampler.isPresent() && this.climateSampler.get().sampleBiomeColor()) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            long shift = x * 0x2fc20f + z * 0x5d8875 + y;
            shift = shift * shift * 0x285b825L + shift * 11L;
            x = (int) ((long) x + (shift >> 14 & 31L));
            y = (int) ((long) y + (shift >> 19 & 31L));
            z = (int) ((long) z + (shift >> 24 & 31L));
            
            Clime clime = this.climateSampler.get().sampleClime(x, z);
            double temp = MathHelper.clamp(clime.temp() + getTempOffset(y), 0.0, 1.0);
            double rain = clime.rain();

            return GrassColors.getColor(temp, rain);
        }
        
        return BiomeColors.getGrassColor(view, pos);
    }
    
    public int getFoliageColor(BlockState state, BlockRenderView view, BlockPos pos, int tintNdx) {
        if (view == null || pos == null) { // Appears to enter here when loading color for inventory block
            return 4764952; // Default tint, from wiki
        }
        
        if (this.climateSampler.isPresent() && this.climateSampler.get().sampleBiomeColor()) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            
            Clime clime = this.climateSampler.get().sampleClime(x, z);
            double temp = MathHelper.clamp(clime.temp() + getTempOffset(y), 0.0, 1.0);
            double rain = clime.rain();
            
            return FoliageColors.getColor(temp, rain);
        }
        
        return BiomeColors.getFoliageColor(view, pos);
    }
    
    public int getReedColor(BlockState state, BlockRenderView view, BlockPos pos, int tintNdx) {
        if (this.climateSampler.isPresent() && this.climateSampler.get().sampleBiomeColor()) {
            return 0xFFFFFF;
        }
        
        return BiomeColors.getGrassColor(view, pos);
    }
    
    private static double getTempOffset(int y) {
        y = MathHelper.clamp(y, WORLD_MIN_Y, WORLD_HEIGHT + WORLD_MIN_Y);
        y += -WORLD_MIN_Y;
        
        return 0.0;
        //return TEMP_OFFSETS[y];
    }
    
    private static double getUndergroundTempOffset(int y) {
        double offset = y / 128D;
        
        return MathHelper.clamp(offset, -0.5, 0.0);
    }
    
    private static double getAboveTempOffset(int y) {
        double offset = 1.0 - y / 128D;
        
        return MathHelper.clamp(offset, -0.5, 0.0);
    }
    
    static {
        Arrays.fill(TEMP_OFFSETS, 0.0);
        
        for (int y = 0; y < 384; ++y) {
            int worldY = y + WORLD_MIN_Y;
            
            TEMP_OFFSETS[y] += getUndergroundTempOffset(worldY);
            TEMP_OFFSETS[y] += getAboveTempOffset(worldY);
        }
    }
}
