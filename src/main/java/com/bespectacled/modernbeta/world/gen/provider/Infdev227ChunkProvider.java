package com.bespectacled.modernbeta.world.gen.provider;

import java.util.Random;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.AbstractBiomeProvider;
import com.bespectacled.modernbeta.api.AbstractChunkProvider;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.gen.BlockStructureWeightSampler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class Infdev227ChunkProvider extends AbstractChunkProvider {
    private boolean generateInfdevPyramid = true;
    private boolean generateInfdevWall = true;

    private final PerlinOctaveNoise noiseOctavesA;
    private final PerlinOctaveNoise noiseOctavesB;
    private final PerlinOctaveNoise noiseOctavesC;
    private final PerlinOctaveNoise noiseOctavesD;
    private final PerlinOctaveNoise noiseOctavesE;
    private final PerlinOctaveNoise noiseOctavesF;
    private final PerlinOctaveNoise forestNoiseOctaves;
    
    public Infdev227ChunkProvider(long seed, AbstractBiomeProvider biomeProvider, Supplier<ChunkGeneratorSettings> generatorSettings, NbtCompound providerSettings) {
        //super(seed, settings);
        super(seed, -64, 192, 64, 50, 0, -10, 2, 1, 1.0, 1.0, 80, 160, 0, 0, 0, 0, 0, 0, true, true, true, BlockStates.STONE, BlockStates.WATER, biomeProvider, generatorSettings, providerSettings);
        
        // Noise Generators
        noiseOctavesA = new PerlinOctaveNoise(RAND, 16, true); 
        noiseOctavesB = new PerlinOctaveNoise(RAND, 16, true);
        noiseOctavesC = new PerlinOctaveNoise(RAND, 8, true);
        noiseOctavesD = new PerlinOctaveNoise(RAND, 4, true);
        noiseOctavesE = new PerlinOctaveNoise(RAND, 4, true);
        noiseOctavesF = new PerlinOctaveNoise(RAND, 5, true);
        new PerlinOctaveNoise(RAND, 3, true);
        new PerlinOctaveNoise(RAND, 3, true);
        new PerlinOctaveNoise(RAND, 3, true);
        forestNoiseOctaves = new PerlinOctaveNoise(RAND, 8, true);
        
        if (this.providerSettings.contains("generateInfdevPyramid")) 
            this.generateInfdevPyramid = this.providerSettings.getBoolean("generateInfdevPyramid");
        if (this.providerSettings.contains("generateInfdevWall")) 
            this.generateInfdevWall = this.providerSettings.getBoolean("generateInfdevWall");
        
        setForestOctaves(forestNoiseOctaves);
    }

    @Override
    public Chunk provideChunk(StructureAccessor structureAccessor, Chunk chunk) {
        this.generateTerrain(chunk, structureAccessor);  
        return chunk;
    }

    @Override
    public void provideSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        int startX = chunk.getPos().getStartX();
        int startZ = chunk.getPos().getStartZ();
        
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int absX = startX + x;
                int absZ = startZ + z;
                
                for (int y = this.worldHeight - Math.abs(this.minY) - 1; y >= this.minY; --y) {
                    Biome biome = biomeSource.getBiomeForSurfaceGen(region, mutable.set(absX, 0, absZ));
                    BlockState topBlock = biome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                    BlockState fillerBlock = biome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();
                    
                    BlockState blockstateToSet = chunk.getBlockState(mutable.set(x, y, z));
                    
                    if (blockstateToSet.equals(BlockStates.GRASS_BLOCK)) {
                        blockstateToSet = topBlock;
                    } else if (blockstateToSet.equals(BlockStates.DIRT)) {
                        blockstateToSet = fillerBlock;
                    }

                    chunk.setBlockState(mutable.set(x, y, z), blockstateToSet, false);
                }
            }
        }
    }

    @Override
    public int getHeight(int x, int z, Type type) {
        int groundHeight = this.sampleHeightmap(x, z) + 1;
        
        if (type == Heightmap.Type.WORLD_SURFACE_WG && groundHeight < this.seaLevel)
            groundHeight = this.seaLevel + 1;
        
        return groundHeight;
    }
    
    @Override
    public PerlinOctaveNoise getBeachNoiseOctaves() {
        return null;
    }
  
    private void generateTerrain(Chunk chunk, StructureAccessor structureAccessor) {
        Random rand = new Random();
        Random chunkRand = this.createChunkRand(chunk.getPos().x, chunk.getPos().z);
        
        Heightmap heightmapOCEAN = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSURFACE = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
        
        BlockStructureWeightSampler structureWeightSampler = new BlockStructureWeightSampler(structureAccessor, chunk);
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
                    
                int heightVal = (int)(noiseA + this.seaLevel + noiseB);
                if ((float)this.noiseOctavesE.sample(absX, absZ) < 0.0f) {
                    heightVal = heightVal / 2 << 1;
                    if ((float)this.noiseOctavesE.sample(absX / 5, absZ / 5) < 0.0f) {
                        ++heightVal;
                    }
                }
                
                for (int y = this.minY; y < this.worldTopY; ++y) {
                    Block blockToSet = Blocks.AIR;
                    
                    if (this.generateInfdevWall && (absX == 0 || absZ == 0) && y <= heightVal + 2) {
                        blockToSet = Blocks.OBSIDIAN;
                    }
                    else if (y == heightVal + 1 && heightVal >= this.seaLevel && Math.random() < 0.02) {
                        blockToSet = Blocks.DANDELION;
                    }
                    else if (y == heightVal && heightVal >= this.seaLevel) {
                        blockToSet = Blocks.GRASS_BLOCK;
                    }
                    else if (y <= heightVal - 2) {
                        blockToSet = defaultBlock;
                    }
                    else if (y <= heightVal) {
                        blockToSet = Blocks.DIRT;
                    }
                    else if (y <= this.seaLevel) {
                        blockToSet = defaultFluid;
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
                        
                        if (y <= bX && (blockToSet == Blocks.AIR || blockToSet == Blocks.WATER))
                            blockToSet = Blocks.BRICKS;     
                    }
                    
                    if (y <= bedrockFloor + chunkRand.nextInt(5)) {
                        blockToSet = Blocks.BEDROCK;
                    }
                    
                    blockToSet = structureWeightSampler.getBlockWeight(absX, y, absZ, blockToSet);
                    BlockState blockstateToSet = this.getBlockState(absX, y, absZ, blockToSet);
                    
                    chunk.setBlockState(mutable.set(x, y, z), blockstateToSet, false);
                    
                    heightmapOCEAN.trackUpdate(x, y, z, blockstateToSet);
                    heightmapSURFACE.trackUpdate(x, y, z, blockstateToSet);
                }
            }
        }
    }
    
    private int sampleHeightmap(int sampleX, int sampleZ) {
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
            
        int heightVal = (int)(noiseA + this.seaLevel + noiseB);
        if ((float)this.noiseOctavesE.sample(x, z) < 0.0f) {
            heightVal = heightVal / 2 << 1;
            if ((float)this.noiseOctavesE.sample(x / 5, z / 5) < 0.0f) {
                ++heightVal;
            }
        }
        
        return heightVal;
    }
}
