package com.bespectacled.modernbeta.api.world.gen;

import java.util.ArrayDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.biome.ClimateBiomeProvider;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.biome.provider.VanillaBiomeProvider;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.world.spawn.IndevSpawnLocator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.StructureAccessor;

public abstract class FiniteChunkProvider extends BaseChunkProvider implements NoiseChunkImitable {
    protected final int levelWidth;
    protected final int levelLength;
    protected final int levelHeight;
    protected final float caveRadius;
    
    protected final int[] heightmap;
    protected final Block[][][] blockArr;
    
    private final AtomicBoolean generated;
    private final CountDownLatch generatedLatch;
    
    public FiniteChunkProvider(OldChunkGenerator chunkGenerator) {
        this(
            chunkGenerator,
            NbtUtil.readInt(NbtTags.LEVEL_WIDTH, chunkGenerator.getChunkSettings(), ModernBeta.GEN_CONFIG.preInfGenConfig.levelWidth),
            NbtUtil.readInt(NbtTags.LEVEL_LENGTH, chunkGenerator.getChunkSettings(), ModernBeta.GEN_CONFIG.preInfGenConfig.levelLength),
            NbtUtil.readInt(NbtTags.LEVEL_HEIGHT, chunkGenerator.getChunkSettings(), ModernBeta.GEN_CONFIG.preInfGenConfig.levelHeight),
            NbtUtil.readFloat(NbtTags.LEVEL_CAVE_RADIUS, chunkGenerator.getChunkSettings(), ModernBeta.GEN_CONFIG.preInfGenConfig.caveRadius)
        );
    }
    
    public FiniteChunkProvider(
        OldChunkGenerator chunkGenerator,
        int levelWidth,
        int levelLength,
        int levelHeight,
        float caveRadius
    ) {
        super(chunkGenerator);
        
        this.levelWidth = levelWidth;
        this.levelLength = levelLength;
        this.levelHeight = levelHeight;
        this.caveRadius = caveRadius;
        
        this.heightmap = new int[this.levelWidth * this.levelLength];
        this.blockArr = new Block[this.levelWidth][this.levelHeight][this.levelLength];
        this.fillBlockArr(Blocks.AIR);
        
        this.generated = new AtomicBoolean(false);
        this.generatedLatch = new CountDownLatch(1);
        
        this.spawnLocator = new IndevSpawnLocator(this);
    }

    @Override
    public Chunk provideChunk(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk) {
        ChunkPos pos = chunk.getPos();

        if (this.inWorldBounds(pos.getStartX(), pos.getStartZ())) {
            this.pregenerateTerrainOrWait();
            this.generateTerrain(chunk, structureAccessor);
        } else {
            this.generateBorder(chunk);
        }

        return chunk;
    }
    
    @Override
    public void provideSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource) {
        BlockPos.Mutable pos = new BlockPos.Mutable();
        
        int startX = chunk.getPos().getStartX();
        int startZ = chunk.getPos().getStartZ();
        
        int worldTopY = this.worldHeight + this.worldMinY;
        
        for (int localX = 0; localX < 16; ++localX) {
            for (int localZ = 0; localZ < 16; ++localZ) {
                int x = startX + localX;
                int z = startZ + localZ;
                Biome biome = biomeSource.getBiomeForSurfaceGen(region, pos.set(x, 0, z));
                
                boolean isCold;
                if (biomeSource.getBiomeProvider() instanceof VanillaBiomeProvider) {
                    isCold = biome.getTemperature(pos) < 0.15f;
                } else if (biomeSource.getBiomeProvider() instanceof ClimateBiomeProvider climateBiomeProvider) {
                    isCold = climateBiomeProvider.getClimateSampler().sampleClime(x, z).temp() < 0.5D;
                } else {
                    isCold = biome.getTemperature(pos) < 0.15f;
                }
                
                for (int y = worldTopY - 1; y >= this.worldMinY; --y) {
                    pos.set(x, y, z);
                    
                    BlockState blockState = this.postProcessSurfaceState(chunk.getBlockState(pos), biome, pos, isCold);
                    
                    chunk.setBlockState(pos, blockState, false);

                    // Set snow on top of snowy blocks
                    if (blockState.contains(Properties.SNOWY) && blockState.get(Properties.SNOWY).booleanValue())
                        chunk.setBlockState(pos.up(), BlockStates.SNOW, false);
                        
                }
            }
        }
    }
    
    @Override
    public int getHeight(int x, int z, Type type) {
        x += this.levelWidth / 2;
        z += this.levelLength / 2;
        
        if (x < 0 || x >= this.levelWidth || z < 0 || z >= this.levelLength) 
            return seaLevel;
        
        this.pregenerateTerrainOrWait();
        int height = this.getLevelHighestBlock(x, z);
        int seaLevel = this.getSeaLevel();
        
        if (type == Heightmap.Type.WORLD_SURFACE_WG && height < seaLevel) 
            height = seaLevel;
         
        return height;
    }
    
    @Override
    public boolean skipChunk(int chunkX, int chunkZ, ChunkStatus chunkStatus) {
        boolean inWorldBounds = this.inWorldBounds(chunkX << 4, chunkZ << 4);
        
        if (chunkStatus == ChunkStatus.FEATURES) {
            return !inWorldBounds;
        } else if (chunkStatus == ChunkStatus.STRUCTURE_STARTS) {
            return !inWorldBounds;
        }  else if (chunkStatus == ChunkStatus.CARVERS || chunkStatus == ChunkStatus.LIQUID_CARVERS) {
            return true;
        } else if (chunkStatus == ChunkStatus.SURFACE) { 
            return false;
        }
        
        return false;
    }
    
    public int getLevelWidth() {
        return this.levelWidth;
    }
    
    public int getLevelLength() {
        return this.levelLength;
    }
    
    public int getLevelHeight() {
        return this.levelHeight;
    }
    
    public float getCaveRadius() {
        return this.caveRadius;
    }
    
    public Block getLevelBlock(int x, int y, int z) {
        x = MathHelper.clamp(x, 0, this.levelWidth - 1);
        y = MathHelper.clamp(y, 0, this.levelHeight - 1);
        z = MathHelper.clamp(z, 0, this.levelLength - 1);
        
        return this.blockArr[x][y][z];
    }
    
    public int getLevelHighestBlock(int x, int z) {
        x = MathHelper.clamp(x, 0, this.levelWidth - 1);
        z = MathHelper.clamp(z, 0, this.levelLength - 1);
        
        int y;
        
        for (y = this.levelHeight; this.getLevelBlock(x, y - 1, z) == Blocks.AIR && y > 0; --y);
        
        return y;
    }
    
    public Block getLevelFluidBlock() {
        return this.defaultFluid.getBlock();
    }
    
    protected abstract void pregenerateTerrain();
    
    protected abstract void generateBorder(Chunk chunk);
    
    protected abstract BlockState postProcessTerrainState(
        Block block,
        StructureWeightSampler weightSampler,
        TerrainState terrainState,
        BlockPos pos
    );
    
    protected abstract void generateBedrock(Chunk chunk, Block block, BlockPos pos);

    protected abstract BlockState postProcessSurfaceState(BlockState blockState, Biome biome, BlockPos pos, boolean isCold);
    
    protected void generateTerrain(Chunk chunk, StructureAccessor structureAccessor) {
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        int offsetX = (chunkX + this.levelWidth / 16 / 2) * 16;
        int offsetZ = (chunkZ + this.levelLength / 16 / 2) * 16;
        
        Heightmap heightmapOcean = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSurface = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
        
        StructureWeightSampler structureWeightSampler = new StructureWeightSampler(structureAccessor, chunk);
        BlockPos.Mutable pos = new BlockPos.Mutable();
        
        for (int localX = 0; localX < 16; ++localX) {
            for (int localZ = 0; localZ < 16; ++localZ) {
                
                int x = localX + (chunkX << 4);
                int z = localZ + (chunkZ << 4);
                
                TerrainState terrainState = new TerrainState();
                
                for (int y = this.levelHeight - 1; y >= 0; --y) {
                    pos.set(x, y, z);
                    
                    Block block = this.blockArr[offsetX + localX][y][offsetZ + localZ];
                    BlockState blockState = this.postProcessTerrainState(block, structureWeightSampler, terrainState, pos);
                    
                    chunk.setBlockState(pos.set(localX, y, localZ), blockState, false);
                     
                    this.generateBedrock(chunk, block, pos);
                    
                    heightmapOcean.trackUpdate(localX, y, localZ, block.getDefaultState());
                    heightmapSurface.trackUpdate(localX, y, localZ, block.getDefaultState());
                }
            }
        }
    }

    protected boolean inLevelBounds(int x, int y, int z) {
        if (x < 0 || x >= this.levelWidth || y < 0 || y >= this.levelHeight || z < 0 || z >= this.levelLength) {
            return false;
        }
            
        return true;
    }

    protected boolean inWorldBounds(int x, int z) {
        int halfWidth = this.levelWidth / 2;
        int halfLength = this.levelLength / 2;
        
        if (x >= -halfWidth && x < halfWidth && z >= -halfLength && z < halfLength) {
            return true;
        }
        
        return false;
    }

    protected void setPhase(String phase) {
        ModernBeta.log(Level.INFO, phase + "..");
    }
    
    protected void fillOblateSpheroid(float centerX, float centerY, float centerZ, float radius, Block fillBlock) {
        for (int x = (int)(centerX - radius); x < (int)(centerX + radius); ++x) {
            for (int y = (int)(centerY - radius); y < (int)(centerY + radius); ++y) {
                for (int z = (int)(centerZ - radius); z < (int)(centerZ + radius); ++z) {
                
                    float dx = x - centerX;
                    float dy = y - centerY;
                    float dz = z - centerZ;
                    
                    if ((dx * dx + dy * dy * 2.0f + dz * dz) < radius * radius && inLevelBounds(x, y, z)) {
                        Block block = this.blockArr[x][y][z];
                        
                        if (block == this.defaultBlock.getBlock()) {
                            this.blockArr[x][y][z] = fillBlock;
                        }
                    }
                }
            }
        }
    }
    
    protected void flood(int x, int y, int z, Block fillBlock) {
        ArrayDeque<Vec3d> positions = new ArrayDeque<Vec3d>();
        
        positions.add(new Vec3d(x, y, z));
        
        while (!positions.isEmpty()) {
            Vec3d curPos = positions.poll();
            x = (int)curPos.x;
            y = (int)curPos.y;
            z = (int)curPos.z;
            
            Block block = this.blockArr[x][y][z];
    
            if (block == Blocks.AIR) {
                this.blockArr[x][y][z] = fillBlock;
                
                if (y - 1 >= 0)               positions.add(new Vec3d(x, y - 1, z));
                if (x - 1 >= 0)               positions.add(new Vec3d(x - 1, y, z));
                if (x + 1 < this.levelWidth)  positions.add(new Vec3d(x + 1, y, z));
                if (z - 1 >= 0)               positions.add(new Vec3d(x, y, z - 1));
                if (z + 1 < this.levelLength) positions.add(new Vec3d(x, y, z + 1));
            }
        }
    }

    private void pregenerateTerrainOrWait() {
        // Only one thread should enter pregeneration method,
        // others should funnel into awaiting for latch to count down.
        if (!this.generated.getAndSet(true)) {
            this.pregenerateTerrain();
            this.generatedLatch.countDown();
        } else {
            try {
                this.generatedLatch.await();
            } catch (InterruptedException e) {
                ModernBeta.log(Level.ERROR, "Indev chunk provider failed to pregenerate terrain!");
                e.printStackTrace();
            }
        }
    }
    
    private void fillBlockArr(Block block) {
        for (int x = 0; x < this.levelWidth; ++x) {
            for (int z = 0; z < this.levelLength; ++z) {
                for (int y = 0; y < this.levelHeight; ++y) {
                    this.blockArr[x][y][z] = block;
                }
            }
        }
    }
    
    protected static class TerrainState {
        private int runDepth;
        private boolean terrainModified;
        
        public TerrainState() {
            this.runDepth = 0;
            this.terrainModified = false;
        }
        
        public int getRunDepth() {
            return this.runDepth;
        }
        
        public void incrementRunDepth() {
            this.runDepth++;
        }
        
        public boolean isTerrainModified() {
            return this.terrainModified;
        }
        
        public void terrainModified() {
            this.terrainModified = true;
        }
    }
}
