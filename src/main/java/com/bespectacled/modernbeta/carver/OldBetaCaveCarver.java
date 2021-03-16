package com.bespectacled.modernbeta.carver;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.carver.CarverConfig;
import net.minecraft.world.gen.carver.CarverContext;
import net.minecraft.world.gen.carver.CaveCarver;

public class OldBetaCaveCarver extends CaveCarver implements IOldCaveCarver {

    public OldBetaCaveCarver(Codec<CarverConfig> codec) {
        super(codec);
    }

    //@Override
    public boolean carve(
        CarverContext context,
        Chunk chunk,
        Random random,
        int chunkX,
        int chunkZ, 
        int mainChunkX, 
        int mainChunkZ
    ) {
        int caveCount = random.nextInt(random.nextInt(random.nextInt(40) + 1) + 1);
        if (random.nextInt(getMaxCaveCount()) != 0) {
            caveCount = 0;
        }

        for (int i = 0; i < caveCount; ++i) {
            double x = chunkX * 16 + random.nextInt(16); // Starts
            double y = getCaveY(context, random);
            double z = chunkZ * 16 + random.nextInt(16);

            int tunnelCount = 1;
            if (random.nextInt(4) == 0) {
                this.carveCave(context, chunk, random, mainChunkX, mainChunkZ, x, y, z);
                tunnelCount += random.nextInt(4);
            }

            for (int j = 0; j < tunnelCount; ++j) {
                float tunnelYaw = random.nextFloat() * 3.141593F * 2.0F;
                float tunnelPitch = ((random.nextFloat() - 0.5F) * 2.0F) / 8F;
                float tunnelWidth = getTunnelSystemWidth(random);

                carveTunnels(context, chunk, random, mainChunkX, mainChunkZ, x, y, z, tunnelWidth, tunnelYaw, tunnelPitch, 0, 0, 1.0D);
            }
        }

        return false;
    }

    private void carveCave(CarverContext context, Chunk chunk, Random random, int mainChunkX, int mainChunkZ, double x, double y, double z) {
        carveTunnels(context, chunk, random, mainChunkX, mainChunkZ, x, y, z, 1.0F + random.nextFloat() * 6F, 0.0F, 0.0F, -1, -1, 0.5D);
    }

    private void carveTunnels(
        CarverContext context,
        Chunk chunk, 
        Random rand, 
        int mainChunkX, 
        int mainChunkZ, 
        double x, 
        double y, 
        double z,
        float tunnelWidth, 
        float tunnelYaw, 
        float tunnelPitch, 
        int branch,
        int branchCount, 
        double tunnelWHRatio
    ) {
        float f2 = 0.0F;
        float f3 = 0.0F;

        Random random = new Random(rand.nextLong());

        if (branchCount <= 0) {
            int someNumMaxStarts = 8 * 16 - 16;
            branchCount = someNumMaxStarts - random.nextInt(someNumMaxStarts / 4);
        }

        boolean noStarts = false;
        if (branch == -1) {
            branch = branchCount / 2;
            noStarts = true;
        }

        int randBranch = random.nextInt(branchCount / 2) + branchCount / 4;
        boolean vary = random.nextInt(6) == 0;

        for (; branch < branchCount; branch++) {
            double yaw = 1.5D + (double) (MathHelper.sin(((float) branch * 3.141593F) / (float) branchCount)
                    * tunnelWidth * 1.0F);
            double pitch = yaw * tunnelWHRatio;

            float f4 = MathHelper.cos(tunnelPitch);
            float f5 = MathHelper.sin(tunnelPitch);

            x += MathHelper.cos(tunnelYaw) * f4;
            y += f5;
            z += MathHelper.sin(tunnelYaw) * f4;

            tunnelPitch *= vary ? 0.92F : 0.7F;

            tunnelPitch += f3 * 0.1F;
            tunnelYaw += f2 * 0.1F;

            f3 *= 0.9F;
            f2 *= 0.75F;

            f3 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
            f2 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4F;

            if (!noStarts && branch == randBranch && tunnelWidth > 1.0F) {
                carveTunnels(context, chunk, rand, mainChunkX, mainChunkZ, x, y, z, random.nextFloat() * 0.5F + 0.5F,
                        tunnelYaw - 1.570796F, tunnelPitch / 3F, branch, branchCount, 1.0D);
                carveTunnels(context, chunk, rand, mainChunkX, mainChunkZ, x, y, z, random.nextFloat() * 0.5F + 0.5F,
                        tunnelYaw + 1.570796F, tunnelPitch / 3F, branch, branchCount, 1.0D);
                return;
            }

            if (!noStarts && random.nextInt(4) == 0) {
                continue;
            }

            if (!canCarveBranch(mainChunkX, mainChunkZ, x, z, branch, branchCount, tunnelWidth)) {
                return;
            }

            this.carveRegion(context, chunk, mainChunkX, mainChunkZ, x, y, z, yaw, pitch);

            if (noStarts) {
                break;
            }
        }

    }

    private boolean carveRegion(
        CarverContext context,
        Chunk chunk,
        int mainChunkX, 
        int mainChunkZ, 
        double x, 
        double y, 
        double z, 
        double yaw, 
        double pitch
    ) {
        double ctrX = mainChunkX * 16 + 8;
        double ctrZ = mainChunkZ * 16 + 8;

        BlockPos.Mutable blockPos = new BlockPos.Mutable();

        if ( // Check for valid tunnel starts, I guess? Or to prevent overlap?
        x < ctrX - 16D - yaw * 2D || z < ctrZ - 16D - yaw * 2D || x > ctrX + 16D + yaw * 2D
                || z > ctrZ + 16D + yaw * 2D) {
            return false;
        }

        int minX = MathHelper.floor(x - yaw) - mainChunkX * 16 - 1; // Get min and max extents of tunnel, relative to
                                                                    // chunk coords
        int maxX = (MathHelper.floor(x + yaw) - mainChunkX * 16) + 1;

        int minY = MathHelper.floor(y - pitch) - 1;
        int maxY = MathHelper.floor(y + pitch) + 1;

        int minZ = MathHelper.floor(z - yaw) - mainChunkZ * 16 - 1;
        int maxZ = (MathHelper.floor(z + yaw) - mainChunkZ * 16) + 1;

        if (minX < 0) {
            minX = 0;
        }
        if (maxX > 16) {
            maxX = 16;
        }

        if (minY < 1) {
            minY = 1;
        }
        if (maxY > 120) {
            maxY = 120;
        }

        if (minZ < 0) {
            minZ = 0;
        }
        if (maxZ > 16) {
            maxZ = 16;
        }

        if (this.isRegionUncarvable(context, chunk, mainChunkX, mainChunkZ, minX, maxX, minY, maxY, minZ, maxZ)) { 
            return false;
        }

        for (int relX = minX; relX < maxX; relX++) {
            double scaledRelX = (((double) (relX + mainChunkX * 16) + 0.5D) - x) / yaw;

            for (int relZ = minZ; relZ < maxZ; relZ++) {
                double scaledRelZ = (((double) (relZ + mainChunkZ * 16) + 0.5D) - z) / yaw;
                boolean isGrassBlock = false;

                for (int relY = maxY; relY > minY; relY--) {
                    double scaledRelY = (((double) (relY - 1) + 0.5D) - y) / pitch;

                    if (isPositionExcluded(scaledRelX, scaledRelY, scaledRelZ, -1)) {
                        Block block = chunk.getBlockState(blockPos.set(relX, relY, relZ)).getBlock();

                        if (block == Blocks.GRASS_BLOCK) {
                            isGrassBlock = true;
                        }

                        // Don't use canCarveBlock for accuracy, for now.
                        if (block == Blocks.STONE || block == Blocks.DIRT || block == Blocks.GRASS_BLOCK || block == Blocks.DEEPSLATE) { 
                            if (relY < 11) { // Set lava below y = 11
                                chunk.setBlockState(blockPos.set(relX, relY, relZ), Blocks.LAVA.getDefaultState(), false);
                            } else {
                                chunk.setBlockState(blockPos.set(relX, relY, relZ), Blocks.CAVE_AIR.getDefaultState(), false);

                                // Replaces carved-out dirt with grass, if block that was removed was grass.
                                if (isGrassBlock && chunk.getBlockState(blockPos.set(relX, relY - 1, relZ)).getBlock() == Blocks.DIRT) {
                                    chunk.setBlockState(blockPos.set(relX, relY - 1, relZ), Blocks.GRASS_BLOCK.getDefaultState(), false);
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    private boolean canCarveBranch(
        int mainChunkX, 
        int mainChunkZ, 
        double x, 
        double z, 
        int branch, 
        int branchCount,
        float baseWidth
    ) {
        double ctrX = mainChunkX * 16 + 8;
        double ctrZ = mainChunkZ * 16 + 8;

        double d1 = x - ctrX;
        double d2 = z - ctrZ;
        double d3 = branchCount - branch;
        double d4 = baseWidth + 2.0F + 16F;

        if ((d1 * d1 + d2 * d2) - d3 * d3 > d4 * d4) {
            return false;
        }

        return true;
    }

    private boolean isRegionUncarvable(
        CarverContext context,
        Chunk chunk, 
        int mainChunkX, 
        int mainChunkZ, 
        int relMinX, 
        int relMaxX,
        int minY, 
        int maxY,
        int relMinZ, 
        int relMaxZ
    ) {
        BlockPos.Mutable blockPos = new BlockPos.Mutable();

        for (int relX = relMinX; relX < relMaxX; relX++) {
            for (int relZ = relMinZ; relZ < relMaxZ; relZ++) {
                for (int relY = maxY + 1; relY >= minY - 1; relY--) {

                    if (relY < 0 || relY >= 128) {
                        continue;
                    }

                    Block block = chunk.getBlockState(blockPos.set(relX, relY, relZ)).getBlock();

                    // Second check is to avoid overlapping (and generating lava in) noise caves.
                    if (block.equals(Blocks.WATER) || (block.equals(Blocks.AIR) && relY < 10)) {
                        return true;
                    }

                    if (relY != minY - 1 && isOnBoundary(relMinX, relMaxX, relMinZ, relMaxZ, relX, relZ)) {
                        relY = minY;
                    }
                }

            }
        }

        return false;
    }

    private boolean isPositionExcluded(
        double scaledRelativeX, 
        double scaledRelativeY, 
        double scaledRelativeZ,   
        int y
    ) {
        return 
            scaledRelativeY > -0.69999999999999996D && 
            scaledRelativeX * scaledRelativeX + 
            scaledRelativeY * scaledRelativeY + 
            scaledRelativeZ * scaledRelativeZ < 1.0D;
    }

    private boolean isOnBoundary(int minX, int maxX, int minZ, int maxZ, int relX, int relZ) {
        return relX != minX && relX != maxX - 1 && relZ != minZ && relZ != maxZ - 1;
    }
    
    protected int getCaveY(CarverContext context, Random random) {
        return random.nextInt(random.nextInt(120) + 8);
    }

    protected int getMaxCaveCount() {
        return 15;
    }

    protected float getTunnelSystemWidth(Random random) {
        float width = random.nextFloat() * 2.0f + random.nextFloat();
        return width;
    }


}
