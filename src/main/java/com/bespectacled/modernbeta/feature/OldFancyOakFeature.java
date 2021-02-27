package com.bespectacled.modernbeta.feature;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

/**
 * 
 * @author Paulevs
 *
 */
public class OldFancyOakFeature extends Feature<DefaultFeatureConfig> {
    protected static final Mutable B_POS = new Mutable();
    protected static final BlockState LOG = Blocks.OAK_LOG.getDefaultState();
    protected static final BlockState LEAVES = Blocks.OAK_LEAVES.getDefaultState().with(LeavesBlock.DISTANCE, 1);
    
    private static final byte[] INDEXES = new byte[] {2, 0, 0, 1, 2, 1};
    private static final Random RANDOM = new Random();
    private static final int[] POS = new int[3];
    private static final int[] DISTANCE = new int[3];
    
    private int height = 0;
    private int actualHeight;
    private int maxHeight = 12;
    //private int field_366 = 4;
    private int[][] field_367;

    public OldFancyOakFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }
    
    private void blockLine(StructureWorldAccess world, int[] start, int[] end)
    {
        byte index1 = 0;
        byte index2 = 0;

        for (index1 = 0; index1 < 3; ++index1)
        {
            DISTANCE[index1] = end[index1] - start[index1];
            if (Math.abs(DISTANCE[index1]) > Math.abs(DISTANCE[index2]))
            {
                index2 = index1;
            }
        }

        if (DISTANCE[index2] != 0)
        {
            byte index3 = INDEXES[index2];
            index1 = INDEXES[index2 + 3];
            int offset = DISTANCE[index2] > 0 ? 1 : -1;

            double d1 = (double) DISTANCE[index3] / (double) DISTANCE[index2];
            double d2 = (double) DISTANCE[index1] / (double) DISTANCE[index2];

            int[] pos = new int[3];
            int i = 0;

            for (int dist = DISTANCE[index2] + offset; i != dist; i += offset)
            {
                pos[index2] = MathHelper.floor((double) (start[index2] + i) + 0.5D);
                pos[index3] = MathHelper.floor((double) start[index3] + (double) i * d1 + 0.5D);
                pos[index1] = MathHelper.floor((double) start[index1] + (double) i * d2 + 0.5D);
                world.setBlockState(B_POS.set(pos[0], pos[1], pos[2]), LOG, 19);
            }
        }
    }

    private int getLineLength(StructureWorldAccess world, int[] start, int[] end)
    {
        byte index1 = 0;
        byte index2 = 0;

        for (index1 = 0; index1 < 3; ++index1)
        {
            DISTANCE[index1] = end[index1] - start[index1];
            if (Math.abs(DISTANCE[index1]) > Math.abs(DISTANCE[index2]))
            {
                index2 = index1;
            }
        }

        if (DISTANCE[index2] == 0)
        {
            return -1;
        }
        else
        {
            byte var14 = INDEXES[index2];
            index1 = INDEXES[index2 + 3];

            int offset = DISTANCE[index2] > 0 ? 1 : -1;

            double var9 = (double) DISTANCE[var14] / (double) DISTANCE[index2];
            double var11 = (double) DISTANCE[index1] / (double) DISTANCE[index2];
            int[] pos = new int[] { 0, 0, 0 };
            int i = 0;

            int dist = 0;
            for (dist = DISTANCE[index2] + offset; i != dist; i += offset)
            {
                pos[index2] = start[index2] + i;
                pos[var14] = (int) ((double) start[var14] + (double) i * var9);
                pos[index1] = (int) ((double) start[index1] + (double) i * var11);
                BlockState var13;
                if (!(var13 = world.getBlockState(B_POS.set(pos[0], pos[1], pos[2]))).isAir() && var13.getBlock() != Blocks.OAK_LEAVES)
                {
                    break;
                }
            }

            return i == dist ? -1 : Math.abs(i);
        }
    }
    
    public final void chunkReset()
    {
        POS[0] = 0;
        POS[1] = 0;
        POS[2] = 0;
        height = 0;
        maxHeight = 12;
        //field_366 = 4;
    }

    @Override
    public final boolean generate(FeatureContext<DefaultFeatureConfig> featureContext) {
        StructureWorldAccess world = featureContext.getWorld();
        BlockPos pos = featureContext.getOrigin();
        Random random = featureContext.getRandom();
        
        maxHeight = 12;
        
        RANDOM.setSeed(random.nextLong());
        
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        
        POS[0] = x;
        POS[1] = y;
        POS[2] = z;
        
        if (height == 0)
        {
            height = 5 + RANDOM.nextInt(maxHeight);
        }

        int[] start = new int[] {POS[0], POS[1], POS[2]};
        int[] end = new int[] {POS[0], POS[1] + height - 1, POS[2]};

        boolean canGenerate;

        Block block = world.getBlockState(B_POS.set(POS[0], POS[1] - 1, POS[2])).getBlock();
        if (block != Blocks.DIRT && block != Blocks.GRASS_BLOCK)
        {
            canGenerate = false;
        }
        else if ((z = getLineLength(world, start, end)) == -1)
        {
            canGenerate = true;
        }
        else if (z < 6)
        {
            canGenerate = false;
        }
        else
        {
            height = z;
            canGenerate = true;
        }

        if (!canGenerate)
        {
            return false;
        }
        else
        {
            actualHeight = (int) ((double) height * 0.618);
            if (actualHeight >= height)
            {
                actualHeight = height - 1;
            }

            int var42;
            if ((var42 = (int) (1.382D + Math.pow((double) height / 13.0D, 2.0D))) <= 0)
            {
                var42 = 1;
            }

            int[][] var47 = new int[var42 * height][4];
            y = POS[1] + height - 5;
            z = 1;
            int var64 = POS[1] + actualHeight;
            int var7 = y - POS[1];
            var47[0][0] = POS[0];
            var47[0][1] = y;
            var47[0][2] = POS[2];
            var47[0][3] = var64;
            --y;

            while (var7 >= 0)
            {
                int var8 = 0;
                float var86;
                if ((double) var7 < (double) ((float) height) * 0.3D)
                {
                    var86 = -1.618F;
                } else
                {
                    float var13 = (float) height / 2.0F;
                    float var14;
                    float var36;
                    if ((var14 = (float) height / 2.0F - (float) var7) == 0.0F)
                    {
                        var36 = var13;
                    } else if (Math.abs(var14) >= var13)
                    {
                        var36 = 0.0F;
                    } else
                    {
                        var36 = (float) Math.sqrt(
                                Math.pow((double) Math.abs(var13), 2.0D) - Math.pow((double) Math.abs(var14), 2.0D));
                    }

                    var86 = var36 * 0.5F;
                }

                float var9 = var86;
                if (var86 < 0.0F)
                {
                    --y;
                    --var7;
                } else
                {
                    for (; var8 < var42; ++var8)
                    {
                        double var19 = (double) var9 * ((double) RANDOM.nextFloat() + 0.328D);
                        double var21 = (double) RANDOM.nextFloat() * 2.0D * 3.14159D;
                        int var10 = (int) (var19 * Math.sin(var21) + (double) POS[0] + 0.5D);
                        int var11 = (int) (var19 * Math.cos(var21) + (double) POS[2] + 0.5D);
                        int[] var12 = new int[] { var10, y, var11 };
                        int[] var76 = new int[] { var10, y + 5, var11 };
                        if (getLineLength(world, var12, var76) == -1)
                        {
                            var76 = new int[] { POS[0], POS[1], POS[2] };
                            double var30 = Math.sqrt(Math.pow((double) Math.abs(POS[0] - var12[0]), 2.0D) + Math.pow((double) Math.abs(POS[2] - var12[2]), 2.0D)) * 0.381D;
                            if ((double) var12[1] - var30 > (double) var64)
                            {
                                var76[1] = var64;
                            } else
                            {
                                var76[1] = (int) ((double) var12[1] - var30);
                            }

                            if (getLineLength(world, var76, var12) == -1)
                            {
                                var47[z][0] = var10;
                                var47[z][1] = y;
                                var47[z][2] = var11;
                                var47[z][3] = var76[1];
                                ++z;
                            }
                        }
                    }

                    --y;
                    --var7;
                }
            }

            field_367 = new int[z][4];
            System.arraycopy(var47, 0, field_367, 0, z);
            var42 = 0;

            for (int var48 = field_367.length; var42 < var48; ++var42)
            {
                y = field_367[var42][0];
                z = field_367[var42][1];
                var64 = field_367[var42][2];
                int var55 = var64;
                int var72 = z;
                int var71 = y;

                for (int var82 = z + 5; z < var82; ++z)
                {
                    int var22 = z - var72;
                    float var20 = var22 >= 0 && var22 < 5 ? (var22 != 0 && var22 != 5 - 1 ? 3.0F : 2.0F) : -1.0F;
                            int var78 = 18;
                            var78 = 1;
                            float var75 = var20;
                            int var28 = (int) ((double) var20 + 0.618D);
                            byte var29 = INDEXES[1];
                            byte var84 = INDEXES[4];
                            int[] var31 = new int[] { var71, z, var55 };
                            int[] var73 = new int[] { 0, 0, 0 };
                            int var74 = -var28;

                            for (var73[1] = var31[1]; var74 <= var28; ++var74)
                            {
                                var73[var29] = var31[var29] + var74;
                                var78 = -var28;

                                while (var78 <= var28)
                                {
                                    if (Math.sqrt(Math.pow((double) Math.abs(var74) + 0.5D, 2.0D)
                                            + Math.pow((double) Math.abs(var78) + 0.5D, 2.0D)) > (double) var75)
                                    {
                                        ++var78;
                                    } else
                                    {
                                        var73[var84] = var31[var84] + var78;
                                        BlockState var81 = world.getBlockState(B_POS.set(var73[0], var73[1], var73[2]));
                                        if (!var81.isAir() && var81.getBlock() != Blocks.OAK_LEAVES)
                                        {
                                            ++var78;
                                        }
                                        else
                                        {
                                            world.setBlockState(B_POS, LEAVES, 19);
                                            ++ var78;
                                        }
                                    }
                                }
                            }
                }
            }

            var42 = POS[0];
            int var49 = POS[1];
            y = POS[1] + actualHeight;
            z = POS[2];
            int[] var66 = new int[] {var42, var49, z};
            int[] var69 = new int[] {var42, y, z};
            blockLine(world, var66, var69);

            var42 = 0;
            var49 = field_367.length;

            for (int[] var57 = new int[] { POS[0], POS[1],
                    POS[2] }; var42 < var49; ++var42)
            {
                int[] var63 = field_367[var42];
                var66 = new int[] { var63[0], var63[1], var63[2] };
                var57[1] = var63[3];
                int var70 = var57[1] - POS[1];
                if ((double) var70 >= (double) height * 0.2D)
                {
                    blockLine(world, var57, var66);
                }
            }

            return true;
        }
    }

    
}
