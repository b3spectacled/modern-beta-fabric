package com.bespectacled.modernbeta.surfacebuilders;

import java.util.Random;
import java.util.stream.IntStream;

import com.bespectacled.modernbeta.noise.BetaNoiseGeneratorOctaves;
import com.mojang.serialization.Codec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

public class BeachSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {
    private static final BlockState SAND = Blocks.SAND.getDefaultState();
    private static final BlockState GRAVEL = Blocks.GRAVEL.getDefaultState();
    private static final BlockState SANDSTONE = Blocks.SANDSTONE.getDefaultState();
    private static final BlockState AIR = Blocks.AIR.getDefaultState();

    private Random rand;
    private long seed;

    private BetaNoiseGeneratorOctaves beachNoiseOctaves; // field_909_n
    private BetaNoiseGeneratorOctaves stoneNoiseOctaves; // field_908_o
    protected OctavePerlinNoiseSampler noise;

    private double sandNoise[];
    private double gravelNoise[];
    private double stoneNoise[];

    private int prevChunkX;
    private int prevChunkZ;

    public BeachSurfaceBuilder(Codec<TernarySurfaceConfig> codec) {
        super(codec);

        prevChunkX = -100;
        prevChunkZ = -100;
    }

    @Override
    public void generate(Random random, Chunk chunk, Biome biome, int l, int m, int n, double noise,
            BlockState defaultBlock, // Stone usually
            BlockState defaultFluid, // Water usually
            int seaLevel, long seed, TernarySurfaceConfig config) {

        double eighth = 0.03125D; // eighth

        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;

        int x = l & 0xF;
        int z = m & 0xF;

        BlockPos.Mutable mutableBlock = new BlockPos.Mutable();

        if (prevChunkX != chunkX && prevChunkZ != chunkZ) {
            sandNoise = beachNoiseOctaves.generateNoiseOctaves(sandNoise, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1,
                    eighth, eighth, 1.0D);
            gravelNoise = beachNoiseOctaves.generateNoiseOctaves(gravelNoise, chunkX * 16, 109.0134D, chunkZ * 16, 16,
                    1, 16, eighth, 1.0D, eighth);
            stoneNoise = stoneNoiseOctaves.generateNoiseOctaves(stoneNoise, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1,
                    eighth * 2D, eighth * 2D, eighth * 2D);

            prevChunkX = chunkX;
            prevChunkZ = chunkZ;
        }

        boolean genSandBeach = sandNoise[x + z * 16] * rand.nextDouble() * 0.20000000000000001D > 0.0D;
        boolean genGravelBeach = gravelNoise[x + z * 16] + rand.nextDouble() * 0.20000000000000001D > 3D;
        int genStone = (int) (stoneNoise[x + z * 16] / 3D + 3D + rand.nextDouble() * 0.25D);

        BlockState topBlock = config.getTopMaterial();
        BlockState fillerBlock = config.getUnderMaterial();

        int flag = -1;

        for (int y = 127; y >= 0; --y) {
            mutableBlock.set(x, y, z);

            BlockState testBlock = chunk.getBlockState(mutableBlock);

            if (testBlock.isAir()) {
                flag = -1;
            }

            if (!testBlock.isOf(defaultBlock.getBlock())) {
                continue;
            }

            if (flag == -1) {
                if (genStone <= 0) {
                    topBlock = BeachSurfaceBuilder.AIR;
                    fillerBlock = defaultBlock;
                } else if (y >= seaLevel - 4 && y <= seaLevel + 1) {
                    topBlock = config.getTopMaterial();
                    fillerBlock = config.getUnderMaterial();

                    if (genGravelBeach) {
                        topBlock = BeachSurfaceBuilder.AIR; // This reduces gravel beach height by 1
                        fillerBlock = BeachSurfaceBuilder.GRAVEL;
                    }

                    if (genSandBeach) {
                        topBlock = BeachSurfaceBuilder.SAND;
                        fillerBlock = BeachSurfaceBuilder.SAND;
                    }
                }

                if (y < seaLevel && testBlock.isAir()) { // Generate water bodies
                    topBlock = defaultFluid;
                }

                flag = genStone;
                if (y >= seaLevel - 1) {
                    chunk.setBlockState(mutableBlock, topBlock, false);
                } else {
                    chunk.setBlockState(mutableBlock, fillerBlock, false);
                }

                continue;

            }

            if (flag <= 0)
                continue;

            flag--;
            chunk.setBlockState(mutableBlock, fillerBlock, false);

            // Generates layer of sandstone starting at lowest block of sand, of height 1 to
            // 4.
            if (flag == 0 && fillerBlock.isOf(Blocks.SAND)) {
                flag = rand.nextInt(4);
                fillerBlock = BeachSurfaceBuilder.SANDSTONE;
            }
        }

    }

    @Override
    public void initSeed(long seed) {
        // Based on Nether Surface Builder
        if (this.seed != seed || this.beachNoiseOctaves == null || this.stoneNoiseOctaves == null) {
            initOctaveGen(seed);
            this.noise = new OctavePerlinNoiseSampler(new ChunkRandom(seed), IntStream.rangeClosed(-3, 0));
        }
        this.seed = seed;
    }

    private void initOctaveGen(long seed) {
        rand = new Random(seed);

        beachNoiseOctaves = new BetaNoiseGeneratorOctaves(rand, 4); // field_909_n
        stoneNoiseOctaves = new BetaNoiseGeneratorOctaves(rand, 4); // field_908_o
    }

    public void genNoise(Chunk chunk, long seed) {
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        double eighth = 0.03125D;

        if (beachNoiseOctaves == null || stoneNoiseOctaves == null) {
            initOctaveGen(seed);
            this.noise = new OctavePerlinNoiseSampler(new ChunkRandom(seed), IntStream.rangeClosed(-3, 0));
        }

        sandNoise = beachNoiseOctaves.generateNoiseOctaves(sandNoise, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1, eighth,
                eighth, 1.0D);
        gravelNoise = beachNoiseOctaves.generateNoiseOctaves(gravelNoise, chunkX * 16, 109.0134D, chunkZ * 16, 16, 1,
                16, eighth, 1.0D, eighth);
        stoneNoise = stoneNoiseOctaves.generateNoiseOctaves(stoneNoise, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1,
                eighth * 2D, eighth * 2D, eighth * 2D);

    }

}
