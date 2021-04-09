package com.bespectacled.modernbeta.world.carver;

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

public class OldBetaCaveCarver extends Carver<ProbabilityConfig> {

    public OldBetaCaveCarver(Codec<ProbabilityConfig> codec, int heightLimit) {
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
            double x = chunkX * 16 + random.nextInt(16); // Starts
            double y = getCaveY(random);
            double z = chunkZ * 16 + random.nextInt(16);

            int tunnelCount = 1;
            if (random.nextInt(4) == 0) {
                this.carveCave(chunk, random, mainChunkX, mainChunkZ, x, y, z);
                tunnelCount += random.nextInt(4);
            }

            for (int j = 0; j < tunnelCount; ++j) {
                float f = random.nextFloat() * 3.141593F * 2.0F;
                float f1 = ((random.nextFloat() - 0.5F) * 2.0F) / 8F;
                float tunnelSysWidth = getTunnelSystemWidth(random);

                carveTunnels(chunk, random, mainChunkX, mainChunkZ, x, y, z, tunnelSysWidth, f, f1, 0, 0, 1.0D);
            }
        }

        return false;
    }

    protected void carveCave(Chunk chunk, Random random, int mainChunkX, int mainChunkZ, double x, double y, double z) {
        carveTunnels(chunk, random, mainChunkX, mainChunkZ, x, y, z, 1.0F + random.nextFloat() * 6F, 0.0F, 0.0F, -1, -1,
                0.5D);
    }

    protected void carveTunnels(Chunk chunk, Random rand, int mainChunkX, int mainChunkZ, double x, double y, double z,
            float tunnelSysWidth, float f1, float f2, int branch, int branchCount, double tunnelWHRatio) {

        float f3 = 0.0F;
        float f4 = 0.0F;

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
                    * tunnelSysWidth * 1.0F);
            double pitch = yaw * tunnelWHRatio;

            float f5 = MathHelper.cos(f2);
            float f6 = MathHelper.sin(f2);

            x += MathHelper.cos(f1) * f5;
            y += f6;
            z += MathHelper.sin(f1) * f5;

            f2 *= vary ? 0.92F : 0.7F;

            f2 += f4 * 0.1F;
            f1 += f3 * 0.1F;

            f4 *= 0.9F;
            f3 *= 0.75F;

            f4 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
            f3 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4F;

            if (!noStarts && branch == randBranch && tunnelSysWidth > 1.0F) {
                carveTunnels(chunk, rand, mainChunkX, mainChunkZ, x, y, z, random.nextFloat() * 0.5F + 0.5F,
                        f1 - 1.570796F, f2 / 3F, branch, branchCount, 1.0D);
                carveTunnels(chunk, rand, mainChunkX, mainChunkZ, x, y, z, random.nextFloat() * 0.5F + 0.5F,
                        f1 + 1.570796F, f2 / 3F, branch, branchCount, 1.0D);
                return;
            }

            if (!noStarts && random.nextInt(4) == 0) {
                continue;
            }

            if (!canCarveBranch(mainChunkX, mainChunkZ, x, z, branch, branchCount, tunnelSysWidth)) {
                return;
            }

            carveRegion(chunk, null, 0, 64, mainChunkX, mainChunkZ, x, y, z, yaw, pitch, null); // Don't move this into
                                                                                                // above if statement,
                                                                                                // breaks.

            if (noStarts) {
                break;
            }
        }

    }

    @Override
    protected boolean carveRegion(Chunk chunk, Function<BlockPos, Biome> posToBiome, long seed, int seaLevel,
            int mainChunkX, int mainChunkZ, double x, double y, double z, double yaw, double pitch,
            BitSet carvingMask) {
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

        // Use vanilla methods, for now.
        if (super.isRegionUncarvable(chunk, mainChunkX, mainChunkZ, minX, maxX, minY, maxY, minZ, maxZ)) { 
            return false;
        }

        for (int relX = minX; relX < maxX; relX++) {

            double scaledRelX = (((double) (relX + mainChunkX * 16) + 0.5D) - x) / yaw;

            for (int relZ = minZ; relZ < maxZ; relZ++) {

                double scaledRelZ = (((double) (relZ + mainChunkZ * 16) + 0.5D) - z) / yaw;
                boolean isGrassBlock = false;

                int setY = maxY; // Replaces: int setY = (relX * 16 + absZ) * 128 + maxY;

                for (int relY = maxY - 1; relY >= minY; relY--) {
                    double scaledRelY = (((double) relY + 0.5D) - y) / pitch;

                    if (isPositionExcluded(scaledRelX, scaledRelY, scaledRelZ, -1)) {
                        Block block = chunk.getBlockState(new BlockPos(relX, setY, relZ)).getBlock();
                        // Block blockAbove = chunk.getBlockState(new BlockPos(relX, setY + 1,
                        // relZ)).getBlock();

                        if (block == Blocks.GRASS_BLOCK) {
                            isGrassBlock = true;
                        }

                        // Don't use canCarveBlock for accuracy, for now.
                        if (block == Blocks.STONE || block == Blocks.DIRT || block == Blocks.GRASS_BLOCK) { 
                            if (relY < 10) { // Set lava below y = 10
                                chunk.setBlockState(blockPos.set(relX, setY, relZ), Blocks.LAVA.getDefaultState(),
                                        false);
                            } else {
                                chunk.setBlockState(blockPos.set(relX, setY, relZ), Blocks.CAVE_AIR.getDefaultState(),
                                        false);

                                // I believe this replaces carved-out dirt with grass, if block that was removed
                                // was grass.
                                if (isGrassBlock && chunk.getBlockState(new BlockPos(relX, setY - 1, relZ))
                                        .getBlock() == Blocks.DIRT) {
                                    chunk.setBlockState(blockPos.set(relX, setY - 1, relZ),
                                            Blocks.GRASS_BLOCK.getDefaultState(), false);
                                }
                            }
                        }
                    }
                    setY--;
                }

            }
        }

        return true;
    }

    @Override
    protected boolean canCarveBranch(int mainChunkX, int mainChunkZ, double x, double z, int branch, int branchCount,
            float baseWidth) {
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

    @Override
    protected boolean isRegionUncarvable(Chunk chunk, int mainChunkX, int mainChunkZ, int relMinX, int relMaxX,
            int minY, int maxY, int relMinZ, int relMaxZ) {
        BlockPos.Mutable blockPos = new BlockPos.Mutable();

        for (int relX = relMinX; relX < relMaxX; relX++) {
            for (int relZ = relMinZ; relZ < relMaxZ; relZ++) {
                for (int relY = maxY + 1; relY >= minY - 1; relY--) {

                    if (relY < 0 || relY >= 128) {
                        continue;
                    }

                    Block block = chunk.getBlockState(blockPos.set(relX, relY, relZ)).getBlock();

                    if (block.equals(Blocks.WATER)) {
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

    @Override
    protected boolean isPositionExcluded(double scaledRelativeX, double scaledRelativeY, double scaledRelativeZ,
            int y) {
        return scaledRelativeY > -0.69999999999999996D && scaledRelativeX * scaledRelativeX
                + scaledRelativeY * scaledRelativeY + scaledRelativeZ * scaledRelativeZ < 1.0D;
    }

    private boolean isOnBoundary(int minX, int maxX, int minZ, int maxZ, int relX, int relZ) {
        return relX != minX && relX != maxX - 1 && relZ != minZ && relZ != maxZ - 1;
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