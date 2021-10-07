package com.bespectacled.modernbeta.world.carver;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;

import net.minecraft.class_6643;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverContext;
import net.minecraft.world.gen.carver.CaveCarver;
import net.minecraft.world.gen.carver.CaveCarverConfig;
import net.minecraft.world.gen.chunk.AquiferSampler;

public class OldCaveCarver extends CaveCarver {
    private static final Set<Block> ALWAYS_CARVABLE_BLOCKS;

    public OldCaveCarver(Codec<CaveCarverConfig> codec) {
        super(codec);
    }
    
    @Override
    public boolean shouldCarve(CaveCarverConfig config, Random random) {
        return true;
    }
    
    @Override
    public boolean carve(
        CarverContext context, 
        CaveCarverConfig config, 
        Chunk mainChunk, 
        Function<BlockPos, Biome> posToBiome, 
        Random random, 
        AquiferSampler aquiferSampler, 
        ChunkPos pos,
        class_6643 bitSet
    ) {
        int caveCount = random.nextInt(random.nextInt(random.nextInt(40) + 1) + 1);
        if (random.nextInt(getMaxCaveCount()) != 0) {
            caveCount = 0;
        }

        for (int i = 0; i < caveCount; ++i) {
            double x = pos.getOffsetX(random.nextInt(16)); // Starts
            double y = config.y.get(random, context); // 1.17 stuff
            double z = pos.getOffsetZ(random.nextInt(16));
            
            // 1.17 stuff
            double horizontalScale = config.horizontalRadiusMultiplier.get(random);
            double verticalScale = config.verticalRadiusMultiplier.get(random);
            double floorLevel = config.floorLevel.get(random);
            
            Carver.SkipPredicate skipPredicate = (carverContext, scaledRelativeX, scaledRelativeY, scaledRelativeZ, relativeY) ->
                this.isPositionExcluded(scaledRelativeX, scaledRelativeY, scaledRelativeZ, floorLevel);

            int tunnelCount = 1;
            if (random.nextInt(4) == 0) {
                double yScale = config.yScale.get(random);
                
                this.carveCave(context, config, mainChunk, random, mainChunk.getPos().x, mainChunk.getPos().z, x, y, z, yScale, skipPredicate);
                tunnelCount += random.nextInt(4);
            }

            for (int j = 0; j < tunnelCount; ++j) {
                float yaw = random.nextFloat() * 3.141593F * 2.0F;
                float pitch = ((random.nextFloat() - 0.5F) * 2.0F) / 8F;
                float width = getTunnelSystemWidth(random);

                this.carveTunnels(
                    context, 
                    config, 
                    mainChunk, 
                    random, 
                    mainChunk.getPos().x, mainChunk.getPos().z, 
                    x, y, z, 
                    horizontalScale, verticalScale, 
                    width, yaw, pitch, 
                    0, 0, 1.0D,
                    skipPredicate
                );
            }
        }

        return true;
    }

    private void carveCave(
        CarverContext context, 
        CaveCarverConfig config, 
        Chunk chunk, 
        Random random, 
        int mainChunkX, 
        int mainChunkZ, 
        double x, 
        double y, 
        double z,
        double yScale,
        Carver.SkipPredicate skipPredicate
    ) {
        this.carveTunnels(
            context, 
            config, 
            chunk, 
            random, 
            mainChunkX, 
            mainChunkZ, 
            x, y, z, 
            1.0, 1.0,
            1.0F + random.nextFloat() * 6F, 
            0.0F, 0.0F, 
            -1, -1, yScale,
            skipPredicate
        );
    }

    private void carveTunnels(
        CarverContext context,
        CaveCarverConfig config,
        Chunk chunk, 
        Random rand, 
        int mainChunkX, 
        int mainChunkZ,
        double x, 
        double y,
        double z,
        double horizontalScale,
        double verticalScale,
        float width, 
        float yaw, 
        float pitch, 
        int branch,
        int branchCount, 
        double yawPitchRatio,
        Carver.SkipPredicate skipPredicate
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
            double tunnelHorizontalScale = 1.5D + (double) (MathHelper.sin(((float) branch * 3.141593F) / (float) branchCount)
                    * width * 1.0F);
            double tunnelVerticalScale = tunnelHorizontalScale * yawPitchRatio;

            float f4 = MathHelper.cos(pitch);
            float f5 = MathHelper.sin(pitch);

            x += MathHelper.cos(yaw) * f4;
            y += f5;
            z += MathHelper.sin(yaw) * f4;

            pitch *= vary ? 0.92F : 0.7F;

            pitch += f3 * 0.1F;
            yaw += f2 * 0.1F;

            f3 *= 0.9F;
            f2 *= 0.75F;

            f3 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
            f2 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4F;

            if (!noStarts && branch == randBranch && width > 1.0F) {
                carveTunnels(
                    context, 
                    config, 
                    chunk, 
                    rand, 
                    mainChunkX, mainChunkZ, 
                    x, y, z, 
                    horizontalScale, verticalScale, 
                    random.nextFloat() * 0.5F + 0.5F,
                    yaw - 1.570796F, pitch / 3F, 
                    branch, branchCount, 1.0D,
                    skipPredicate
                );
                carveTunnels(
                    context, 
                    config, 
                    chunk, 
                    rand, 
                    mainChunkX, mainChunkZ, 
                    x, y, z, 
                    horizontalScale, verticalScale, 
                    random.nextFloat() * 0.5F + 0.5F,
                    yaw + 1.570796F, pitch / 3F, 
                    branch, branchCount, 1.0D,
                    skipPredicate
                );
                return;
            }

            if (!noStarts && random.nextInt(4) == 0) {
                continue;
            }

            if (!canCarveBranch(mainChunkX, mainChunkZ, x, z, branch, branchCount, width)) {
                return;
            }

            this.carveRegion(
                context, 
                config, 
                chunk, 
                mainChunkX, mainChunkZ, 
                x, y, z, 
                tunnelHorizontalScale * horizontalScale, 
                tunnelVerticalScale * verticalScale,
                skipPredicate
            );

            if (noStarts) {
                break;
            }
        }

    }

    private boolean carveRegion(
        CarverContext context,
        CaveCarverConfig config,
        Chunk chunk,
        int mainChunkX, 
        int mainChunkZ, 
        double x, 
        double y, 
        double z, 
        double horizontalScale,
        double verticalScale,
        Carver.SkipPredicate skipPredicate
    ) {
        double ctrX = mainChunkX * 16 + 8;
        double ctrZ = mainChunkZ * 16 + 8;

        BlockPos.Mutable blockPos = new BlockPos.Mutable();

        if ( // Check for valid tunnel starts, I guess? Or to prevent overlap?
        x < ctrX - 16D - horizontalScale * 2D || z < ctrZ - 16D - horizontalScale * 2D || x > ctrX + 16D + horizontalScale * 2D
                || z > ctrZ + 16D + horizontalScale * 2D) {
            return false;
        }

        int mainChunkStartX = mainChunkX * 16;
        int mainChunkStartZ = mainChunkZ * 16;
        
        // Get min and max extents of tunnel, relative to chunk coords.
        int minX = MathHelper.floor(x - horizontalScale) - mainChunkStartX - 1;         
        int maxX = (MathHelper.floor(x + horizontalScale) - mainChunkStartX) + 1;

        int minY = MathHelper.floor(y - verticalScale) - 1;
        int maxY = MathHelper.floor(y + verticalScale) + 1;

        int minZ = MathHelper.floor(z - horizontalScale) - mainChunkStartZ - 1;
        int maxZ = (MathHelper.floor(z + horizontalScale) - mainChunkStartZ) + 1;

        if (minX < 0) {
            minX = 0;
        }
        if (maxX > 16) {
            maxX = 16;
        }

        if (minY < context.getMinY() + 1) {
            minY = context.getMinY() + 1;
        }
        if (maxY > context.getMinY() + context.getHeight() - 8) {
            maxY = context.getMinY() + context.getHeight() - 8;
        }

        if (minZ < 0) {
            minZ = 0;
        }
        if (maxZ > 16) {
            maxZ = 16;
        }

        if (this.isRegionUncarvable(context, config, chunk, mainChunkX, mainChunkZ, minX, maxX, minY, maxY, minZ, maxZ)) { 
            return false;
        }

        for (int relX = minX; relX < maxX; relX++) {
            double scaledRelX = (((double) (relX + mainChunkX * 16) + 0.5D) - x) / horizontalScale;

            for (int relZ = minZ; relZ < maxZ; relZ++) {
                double scaledRelZ = (((double) (relZ + mainChunkZ * 16) + 0.5D) - z) / horizontalScale;
                boolean isGrassBlock = false;

                for (int relY = maxY; relY > minY; relY--) {
                    double scaledRelY = (((double) (relY - 1) + 0.5D) - y) / verticalScale;

                    //if (isPositionExcluded(scaledRelX, scaledRelY, scaledRelZ, -0.69999999999999996D)) {
                    if (skipPredicate.shouldSkip(context, scaledRelX, scaledRelY, scaledRelZ, relY)) {
                        Block block = chunk.getBlockState(blockPos.set(relX, relY, relZ)).getBlock();

                        if (block == Blocks.GRASS_BLOCK) {
                            isGrassBlock = true;
                        }

                        // Don't use canCarveBlock for accuracy, for now.
                        if (ALWAYS_CARVABLE_BLOCKS.contains(block)) { 
                            //if (relY < 11) { // Set lava below y = 11
                            // Will not hit this lava check at default minY (-64), since minY is capped at 1,
                            // however, if world minY is set to 0 (i.e. through noise settings), lava should generate, preserving accuracy.
                            if (relY < config.lavaLevel.getY(context)) {
                            //if (relY < context.getMinY() + 11) {
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
        CaveCarverConfig config,
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

                    if (relY < context.getMinY() || relY >= context.getMinY() + context.getHeight()) {
                        continue;
                    }

                    int lavaLevel = config.lavaLevel.getY(context);
                    Block block = chunk.getBlockState(blockPos.set(relX, relY, relZ)).getBlock();

                    // Second check is to avoid overlapping (and generating lava in) noise caves.
                    if (block.equals(Blocks.WATER) || (block.equals(Blocks.AIR) && relY < lavaLevel)) {
                        return true;
                    }
                    
                    // Don't carve into lava aquifers that spawn above lava level.
                    if (block.equals(Blocks.LAVA) && relY >= lavaLevel) {
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
        double floorY
    ) {
        return 
            scaledRelativeY > floorY && 
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

    static {
        ALWAYS_CARVABLE_BLOCKS = ImmutableSet.<Block>of(
            Blocks.STONE,
            Blocks.DIRT,
            Blocks.GRASS_BLOCK,
            Blocks.DEEPSLATE, 
            Blocks.TUFF, 
            Blocks.GRANITE, 
            Blocks.IRON_ORE, 
            Blocks.DEEPSLATE_IRON_ORE,
            Blocks.COPPER_ORE
        );
    }
}
