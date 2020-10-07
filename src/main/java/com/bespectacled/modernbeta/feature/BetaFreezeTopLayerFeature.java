package com.bespectacled.modernbeta.feature;

import java.util.Random;

import com.bespectacled.modernbeta.util.BiomeMath;
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
    public BetaFreezeTopLayerFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }
    
    public void setSeed(long seed) {
        BiomeMath.initOctaves(seed);
    }
    
    @Override
    public boolean generate(
        StructureWorldAccess structureWorldAccess, 
        ChunkGenerator chunkGenerator, 
        Random random, BlockPos blockPos, 
        DefaultFeatureConfig defaultFeatureConfig
    ) {
        
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        BlockPos.Mutable mutableDown = new BlockPos.Mutable();
        
        int chunkX = blockPos.getX() / 16; // Divide first to truncate to closest chunk coordinate
        int chunkZ = blockPos.getZ() / 16;
        
        BiomeMath.fetchTempHumid(chunkX * 16, chunkZ * 16, 16, 16);
        
        int i = 0;
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int curX = blockPos.getX() + x;
                int curZ = blockPos.getZ() + z;
                int curY = structureWorldAccess.getTopY(Heightmap.Type.MOTION_BLOCKING, curX, curZ);
                
                
                mutable.set(curX, curY, curZ);
                mutableDown.set(mutable).move(Direction.DOWN, 1);
                
                if (canSetIce(structureWorldAccess, mutableDown, false, BiomeMath.temps[i])) {
                    structureWorldAccess.setBlockState(mutableDown, Blocks.ICE.getDefaultState(), 2);
                }
                
                if (canSetSnow(structureWorldAccess, mutable, BiomeMath.temps[i])) {
                    structureWorldAccess.setBlockState(mutable, Blocks.SNOW.getDefaultState(), 2);
                    
                    BlockState blockState = structureWorldAccess.getBlockState(mutableDown);
                    if (blockState.contains(SnowyBlock.SNOWY)) {
                        structureWorldAccess.setBlockState(mutableDown, blockState.with(SnowyBlock.SNOWY, true), 2);
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
                
                boolean boolean7 = worldView.isWater(blockPos.west()) && worldView.isWater(blockPos.east()) && worldView.isWater(blockPos.north()) && worldView.isWater(blockPos.south());
                if (!boolean7) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean canSetSnow(WorldView worldView, BlockPos blockPos, double temp) {
        double heightTemp = temp - ((double)(blockPos.getY() - 64) / 64D) * 0.29999999999999999D;
        
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
