package com.bespectacled.modernbeta.world.gen.provider;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.gen.ChunkProvider;
import com.bespectacled.modernbeta.api.world.gen.NoiseChunkImitable;
import com.bespectacled.modernbeta.api.world.gen.SurfaceConfig;
import com.bespectacled.modernbeta.util.BlockColumnHolder;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.world.gen.blocksource.SimpleBlockSource;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.HeightContext;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.StructureWeightSampler;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

public class Infdev227ChunkProvider extends ChunkProvider implements NoiseChunkImitable {
    private final int worldMinY;
    private final int worldHeight;
    private final int worldTopY;
    private final int seaLevel;
    
    private final int bedrockFloor;
    
    private final BlockState defaultBlock;
    private final BlockState defaultFluid;
    
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
        
        ChunkGeneratorSettings generatorSettings = chunkGenerator.getGeneratorSettings().value();
        GenerationShapeConfig shapeConfig = generatorSettings.getGenerationShapeConfig();
        
        this.worldMinY = shapeConfig.minimumY();
        this.worldHeight = shapeConfig.height();
        this.worldTopY = worldHeight + worldMinY;
        this.seaLevel = generatorSettings.getSeaLevel();
        this.bedrockFloor = 0;

        this.defaultBlock = generatorSettings.getDefaultBlock();
        this.defaultFluid = generatorSettings.getDefaultFluid();
        
        // Noise Generators
        this.noiseOctavesA = new PerlinOctaveNoise(random, 16, true); 
        this.noiseOctavesB = new PerlinOctaveNoise(random, 16, true);
        this.noiseOctavesC = new PerlinOctaveNoise(random, 8, true);
        this.noiseOctavesD = new PerlinOctaveNoise(random, 4, true);
        this.noiseOctavesE = new PerlinOctaveNoise(random, 4, true);
        this.noiseOctavesF = new PerlinOctaveNoise(random, 5, true);
        new PerlinOctaveNoise(random, 3, true);
        new PerlinOctaveNoise(random, 3, true);
        new PerlinOctaveNoise(random, 3, true);
        this.forestNoiseOctaves = new PerlinOctaveNoise(random, 8, true);

        this.generateInfdevPyramid = NbtUtil.toBoolean(
            this.providerSettings.get(NbtTags.GEN_INFDEV_PYRAMID),
            ModernBeta.GEN_CONFIG.generateInfdevPyramid
        );
        
        this.generateInfdevWall = NbtUtil.toBoolean(
            this.providerSettings.get(NbtTags.GEN_INFDEV_WALL),
            ModernBeta.GEN_CONFIG.generateInfdevWall
        );
        
        setForestOctaves(forestNoiseOctaves);
    }

    @Override
    public CompletableFuture<Chunk> provideChunk(Executor executor, Blender blender, StructureAccessor structureAccessor, Chunk chunk) {
        this.generateTerrain(chunk, structureAccessor);  
        
        return CompletableFuture.<Chunk>supplyAsync(
            () -> chunk, Util.getMainWorkerExecutor()
        );
    }

    public void provideSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource) {
        BlockPos.Mutable pos = new BlockPos.Mutable();
        
        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;
        
        int startX = chunk.getPos().getStartX();
        int startZ = chunk.getPos().getStartZ();
        
        int bedrockFloor = this.worldMinY + this.bedrockFloor;
        
        Random bedrockRand = this.createSurfaceRandom(chunkX, chunkZ);
        
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
                int surfaceTopY = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG).get(localX, localZ);
                
                RegistryEntry<Biome> biome = biomeSource.getBiomeForSurfaceGen(region, pos.set(x, surfaceTopY, z));
                
                SurfaceConfig surfaceConfig = SurfaceConfig.getSurfaceConfig(biome);
                BlockState topBlock = surfaceConfig.topBlock();
                BlockState fillerBlock = surfaceConfig.fillerBlock();

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
                    BlockState blockState;
                    
                    pos.set(localX, y, localZ);
                    blockState = chunk.getBlockState(pos);
                    
                    // Place deepslate
                    BlockState deepslateState = this.sampleDeepslateState(x, y, z);
                    if (deepslateState != null && blockState.isOf(this.defaultBlock.getBlock())) {
                        chunk.setBlockState(pos, deepslateState, false);
                    }
                    
                    // Place bedrock
                    if (y <= bedrockFloor + bedrockRand.nextInt(5)) {
                        chunk.setBlockState(pos, BlockStates.BEDROCK, false);
                        continue;
                    }

                    boolean inFluid = blockState.equals(BlockStates.AIR) || blockState.equals(this.defaultFluid);
                    
                    // Skip if used custom surface generation or if below minimum surface level.
                    if (usedCustomSurface) {
                        continue;
                    }
                    
                    if (inFluid) {
                        runDepth = 0;
                        continue;
                    }
                    
                    if (!blockState.isOf(this.defaultBlock.getBlock())) {
                        continue;
                    }
                        
                    if (runDepth == 0) blockState = (y >= this.seaLevel) ? topBlock : fillerBlock;
                    if (runDepth == 1) blockState = fillerBlock;
                    
                    runDepth++;

                    chunk.setBlockState(pos, blockState, false);
                }
            }
        }
    }

    @Override
    public int getHeight(int x, int z, Type type) {
        int height = this.sampleHeightmap(x, z);
        
        if (type == Heightmap.Type.WORLD_SURFACE_WG && height < this.seaLevel)
            height = this.seaLevel;
        
        return height + 1;
    }
    
    protected void generateTerrain(Chunk chunk, StructureAccessor structureAccessor) {
        Random rand = new Random();
        
        Heightmap heightmapOcean = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSurface = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);

        StructureWeightSampler structureWeightSampler = new StructureWeightSampler(structureAccessor, chunk);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        int startX = chunk.getPos().getStartX();
        int startZ = chunk.getPos().getStartZ();
        
        Block defaultBlock = this.defaultBlock.getBlock();
        Block defaultFluid = this.defaultFluid.getBlock();

        for (int localX = 0; localX < 16; ++localX) {
            int x = startX + localX;
            int rX = x / 1024;
            
            for (int localZ = 0; localZ < 16; ++localZ) {    
                int z = startZ + localZ;
                int rZ = z / 1024;
                
                int heightVal = this.sampleHeightmap(x, z);
                
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
                    
                    else if (y <= this.seaLevel) {
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
                    
                    BlockState blockState = this.getBlockState(
                        structureWeightSampler,
                        SimpleBlockSource.DEFAULT,
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
    
    private int sampleHeightmap(int x, int z) {
        float noiseA = (float)(
            this.noiseOctavesA.sample(x / 0.03125f, 0.0, z / 0.03125f) - 
            this.noiseOctavesB.sample(x / 0.015625f, 0.0, z / 0.015625f)) / 512.0f / 4.0f;
        float noiseB = (float)this.noiseOctavesE.sampleXY(x / 4.0f, z / 4.0f);
        float noiseC = (float)this.noiseOctavesF.sampleXY(x / 8.0f, z / 8.0f) / 8.0f;
        
        noiseB = noiseB > 0.0f ? 
            ((float)(this.noiseOctavesC.sampleXY(x * 0.25714284f * 2.0f, z * 0.25714284f * 2.0f) * noiseC / 4.0)) :
            ((float)(this.noiseOctavesD.sampleXY(x * 0.25714284f, z * 0.25714284f) * noiseC));
            
        int heightVal = (int)(noiseA + this.seaLevel + noiseB);

        if ((float)this.noiseOctavesE.sampleXY(x, z) < 0.0f) {
            heightVal = heightVal / 2 << 1;
            if ((float)this.noiseOctavesE.sampleXY(x / 5, z / 5) < 0.0f) {
                ++heightVal;
            }
        }
        
        return heightVal;
    }
}
