package com.bespectacled.modernbeta.world.feature;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.bespectacled.modernbeta.util.BlockStates;
import com.mojang.serialization.Codec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

/**
 * TODO: NEEDS CLEAN-UP
 */
public class OldFancyOakFeature extends Feature<DefaultFeatureConfig> { 
    private static final Set<BlockState> DIRT_REPLACEABLE = Stream.of(BlockStates.GRASS_BLOCK, BlockStates.PODZOL).collect(Collectors.toCollection(HashSet::new));

    protected static final BlockState LOG = Blocks.OAK_LOG.getDefaultState();
    protected static final BlockState LEAVES = Blocks.OAK_LEAVES.getDefaultState().with(LeavesBlock.DISTANCE, 1);
    
    private static final byte[] INDEXES = new byte[] {2, 0, 0, 1, 2, 1};
    private static final Random RANDOM = new Random();
    private static final int[] POS = new int[3];
    private static final int[] DISTANCE = new int[3];
    
    private int treeHeight = 0;
    //private int actualHeight;
    private int treeMaxHeight = 12;
    //private int field_366 = 4;
    private int[][] field_367;

    public OldFancyOakFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }
    
    private void blockLine(StructureWorldAccess world, int[] start, int[] end) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
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
                world.setBlockState(mutable.set(pos[0], pos[1], pos[2]), LOG, 19);
            }
        }
    }

    private int getLineLength(StructureWorldAccess world, int[] start, int[] end) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        byte index1 = 0;
        byte index2 = 0;
        
        for (index1 = 0; index1 < 3; ++index1) {
            DISTANCE[index1] = end[index1] - start[index1];
            if (Math.abs(DISTANCE[index1]) > Math.abs(DISTANCE[index2])) {
                index2 = index1;
            }
        }

        if (DISTANCE[index2] == 0) {
            return -1;
        } else {
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
                if (!(var13 = world.getBlockState(mutable.set(pos[0], pos[1], pos[2]))).isAir() && var13.getBlock() != Blocks.OAK_LEAVES)
                {
                    break;
                }
            }

            return i == dist ? -1 : Math.abs(i);
        }
    }
    
    /*
    private int getLineLengthNew(StructureWorldAccess world, BlockPos startPos, BlockPos endPos) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        byte curNdx = 0;
        byte longestSideNdx = 0;
        
        int[] distance = new int[3];
        
        BlockPos offset = endPos.add(-startPos.getX(), -startPos.getY(), -startPos.getZ());
        
        // Go through each offset component to determine index (component) of longest side.
        while (curNdx < 3) {
            if (Math.abs(getComponentFromIndex(offset, curNdx)) > Math.abs(getComponentFromIndex(offset, longestSideNdx))) {
                longestSideNdx = curNdx;
            }
            
            curNdx++;
        }
        
        if (getComponentFromIndex(offset, longestSideNdx) == 0) {
            return - 1;
        } else {
            byte something1 = INDEXES[longestSideNdx];     // Gets something from first half of INDEXES, foliage shape?
            byte something2 = INDEXES[longestSideNdx + 3]; // Gets something from second half of INDEXES, foliage shape?
            
            int offset = 
        }
         
    }
    
    
    private boolean canGenerate(StructureWorldAccess world, BlockPos startPos) {
        BlockPos basePos = new BlockPos(startPos.getX(), startPos.getY() - 1, startPos.getZ());
        BlockPos endPos = new BlockPos(startPos.getX(), startPos.getY() + this.treeHeight - 1, startPos.getZ());
        
        Block baseBlock = world.getBlockState(basePos).getBlock();
        
        if (baseBlock != Blocks.DIRT && baseBlock != Blocks.GRASS_BLOCK && baseBlock != Blocks.PODZOL) {
            return false;
        }
        
        int lineLength = this.getLineLengthNew(world, startPos, endPos);
        if (lineLength == -1) {
            return true;
        } else if (lineLength < 6) {
            return false;
        } 
        
        this.treeHeight = lineLength;
        return true;
    }
    */
    
    public final void chunkReset()
    {
        POS[0] = 0;
        POS[1] = 0;
        POS[2] = 0;
        treeHeight = 0;
        treeMaxHeight = 12;
        //field_366 = 4;
    }

    @Override
    public final boolean generate(FeatureContext<DefaultFeatureConfig> featureContext) {
        StructureWorldAccess world = featureContext.getWorld();
        BlockPos pos = featureContext.getOrigin();
        Random random = featureContext.getRandom();
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        RANDOM.setSeed(random.nextLong());
        
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        
        // Starting coordinates, base of the tree
        POS[0] = x;
        POS[1] = y;
        POS[2] = z;
        
        if (treeHeight == 0) {
            treeHeight = 5 + RANDOM.nextInt(treeMaxHeight);
        }

        int[] treeStartPos = new int[] {POS[0], POS[1], POS[2]};
        int[] treeEndPos = new int[] {POS[0], POS[1] + treeHeight - 1, POS[2]};

        boolean canGenerate;

        Block block = world.getBlockState(mutable.set(POS[0], POS[1] - 1, POS[2])).getBlock();
        if (block != Blocks.DIRT && block != Blocks.GRASS_BLOCK && block != Blocks.PODZOL) {
            canGenerate = false;
        } else if ((z = getLineLength(world, treeStartPos, treeEndPos)) == -1) {
            canGenerate = true;
        } else if (z < 6) {
            canGenerate = false;
        } else {
            treeHeight = z;
            canGenerate = true;
        }

        if (canGenerate) {
            // Replace grass or podzol block with dirt
            if (DIRT_REPLACEABLE.contains(world.getBlockState(mutable.set(POS[0], POS[1] - 1, POS[2])))) {
                world.setBlockState(mutable, BlockStates.DIRT, 0);
            }
            
            int actualHeight = (int) ((double) treeHeight * 0.618);
            
            if (actualHeight >= treeHeight) {
                actualHeight = treeHeight - 1;
            }

            int var42 = (int) (1.382D + Math.pow((double) treeHeight / 13.0D, 2.0D));
            if (var42 <= 0) {
                var42 = 1;
            }

            int[][] var47 = new int[var42 * treeHeight][4];
            y = POS[1] + treeHeight - 5;
            z = 1;
            int var64 = POS[1] + actualHeight;
            int currentHeight = y - POS[1];
            var47[0][0] = POS[0];
            var47[0][1] = y;
            var47[0][2] = POS[2];
            var47[0][3] = var64;
            --y;

            while (currentHeight >= 0) {
                int var8 = 0;
                float branchThreshold;
                
                // Check to generate branch
                if ((double) currentHeight < (double) ((float) treeHeight) * 0.3D) {
                    branchThreshold = -1.618F;
                    
                } else {
                    float halfTreeHeight = (float) treeHeight / 2.0F;
                    float heightDiff = halfTreeHeight - (float) currentHeight;
                    
                    float branchStart;
                    if (heightDiff == 0.0F) {
                        branchStart = halfTreeHeight;
                    } else if (Math.abs(heightDiff) >= halfTreeHeight) {
                        branchStart = 0.0F;
                    } else {
                        //var36 = (float) Math.sqrt(Math.pow((double) Math.abs(var13), 2.0D) - Math.pow((double) Math.abs(var14), 2.0D));
                        branchStart = (float) Math.sqrt(halfTreeHeight * halfTreeHeight - heightDiff * heightDiff);
                    }

                    branchThreshold = branchStart * 0.5F;
                }
                
                // If branch threshold reached, generate branches
                if (branchThreshold >= 0.0F) {
                    for (; var8 < var42; ++var8) {
                        double randR = (double) branchThreshold * ((double) RANDOM.nextFloat() + 0.328D);
                        double randAngle = (double) RANDOM.nextFloat() * 2.0D * 3.14159D;
                        
                        int randX = (int) (randR * Math.sin(randAngle) + (double) POS[0] + 0.5D);
                        int randZ = (int) (randR * Math.cos(randAngle) + (double) POS[2] + 0.5D);
                        
                        int[] startPos = new int[] { randX, y, randZ };
                        int[] endPos = new int[] { randX, y + 5, randZ };
                        
                        if (getLineLength(world, startPos, endPos) == -1) {
                            endPos = new int[] { POS[0], POS[1], POS[2] };
                            double var30 = Math.sqrt(Math.pow((double) Math.abs(POS[0] - startPos[0]), 2.0D) + Math.pow((double) Math.abs(POS[2] - startPos[2]), 2.0D)) * 0.381D;
                            
                            if ((double) startPos[1] - var30 > (double) var64) {
                                endPos[1] = var64;
                            } else {
                                endPos[1] = (int) ((double) startPos[1] - var30);
                            }

                            if (getLineLength(world, endPos, startPos) == -1)
                            {
                                var47[z][0] = randX;
                                var47[z][1] = y;
                                var47[z][2] = randZ;
                                var47[z][3] = endPos[1];
                                ++z;
                            }
                        }
                    }
                }
                
                --y;
                --currentHeight;
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
                    //int off2 = 18;
                    //off2 = 1;
                    float radius = var20;
                    int foliageRadius = (int) ((double) var20 + 0.618D);
                    
                    byte ndx0 = INDEXES[1];
                    byte ndx2 = INDEXES[4];
                    
                    int[] centerBlockCoord = new int[] { var71, z, var55 };
                    int[] foliageCoord = new int[] { 0, 0, 0 };
                    //int off1 = -foliageRadius;

                    for (int off1 = -foliageRadius; off1 <= foliageRadius; ++off1) {
                        for (int off2 = -foliageRadius; off2 <= foliageRadius; ++off2) {
                            double foliageDist = Math.sqrt(
                                Math.pow((double) Math.abs(off1) + 0.5D, 2.0D) + 
                                Math.pow((double) Math.abs(off2) + 0.5D, 2.0D));
                            
                            if (foliageDist > radius)
                                continue;
                            
                            foliageCoord[ndx0] = centerBlockCoord[ndx0] + off1; // 0
                            foliageCoord[1] = centerBlockCoord[1];              // 1
                            foliageCoord[ndx2] = centerBlockCoord[ndx2] + off2; // 2
                            
                            BlockState blockState = world.getBlockState(mutable.set(foliageCoord[0], foliageCoord[1], foliageCoord[2]));
                            if (blockState.isAir() || blockState.equals(LEAVES)) {
                                world.setBlockState(mutable, LEAVES, 19);
                            }
                            
                            /*
                            if (!blockState.isAir() && blockState.getBlock() != Blocks.OAK_LEAVES) { }
                            else {
                                world.setBlockState(mutable, LEAVES, 19);
                            }
                            */
                        }
                    }
                    
                    /*
                    for (coord[1] = centerBlockCoord[1]; off1 <= foliageRadius; ++off1)
                    {
                        coord[var29] = centerBlockCoord[var29] + off1;
                        off2 = -foliageRadius;

                        while (off2 <= foliageRadius)
                        {
                            if (Math.sqrt(Math.pow((double) Math.abs(off1) + 0.5D, 2.0D)
                                    + Math.pow((double) Math.abs(off2) + 0.5D, 2.0D)) > (double) radius) {
                                ++off2;
                            } else {
                                coord[var84] = centerBlockCoord[var84] + off2;
                                BlockState blockState = world.getBlockState(mutable.set(coord[0], coord[1], coord[2]));
                                
                                if (!blockState.isAir() && blockState.getBlock() != Blocks.OAK_LEAVES) {
                                    ++off2;
                                } else {
                                    world.setBlockState(mutable, LEAVES, 19);
                                    ++off2;
                                }
                            }
                        }
                    }
                    */
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
                if ((double) var70 >= (double) treeHeight * 0.2D)
                {
                    blockLine(world, var57, var66);
                }
            }

            return true;
        }
        
        return false;
    }

    private static int getComponentFromIndex(BlockPos pos, int ndx) {
        if (ndx == 0) return pos.getX();
        if (ndx == 1) return pos.getY();
        if (ndx == 2) return pos.getZ();
        
        throw new IllegalArgumentException("[Modern Beta] Cannot translate index (<= 2) to BlockPos component!");
    }
}
