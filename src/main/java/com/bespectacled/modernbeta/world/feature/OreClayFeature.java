package com.bespectacled.modernbeta.world.feature;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkSectionCache;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class OreClayFeature extends Feature<OreFeatureConfig> {
    public OreClayFeature(Codec<OreFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<OreFeatureConfig> featureContext) {
        StructureWorldAccess world = featureContext.getWorld();
        BlockPos pos = featureContext.getOrigin();
        OreFeatureConfig config = featureContext.getConfig();
        Random random = featureContext.getRandom();
        
        int startX = pos.getX();
        int startY = pos.getY();
        int startZ = pos.getZ();
        
        int numberOfBlocks = config.size;
        
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();
        
        if(world.testBlockState(pos, state -> state.isOf(Blocks.WATER))) {
            return false;
        }
        
        float f = random.nextFloat() * 3.141593F;
        
        double d0 = (float)(startX + 8) + (MathHelper.sin(f) * (float)numberOfBlocks) / 8F;
        double d1 = (float)(startX + 8) - (MathHelper.sin(f) * (float)numberOfBlocks) / 8F;
        double d2 = (float)(startZ + 8) + (MathHelper.cos(f) * (float)numberOfBlocks) / 8F;
        double d3 = (float)(startZ + 8) - (MathHelper.cos(f) * (float)numberOfBlocks) / 8F;
        
        double d4 = startY + random.nextInt(3) + 2;
        double d5 = startY + random.nextInt(3) + 2;
        
        try (ChunkSectionCache chunkSectionCache = new ChunkSectionCache(world)) {
            for(int l = 0; l <= numberOfBlocks; l++) {
                double d6 = d0 + ((d1 - d0) * (double)l) / (double)numberOfBlocks;
                double d7 = d4 + ((d5 - d4) * (double)l) / (double)numberOfBlocks;
                double d8 = d2 + ((d3 - d2) * (double)l) / (double)numberOfBlocks;
                
                double d9 = (random.nextDouble() * (double)numberOfBlocks) / 16D;
                
                double d10 = (double)(MathHelper.sin(((float)l * 3.141593F) / (float)numberOfBlocks) + 1.0F) * d9 + 1.0D;
                double d11 = (double)(MathHelper.sin(((float)l * 3.141593F) / (float)numberOfBlocks) + 1.0F) * d9 + 1.0D;
                
                int minX = MathHelper.floor(d6 - d10 / 2D);
                int maxX = MathHelper.floor(d6 + d10 / 2D);
                int minY = MathHelper.floor(d7 - d11 / 2D);
                int maxY = MathHelper.floor(d7 + d11 / 2D);
                int minZ = MathHelper.floor(d8 - d10 / 2D);
                int maxZ = MathHelper.floor(d8 + d10 / 2D);
                
                for(int x = minX; x <= maxX; x++) {
                    for(int y = minY; y <= maxY; y++) {
                        for(int z = minZ; z <= maxZ; z++) {
                            double d12 = (((double)x + 0.5D) - d6) / (d10 / 2D);
                            double d13 = (((double)y + 0.5D) - d7) / (d11 / 2D);
                            double d14 = (((double)z + 0.5D) - d8) / (d10 / 2D);
                            
                            if(d12 * d12 + d13 * d13 + d14 * d14 >= 1.0D) {
                                continue;
                            }

                            ChunkSection chunkSection = chunkSectionCache.getSection(mutablePos.set(x, y, z));
                            
                            if (chunkSection != WorldChunk.EMPTY_SECTION) {
                                int localX = ChunkSectionPos.getLocalCoord(x); 
                                int localY = ChunkSectionPos.getLocalCoord(y);
                                int localZ = ChunkSectionPos.getLocalCoord(z);
                                BlockState state = chunkSection.getBlockState(localX, localY, localZ);
                                
                                for (final OreFeatureConfig.Target target : config.targets) {
                                    if (OreFeature.shouldPlace(state, chunkSectionCache::getBlockState, random, config, target, mutablePos)) {
                                        chunkSection.setBlockState(localX, localY, localZ, target.state, false);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return true;
    }
}
