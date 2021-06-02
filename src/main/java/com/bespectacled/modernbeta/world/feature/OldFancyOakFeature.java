package com.bespectacled.modernbeta.world.feature;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.bespectacled.modernbeta.util.BlockStates;
import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class OldFancyOakFeature extends Feature<DefaultFeatureConfig> { 
    private static final Set<BlockState> DIRT_REPLACEABLE = Stream.of(
        BlockStates.DIRT,
        BlockStates.GRASS_BLOCK, 
        BlockStates.PODZOL, 
        Blocks.FARMLAND.getDefaultState()
    ).collect(Collectors.toCollection(HashSet::new));
    
    private static final BlockState OAK_LEAVES = Blocks.OAK_LEAVES.getDefaultState().with(LeavesBlock.DISTANCE, 1);
    private static final byte[] AXIS_LOOKUP = new byte[] {2, 0, 0, 1, 2, 1};
    private static final int FOLIAGE_BLOB_HEIGHT = 5;
    
    private final int treeMaxHeight;
    
    private int height;
    private int treeHeight;
    
    private int[][] foliageBlobPositions;

    public OldFancyOakFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
        
        this.treeMaxHeight = 12;
        this.height = 0;
    }
    
    @Override
    public final boolean generate(
        StructureWorldAccess world, 
        ChunkGenerator chunkGenerator, 
        Random random, 
        BlockPos pos, 
        DefaultFeatureConfig config
    ) {
        Random treeRandom = new Random(random.nextLong());
        
        if (this.height == 0) {
            this.height = 5 + treeRandom.nextInt(this.treeMaxHeight);
        }
    
        if (this.canGenerate(world, pos)) {
            this.initializeTree(world, pos, treeRandom);
            this.placeFoliageBlobs(world, pos);
            this.placeTreeTrunk(world, pos);
            this.placeTreeBranches(world, pos);
    
            return true;
        }
        
        return false;
    }

    public final void chunkReset() {
        this.height = 0;
    }
    
    private boolean canGenerate(StructureWorldAccess world, BlockPos basePos) {
        int[] treeStartPos = { basePos.getX(), basePos.getY(), basePos.getZ() };
        int[] treeEndPos = { basePos.getX(), basePos.getY() + this.height - 1, basePos.getZ() };

        BlockPos treeBasePos = new BlockPos(basePos.getX(), basePos.getY() - 1, basePos.getZ());
        
        BlockState blockState = world.getBlockState(treeBasePos);
        if (!DIRT_REPLACEABLE.contains(blockState)) {
            return false;
        }
        
        int testHeight = this.getBranchLength(world, treeStartPos, treeEndPos);
        
        if (testHeight == -1) {
            return true;
        } else if (testHeight < 6) {
            return false;
        } 
        
        this.treeHeight = testHeight;
        return true;
    }
    
    private void initializeTree(StructureWorldAccess world, BlockPos basePos, Random random) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        // Replace grass/podzol/etc. at base position
        if (DIRT_REPLACEABLE.contains(world.getBlockState(mutable.set(basePos.getX(), basePos.getY() - 1, basePos.getZ())))) {
            world.setBlockState(mutable, BlockStates.DIRT, 0);
        }
        
        // Height of trunk section
        this.treeHeight = (int) ((double) this.height * 0.618);
        
        if (this.treeHeight >= this.height) {
            this.treeHeight = this.height - 1;
        }
    
        // Foliage blob count per y level
        int foliageBlobCount = (int) (1.382D + Math.pow((double) this.height / 13.0D, 2.0D));
        if (foliageBlobCount <= 0) {
            foliageBlobCount = 1;
        }
        
        int foliageBaseY = basePos.getY() + this.height - FOLIAGE_BLOB_HEIGHT;
        int treeTopY = basePos.getY() + this.treeHeight;
        int treeRelY = foliageBaseY - basePos.getY();
        int blobCount = 1;
        
        // Create and maintain list of foliage blob positions where position is [x, baseY, z, topY]
        int[][] foliageBlobPositions = new int[foliageBlobCount * this.height][4];
        foliageBlobPositions[0][0] = basePos.getX();
        foliageBlobPositions[0][1] = foliageBaseY;
        foliageBlobPositions[0][2] = basePos.getZ();
        foliageBlobPositions[0][3] = treeTopY;
        
        --foliageBaseY;
    
        while (treeRelY >= 0) {
            int currentBlobCount = 0;
            float foliageDistance = this.getFoliageDistance(treeRelY);
            
            // If foliage distance given, generate foliage
            if (foliageDistance >= 0.0F) {
                while (currentBlobCount < foliageBlobCount) {
                    double randRadius = (double) foliageDistance * ((double) random.nextFloat() + 0.328D);
                    double randAngle = (double) random.nextFloat() * 2.0D * 3.14159D;
                    
                    int randX = (int) (randRadius * Math.sin(randAngle) + (double) basePos.getX() + 0.5D);
                    int randZ = (int) (randRadius * Math.cos(randAngle) + (double) basePos.getZ() + 0.5D);
                    
                    int[] startPos = new int[] { randX, foliageBaseY, randZ };
                    int[] endPos = new int[] { randX, foliageBaseY + FOLIAGE_BLOB_HEIGHT, randZ };
                    
                    if (this.getBranchLength(world, startPos, endPos) == -1) {
                        endPos = new int[] { basePos.getX(), basePos.getY(), basePos.getZ() };
                        
                        double distance = Math.sqrt(
                            Math.pow((double) Math.abs(basePos.getX() - startPos[0]), 2.0D) + 
                            Math.pow((double) Math.abs(basePos.getZ() - startPos[2]), 2.0D)
                        ) * 0.381D;
                        
                        if ((double) startPos[1] - distance > (double) treeTopY) {
                            endPos[1] = treeTopY;
                        } else {
                            endPos[1] = (int) ((double) startPos[1] - distance);
                        }
    
                        if (this.getBranchLength(world, endPos, startPos) == -1) {
                            foliageBlobPositions[blobCount][0] = randX;
                            foliageBlobPositions[blobCount][1] = foliageBaseY;
                            foliageBlobPositions[blobCount][2] = randZ;
                            foliageBlobPositions[blobCount][3] = endPos[1];
                            
                            ++blobCount;
                        }
                    }
                    
                    ++currentBlobCount;
                }
            }
            
            --foliageBaseY;
            --treeRelY;
        }
    
        // Save foliage blob positions
        this.foliageBlobPositions = new int[blobCount][4];
        System.arraycopy(foliageBlobPositions, 0, this.foliageBlobPositions, 0, blobCount);
    }

    private void placeFoliageBlobs(StructureWorldAccess world, BlockPos basePos) {
        int curBlob = 0;
    
        while(curBlob < this.foliageBlobPositions.length) {
            int x = this.foliageBlobPositions[curBlob][0];
            int y = this.foliageBlobPositions[curBlob][1];
            int z = this.foliageBlobPositions[curBlob][2];
            
            this.placeFoliageBlob(world, x, y, z);
            
            curBlob++;
        }
    }

    private void placeTreeTrunk(StructureWorldAccess world, BlockPos basePos) {
        int x = basePos.getX();
        int z = basePos.getZ();
        
        int startY = basePos.getY();
        int topY = basePos.getY() + this.treeHeight;
        
        int[] startPos = {x, startY, z};
        int[] endPos = {x, topY, z};
        
        this.placeBranch(world, startPos, endPos, BlockStates.OAK_LOG);
    }

    private void placeTreeBranches(StructureWorldAccess world, BlockPos basePos) {
        int curBranch = 0 ;
        int[] branchStartPos = { basePos.getX(), basePos.getY(), basePos.getZ() };
        
        while (curBranch < this.foliageBlobPositions.length) {
            int[] foliageBlobPos = this.foliageBlobPositions[curBranch];
            int[] branchEndPos = { foliageBlobPos[0], foliageBlobPos[1], foliageBlobPos[2] };
            
            // Set start y to bottom of foliage blob
            branchStartPos[1] = foliageBlobPos[3];
            
            int relY = branchStartPos[1] - basePos.getY();
            if (relY >= this.height * 0.2D) {
                this.placeBranch(world, branchStartPos, branchEndPos, BlockStates.OAK_LOG);
            }
            
            curBranch++;
        }
    }

    private void placeBranch(StructureWorldAccess world, int[] startPos, int[] endPos, BlockState state) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        int[] distance = {0, 0, 0};
        
        byte sideNdx = 0;
        byte longestSideNdx = 0;
    
        for (sideNdx = 0; sideNdx < 3; ++sideNdx) {
            distance[sideNdx] = endPos[sideNdx] - startPos[sideNdx];
            if (Math.abs(distance[sideNdx]) > Math.abs(distance[longestSideNdx])) {
                longestSideNdx = sideNdx;
            }
        }
    
        if (distance[longestSideNdx] != 0) {
            byte ndx0 = AXIS_LOOKUP[longestSideNdx];
            byte ndx1 = AXIS_LOOKUP[longestSideNdx + 3];
            
            int branchDir = distance[longestSideNdx] > 0 ? 1 : -1;
    
            double branchStep0 = (double) distance[ndx0] / (double) distance[longestSideNdx];
            double branchStep1 = (double) distance[ndx1] / (double) distance[longestSideNdx];
    
            int[] pos = new int[3];
            
            int offset = 0;
            int endOffset = distance[longestSideNdx] + branchDir;
            
            while(offset != endOffset) {
                pos[longestSideNdx] = MathHelper.floor((startPos[longestSideNdx] + offset) + 0.5D);
                pos[ndx0] = MathHelper.floor(startPos[ndx0] + offset * branchStep0 + 0.5D);
                pos[ndx1] = MathHelper.floor(startPos[ndx1] + offset * branchStep1 + 0.5D);
                
                world.setBlockState(mutable.set(pos[0], pos[1], pos[2]), state, 19);
                
                offset += branchDir;
            }
        }
    }

    private void placeLayer(StructureWorldAccess world, int x, int y, int z, float radius, byte axis, BlockState state) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int layerRadius = (int) (radius + 0.618D);
        
        byte ndx0 = AXIS_LOOKUP[axis];
        byte ndx1 = AXIS_LOOKUP[axis + 3];
        
        int[] centerPos = new int[] { x, y, z };
        int[] pos = new int[] { 0, 0, 0 };
        
        for (int off1 = -layerRadius; off1 <= layerRadius; ++off1) {
            for (int off2 = -layerRadius; off2 <= layerRadius; ++off2) {
                double foliageDist = Math.sqrt(
                    Math.pow((double) Math.abs(off1) + 0.5D, 2.0D) + 
                    Math.pow((double) Math.abs(off2) + 0.5D, 2.0D));
                
                if (foliageDist > radius)
                    continue;
                
                pos[ndx0] = centerPos[ndx0] + off1; // 0
                pos[1] = centerPos[1];              // 1
                pos[ndx1] = centerPos[ndx1] + off2; // 2
                
                BlockState blockState = world.getBlockState(mutable.set(pos[0], pos[1], pos[2]));
                if (blockState.isAir() || blockState.getBlock().equals(Blocks.OAK_LEAVES)) {
                    world.setBlockState(mutable.set(pos[0], pos[1], pos[2]), state, 19);
                }
            }
        }
    }
    
    private void placeFoliageBlob(StructureWorldAccess world, int x, int y, int z) {
        int curY = y;
        int topY = y + FOLIAGE_BLOB_HEIGHT; // Foliage height
        
        while (curY < topY) {
            int blobRelY = curY - y;
            float radius = getFoliageBlobRadius(blobRelY);
            
            // Generate blob layer at curY
            this.placeLayer(world, x, curY, z, radius, (byte) 1, OAK_LEAVES);
            
            curY++;
        }
    }

    private float getFoliageBlobRadius(int blobRelY) {
        return blobRelY >= 0 && blobRelY < FOLIAGE_BLOB_HEIGHT ? 
            (blobRelY != 0 && blobRelY != FOLIAGE_BLOB_HEIGHT - 1 ? 3.0F : 2.0F) : 
            -1.0F;
    }

    /**
     * 
     * @param treeRelY
     * @return Distance foliage blob should be from main trunk, negative number if blob should not be created at y.
     */
    private float getFoliageDistance(int treeRelY) {
        float distance;
        
        // Check to generate branch
        if ((double) treeRelY < (double) ((float) this.height) * 0.3D) {
            distance = -1.618F;
            
        } else {
            float treeRadius = (float) this.height / 2.0F;
            float distFromRadius = treeRadius - (float) treeRelY;
            
            if (distFromRadius == 0.0F) {
                distance = treeRadius;
            } else if (Math.abs(distFromRadius) >= treeRadius) {
                distance = 0.0F;
            } else {
                distance = (float) Math.sqrt(treeRadius * treeRadius - distFromRadius * distFromRadius);
            }
    
            distance *= 0.5F;
        }
        
        return distance;
    }
    
    private int getBranchLength(StructureWorldAccess world, int[] startPos, int[] endPos) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        int[] distance = {0, 0, 0};
        
        byte sideNdx = 0;
        byte longestSideNdx = 0;
    
        for (sideNdx = 0; sideNdx < 3; ++sideNdx) {
            distance[sideNdx] = endPos[sideNdx] - startPos[sideNdx];
            if (Math.abs(distance[sideNdx]) > Math.abs(distance[longestSideNdx])) {
                longestSideNdx = sideNdx;
            }
        }
        
        if (distance[longestSideNdx] != 0) {
            byte ndx0 = AXIS_LOOKUP[longestSideNdx];
            byte ndx1 = AXIS_LOOKUP[longestSideNdx + 3];
            
            int branchDir = distance[longestSideNdx] > 0 ? 1 : -1;
    
            double branchStep0 = (double) distance[ndx0] / (double) distance[longestSideNdx];
            double branchStep1 = (double) distance[ndx1] / (double) distance[longestSideNdx];
    
            int[] pos = { 0, 0, 0 };
            
            int offset = 0;
            int endOffset = distance[longestSideNdx] + branchDir;
            
            while(offset != endOffset) {
                pos[longestSideNdx] = startPos[longestSideNdx] + offset;
                pos[ndx0] = MathHelper.floor(startPos[ndx0] + offset * branchStep0);
                pos[ndx1] = MathHelper.floor(startPos[ndx1] + offset * branchStep1);

                BlockState blockState = world.getBlockState(mutable.set(pos[0], pos[1], pos[2]));
                
                if (!blockState.isAir() && blockState.getBlock().equals(Blocks.OAK_LEAVES)) {
                    break;
                }
                
                offset += branchDir;
            }
            
            if (offset == endOffset)
                return -1;
            
            return Math.abs(offset);
        }
        
        return -1;
    }
}
