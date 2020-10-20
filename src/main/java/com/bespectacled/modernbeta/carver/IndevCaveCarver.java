package com.bespectacled.modernbeta.carver;

import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.mojang.serialization.Codec;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.Carver;

public class IndevCaveCarver extends Carver<ProbabilityConfig> {
    
    private float radius;

    public IndevCaveCarver(Codec<ProbabilityConfig> codec, int heightLimit, float radius) {
        super(codec, heightLimit);
        
        this.radius = radius;
    }

    @Override
    public boolean shouldCarve(Random random, int chunkX, int chunkZ, ProbabilityConfig config) {
        return random.nextFloat() <= config.probability;
    }

    @Override
    public boolean carve(Chunk chunk, Function<BlockPos, Biome> posToBiome, Random random, int seaLevel, int chunkX,
            int chunkZ, int mainChunkX, int mainChunkZ, BitSet carvingMask, ProbabilityConfig carverConfig) {
        float caveX = chunkX * 16 + random.nextInt(16);
        float caveY = getCaveY(random);
        float caveZ = chunkZ * 16 + random.nextInt(16);
        
        int caveLen = (int)((random.nextFloat() + random.nextFloat()) * 200F);
        
        float theta = random.nextFloat() * 3.1415927f * 2.0f;
        float phi = random.nextFloat() * 3.1415927f * 2.0f;
        
        float caveRadius = random.nextFloat() * random.nextFloat() * this.radius;
        
        
        carveTunnels(chunk, random, mainChunkX, mainChunkZ, caveX, caveY, caveZ, caveLen, caveRadius, theta, phi);

        return false;
    }
    
    protected void carveTunnels(Chunk chunk, Random random, int mainChunkX, int mainChunkZ, double caveX, double caveY, double caveZ,
            float caveLen, float caveRadius, float theta, float phi) {
        float deltaTheta = 0.0f;
        float deltaPhi = 0.0f;
        
        for (int len = 0; len < caveLen; ++len) {
            caveX += MathHelper.sin(theta) * MathHelper.cos(phi);
            caveZ += MathHelper.cos(theta) * MathHelper.cos(phi);
            caveY += MathHelper.sin(phi);
            
            theta = theta + deltaTheta * 0.2f;
            deltaTheta = (deltaTheta * 0.9f) + (random.nextFloat() - random.nextFloat());
            phi = phi / 2 + deltaPhi / 4;
            deltaPhi = (deltaPhi * 0.75f) + (random.nextFloat() - random.nextFloat());
            
            if (random.nextFloat() >= 0.25f) {
                float centerX = (float)caveX + (random.nextFloat() * 4.0f - 2.0f) * 0.2f;
                float centerY = (float)caveY + (random.nextFloat() * 4.0f - 2.0f) * 0.2f;
                float centerZ = (float)caveZ + (random.nextFloat() * 4.0f - 2.0f) * 0.2f;
                
                float radius = (this.heightLimit - centerY) / this.heightLimit;
                radius = 1.2f + (radius * 3.5f + 1.0f) * caveRadius;
                radius = radius * MathHelper.sin(len * 3.1415927f / caveLen);
                
                fillOblateSpheroid(chunk, centerX, centerY, centerZ, radius, Blocks.AIR);
            }
        }
    }
    
    protected void fillOblateSpheroid(Chunk chunk, float centerX, float centerY, float centerZ, float radius, Block fillBlock) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        for (int x = (int)(centerX - radius); x < (int)(centerX + radius); ++x) {
            for (int y = (int)(centerY - radius); y < (int)(centerY + radius); ++y) {
                for (int z = (int)(centerZ - radius); z < (int)(centerZ + radius); ++z) {
                
                    float dx = x - centerX;
                    float dy = y - centerY;
                    float dz = z - centerZ;
                    
                    if ((dx * dx + dy * dy * 2.0f + dz * dz) < radius * radius) {
                        Block someBlock = chunk.getBlockState(mutable.set(x, y, z)).getBlock();
                        
                        if (someBlock == Blocks.STONE) {
                            chunk.setBlockState(mutable.set(x, y, z), fillBlock.getDefaultState(), false);
                        }
                        
                    }
                }
            }
        }
    }

    @Override
    protected boolean carveRegion(Chunk chunk, Function<BlockPos, Biome> posToBiome, long seed, int seaLevel,
            int mainChunkX, int mainChunkZ, double x, double y, double z, double yaw, double pitch,
            BitSet carvingMask) {
     
        return true;
    }

    @Override
    protected boolean canCarveBranch(int mainChunkX, int mainChunkZ, double x, double z, int branch, int branchCount,
            float baseWidth) {

        return true;
    }

    @Override
    protected boolean isRegionUncarvable(Chunk chunk, int mainChunkX, int mainChunkZ, int relMinX, int relMaxX,
            int minY, int maxY, int relMinZ, int relMaxZ) {

        return false;
    }

    protected int getCaveY(Random random) {
        return random.nextInt(this.heightLimit);
    }

    @Override
    protected boolean isPositionExcluded(double scaledRelativeX, double scaledRelativeY, double scaledRelativeZ,
            int y) {
        // TODO Auto-generated method stub
        return false;
    }


}
