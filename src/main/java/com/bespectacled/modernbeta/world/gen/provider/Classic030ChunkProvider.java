package com.bespectacled.modernbeta.world.gen.provider;

import com.bespectacled.modernbeta.api.world.gen.FiniteChunkProvider;
import com.bespectacled.modernbeta.api.world.gen.SurfaceConfig;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.noise.PerlinOctaveNoiseCombined;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.world.gen.blocksource.SimpleBlockSource;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowyBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.BlockSource;
import net.minecraft.world.gen.StructureWeightSampler;

public class Classic030ChunkProvider extends FiniteChunkProvider {
    private PerlinOctaveNoiseCombined minHeightNoiseOctaves;
    private PerlinOctaveNoiseCombined maxHeightNoiseOctaves;
    private PerlinOctaveNoise mainHeightNoiseOctaves;
    
    private PerlinOctaveNoise dirtNoiseOctaves;
    
    private PerlinOctaveNoiseCombined erodeNoiseOctaves1;
    private PerlinOctaveNoiseCombined erodeNoiseOctaves2;
    
    private PerlinOctaveNoise sandNoiseOctaves;
    private PerlinOctaveNoise gravelNoiseOctaves;
    
    private final int waterLevel;

    public Classic030ChunkProvider(OldChunkGenerator chunkGenerator) {
        super(chunkGenerator);
        
        this.waterLevel = this.levelHeight / 2;
    }

    @Override
    protected void pregenerateTerrain() {
        this.generateHeightmap();
        this.erodeTerrain();
        this.soilTerrain();
        this.carveTerrain();
        // Ore population step here, but not included
        this.floodFluid();
        this.floodLava();
        this.growSurface();
    }

    @Override
    protected void generateBorder(Chunk chunk) {
        BlockPos.Mutable pos = new BlockPos.Mutable();
        
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = 0; y < this.levelHeight; ++y) {
                    pos.set(x, y, z);
                    
                    if (y < this.waterLevel - 2) {
                        chunk.setBlockState(pos, BlockStates.BEDROCK, false);
                    } else if (y < this.waterLevel) {
                        chunk.setBlockState(pos, this.defaultFluid, false);
                    }
                }
            }
        }
    }

    @Override
    protected BlockState postProcessTerrainState(Block block, BlockSource blockSource, StructureWeightSampler weightSampler, TerrainState terrainState, BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        
        BlockState blockState = block.getDefaultState();
        BlockState modifiedBlockState = this.getBlockState(
            weightSampler,
            SimpleBlockSource.DEFAULT,
            x, y, z,
            block,
            this.defaultBlock.getBlock(),
            this.defaultFluid.getBlock()
        );
        
        boolean inFluid = modifiedBlockState.isAir() || modifiedBlockState.isOf(this.defaultFluid.getBlock());
        int runDepth = terrainState.getRunDepth();
        
        // Check to see if structure weight sampler modifies terrain.
        if (!blockState.equals(modifiedBlockState)) {
            terrainState.terrainModified();
        }
        
        // Replace default block set by structure sampling with topsoil blocks.
        if (terrainState.isTerrainModified() && !inFluid) {
            if (runDepth == 0) {
                modifiedBlockState = y >= this.waterLevel - 1 ? BlockStates.GRASS_BLOCK : BlockStates.DIRT;
            }
            
            if (runDepth == 1) {
                modifiedBlockState = BlockStates.DIRT;
            }
            
            terrainState.incrementRunDepth();
        }
        
        return modifiedBlockState;
    }

    @Override
    protected void generateBedrock(Chunk chunk, Block block, BlockPos pos) {
        int y = pos.getY();
        
        // Set bedrock at y0 to simulate bottom of world.
        if (y == 0)
            chunk.setBlockState(pos, BlockStates.BEDROCK, false);
    }

    @Override
    protected BlockState postProcessSurfaceState(BlockState blockState, Biome biome, BlockPos pos, boolean isCold) {
        SurfaceConfig surfaceConfig = SurfaceConfig.getSurfaceConfig(biome);
        BlockState topBlock = surfaceConfig.topBlock();
        BlockState fillerBlock = surfaceConfig.fillerBlock();
        
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        
        if (blockState.isOf(BlockStates.GRASS_BLOCK.getBlock())) {
            blockState = topBlock;
        } else if (blockState.isOf(BlockStates.DIRT.getBlock())) {
            blockState = fillerBlock;
        }
        
        // Set snow/ice
        if (!this.inWorldBounds(x, z)) {
            if (y == this.waterLevel) {
                if (isCold && blockState.equals(topBlock)) {
                    blockState = topBlock.with(SnowyBlock.SNOWY, true);
                }
                
            } else if (y == this.waterLevel - 1) {
                if (isCold && blockState.equals(BlockStates.WATER)) {
                    blockState = BlockStates.ICE;
                }
            }
        }
        
        return blockState;
    }

    private void generateHeightmap() {
        this.setPhase("Raising");
        
        this.minHeightNoiseOctaves = new PerlinOctaveNoiseCombined(new PerlinOctaveNoise(random, 8, false), new PerlinOctaveNoise(random, 8, false));
        this.maxHeightNoiseOctaves = new PerlinOctaveNoiseCombined(new PerlinOctaveNoise(random, 8, false), new PerlinOctaveNoise(random, 8, false));
        this.mainHeightNoiseOctaves = new PerlinOctaveNoise(random, 6, false);
        
        for (int x = 0; x < this.levelWidth; ++x) {
            for (int z = 0; z < this.levelLength; ++z) {
                double heightLow = minHeightNoiseOctaves.sample(x * 1.3f, z * 1.3f) / 6.0 - 4.0;
                double heightHigh = maxHeightNoiseOctaves.sample(x * 1.3f, z * 1.3f) / 5.0 + 10.0 - 4.0;
                
                double heightSelector = mainHeightNoiseOctaves.sampleXY(x, z) / 8.0;
                
                if (heightSelector > 0.0) {
                    heightHigh = heightLow;
                }
                
                double heightResult = Math.max(heightLow, heightHigh) / 2.0;
                
                if (heightResult < 0.0) {
                    heightResult *= 0.8;
                }
                
                this.heightmap[x + z * this.levelWidth] = (int)heightResult;
            }
        }
    }
    
    private void erodeTerrain() {
        this.setPhase("Eroding");
        
        this.erodeNoiseOctaves1 = new PerlinOctaveNoiseCombined(new PerlinOctaveNoise(random, 8, false), new PerlinOctaveNoise(random, 8, false));
        this.erodeNoiseOctaves2 = new PerlinOctaveNoiseCombined(new PerlinOctaveNoise(random, 8, false), new PerlinOctaveNoise(random, 8, false));
        
        for (int x = 0; x < this.levelWidth; ++x) {
            for (int z = 0; z < this.levelLength; ++z) {
                double erodeSelector = erodeNoiseOctaves1.sample(x << 1, z << 1) / 8.0;
                int erodeNoise = erodeNoiseOctaves2.sample(x << 1, z << 1) > 0.0 ? 1 : 0;
            
                if (erodeSelector > 2.0) {
                    int heightResult = this.heightmap[x + z * this.levelWidth];
                    heightResult = ((heightResult - erodeNoise) / 2 << 1) + erodeNoise;
                    
                    this.heightmap[x + z * this.levelWidth] = heightResult;
                }
            }
        }
    }
    
    private void soilTerrain() {
        this.setPhase("Soiling");
        
        this.dirtNoiseOctaves = new PerlinOctaveNoise(random, 8, false);
        
        for (int x = 0; x < this.levelWidth; ++x) {
            for (int z = 0; z < this.levelLength; ++z) {
                int dirtThickness = (int)(dirtNoiseOctaves.sampleXY(x, z) / 24.0) - 4;
                int dirtThreshold = this.heightmap[x + z * this.levelWidth] + this.waterLevel;
                
                int stoneThreshold = dirtThickness + dirtThreshold;
                this.heightmap[x + z * this.levelWidth] = Math.max(dirtThreshold, stoneThreshold);
                
                if (this.heightmap[x + z * this.levelWidth] > this.levelHeight - 2) {
                    this.heightmap[x + z * this.levelWidth] = this.levelHeight - 2;
                }
             
                if (this.heightmap[x + z * this.levelWidth] < 1) {
                    this.heightmap[x + z * this.levelWidth] = 1;
                }
                
                for (int y = 0; y < this.levelHeight; ++y) {
                    Block block = Blocks.AIR;
                    
                    if (y <= dirtThreshold)
                        block = Blocks.DIRT;
                    
                    if (y <= stoneThreshold)
                        block = Blocks.STONE;
                    
                    // Move lava up a block to leave room for bedrock
                    if (y == 1) 
                        block = Blocks.LAVA;
                    
                    this.blockArr[x][y][z] = block;
                }
            }
        }
    }
    
    private void carveTerrain() {
        this.setPhase("Carving");
        
        int caveCount = this.levelWidth * this.levelLength * this.levelHeight / 256 / 64 << 1;
        
        for (int i = 0; i < caveCount; ++i) {
            float caveX = random.nextFloat() * this.levelWidth;
            float caveY = random.nextFloat() * this.levelHeight;
            float caveZ = random.nextFloat() * this.levelLength;

            int caveLen = (int)((random.nextFloat() + random.nextFloat()) * 200F);
            
            float theta = random.nextFloat() * 3.1415927f * 2.0f;
            float deltaTheta = 0.0f;
            float phi = random.nextFloat() * 3.1415927f * 2.0f;
            float deltaPhi = 0.0f;
            
            float caveRadius = random.nextFloat() * random.nextFloat() * this.caveRadius;
            
            for (int len = 0; len < caveLen; ++len) {
                caveX += MathHelper.sin(theta) * MathHelper.cos(phi);
                caveZ += MathHelper.cos(theta) * MathHelper.cos(phi);
                caveY += MathHelper.sin(phi);
                
                // TODO: Double-check
                theta = theta + deltaTheta * 0.2f;
                deltaTheta = (deltaTheta * 0.9f) + (random.nextFloat() - random.nextFloat());
                phi = phi * 0.5f + deltaPhi * 0.25f;
                deltaPhi = (deltaPhi * 0.75f) + (random.nextFloat() - random.nextFloat());
                
                if (random.nextFloat() >= 0.25f) {
                    float centerX = caveX + (random.nextFloat() * 4.0f - 2.0f) * 0.2f;
                    float centerY = caveY + (random.nextFloat() * 4.0f - 2.0f) * 0.2f;
                    float centerZ = caveZ + (random.nextFloat() * 4.0f - 2.0f) * 0.2f;
                    
                    float radius = (this.levelHeight - centerY) / this.levelHeight;
                    radius = 1.2f + (radius * 3.5f + 1.0f) * caveRadius;
                    radius = radius * MathHelper.sin(len * 3.1415927f / caveLen);
                    
                    fillOblateSpheroid(centerX, centerY, centerZ, radius, Blocks.AIR);
                }
            }
        }
    }
    
    // Using Classic generation algorithm
    private void floodFluid() {
        this.setPhase("Watering");
        
        Block fluid = this.defaultFluid.getBlock();

        for (int x = 0; x < this.levelWidth; ++x) {
            flood(x, this.waterLevel - 1, 0, fluid);
            flood(x, this.waterLevel - 1, this.levelLength - 1, fluid);
        }
        
        for (int z = 0; z < this.levelLength; ++z) {
            flood(this.levelWidth - 1, this.waterLevel - 1, z, fluid);
            flood(0, this.waterLevel - 1, z, fluid);
        }
        
        int waterSourceCount = this.levelWidth * this.levelLength / 8000;
        
        for (int i = 0; i < waterSourceCount; ++i) {
            int randX = random.nextInt(this.levelWidth);
            int randZ = random.nextInt(this.levelLength);
            int randY = (this.waterLevel - 1) - random.nextInt(2);
            
            this.flood(randX, randY, randZ, fluid);
        }
       
    }
    
    // Using Classic generation algorithm
    private void floodLava() {
        this.setPhase("Melting");

        int lavaSourceCount = this.levelWidth * this.levelLength / 20000;
         
        for (int i = 0; i < lavaSourceCount; ++i) {
            int randX = random.nextInt(this.levelWidth);
            int randZ = random.nextInt(this.levelLength);
            int randY = (int)((float)(this.waterLevel - 3) * random.nextFloat() * random.nextFloat());
            
            this.flood(randX, randY, randZ, Blocks.LAVA);
        }
    }
    
    private void growSurface() {
        this.setPhase("Growing");
        
        this.sandNoiseOctaves = new PerlinOctaveNoise(random, 8, false);
        this.gravelNoiseOctaves = new PerlinOctaveNoise(random, 8, false);
        
        for (int x = 0; x < this.levelWidth; ++x) {
            for (int z = 0; z < this.levelLength; ++z) {
                boolean genSand = sandNoiseOctaves.sampleXY(x, z) > 8.0;
                boolean genGravel = gravelNoiseOctaves.sampleXY(x, z) > 12.0;
                
                int heightResult = heightmap[x + z * this.levelWidth];
                Block blockUp = this.blockArr[x][heightResult + 1][z];
                
                if (blockUp == this.defaultFluid.getBlock() && heightResult <= this.waterLevel - 1 && genGravel) {
                    this.blockArr[x][heightResult][z] = Blocks.GRAVEL;
                }
                
                if (blockUp == Blocks.AIR) {
                    Block surfaceBlock = Blocks.GRASS_BLOCK;
                    
                    if (heightResult <= this.waterLevel - 1 && genSand) {
                        surfaceBlock = Blocks.SAND;
                    }
                    
                    this.blockArr[x][heightResult][z] = surfaceBlock;  
                }
            }
        }
    }
}
