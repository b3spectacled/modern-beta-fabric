package com.bespectacled.modernbeta.world.gen.provider;

import java.util.Random;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.gen.BaseChunkProvider;
import com.bespectacled.modernbeta.api.world.gen.NoiseChunkImitable;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.GenUtil;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.BlockSource;
import net.minecraft.world.gen.DefaultBlockSource;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.StructureWeightSampler;

public class Infdev227ChunkProvider extends BaseChunkProvider implements NoiseChunkImitable {
    private final boolean generateInfdevPyramid;
    private final boolean generateInfdevWall;

    private final PerlinOctaveNoise noiseOctavesA;
    private final PerlinOctaveNoise noiseOctavesB;
    private final PerlinOctaveNoise noiseOctavesC;
    private final PerlinOctaveNoise noiseOctavesD;
    private final PerlinOctaveNoise noiseOctavesE;
    private final PerlinOctaveNoise noiseOctavesF;
    private final PerlinOctaveNoise forestNoiseOctaves;
    
    public Infdev227ChunkProvider(OldChunkGenerator chunkGenerator) {
        super(chunkGenerator);
        //super(chunkGenerator, 0, 128, 64, 0, 0, -10, BlockStates.STONE, BlockStates.WATER);
        
        // Noise Generators
        this.noiseOctavesA = new PerlinOctaveNoise(rand, 16, true); 
        this.noiseOctavesB = new PerlinOctaveNoise(rand, 16, true);
        this.noiseOctavesC = new PerlinOctaveNoise(rand, 8, true);
        this.noiseOctavesD = new PerlinOctaveNoise(rand, 4, true);
        this.noiseOctavesE = new PerlinOctaveNoise(rand, 4, true);
        this.noiseOctavesF = new PerlinOctaveNoise(rand, 5, true);
        new PerlinOctaveNoise(rand, 3, true);
        new PerlinOctaveNoise(rand, 3, true);
        new PerlinOctaveNoise(rand, 3, true);
        this.forestNoiseOctaves = new PerlinOctaveNoise(rand, 8, true);
        
        this.generateInfdevPyramid = NbtUtil.readBoolean(NbtTags.GEN_INFDEV_PYRAMID, providerSettings, ModernBeta.GEN_CONFIG.inf227GenConfig.generateInfdevPyramid);
        this.generateInfdevWall = NbtUtil.readBoolean(NbtTags.GEN_INFDEV_WALL, providerSettings, ModernBeta.GEN_CONFIG.inf227GenConfig.generateInfdevWall);
        
        setForestOctaves(forestNoiseOctaves);
    }

    @Override
    public Chunk provideChunk(StructureAccessor structureAccessor, Chunk chunk) {
        this.generateTerrain(chunk, structureAccessor);  
        return chunk;
    }

    public void provideSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource) {
        BlockPos.Mutable pos = new BlockPos.Mutable();
        Random rand = this.createSurfaceRandom(chunk.getPos().x, chunk.getPos().z);
        
        int startX = chunk.getPos().getStartX();
        int startZ = chunk.getPos().getStartZ();
        
        for (int localX = 0; localX < 16; ++localX) {
            for (int localZ = 0; localZ < 16; ++localZ) {
                int x = startX + localX;
                int z = startZ + localZ;
                int surfaceTopY = GenUtil.getSolidHeight(chunk, this.worldHeight, this.minY, localX, localZ, this.defaultFluid) + 1;
                
                Biome biome = biomeSource.getBiomeForSurfaceGen(region, pos.set(x, surfaceTopY, z));
                
                BlockState topBlock = biome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                BlockState fillerBlock = biome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();

                int runDepth = 0;

                boolean usedCustomSurface = this.useCustomSurfaceBuilder(biome, biomeSource.getBiomeRegistry().getId(biome), region, chunk, rand, pos);
                
                for (int y = this.worldHeight - Math.abs(this.minY) - 1; y >= this.minY; --y) {
                    BlockState blockState = chunk.getBlockState(pos.set(localX, y, localZ));
                    
                    boolean inFluid = blockState.equals(BlockStates.AIR) || blockState.equals(this.defaultFluid);
                    
                    // Skip if used custom surface generation or if below minimum surface level.
                    if (usedCustomSurface || y < this.minSurfaceY) {
                        continue;
                    }
                    
                    if (inFluid) {
                        runDepth = 0;
                        continue;
                    }
                    
                    if (!blockState.equals(this.defaultBlock)) {
                        continue;
                    }
                        
                    if (runDepth == 0) blockState = (y >= this.seaLevel - 1) ? topBlock : fillerBlock;
                    if (runDepth == 1) blockState = fillerBlock;
                    
                    runDepth++;

                    chunk.setBlockState(pos.set(localX, y, localZ), blockState, false);
                }
            }
        }
    }

    @Override
    public int getHeight(int x, int z, Type type, HeightLimitView world) {
        int groundHeight = this.sampleHeightmap(x, z) + 1;
        
        if (type == Heightmap.Type.WORLD_SURFACE_WG && groundHeight < this.seaLevel)
            groundHeight = this.seaLevel;
        
        return groundHeight;
    }
    
    protected void generateTerrain(Chunk chunk, StructureAccessor structureAccessor) {
        Random rand = new Random();
        Random chunkRand = this.createSurfaceRandom(chunk.getPos().x, chunk.getPos().z);
        
        Heightmap heightmapOCEAN = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSURFACE = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
        
        BlockSource blockSource = new DefaultBlockSource(this.defaultBlock);
        StructureWeightSampler structureWeightSampler = new StructureWeightSampler(structureAccessor, chunk);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        int startX = chunk.getPos().getStartX();
        int startZ = chunk.getPos().getStartZ();
        
        int bedrockFloor = this.minY + this.bedrockFloor;
        
        Block defaultBlock = this.defaultBlock.getBlock();
        Block defaultFluid = this.defaultFluid.getBlock();

        for (int x = 0; x < 16; ++x) {
            int absX = startX + x;
            int rX = absX / 1024;
            
            for (int z = 0; z < 16; ++z) {    
                int absZ = startZ + z;
                int rZ = absZ / 1024;
                
                float noiseA = (float)(
                    this.noiseOctavesA.sample(absX / 0.03125f, 0.0, absZ / 0.03125f) - 
                    this.noiseOctavesB.sample(absX / 0.015625f, 0.0, absZ / 0.015625f)) / 512.0f / 4.0f;
                float noiseB = (float)this.noiseOctavesE.sample(absX / 4.0f, absZ / 4.0f);
                float noiseC = (float)this.noiseOctavesF.sample(absX / 8.0f, absZ / 8.0f) / 8.0f;
                
                noiseB = noiseB > 0.0f ? 
                    ((float)(this.noiseOctavesC.sample(absX * 0.25714284f * 2.0f, absZ * 0.25714284f * 2.0f) * noiseC / 4.0)) :
                    ((float)(this.noiseOctavesD.sample(absX * 0.25714284f, absZ * 0.25714284f) * noiseC));
                
                //int heightVal = (int)(noiseA + this.seaLevel + noiseB);

                // Subtract 1 to be more consistent with modern versions.
                int heightVal = (int)(noiseA + (this.seaLevel - 1) + noiseB);
                if ((float)this.noiseOctavesE.sample(absX, absZ) < 0.0f) {
                    heightVal = heightVal / 2 << 1;
                    if ((float)this.noiseOctavesE.sample(absX / 5, absZ / 5) < 0.0f) {
                        ++heightVal;
                    }
                }
                
                for (int y = this.minY; y < this.worldTopY; ++y) {
                    Block block = Blocks.AIR;
                    
                    if (this.generateInfdevWall && (absX == 0 || absZ == 0) && y <= heightVal + 2) {
                        block = Blocks.OBSIDIAN;
                    }
                    
                    /* Original code for reference, but unused so conventional surface/feature generation can be used.
                    else if (y == heightVal + 1 && heightVal >= this.seaLevel && Math.random() < 0.02) {
                        //block = Blocks.DANDELION;
                    }
                    else if (y == heightVal && heightVal >= this.seaLevel) {
                        block = Blocks.GRASS_BLOCK;
                    }
                    else if (y <= heightVal - 2) {
                        block = defaultBlock;
                    }
                    else if (y <= heightVal) {
                        block = Blocks.DIRT;
                    }
                    */
                    
                    else if (y <= heightVal) {
                        block = defaultBlock;
                    }
                    
                    /*
                    else if (y <= this.seaLevel) {
                        block = defaultFluid;
                    }
                    */

                    // Subtract 1 to be more consistent with modern versions.
                    else if (y <= this.seaLevel - 1) {
                        block = defaultFluid;
                    }
                    
                    if (this.generateInfdevPyramid) {
                        rand.setSeed(rX + rZ * 13871);
                        int bX = (rX << 10) + 128 + rand.nextInt(512);
                        int bZ = (rZ << 10) + 128 + rand.nextInt(512);
                        
                        bX = absX - bX;
                        bZ = absZ - bZ;
                        
                        if (bX < 0) bX = -bX;
                        if (bZ < 0) bZ = -bZ;
                        
                        if (bZ > bX) bX = bZ;
                        if ((bX = 127 - bX) == 255) bX = 1;
                        if (bX < heightVal) bX = heightVal;
                        
                        if (y <= bX && (block == Blocks.AIR || block == defaultFluid))
                            block = Blocks.BRICKS;     
                    }
                    
                    if (y <= bedrockFloor + chunkRand.nextInt(5)) {
                        block = Blocks.BEDROCK;
                    }
                    
                    //block = blockWeightSampler.getBlockWeight(absX, y, absZ, block);
                    //BlockState blockState = this.getBlockState(absX, y, absZ, block);
                    
                    BlockState blockState = this.getBlockState(structureWeightSampler, blockSource, absX, y, absZ, block, this.defaultFluid.getBlock());
                    
                    chunk.setBlockState(mutable.set(x, y, z), blockState, false);
                    
                    heightmapOCEAN.trackUpdate(x, y, z, blockState);
                    heightmapSURFACE.trackUpdate(x, y, z, blockState);
                }
            }
        }
    }
    
    protected int sampleHeightmap(int sampleX, int sampleZ) {
        int startX = (sampleX >> 4) << 4;
        int startZ = (sampleZ >> 4) << 4;
        
        int x = startX + Math.abs(sampleX) % 16;
        int z = startZ + Math.abs(sampleZ) % 16;
        
        float noiseA = (float)(
            this.noiseOctavesA.sample(x / 0.03125f, 0.0, z / 0.03125f) - 
            this.noiseOctavesB.sample(x / 0.015625f, 0.0, z / 0.015625f)) / 512.0f / 4.0f;
        float noiseB = (float)this.noiseOctavesE.sample(x / 4.0f, z / 4.0f);
        float noiseC = (float)this.noiseOctavesF.sample(x / 8.0f, z / 8.0f) / 8.0f;
        
        noiseB = noiseB > 0.0f ? 
            ((float)(this.noiseOctavesC.sample(x * 0.25714284f * 2.0f, z * 0.25714284f * 2.0f) * noiseC / 4.0)) :
            ((float)(this.noiseOctavesD.sample(x * 0.25714284f, z * 0.25714284f) * noiseC));
            
        //int heightVal = (int)(noiseA + this.seaLevel + noiseB);

        // Subtract 1 to be more consistent with modern versions.
        int heightVal = (int)(noiseA + (this.seaLevel - 1) + noiseB);
        if ((float)this.noiseOctavesE.sample(x, z) < 0.0f) {
            heightVal = heightVal / 2 << 1;
            if ((float)this.noiseOctavesE.sample(x / 5, z / 5) < 0.0f) {
                ++heightVal;
            }
        }
        
        return heightVal;
    }
}
