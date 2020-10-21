package com.bespectacled.modernbeta.feature;

import java.util.Random;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.gen.IndevChunkGenerator;
import com.mojang.serialization.Codec;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class IndevHouseFeature extends Feature<DefaultFeatureConfig> {
    public IndevHouseFeature(Codec<DefaultFeatureConfig> config) {
        super(config);
    }
    
    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator generator, Random random, BlockPos pos, DefaultFeatureConfig config) {
        BlockPos topPos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, pos);
        BlockPos.Mutable housePos = new BlockPos.Mutable();
        
        
        int spawnX = topPos.getX();
        int spawnY = topPos.getY() + 1;
        int spawnZ = topPos.getZ();
        
        // Place only one if in indev world
        if (generator instanceof IndevChunkGenerator) {
            int worldSpawnX = ((IndevChunkGenerator)generator).getSpawnX();
            int worldSpawnZ = ((IndevChunkGenerator)generator).getSpawnZ();
            
            int worldChunkX = worldSpawnX / 16;
            int worldChunkZ = worldSpawnZ / 16;
            
            if (worldChunkX == spawnX / 16 && worldChunkZ == spawnZ / 16) {
                topPos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, housePos.set(worldSpawnX, 0, worldSpawnZ));
                
                if (topPos.getY() <= 0) {
                    ModernBeta.LOGGER.log(Level.INFO, "Indev House Y is at 0, skipping placement...");
                    return false;
                }
                
                ModernBeta.LOGGER.log(Level.INFO, "[Indev] Building..");
                generateHouse(world, topPos);    
            }
        } else {
            generateHouse(world, topPos);
        }
        
        return false;
    }
    
    private boolean generateHouse(StructureWorldAccess world, BlockPos topPos) {
        BlockPos.Mutable blockPos = new BlockPos.Mutable();
        int spawnX = topPos.getX();
        int spawnY = topPos.getY() + 1;
        int spawnZ = topPos.getZ();
        
        for (int x = spawnX - 3; x <= spawnX + 3; ++x) {
            for (int y = spawnY - 2; y <= spawnY + 2; ++y) {
                for (int z = spawnZ - 3; z <= spawnZ + 3; ++z) {
                    
                    Block blockToSet = (y < spawnY - 1) ? Blocks.OBSIDIAN : Blocks.AIR;
                    
                    if (x == spawnX - 3 || z == spawnZ - 3 || x == spawnX + 3 || z == spawnZ + 3 || y == spawnY - 2 || y == spawnY + 2) {
                        blockToSet = Blocks.STONE;
                        if (y >= spawnY - 1) {
                            blockToSet = Blocks.OAK_PLANKS;
                        }
                    }
                    if (z == spawnZ - 3 && x == spawnX && y >= spawnY - 1 && y <= spawnY) {
                        blockToSet = Blocks.AIR;
                    }
                    
                    world.setBlockState(blockPos.set(x, y, z), blockToSet.getDefaultState(), 0);
                }
            }
        }
        
        world.setBlockState(blockPos.set(spawnX - 3 + 1, spawnY, spawnZ), Blocks.WALL_TORCH.getDefaultState().rotate(BlockRotation.CLOCKWISE_90), 0);
        world.setBlockState(blockPos.set(spawnX + 3 - 1, spawnY, spawnZ), Blocks.WALL_TORCH.getDefaultState().rotate(BlockRotation.COUNTERCLOCKWISE_90), 0);
        
        return true;
    }
}
