package com.bespectacled.modernbeta.feature;

import java.util.Arrays;
import java.util.Random;

import com.bespectacled.modernbeta.biome.*;
import com.bespectacled.modernbeta.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.biome.beta.BetaClimateSampler;
import com.bespectacled.modernbeta.util.BiomeUtil;
import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.SnowyBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.Heightmap;
import net.minecraft.world.LightType;

public class BetaFreezeTopLayerFeature extends Feature<DefaultFeatureConfig> {
    private Long seed;
    
    private static final double[] TEMPS = new double[256];
    private static final double[] HUMIDS = new double[256];

    public BetaFreezeTopLayerFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);

        seed = 0L;
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos,
            DefaultFeatureConfig defaultFeatureConfig) {
        // Shouldn't be used if this isn't an instance of OldBiomeSource
        if (!(chunkGenerator.getBiomeSource() instanceof OldBiomeSource)) return false;
        
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        BlockPos.Mutable mutableDown = new BlockPos.Mutable();

        int chunkX = blockPos.getX() >> 4; // Divide first to truncate to closest chunk coordinate
        int chunkZ = blockPos.getZ() >> 4;
        
        OldBiomeSource betaSource = (OldBiomeSource)chunkGenerator.getBiomeSource();

        if (betaSource.isSkyDim()) {
            Arrays.fill(TEMPS, 0, TEMPS.length, betaSource.getBiomeRegistry().get(BetaBiomes.SKY_ID).getTemperature());
            Arrays.fill(HUMIDS, 0, HUMIDS.length, betaSource.getBiomeRegistry().get(BetaBiomes.SKY_ID).getDownfall());
        } else {
            BetaClimateSampler.getInstance().sampleTempHumid(chunkX << 4, chunkZ << 4, TEMPS, HUMIDS);
        }
        
        
        int i = 0;
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int curX = blockPos.getX() + x;
                int curZ = blockPos.getZ() + z;
                int curY = world.getTopY(Heightmap.Type.MOTION_BLOCKING, curX, curZ);

                mutable.set(curX, curY, curZ);
                mutableDown.set(mutable).move(Direction.DOWN, 1);

                //if (canSetIce(world, mutableDown, false, biomeSource.temps[i])) {
                if (canSetIce(world, mutableDown, false, TEMPS[i])) {
                    world.setBlockState(mutableDown, Blocks.ICE.getDefaultState(), 2);
                }

                //if (canSetSnow(world, mutable, biomeSource.temps[i])) {
                if (canSetSnow(world, mutable, TEMPS[i])) {
                    world.setBlockState(mutable, Blocks.SNOW.getDefaultState(), 2);

                    BlockState blockState = world.getBlockState(mutableDown);
                    if (blockState.contains(SnowyBlock.SNOWY)) {
                        world.setBlockState(mutableDown, blockState.with(SnowyBlock.SNOWY, true), 2);
                    }
                }

                ++i;
            }
        }
        return true;
    }

    private boolean canSetIce(WorldView worldView, BlockPos blockPos, boolean doWaterCheck, double temp) {
        if (temp >= 0.5D) {
            return false;
        }
        if (blockPos.getY() >= 0 && blockPos.getY() < 256 && worldView.getLightLevel(LightType.BLOCK, blockPos) < 10) {
            BlockState blockState = worldView.getBlockState(blockPos);
            FluidState fluidState = worldView.getFluidState(blockPos);

            if (fluidState.getFluid() == Fluids.WATER && blockState.getBlock() instanceof FluidBlock) {
                if (!doWaterCheck) {
                    return true;
                }

                boolean boolean7 = worldView.isWater(blockPos.west()) && worldView.isWater(blockPos.east())
                        && worldView.isWater(blockPos.north()) && worldView.isWater(blockPos.south());
                if (!boolean7) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canSetSnow(WorldView worldView, BlockPos blockPos, double temp) {
        double heightTemp = temp - ((double) (blockPos.getY() - 64) / 64D) * 0.29999999999999999D;

        if (heightTemp >= 0.5D) {
            return false;
        }
        if (blockPos.getY() >= 0 && blockPos.getY() < 256 && worldView.getLightLevel(LightType.BLOCK, blockPos) < 10) {
            BlockState blockState = worldView.getBlockState(blockPos);
            if (blockState.isAir() && Blocks.SNOW.getDefaultState().canPlaceAt(worldView, blockPos)) {
                return true;
            }
        }
        return false;
    }

}
