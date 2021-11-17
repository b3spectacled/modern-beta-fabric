package com.bespectacled.modernbeta.world.gen.provider;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.gen.BaseChunkProvider;
import com.bespectacled.modernbeta.api.world.gen.NoiseChunkImitable;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BlockColumnHolder;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.GenUtil;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.world.gen.blocksource.LayerTransitionBlockSource;

import net.minecraft.class_6748;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.BlockSource;
import net.minecraft.world.gen.HeightContext;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.StructureWeightSampler;
import net.minecraft.world.gen.random.AtomicSimpleRandom;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

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
    
    private final BlockSource deepslateSource;
    
    public Infdev227ChunkProvider(OldChunkGenerator chunkGenerator) {
        super(chunkGenerator);
        
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
        
        // Block Source
        AtomicSimpleRandom blockSourceRandom = new AtomicSimpleRandom(seed);
        this.deepslateSource = this.generateDeepslate ? 
            new LayerTransitionBlockSource(blockSourceRandom.createRandomDeriver(), BlockStates.DEEPSLATE, null, 0, 8) :
            (sampler, x, y, z) -> null;
    }

    @Override
    public CompletableFuture<Chunk> provideChunk(Executor executor, class_6748 blender, StructureAccessor structureAccessor, Chunk chunk) {
        this.generateTerrain(chunk, structureAccessor);  
        
        return CompletableFuture.<Chunk>supplyAsync(
            () -> chunk, Util.getMainWorkerExecutor()
        );
    }

    public void provideSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        int startX = chunk.getPos().getStartX();
        int startZ = chunk.getPos().getStartZ();
        
        // Surface builder stuff
        BlockColumnHolder blockColumn = new BlockColumnHolder(chunk);
        HeightContext context = new HeightContext(this.chunkGenerator, region);
        
        BiomeAccess biomeAccess = region.getBiomeAccess();
        Registry<Biome> biomeRegistry = region.getRegistryManager().get(Registry.BIOME_KEY);
        
        MaterialRules.MaterialRuleContext ruleContext = new MaterialRules.MaterialRuleContext(
            this.surfaceBuilder,
            chunk,
            this.dummyNoiseChunkSampler,
            biomeAccess::getBiome,
            biomeRegistry,
            context
        );
        MaterialRules.BlockStateRule blockStateRule = this.surfaceRule.apply(ruleContext);
        
        for (int localX = 0; localX < 16; ++localX) {
            for (int localZ = 0; localZ < 16; ++localZ) {
                int x = startX + localX;
                int z = startZ + localZ;
                int surfaceTopY = GenUtil.getLowestSolidHeight(chunk, this.worldHeight, this.worldMinY, localX, localZ, this.defaultFluid) + 1;
                
                Biome biome = biomeSource.getBiomeForSurfaceGen(region, mutable.set(x, surfaceTopY, z));
                
                BiomeBlocks biomeBlocks = BiomeBlocks.getBiomeBlocks(biome);
                BlockState biomeTopBlock = biomeBlocks.topBlock();
                BlockState biomeFillerBlock = biomeBlocks.fillerBlock();

                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;

                boolean usedCustomSurface = this.surfaceBuilder.buildSurfaceColumn(
                    region.getRegistryManager().get(Registry.BIOME_KEY),
                    region.getBiomeAccess(), 
                    blockColumn, 
                    chunk, 
                    biome, 
                    ruleContext, 
                    blockStateRule,
                    localX,
                    localZ,
                    surfaceTopY
                );

                int runDepth = 0;

                for (int y = this.worldHeight - Math.abs(this.worldMinY) - 1; y >= this.worldMinY; --y) {
                    BlockState blockState = chunk.getBlockState(mutable.set(localX, y, localZ));
                    
                    boolean inFluid = blockState.equals(BlockStates.AIR) || blockState.equals(this.defaultFluid);
                    
                    // Skip if used custom surface generation or if below minimum surface level.
                    if (usedCustomSurface) {
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

                    chunk.setBlockState(mutable.set(localX, y, localZ), blockState, false);
                }
            }
        }
    }

    @Override
    public int getHeight(int x, int z, Type type) {
        int groundHeight = this.sampleHeightmap(x, z) + 1;
        
        if (type == Heightmap.Type.WORLD_SURFACE_WG && groundHeight < this.seaLevel)
            groundHeight = this.seaLevel;
        
        return groundHeight;
    }
    
    protected void generateTerrain(Chunk chunk, StructureAccessor structureAccessor) {
        Random rand = new Random();
        Random chunkRand = this.createSurfaceRandom(chunk.getPos().x, chunk.getPos().z);
        
        Heightmap heightmapOcean = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSurface = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);

        StructureWeightSampler structureWeightSampler = new StructureWeightSampler(structureAccessor, chunk);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        int startX = chunk.getPos().getStartX();
        int startZ = chunk.getPos().getStartZ();
        
        int bedrockFloor = this.worldMinY + this.bedrockFloor;
        
        Block defaultBlock = this.defaultBlock.getBlock();
        Block defaultFluid = this.defaultFluid.getBlock();

        for (int localX = 0; localX < 16; ++localX) {
            int x = startX + localX;
            int rX = x / 1024;
            
            for (int localZ = 0; localZ < 16; ++localZ) {    
                int z = startZ + localZ;
                int rZ = z / 1024;
                
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
                
                for (int y = this.worldMinY; y < this.worldTopY; ++y) {
                    Block block = Blocks.AIR;
                    
                    if (this.generateInfdevWall && (x == 0 || z == 0) && y <= heightVal + 2) {
                        block = Blocks.OBSIDIAN;
                    }
                    
                    /* Original code for reference, but unused so conventional surface/feature generation can be used.
                    else if (y == heightVal + 1 && heightVal >= this.seaLevel && Math.random() < 0.02) {
                        //blockToSet = Blocks.DANDELION;
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
                    */
                    
                    else if (y <= heightVal) {
                        block = defaultBlock;
                    }
                    
                    /*
                    else if (y <= this.seaLevel) {
                        blockToSet = defaultFluid;
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
                        
                        bX = x - bX;
                        bZ = z - bZ;
                        
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
                    
                    BlockState blockState = this.getBlockState(
                        structureWeightSampler, 
                        this.deepslateSource, 
                        x, y, z, 
                        block, 
                        this.defaultBlock.getBlock(), 
                        this.defaultFluid.getBlock()
                    );
                    
                    chunk.setBlockState(mutable.set(localX, y, localZ), blockState, false);
                    
                    heightmapOcean.trackUpdate(localX, y, localZ, blockState);
                    heightmapSurface.trackUpdate(localX, y, localZ, blockState);
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
