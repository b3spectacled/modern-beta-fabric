package com.bespectacled.modernbeta.carver;

import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;

import com.mojang.serialization.Codec;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.Carver;

public class BetaCaveCarver extends Carver<ProbabilityConfig> {

    public BetaCaveCarver(Codec<ProbabilityConfig> codec, int heightLimit) {
        super(codec, heightLimit);
    }
    
    @Override
    public boolean shouldCarve(Random random, int chunkX, int chunkZ, ProbabilityConfig config) {
        // TODO Auto-generated method stub
        return true; // Handled in main carve function
    }

    @Override
    public boolean carve(Chunk chunk, Function<BlockPos, Biome> posToBiome, Random random, int seaLevel, int chunkX,
            int chunkZ, int mainChunkX, int mainChunkZ, BitSet carvingMask, ProbabilityConfig carverConfig) {
        int caveCount = random.nextInt(random.nextInt(random.nextInt(40) + 1) + 1);
        if (random.nextInt(getMaxCaveCount()) != 0) {
            caveCount = 0;
        }
        
        for (int i = 0; i < caveCount; ++i) {
            double x = chunkX * 16 + random.nextInt(16);
            double y = getCaveY(random);
            double z = chunkZ * 16 + random.nextInt(16);
            
            int tunnelCount = 1;
            if (random.nextInt(4) == 0) {
                this.carveCave(
                    chunk, 
                    random,
                    mainChunkX,
                    mainChunkZ, 
                    x, y, z
                );
                tunnelCount += random.nextInt(4);
            }
            
            for (int j = 0; j < tunnelCount; ++j) {
                float f = random.nextFloat() * 3.141593F * 2.0F;
                float f1 = ((random.nextFloat() - 0.5F) * 2.0F) / 8F;
                float f2 = getTunnelSystemWidth(random);
                
                //carveTunnels(chunk, posToBiome, random.nextLong(), seaLevel, mainChunkX, mainChunkZ, x, y, z, f2, f, f1, 0, 0, 1.0, carvingMask);
                carveTunnels(chunk, random, mainChunkX, mainChunkZ, x, y, z, f2, f, f1, 0, 0, 1.0D);
            }
        }
        
       
        return false;
    }
    
    protected void carveCave(
        Chunk chunk, 
        Random random, 
        int mainChunkX,
        int mainChunkZ, 
        double x, double y, double z
    ) {
        //carveTunnels(chunk, rand, mainChunkX, mainChunkZ, x, y, z, 1.0F + random.nextFloat() * 6F, 0.0F, 0.0F, -1, -1, 0.5D);
        carveTunnels(chunk, random, mainChunkX, mainChunkZ, x, y, z, 1.0F + random.nextFloat() * 6F, 0.0F, 0.0F, -1, -1, 0.5D);
    }
    
    protected void carveTunnels(
        Chunk chunk,
        Random rand,
        int mainChunkX, 
        int mainChunkZ,
        double x, double y, double z, 
        float f, float f1, float f2, 
        int k, int l, 
        double d3
    ) {
        double d4 = mainChunkX * 16 + 8;
        double d5 = mainChunkZ * 16 + 8;
        float f3 = 0.0F;
        float f4 = 0.0F;
        //Random random23 = new Random(random.nextLong());
        Random random23 = new Random(rand.nextLong());
        if(l <= 0)
        {
            int i1 = 8 * 16 - 16;
            l = i1 - random23.nextInt(i1 / 4);
        }
        boolean flag = false;
        if(k == -1)
        {
            k = l / 2;
            flag = true;
        }
        int j1 = random23.nextInt(l / 2) + l / 4;
        boolean flag1 = random23.nextInt(6) == 0;
        for(; k < l; k++)
        {
            double d6 = 1.5D + (double)(MathHelper.sin(((float)k * 3.141593F) / (float)l) * f * 1.0F);
            double d7 = d6 * d3;
            float f5 = MathHelper.cos(f2);
            float f6 = MathHelper.sin(f2);
            x += MathHelper.cos(f1) * f5;
            y += f6;
            z += MathHelper.sin(f1) * f5;
            
            if(flag1) {
                f2 *= 0.92F;
            } else {
                f2 *= 0.7F;
            }
            
            f2 += f4 * 0.1F;
            f1 += f3 * 0.1F;
            f4 *= 0.9F;
            f3 *= 0.75F;
            f4 += (random23.nextFloat() - random23.nextFloat()) * random23.nextFloat() * 2.0F;
            f3 += (random23.nextFloat() - random23.nextFloat()) * random23.nextFloat() * 4F;
            if(!flag && k == j1 && f > 1.0F)
            {
                //carveTunnels(mainChunkX, mainChunkZ, chunkArr, x, y, z, random23.nextFloat() * 0.5F + 0.5F, f1 - 1.570796F, f2 / 3F, k, l, 1.0D);
                //carveTunnels(mainChunkX, mainChunkZ, chunkArr, x, y, z, random23.nextFloat() * 0.5F + 0.5F, f1 + 1.570796F, f2 / 3F, k, l, 1.0D);
                carveTunnels(chunk, rand, mainChunkX, mainChunkZ, x, y, z, random23.nextFloat() * 0.5F + 0.5F, f1 - 1.570796F, f2 / 3F, k, l, 1.0D);
                carveTunnels(chunk, rand, mainChunkX, mainChunkZ, x, y, z, random23.nextFloat() * 0.5F + 0.5F, f1 + 1.570796F, f2 / 3F, k, l, 1.0D);
                return;
            }
            if(!flag && random23.nextInt(4) == 0)
            {
                continue;
            }
            double d8 = x - d4;
            double d9 = z - d5;
            double d10 = l - k;
            double d11 = f + 2.0F + 16F;
            if((d8 * d8 + d9 * d9) - d10 * d10 > d11 * d11)
            {
                return;
            }
            if(x < d4 - 16D - d6 * 2D || z < d5 - 16D - d6 * 2D || x > d4 + 16D + d6 * 2D || z > d5 + 16D + d6 * 2D)
            {
                continue;
            }
            d8 = MathHelper.floor(x - d6) - mainChunkX * 16 - 1;
            int k1 = (MathHelper.floor(x + d6) - mainChunkX * 16) + 1;
            d9 = MathHelper.floor(y - d7) - 1;
            int l1 = MathHelper.floor(y + d7) + 1;
            d10 = MathHelper.floor(z - d6) - mainChunkZ * 16 - 1;
            int i2 = (MathHelper.floor(z + d6) - mainChunkZ * 16) + 1;
            if(d8 < 0)
            {
                d8 = 0;
            }
            if(k1 > 16)
            {
                k1 = 16;
            }
            if(d9 < 1)
            {
                d9 = 1;
            }
            if(l1 > 120)
            {
                l1 = 120;
            }
            if(d10 < 0)
            {
                d10 = 0;
            }
            if(i2 > 16)
            {
                i2 = 16;
            }
            boolean flag2 = false;
            for(int j2 = (int) d8; !flag2 && j2 < k1; j2++)
            {
                for(int l2 = (int) d10; !flag2 && l2 < i2; l2++)
                {
                    for(int i3 = l1 + 1; !flag2 && i3 >= d9 - 1; i3--)
                    {
                        int j3 = (j2 * 16 + l2) * 128 + i3;
                        if(i3 < 0 || i3 >= 128)
                        {
                            continue;
                        }
                        Block block = chunk.getBlockState(new BlockPos(j2, i3, l2)).getBlock();
                        
                        /*
                        if(chunkArr[j3] == Block.waterStill.blockID || chunkArr[j3] == Block.waterMoving.blockID)
                        {
                            flag2 = true;
                        }
                        */
                        
                        if (block.equals(Blocks.WATER)) {
                            flag2 = true;
                        }
                        
                        if(i3 != d9 - 1 && j2 != d8 && j2 != k1 - 1 && l2 != d10 && l2 != i2 - 1)
                        {
                            i3 = (int) d9;
                        }
                    }

                }

            }

            if(flag2)
            {
                continue;
            }
            for(int k2 = (int) d8; k2 < k1; k2++)
            {
                double d12 = (((double)(k2 + mainChunkX * 16) + 0.5D) - x) / d6;
                for(int k3 = (int) d10; k3 < i2; k3++)
                {
                    double d13 = (((double)(k3 + mainChunkZ * 16) + 0.5D) - z) / d6;
                    int l3 = (k2 * 16 + k3) * 128 + l1;
                    boolean flag3 = false;
                    
                    int y2 = l1;
                    
                    for(int i4 = l1 - 1; i4 >= d9; i4--)
                    {
                        double d14 = (((double)i4 + 0.5D) - y) / d7;
                        if(d14 > -0.69999999999999996D && d12 * d12 + d14 * d14 + d13 * d13 < 1.0D)
                        {
                            //byte byte0 = chunkArr[l3];
                            Block block = chunk.getBlockState(new BlockPos(k2, y2, k3)).getBlock();
                            
                            if(block == Blocks.GRASS_BLOCK)
                            {
                                flag3 = true;
                            }
                            if(block == Blocks.STONE || block == Blocks.DIRT || block == Blocks.GRASS_BLOCK)
                            {
                                if(i4 < 10)
                                {
                                    //chunkArr[l3] = (byte)Block.lavaStill.blockID;
                                    chunk.setBlockState(new BlockPos(k2, y2, k3), Blocks.LAVA.getDefaultState(), false);
                                } else
                                {
                                    //chunkArr[l3] = 0;
                                    chunk.setBlockState(new BlockPos(k2, y2, k3), Blocks.CAVE_AIR.getDefaultState(), false);
                                    if(flag3 && chunk.getBlockState(new BlockPos(k2, y2 - 1, k3)).getBlock() == Blocks.DIRT)
                                    {
                                        chunk.setBlockState(new BlockPos(k2, y2 - 1, k3), Blocks.GRASS_BLOCK.getDefaultState(), false);
                                    }
                                }
                            }
                        }
                        l3--;
                        y2--;
                    }

                }

            }

            if(flag)
            {
                break;
            }
        }

    }
    
    /*
    protected void carveCave(
        Chunk chunk, 
        Function<BlockPos, Biome> function, 
        long randLong, 
        int seaLevel, 
        int mainChunkX, 
        int mainChunkZ, 
        double x, double y, double z, 
        float float15, 
        double double16, 
        BitSet bitSet
    ) {
        double double19 = 1.5 + MathHelper.sin(1.5707964f) * float15;
        double double21 = double19 * double16;
        this.carveRegion(chunk, function, randLong, seaLevel, mainChunkX, mainChunkZ, x + 1.0, y, z, double19, double21, bitSet);
    }
    
    protected void carveTunnels(Chunk chunk, Function<BlockPos, Biome> function, long long4, int integer6, int integer7, int integer8, double double9, double double11, double double13, float float15, float float16, float float17, int integer18, int integer19, double double20, BitSet bitSet) {
        Random random23 = new Random(long4);
        int integer24 = random23.nextInt(integer19 / 2) + integer19 / 4;
        boolean boolean25 = random23.nextInt(6) == 0;
        float float26 = 0.0f;
        float float27 = 0.0f;
        for (int t = integer18; t < integer19; ++t) {
            double double29 = 1.5 + MathHelper.sin(3.1415927f * t / integer19) * float15;
            double double31 = double29 * double20;
            float float33 = MathHelper.cos(float17);
            double9 += MathHelper.cos(float16) * float33;
            double11 += MathHelper.sin(float17);
            double13 += MathHelper.sin(float16) * float33;
            float17 *= (boolean25 ? 0.92f : 0.7f);
            float17 += float27 * 0.1f;
            float16 += float26 * 0.1f;
            float27 *= 0.9f;
            float26 *= 0.75f;
            float27 += (random23.nextFloat() - random23.nextFloat()) * random23.nextFloat() * 2.0f;
            float26 += (random23.nextFloat() - random23.nextFloat()) * random23.nextFloat() * 4.0f;
            if (t == integer24 && float15 > 1.0f) {
                this.carveTunnels(chunk, function, random23.nextLong(), integer6, integer7, integer8, double9, double11, double13, random23.nextFloat() * 0.5f + 0.5f, float16 - 1.5707964f, float17 / 3.0f, t, integer19, 1.0, bitSet);
                this.carveTunnels(chunk, function, random23.nextLong(), integer6, integer7, integer8, double9, double11, double13, random23.nextFloat() * 0.5f + 0.5f, float16 + 1.5707964f, float17 / 3.0f, t, integer19, 1.0, bitSet);
                return;
            }
            if (random23.nextInt(4) != 0) {
                if (!this.canCarveBranch(integer7, integer8, double9, double13, t, integer19, float15)) {
                    return;
                }
                this.carveRegion(chunk, function, long4, integer6, integer7, integer8, double9, double11, double13, double29, double31, bitSet);
            }
        }
    }
    */

    

    @Override
    protected boolean isPositionExcluded(double scaledRelativeX, double scaledRelativeY, double scaledRelativeZ,
            int y) {
        // TODO Auto-generated method stub
        return false;
    }
    
    protected int getMaxCaveCount() {
        return 15;
    }
    
    protected int getCaveY(Random random) {
        return random.nextInt(random.nextInt(120) + 8);
    }
    
    protected float getTunnelSystemWidth(Random random) {
        float width = random.nextFloat() * 2.0f + random.nextFloat();
        return width;
    }

}
