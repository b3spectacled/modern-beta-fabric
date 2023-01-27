package com.bespectacled.modernbeta.world.feature;

import com.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import com.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.SnowyBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.LightType;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class BetaFreezeTopLayerFeature extends Feature<DefaultFeatureConfig> {
    public BetaFreezeTopLayerFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> featureContext) {
        StructureWorldAccess world = featureContext.getWorld();
        BlockPos pos = featureContext.getOrigin();
        ChunkGenerator generator = featureContext.getGenerator();
        
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        BlockPos.Mutable mutableDown = new BlockPos.Mutable();
        
        for (int localX = 0; localX < 16; ++localX) {
            for (int localZ = 0; localZ < 16; ++localZ) {
                int x = pos.getX() + localX;
                int z = pos.getZ() + localZ;
                int y = world.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z);
                
                mutable.set(x, y, z);
                mutableDown.set(mutable).move(Direction.DOWN, 1);
                
                double temp;
                if (generator.getBiomeSource() instanceof ModernBetaBiomeSource oldBiomeSource && 
                    oldBiomeSource.getBiomeProvider() instanceof ClimateSampler climateSampler &&
                    climateSampler.sampleForFeatureGeneration()
                ) {
                    temp = climateSampler.sample(x, z).temp();
                } else {
                    temp = world.getBiome(mutable).value().getTemperature();
                }
                
                if (canSetIce(world, mutableDown, false, temp)) {
                    world.setBlockState(mutableDown, Blocks.ICE.getDefaultState(), 2);
                }

                if (canSetSnow(world, mutable, temp)) {
                    world.setBlockState(mutable, Blocks.SNOW.getDefaultState(), 2);

                    BlockState blockState = world.getBlockState(mutableDown);
                    if (blockState.contains(SnowyBlock.SNOWY)) {
                        world.setBlockState(mutableDown, blockState.with(SnowyBlock.SNOWY, true), 2);
                    }
                }
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

                boolean submerged = worldView.isWater(blockPos.west()) && worldView.isWater(blockPos.east())
                        && worldView.isWater(blockPos.north()) && worldView.isWater(blockPos.south());
                if (!submerged) {
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
