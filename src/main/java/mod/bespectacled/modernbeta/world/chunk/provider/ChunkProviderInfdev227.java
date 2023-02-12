package mod.bespectacled.modernbeta.world.chunk.provider;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import mod.bespectacled.modernbeta.api.world.blocksource.BlockSource;
import mod.bespectacled.modernbeta.api.world.chunk.ChunkProvider;
import mod.bespectacled.modernbeta.api.world.chunk.ChunkProviderNoiseImitable;
import mod.bespectacled.modernbeta.api.world.chunk.SurfaceConfig;
import mod.bespectacled.modernbeta.util.BlockStates;
import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.StructureWeightSampler;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.noise.NoiseConfig;

public class ChunkProviderInfdev227 extends ChunkProvider implements ChunkProviderNoiseImitable {
    private final int worldMinY;
    private final int worldHeight;
    private final int worldTopY;
    private final int seaLevel;
    
    private final int bedrockFloor;
    
    private final BlockState defaultBlock;
    private final BlockState defaultFluid;
    
    private final boolean infdevUsePyramid;
    private final boolean infdevUseWall;

    private final PerlinOctaveNoise octaveNoiseA;
    private final PerlinOctaveNoise octaveNoiseB;
    private final PerlinOctaveNoise octaveNoiseC;
    private final PerlinOctaveNoise octaveNoiseD;
    private final PerlinOctaveNoise octaveNoiseE;
    private final PerlinOctaveNoise octaveNoiseF;
    private final PerlinOctaveNoise forestOctaveNoise;

    public ChunkProviderInfdev227(ModernBetaChunkGenerator chunkGenerator, long seed) {
        super(chunkGenerator, seed);
        
        ChunkGeneratorSettings generatorSettings = this.chunkGenerator.getGeneratorSettings().value();
        GenerationShapeConfig shapeConfig = generatorSettings.generationShapeConfig();
        
        this.worldMinY = shapeConfig.minimumY();
        this.worldHeight = shapeConfig.height();
        this.worldTopY = this.worldHeight + this.worldMinY;
        this.seaLevel = generatorSettings.seaLevel();
        this.bedrockFloor = 0;

        this.defaultBlock = generatorSettings.defaultBlock();
        this.defaultFluid = generatorSettings.defaultFluid();

        this.infdevUsePyramid = this.chunkSettings.infdevUsePyramid;
        this.infdevUseWall = this.chunkSettings.infdevUseWall;
        
        this.octaveNoiseA = new PerlinOctaveNoise(this.random, 16, true); 
        this.octaveNoiseB = new PerlinOctaveNoise(this.random, 16, true);
        this.octaveNoiseC = new PerlinOctaveNoise(this.random, 8, true);
        this.octaveNoiseD = new PerlinOctaveNoise(this.random, 4, true);
        this.octaveNoiseE = new PerlinOctaveNoise(this.random, 4, true);
        this.octaveNoiseF = new PerlinOctaveNoise(this.random, 5, true);
        new PerlinOctaveNoise(this.random, 3, true);
        new PerlinOctaveNoise(this.random, 3, true);
        new PerlinOctaveNoise(this.random, 3, true);
        this.forestOctaveNoise = new PerlinOctaveNoise(this.random, 8, true);
    }

    @Override
    public CompletableFuture<Chunk> provideChunk(Executor executor, Blender blender, StructureAccessor structureAccessor, Chunk chunk, NoiseConfig noiseConfig) {
        this.generateTerrain(chunk, structureAccessor);  
        
        return CompletableFuture.<Chunk>supplyAsync(
            () -> chunk, Util.getMainWorkerExecutor()
        );
    }

    public void provideSurface(ChunkRegion region, Chunk chunk, ModernBetaBiomeSource biomeSource, NoiseConfig noiseConfig) {
        BlockPos.Mutable pos = new BlockPos.Mutable();
        
        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;
        
        int startX = chunk.getPos().getStartX();
        int startZ = chunk.getPos().getStartZ();
        
        int bedrockFloor = this.worldMinY + this.bedrockFloor;
        
        Random bedrockRand = this.createSurfaceRandom(chunkX, chunkZ);
        
        for (int localX = 0; localX < 16; ++localX) {
            for (int localZ = 0; localZ < 16; ++localZ) {
                int x = startX + localX;
                int z = startZ + localZ;
                int surfaceTopY = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG).get(localX, localZ);
                
                RegistryEntry<Biome> biome = biomeSource.getBiomeForSurfaceGen(region, pos.set(x, surfaceTopY, z));
                
                SurfaceConfig surfaceConfig = SurfaceConfig.getSurfaceConfig(biome);
                BlockState topBlock = surfaceConfig.topBlock();
                BlockState fillerBlock = surfaceConfig.fillerBlock();

                int runDepth = 0;

                for (int y = this.worldHeight - Math.abs(this.worldMinY) - 1; y >= this.worldMinY; --y) {
                    BlockState blockState;
                    
                    pos.set(localX, y, localZ);
                    blockState = chunk.getBlockState(pos);
                    
                    // Place bedrock
                    if (y <= bedrockFloor + bedrockRand.nextInt(5)) {
                        chunk.setBlockState(pos, BlockStates.BEDROCK, false);
                        continue;
                    }

                    boolean inFluid = blockState.equals(BlockStates.AIR) || blockState.equals(this.defaultFluid);
                    
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

        StructureWeightSampler structureWeightSampler = StructureWeightSampler.createStructureWeightSampler(structureAccessor, chunk.getPos());
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
                    
                    if (this.infdevUseWall && (x == 0 || z == 0) && y <= heightVal + 2) {
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
                    
                    if (this.infdevUsePyramid) {
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
                        BlockSource.DEFAULT,
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
    
    @Override
    protected PerlinOctaveNoise getForestOctaveNoise() {
        return this.forestOctaveNoise;
    }
    
    private int sampleHeightmap(int x, int z) {
        float noiseA = (float)(
            this.octaveNoiseA.sample(x / 0.03125f, 0.0, z / 0.03125f) - 
            this.octaveNoiseB.sample(x / 0.015625f, 0.0, z / 0.015625f)) / 512.0f / 4.0f;
        float noiseB = (float)this.octaveNoiseE.sampleXY(x / 4.0f, z / 4.0f);
        float noiseC = (float)this.octaveNoiseF.sampleXY(x / 8.0f, z / 8.0f) / 8.0f;
        
        noiseB = noiseB > 0.0f ? 
            ((float)(this.octaveNoiseC.sampleXY(x * 0.25714284f * 2.0f, z * 0.25714284f * 2.0f) * noiseC / 4.0)) :
            ((float)(this.octaveNoiseD.sampleXY(x * 0.25714284f, z * 0.25714284f) * noiseC));
            
        int heightVal = (int)(noiseA + this.seaLevel + noiseB);

        if ((float)this.octaveNoiseE.sampleXY(x, z) < 0.0f) {
            heightVal = heightVal / 2 << 1;
            if ((float)this.octaveNoiseE.sampleXY(x / 5, z / 5) < 0.0f) {
                ++heightVal;
            }
        }
        
        return heightVal;
    }
}
