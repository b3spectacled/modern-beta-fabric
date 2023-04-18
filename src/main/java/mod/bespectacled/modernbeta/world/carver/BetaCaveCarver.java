package mod.bespectacled.modernbeta.world.carver;

import java.util.function.Function;

import com.mojang.serialization.Codec;

import mod.bespectacled.modernbeta.util.BlockStates;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverConfig;
import net.minecraft.world.gen.carver.CarverContext;
import net.minecraft.world.gen.carver.CarvingMask;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.densityfunction.DensityFunction;

public class BetaCaveCarver extends Carver<BetaCaveCarverConfig> {
    public BetaCaveCarver(Codec<BetaCaveCarverConfig> caveCodec) {
        super(caveCodec);
    }
    
    @Override
    public boolean shouldCarve(BetaCaveCarverConfig config, Random random) {
        return true;
    }
    
    @Override
    public boolean carve(
        CarverContext context, 
        BetaCaveCarverConfig config, 
        Chunk mainChunk, 
        Function<BlockPos, RegistryEntry<Biome>> posToBiome, 
        Random random,
        AquiferSampler aquiferSampler, 
        ChunkPos pos,
        CarvingMask carvingMask
    ) {
        boolean useFixedCaves = config.useFixedCaves.orElse(false);
        
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
                !this.isPositionExcluded(scaledRelativeX, scaledRelativeY, scaledRelativeZ, floorLevel);

            int tunnelCount = 1;
            if (random.nextInt(4) == 0) {
                double yScale = config.yScale.get(random);
                
                this.carveCave(
                    context,
                    config,
                    mainChunk,
                    random,
                    mainChunk.getPos().x,
                    mainChunk.getPos().z,
                    x, y, z,
                    yScale,
                    skipPredicate,
                    carvingMask,
                    aquiferSampler,
                    useFixedCaves
                );
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
                    skipPredicate,
                    carvingMask,
                    aquiferSampler,
                    useFixedCaves
                );
            }
        }

        return true;
    }

    private void carveCave(
        CarverContext context, 
        BetaCaveCarverConfig config, 
        Chunk chunk, 
        Random random,
        int mainChunkX, 
        int mainChunkZ, 
        double x, 
        double y, 
        double z,
        double yScale,
        Carver.SkipPredicate skipPredicate,
        CarvingMask carvingMask,
        AquiferSampler aquiferSampler,
        boolean useFixedCaves
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
            skipPredicate,
            carvingMask,
            aquiferSampler,
            useFixedCaves
        );
    }

    private void carveTunnels(
        CarverContext context,
        BetaCaveCarverConfig config,
        Chunk chunk,
        Random initialRandom,
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
        Carver.SkipPredicate skipPredicate,
        CarvingMask carvingMask,
        AquiferSampler aquiferSampler,
        boolean useFixedCaves
    ) {
        float f2 = 0.0F;
        float f3 = 0.0F;

        Random random = new LocalRandom(initialRandom.nextLong());

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
                    useFixedCaves ? random : initialRandom,
                    mainChunkX, mainChunkZ,
                    x, y, z,
                    horizontalScale, verticalScale,
                    random.nextFloat() * 0.5F + 0.5F,
                    yaw - 1.570796F, pitch / 3F,
                    branch, branchCount, 1.0D,
                    skipPredicate,
                    carvingMask,
                    aquiferSampler,
                    useFixedCaves
                );
                carveTunnels(
                    context,
                    config,
                    chunk,
                    useFixedCaves ? random : initialRandom,
                    mainChunkX, mainChunkZ,
                    x, y, z,
                    horizontalScale, verticalScale,
                    random.nextFloat() * 0.5F + 0.5F,
                    yaw + 1.570796F, pitch / 3F,
                    branch, branchCount, 1.0D,
                    skipPredicate,
                    carvingMask,
                    aquiferSampler,
                    useFixedCaves
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
                skipPredicate,
                carvingMask,
                aquiferSampler
            );

            if (noStarts) {
                break;
            }
        }

    }

    private boolean carveRegion(
        CarverContext context,
        BetaCaveCarverConfig config,
        Chunk chunk,
        int mainChunkX, 
        int mainChunkZ, 
        double x, 
        double y, 
        double z, 
        double horizontalScale,
        double verticalScale,
        Carver.SkipPredicate skipPredicate,
        CarvingMask carvingMask,
        AquiferSampler aquiferSampler
    ) {
        double ctrX = mainChunkX * 16 + 8;
        double ctrZ = mainChunkZ * 16 + 8;

        BlockPos.Mutable pos = new BlockPos.Mutable();

        if ( // Check for valid tunnel starts, I guess? Or to prevent overlap?
        x < ctrX - 16D - horizontalScale * 2D || z < ctrZ - 16D - horizontalScale * 2D || x > ctrX + 16D + horizontalScale * 2D
                || z > ctrZ + 16D + horizontalScale * 2D) {
            return false;
        }

        int mainChunkStartX = mainChunkX * 16;
        int mainChunkStartZ = mainChunkZ * 16;
        
        // Get min and max extents of tunnel, relative to chunk coords.
        int minX = MathHelper.floor(x - horizontalScale) - mainChunkStartX - 1;         
        int maxX = MathHelper.floor(x + horizontalScale) - mainChunkStartX + 1;

        int minY = MathHelper.floor(y - verticalScale) - 1;
        int maxY = MathHelper.floor(y + verticalScale) + 1;

        int minZ = MathHelper.floor(z - horizontalScale) - mainChunkStartZ - 1;
        int maxZ = MathHelper.floor(z + horizontalScale) - mainChunkStartZ + 1;

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

        for (int localX = minX; localX < maxX; localX++) {
            double scaledRelX = (((double) (localX + mainChunkX * 16) + 0.5D) - x) / horizontalScale;

            for (int localZ = minZ; localZ < maxZ; localZ++) {
                double scaledRelZ = (((double) (localZ + mainChunkZ * 16) + 0.5D) - z) / horizontalScale;
                boolean isGrassBlock = false;

                for (int localY = maxY; localY > minY; localY--) {
                    double scaledRelY = (((double) (localY - 1) + 0.5D) - y) / verticalScale;

                    if (skipPredicate.shouldSkip(context, scaledRelX, scaledRelY, scaledRelZ, localY) ||
                        carvingMask.get(localX, localY, localZ))
                        continue;

                    carvingMask.set(localX, localY, localZ);
                    pos.set(localX, localY, localZ);
                    
                    BlockState state = chunk.getBlockState(pos);

                    if (state.isOf(Blocks.GRASS_BLOCK)) {
                        isGrassBlock = true;
                    }

                    // Don't use canCarveBlock for accuracy, for now.
                    if (state.isIn(config.replaceable)) {
                        int offsetX = chunk.getPos().getOffsetX(localX);
                        int offsetZ = chunk.getPos().getOffsetZ(localZ);
                        BlockPos carverPos = new BlockPos(offsetX, localY, offsetZ);
                        
                        BlockState carverState = this.getBlockState(context, config, carverPos, aquiferSampler);
                        
                        if (carverState != null) {
                            chunk.setBlockState(carverPos, carverState, false);
                            
                            if (aquiferSampler.needsFluidTick() && !carverState.getFluidState().isEmpty()) {
                                chunk.markBlockForPostProcessing(carverPos);
                            }
                            
                            // Replaces carved-out dirt with grass, if block that was removed was grass.
                            if (isGrassBlock && chunk.getBlockState(carverPos.down()).getBlock() == Blocks.DIRT) {
                                chunk.setBlockState(carverPos.down(), BlockStates.GRASS_BLOCK, false);
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
    
    private BlockState getBlockState(CarverContext context, BetaCaveCarverConfig config, BlockPos pos, AquiferSampler aquiferSampler) {
        if (pos.getY() < config.lavaLevel.getY(context)) {
            return BlockStates.LAVA;
        }
        
        boolean useAquifers = config.useAquifers.orElse(false);
        
        if (!useAquifers) {
            return BlockStates.AIR;
        }
        
        // TODO: Produces too many flooded caves, re-visit this later.
         
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        BlockState state = aquiferSampler.apply(new DensityFunction.UnblendedNoisePos(x, y, z), 0.0);
        
        if (state == null) {
            return isDebug(config) ? config.debugConfig.getBarrierState() : null;
        }
        
        return isDebug(config) ? getDebugState(config, state) : state;
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
        BetaCaveCarverConfig config,
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
        
        boolean useAquifers = config.useAquifers.orElse(false);

        for (int relX = relMinX; relX < relMaxX; relX++) {
            for (int relZ = relMinZ; relZ < relMaxZ; relZ++) {
                for (int relY = maxY + 1; relY >= minY - 1; relY--) {
                    if (relY < context.getMinY() || relY >= context.getMinY() + context.getHeight()) {
                        continue;
                    }

                    int lavaLevel = config.lavaLevel.getY(context);
                    Block block = chunk.getBlockState(blockPos.set(relX, relY, relZ)).getBlock();

                    // Don't carve into water bodies, unless useAquifers enabled
                    if (!useAquifers && block == Blocks.WATER) {
                        return true;
                    }
                    
                    // Don't carve into lava aquifers that spawn above lava level, unless useAquifers enabled
                    if (!useAquifers && block == Blocks.LAVA && relY >= lavaLevel) {
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
    
    private static BlockState getDebugState(CarverConfig config, BlockState state) {
        if (state.isOf(Blocks.AIR)) {
            return config.debugConfig.getAirState();
        }
        
        if (state.isOf(Blocks.WATER)) {
            BlockState waterState = config.debugConfig.getWaterState();
            if (waterState.contains(Properties.WATERLOGGED)) {
                return (BlockState)waterState.with(Properties.WATERLOGGED, true);
            }
            return waterState;
        }
        
        if (state.isOf(Blocks.LAVA)) {
            return config.debugConfig.getLavaState();
        }
        
        return state;
    }
    
    private static boolean isDebug(CarverConfig config) {
        return config.debugConfig.isDebugMode();
    }
}
